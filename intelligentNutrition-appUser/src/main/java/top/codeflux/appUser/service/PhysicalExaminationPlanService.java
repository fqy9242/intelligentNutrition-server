package top.codeflux.appUser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codeflux.appUser.domain.PhysicalExaminationPlan;

/**
 * @author qht
 * @date 2025/5/26
 */
public interface PhysicalExaminationPlanService extends IService<PhysicalExaminationPlan> {
    PhysicalExaminationPlan getPhysicalExaminationPlan();
}
