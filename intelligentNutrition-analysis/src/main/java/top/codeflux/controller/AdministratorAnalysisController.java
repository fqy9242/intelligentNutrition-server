package top.codeflux.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;
import top.codeflux.common.domain.vo.chart.PieChartVo;
import top.codeflux.domain.vo.*;
import top.codeflux.service.AdministratorAnalysisService;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/3
 */
@RestController
@Slf4j
@Anonymous
@RequestMapping("/admin/analysis")
@RequiredArgsConstructor
// TODO 使用AnalysisIndexVo替换可替换的VO类型
public class AdministratorAnalysisController extends BaseController {
    private final AdministratorAnalysisService analysisService;
    /**
     * 统计注册用户数量
     * @return
     */
//    @Anonymous
    @GetMapping("/countUser")
    public AjaxResult countUser() {
        CountUserVo vo = analysisService.countUser();
        return success(vo);
    }

    /**
     * 统计用户日均运动时长
     * @return
     */
    @GetMapping("/advantageExerciseForDay")
    public AjaxResult advantageExerciseForDay() {
        AdvantageExerciseForDayVo vo = analysisService.advantageExerciseForDay();
        return success(vo);
    }

    /**
     * 统计用户本周平均健康分数
     * @return
     */
    @GetMapping("/thisWeekAdvHealthScore")
    public AjaxResult thisWeekAdvHealthScore() {
        AnalysisIndexVo<Double> vo = analysisService.getThisWeekAdvHealthScore();
        return success(vo);
    }

    /**
     * 统计今日健康打卡人数
     */
    @GetMapping("/countTodayHealthCheckIn")
    public AjaxResult countTodayHealthCheckIn() {
        AnalysisIndexVo<Long> vo = analysisService.countTodayHealthCheckIn();
        return success(vo);
    }

    /**
     * 统计用户增长趋势
     * @return
     */
    @GetMapping("/userTrend")
    public AjaxResult userTrend() {
        ChartVo<Long> vo = analysisService.getUserTrend();
        return success(vo);
    }

    /**
     * bmi平均值趋势
     * @return
     */
    @GetMapping("/bmiAdvTrendForMonth")
    public AjaxResult bmiAdvTrendForMonth() {
        ChartVo<Integer> vo = analysisService.getBmiAdvTrendForMonth();
        return success(vo);
    }

    /**
     * 营养成分分布
     * @return
     */
    @GetMapping("/analysisNutritional")
    public AjaxResult analysisNutritional() {
        PieChartVo vo = analysisService.analysisNutritional();
        return success(vo);
    }
}