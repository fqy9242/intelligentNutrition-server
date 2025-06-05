package top.codeflux.appUser.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.codeflux.ai.domain.vo.NutritionIntakeResult;
import top.codeflux.ai.service.AiService;
import top.codeflux.appUser.domain.DrinkingWaterRecord;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.domain.vo.GetHealthScoreVo;
import top.codeflux.appUser.domain.vo.TodayDietaryRecordVo;
import top.codeflux.appUser.domain.vo.TodayRecommendCalorieVo;
import top.codeflux.appUser.service.*;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.appUser.domain.PhysicalExaminationPlan;
import top.codeflux.appUser.domain.dto.AppUserLoginDto;
import top.codeflux.appUser.domain.vo.AppUserLoginVo;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/16
 */
@RestController
@RequestMapping("/client/user")
@Slf4j
public class AppUserClientController extends BaseController {
    @Autowired
    private IAppUserService userService;
    @Autowired
    private PhysicalExaminationPlanService physicalExaminationPlanService;
    @Autowired
    private HistoryHealthDataService historyHealthDataService;
    @Autowired
    private DietaryRecordService dietaryRecordService;
    @Autowired
    private DrinkingWaterRecordService drinkingWaterRecordService;

    // 用户登录
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@RequestBody AppUserLoginDto dto) {
        AppUserLoginVo vo = userService.login(dto);
        return success(vo);
    }

    /**
     *  获取下一次的体检计划
     * @return
     */
    @GetMapping("/physicalExaminationPlan")
//    @Anonymous
    public AjaxResult physicalExaminationPlan() {
        PhysicalExaminationPlan plan  = physicalExaminationPlanService.getPhysicalExaminationPlan();
        return success(plan);
    }
    @GetMapping("/historyHealthData")
//    @Anonymous
    public AjaxResult historyHealthData(String studentNumber) {
        return success(historyHealthDataService.getByStudentNumber(studentNumber));
    }

    /**
     *
     * @param dietaryRecord
     * @return
     */
    @PostMapping("/dietaryRecord")
    public AjaxResult saveDietaryRecord(@RequestBody DietaryRecord dietaryRecord) {
        dietaryRecordService.save(dietaryRecord);
        return success();
    }
    @GetMapping("/dietaryRecord/{studentNumber}")
//    @Anonymous
    public AjaxResult saveDietaryRecord(@PathVariable String studentNumber, Integer onlyToday) {
        if (onlyToday != null && onlyToday == 1) {
            TodayDietaryRecordVo vo  = dietaryRecordService.getTodayByStudentNumber(studentNumber);
            return success(vo);
        } else {
            List<DietaryRecord> list = dietaryRecordService.getByStudentNumber(studentNumber);
            return success(list);
        }
    }
    @GetMapping("/drinkingWaterRecord")
    public AjaxResult drinkingWaterRecord(String studentNumber,  @RequestParam(value = "onlyToday", required = false) Integer onlyToday) {
        return success(drinkingWaterRecordService.getByStudentNumber(studentNumber, onlyToday));
    }
    @PostMapping("/drinkingWaterRecord")
    public AjaxResult saveDrinkingWaterRecord(@RequestBody DrinkingWaterRecord drinkingWaterRecord) {
        drinkingWaterRecordService.save(drinkingWaterRecord);
        return success();
    }

    /**
     * 今日推荐摄入卡路里
     * @param studentNumber
     * @return
     */
    @GetMapping("/todayRecommendCalories/{studentNumber}")
    @Anonymous
    public AjaxResult todayRecommendCalories(@PathVariable String studentNumber) {
        TodayRecommendCalorieVo vo = userService.todayRecommendCalories(studentNumber);
        return success(vo);
    }

    /**
     *  获取健康建议
     * @return
     */
    @Anonymous
    @GetMapping("/healthAdvise/{studentNumber}")
    public AjaxResult healthAdvise(@PathVariable String studentNumber) {
        List<String> adviseList = userService.getHealthAdvise(studentNumber);
        return success(adviseList);
    }

    /**
     * 学生获取营养摄入分析
     * @param studentNumber
     * @return
     */
    @GetMapping("/nutritionAnalysis/{studentNumber}")
    @Anonymous
    public AjaxResult nutritionAnalysis(@PathVariable String studentNumber) {
        NutritionIntakeResult result = userService.nutritionAnalysis(studentNumber);
        return success(result);
    }

    /**
     * 学生获取健康评分
     * @param studentNumber
     * @return
     */
//    @Anonymous
    @GetMapping("/getHealthScore/{studentNumber}")
    public AjaxResult getHealthScore(@PathVariable String studentNumber) {
        GetHealthScoreVo vo = userService.getHealthScore(studentNumber);
        return success(vo);
    }

//    @Anonymous

    /**
     * 统计该学生健康打卡天数
     * @param studentNumber
     * @return
     */
    @GetMapping("/heathCheckInDays/{studentNumber}")
    public AjaxResult heathCheckInDays(@PathVariable String studentNumber) {
        long days = userService.countHeathCheckInDays(studentNumber);
        return success(days);
    }



}