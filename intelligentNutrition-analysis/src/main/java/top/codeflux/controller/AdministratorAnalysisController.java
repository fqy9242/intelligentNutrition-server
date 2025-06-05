package top.codeflux.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;
import top.codeflux.domain.vo.AdvantageExerciseForDayVo;
import top.codeflux.domain.vo.AnalysisIndexVo;
import top.codeflux.domain.vo.CountUserVo;
import top.codeflux.service.AdministratorAnalysisService;

/**
 * @author qht
 * @date 2025/6/3
 */
@RestController
@Slf4j
@RequestMapping("/admin/analysis")
@RequiredArgsConstructor
@Anonymous // TODO 注释掉该注解
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
}