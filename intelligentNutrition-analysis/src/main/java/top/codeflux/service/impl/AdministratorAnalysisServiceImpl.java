package top.codeflux.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.codeflux.ai.service.AiService;
import top.codeflux.appUser.domain.HealthCheckIn;
import top.codeflux.appUser.domain.HealthScore;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.service.*;
import top.codeflux.common.domain.AppUser;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.common.domain.vo.chart.PieChartVo;
import top.codeflux.domain.vo.*;
import top.codeflux.service.AdministratorAnalysisService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author qht
 * @date 2025/6/4
 */
@Service
@RequiredArgsConstructor
public class AdministratorAnalysisServiceImpl implements AdministratorAnalysisService {
    private final IAppUserService userService;
    private final SportRecordService sportRecordService;
    private final HealthScoreService healthScoreService;
    private final HealthCheckInService healthCheckInService;
    private final DietaryRecordService dietaryRecordService;
    private final AiService aiService;
    /**
     * 统计用户数量
     *
     * @return
     */
    @Override
    public CountUserVo countUser() {
        // 创建一个vo对象
        CountUserVo vo = new CountUserVo();
        // 查询目前注册用户数量 并对vo赋值
        vo.setTotal(userService.lambdaQuery().count());
        // 查询这个月之前的注册用户数量 并对vo赋值
        // 先查询现在是那一号
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        vo.setLastMonthTotal(userService.lambdaQuery().lt(AppUser::getCreateTime,
                LocalDate.now().plusDays(-1 * dayOfMonth)).count());
        vo.setReturnTime(LocalDateTime.now());
        return vo;
    }

    /**
     * 统计用户这个月的日均运动时长 以及上个月
     *
     * @return
     */
    @Override
    public AdvantageExerciseForDayVo advantageExerciseForDay() {
        //  创建一个vo对象
        AdvantageExerciseForDayVo vo = new AdvantageExerciseForDayVo();
        // 统计这个月的所有运动时长 汇总
        // 获取这个月1号的时间
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        // 下个月1号的时间
        LocalDate nextMonthFirstDay = firstDayOfMonth.plusMonths(1);
        // 上个月1号的时间
        LocalDate lastMonthFirstDay = firstDayOfMonth.minusMonths(1);
        // 只统计运动时间属于这个月的记录
        List<SportRecord> thisMonthRecord = sportRecordService.lambdaQuery()
                .ge(SportRecord::getExerciseTime, firstDayOfMonth )
                .lt(SportRecord::getExerciseTime, nextMonthFirstDay)
                .list();
        double advantageDuration = 0.0;
        var countUserSet = new HashSet<>();
        var countDaysSet = new HashSet<>();
        if (thisMonthRecord != null && !thisMonthRecord.isEmpty()) {
            // 统计平均值
            advantageDuration = thisMonthRecord.stream().mapToDouble(SportRecord::getDuration).sum();
            thisMonthRecord.forEach(record -> {
                countUserSet.add(record.getStudentNumber());
                countDaysSet.add(record.getExerciseTime().toLocalDate());
            });
            // 先计算人均这个月
            advantageDuration /= countUserSet.size();
            // 再计算人均每天
            advantageDuration /= countDaysSet.size();
            countUserSet.clear();
            countDaysSet.clear();
        }
        // 计算上个月的平均值
        List<SportRecord> lastMonthRecord = sportRecordService.lambdaQuery()
                .ge(SportRecord::getExerciseTime, lastMonthFirstDay)
                .lt(SportRecord::getExerciseTime, firstDayOfMonth)
                .list();
        double lastMonthAdv = 0.0;
        if (lastMonthRecord != null && !lastMonthRecord.isEmpty()) {
            lastMonthAdv = (lastMonthRecord
                    .stream()
                    .mapToDouble(SportRecord::getDuration)
                    .sum());
            lastMonthRecord.forEach(record -> {
                countUserSet.add(record.getStudentNumber());
                countDaysSet.add(record.getExerciseTime().toLocalDate());
            });
            lastMonthAdv /= countUserSet.size() * countDaysSet.size();
        }
        vo.setDayAdvExerciseMinute(advantageDuration);
        vo.setLastMonthDayAdvExerciseMinute(lastMonthAdv);
        vo.setReturnTime(LocalDateTime.now());
        return vo;
    }

