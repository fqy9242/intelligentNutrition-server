package top.codeflux.service;

import top.codeflux.domain.vo.AdvantageExerciseForDayVo;
import top.codeflux.domain.vo.AnalysisIndexVo;
import top.codeflux.domain.vo.CountUserVo;

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
}
