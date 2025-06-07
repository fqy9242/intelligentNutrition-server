package top.codeflux.service;

import top.codeflux.common.domain.vo.chart.PieChartVo;
import top.codeflux.domain.vo.*;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/4
 */
public interface AdministratorAnalysisService {
    /**
     * 统计用户数量
     * @return
     */
    CountUserVo countUser();

    /**
     * 统计用户这个月的日均运动时长 以及上个月
     * @return
     */
    AdvantageExerciseForDayVo advantageExerciseForDay();

    /**
     * 统计本周平均健康分数
     * @return
     */
    AnalysisIndexVo<Double> getThisWeekAdvHealthScore();

    /**
     * 统计今日健康打卡人数
     */
    AnalysisIndexVo<Long> countTodayHealthCheckIn();

    /**
     * 统计用户增长趋势
     * @return
     */
    ChartVo<Long> getUserTrend();

    /**
     * 统计bmi平均值趋势
     * @return
     */
    ChartVo<Integer> getBmiAdvTrendForMonth();

    /**
     *  营养成分分布
     * @return
     */
    PieChartVo analysisNutritional();

    /**
     * 统计用户活动
     * @return
     */
    BarChartVo analysisUserActivity();

    /**
     * 统计用户平均bmi趋势
     * @return
     */
    ChartVo<Double> bmiAdvTrend();
}