    /**
     * 统计本周平均健康分数
     * @return
     */
    @Override
    public AnalysisIndexVo<Double> getThisWeekAdvHealthScore() {
        // 创建需要返回的vo对象
        AnalysisIndexVo<Double> vo = new AnalysisIndexVo<>();
        // 创建小数格式化工具
        DecimalFormat df = new DecimalFormat("#.##");
        // 获取本周日期区间
        // 先知道现在是星期几
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        // 本周星期一
        LocalDate thisWeekFirstDay = LocalDate.now().minusDays(dayOfWeek - 1);
        // 查询本周的评分记录
        List<HealthScore> thisWeekRecord = healthScoreService.lambdaQuery()
                .ge(HealthScore::getCreateTime, thisWeekFirstDay)
                .lt(HealthScore::getCreateTime, thisWeekFirstDay.plusWeeks(1))
                .list();
        // 平均分
        double advScore = 0.0;
        if (thisWeekRecord != null && !thisWeekRecord.isEmpty()) {
            // 本周总分
            double totalScore = thisWeekRecord.stream().mapToDouble(HealthScore::getScore).sum();
            advScore = Double.parseDouble(df.format(totalScore / thisWeekRecord.size()));
        }
        vo.setValue(advScore);
        // 开始计算上周平均分
        // 获取上周星期一
        LocalDate lastWeekFirstDay = thisWeekFirstDay.minusWeeks(1);
        // 查询数据库
        List<HealthScore> lastWeekRecord = healthScoreService.lambdaQuery()
                .ge(HealthScore::getCreateTime, lastWeekFirstDay)
                .lt(HealthScore::getCreateTime, lastWeekFirstDay.plusWeeks(1))
                .list();

        double lastAdvScore = 0.0;
        if (lastWeekRecord != null && !lastWeekRecord.isEmpty()) {
            // 计算平均值
            double lastWeekTotalScore = lastWeekRecord.stream().mapToDouble(HealthScore::getScore).sum();
            lastAdvScore = Double.parseDouble(df.format(lastWeekTotalScore / lastWeekRecord.size()));
        }

        vo.setLastValue(lastAdvScore);
        vo.setResponseTime(LocalDateTime.now());
        return vo;
    }

    /**
     * 统计今日健康打卡人数
     */
    @Override
    public AnalysisIndexVo<Long> countTodayHealthCheckIn() {
        // 统计今天的
        Long todayCount = healthCheckInService.lambdaQuery()
                .ge(HealthCheckIn::getCreateTime, LocalDate.now().atStartOfDay())
                .le(HealthCheckIn::getCreateTime, LocalDate.now().atTime(LocalTime.MAX))
                .count();
        // 统计昨天的
        Long yesterdayCount = healthCheckInService.lambdaQuery()
                .ge(HealthCheckIn::getCreateTime, LocalDate.now().minusDays(1).atStartOfDay())
                .lt(HealthCheckIn::getCreateTime, LocalDate.now().atStartOfDay())
                .count();
        AnalysisIndexVo<Long> vo = new AnalysisIndexVo<>();
        vo.setValue(todayCount);
        vo.setLastValue(yesterdayCount);
        vo.setResponseTime(LocalDateTime.now());
        return vo;
    }

    /**
     * 统计用户增长趋势
     *
     * @return
     */
    @Override
    public ChartVo<Long> getUserTrend() {
        // 创建一个图表vo对象
        ChartVo<Long> vo = new ChartVo<>();
        // 获取x轴数据 -> 近六个月
        List<String> xAxis = new ArrayList<>();
        LocalDateTime firstDayOfMonth = LocalDateTime.now().withDayOfMonth(1);
//        xAxis.add(firstDayOfMonth.getMonthValue() + "月");
        // yAix -> 用户增长
        List<Long> yAis = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LocalDateTime startTime = firstDayOfMonth.minusMonths(i);
            LocalDateTime endTime = i > 0? firstDayOfMonth.minusMonths(i - 1) : firstDayOfMonth.minusMonths(1);
            xAxis.add(startTime.getMonthValue() + "月");
            // 查询新用户
            Long countNewUser = userService.lambdaQuery()
                    .lt(AppUser::getCreateTime, endTime)
                    .gt(AppUser::getCreateTime, startTime)
                    .count();
            yAis.add(countNewUser);
        }

