package top.codeflux.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.codeflux.appUser.domain.HealthScore;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.service.HealthScoreService;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.appUser.service.SportRecordService;
import top.codeflux.common.domain.AppUser;
import top.codeflux.domain.vo.AdvantageExerciseForDayVo;
import top.codeflux.domain.vo.AnalysisIndexVo;
import top.codeflux.domain.vo.CountUserVo;
import top.codeflux.service.AdministratorAnalysisService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
}