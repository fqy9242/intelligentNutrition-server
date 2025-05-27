package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.codeflux.appUser.domain.PhysicalExaminationPlan;
import top.codeflux.appUser.mapper.PhysicalExaminationPlanMapper;
import top.codeflux.appUser.service.PhysicalExaminationPlanService;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/5/26
 */
@Service

public class PhysicalExaminationPlanServiceImpl extends ServiceImpl<PhysicalExaminationPlanMapper, PhysicalExaminationPlan> implements PhysicalExaminationPlanService {
    @Override
    @Cacheable(value = "physicalExaminationPlan", key = "#result != null ? #result.id : 'default'")
    public PhysicalExaminationPlan getPhysicalExaminationPlan() {
        // select * from physical_examination_plan WHERE physical_examination_plan.physical_examination_time > NOW() ORDER BY physical_examination_plan.physical_examination_time LIMIT 1
        return lambdaQuery()
                .gt(PhysicalExaminationPlan::getPhysicalExaminationTime, LocalDateTime.now()) // physical_examination_time > NOW()
                .orderByAsc(PhysicalExaminationPlan::getPhysicalExaminationTime) // ORDER BY physical_examination_time ASC
                .last("LIMIT 1") // LIMIT 1
                .one(); // 获取单条记录
    }
}