        Collections.reverse(xAxis);
        Collections.reverse(yAis);
        vo.setXAxis(xAxis);
        vo.setYAxis(yAis);
        return vo;
    }


    /**
     * 统计bmi平均值趋势
     *
     * @return
     */
    @Override
    public ChartVo<Integer> getBmiAdvTrendForMonth() {

        LocalDateTime.now().withDayOfMonth(1);
        return null;
    }


    /**
     * 营养成分分布
     *
     * @return
     */
    @Override
    @Cacheable(value = "nutritionalAnalysis", key = "'nutritional:' + T(java.time.LocalDate).now().toString()")
    public PieChartVo analysisNutritional() {
        // 获取最近一个月的学生饮食数据
        // 获取这个月第一天的时间
        LocalDateTime firstDayOfMonth = LocalDateTime.now().withDayOfMonth(1);
        List<DietaryRecord> dietaryRecordList = dietaryRecordService.lambdaQuery()
                .ge(DietaryRecord::getCreateTime, firstDayOfMonth)
                .lt(DietaryRecord::getCreateTime, firstDayOfMonth.plusMonths(1))
                .list();
        // 调用ai进行分析
        return aiService.analysisNutritional(dietaryRecordList.toString());
    }

    /**
     * 统计用户活动
     *
     * @return
     */
    @Override
    public BarChartVo analysisUserActivity() {
        BarChartVo vo = new BarChartVo();
        // 设置x轴数据
        vo.setXAxis(List.of("周一", "周二", "周三", "周四", "周五", "周六", "周日"));
        // y轴数据
        var addSportRecordData = new BarChartVo.SeriesData();
        var addMealRecordData = new BarChartVo.SeriesData();
        var healthTestData = new BarChartVo.SeriesData();

        // 设置系列名称
        addSportRecordData.setName("运动记录");
        addMealRecordData.setName("膳食记录");
        healthTestData.setName("健康测试");

        // 获取本周一的日期
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        LocalDate thisWeekMonday = today.minusDays(dayOfWeek - 1);

        // 初始化每天的统计数据
        List<Integer> sportCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        List<Integer> mealCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        List<Integer> healthCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));

        // 查询运动记录数据
        List<SportRecord> sportRecords = sportRecordService.lambdaQuery()
                .ge(SportRecord::getExerciseTime, thisWeekMonday.atStartOfDay())
                .lt(SportRecord::getExerciseTime, thisWeekMonday.plusDays(7).atStartOfDay())
                .list();

        // 统计每天的运动记录数量
        if (sportRecords != null && !sportRecords.isEmpty()) {
            for (SportRecord record : sportRecords) {
                int dayIndex = record.getExerciseTime().getDayOfWeek().getValue() - 1;
                sportCounts.set(dayIndex, sportCounts.get(dayIndex) + 1);
            }
        }

        // 查询膳食记录数据
        List<DietaryRecord> dietaryRecords = dietaryRecordService.lambdaQuery()
                .ge(DietaryRecord::getCreateTime, thisWeekMonday.atStartOfDay())
                .lt(DietaryRecord::getCreateTime, thisWeekMonday.plusDays(7).atStartOfDay())
                .list();

        // 统计每天的膳食记录数量
        if (dietaryRecords != null && !dietaryRecords.isEmpty()) {
            for (DietaryRecord record : dietaryRecords) {
                int dayIndex = record.getCreateTime().getDayOfWeek().getValue() - 1;
                mealCounts.set(dayIndex, mealCounts.get(dayIndex) + 1);
            }
        }

        // 查询健康测试数据
        List<HealthScore> healthScores = healthScoreService.lambdaQuery()
                .ge(HealthScore::getCreateTime, thisWeekMonday)
                .lt(HealthScore::getCreateTime, thisWeekMonday.plusDays(7))
                .list();

        // 统计每天的健康测试数量
        if (healthScores != null && !healthScores.isEmpty()) {
            for (HealthScore record : healthScores) {
                int dayIndex = record.getCreateTime().getDayOfWeek().getValue() - 1;
                healthCounts.set(dayIndex, healthCounts.get(dayIndex) + 1);
            }
        }
        // 设置各系列数据
        addSportRecordData.setData(sportCounts);
        addMealRecordData.setData(mealCounts);
        healthTestData.setData(healthCounts);

        vo.setSeries(List.of(addSportRecordData, addMealRecordData, healthTestData));
        return vo;
    }
}