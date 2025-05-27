package top.codeflux.appUser.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.codeflux.appUser.domain.PhysicalExaminationPlan;
import top.codeflux.appUser.domain.dto.AppUserLoginDto;
import top.codeflux.appUser.domain.vo.AppUserLoginVo;
import top.codeflux.appUser.service.HistoryHealthDataService;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.appUser.service.PhysicalExaminationPlanService;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;

/**
 * @author qht
 * @date 2025/5/16
 */
@RestController
@RequestMapping("/client/user")
public class AppUserClientController extends BaseController {
    @Autowired
    private IAppUserService userService;
    @Autowired
    private PhysicalExaminationPlanService physicalExaminationPlanService;
    @Autowired
    private HistoryHealthDataService historyHealthDataService;

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
    @Anonymous
    public AjaxResult historyHealthData(String studentNumber) {
        return success(historyHealthDataService.getByStudentNumber(studentNumber));
    }
}