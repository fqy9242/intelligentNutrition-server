package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.codeflux.ai.service.AiService;

import top.codeflux.appUser.domain.vo.TodayDietaryRecordVo;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.appUser.mapper.DietaryRecordMapper;
import top.codeflux.appUser.service.DietaryRecordService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DietaryRecordServiceImpl extends ServiceImpl<DietaryRecordMapper,  DietaryRecord> implements DietaryRecordService {
    private final AiService aiService;
    private final DietaryRecordMapper dietaryRecordMapper;
    @Override
    public boolean save(DietaryRecord entity) {
        // 设置默认值
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(LocalDateTime.now());
        }
        if (entity.getMealType() == null) {
            // 根据时间段判断餐次
            int hour = entity.getCreateTime().getHour();
            if (hour >= 5 && hour < 10) {
                entity.setMealType(DietaryRecord.BREAKFAST); // 早餐 5:00-10:00
            } else if (hour >= 10 && hour < 15) {
                entity.setMealType(DietaryRecord.LUNCH);     // 午餐 10:00-15:00
            } else if (hour >= 15 && hour < 21) {
                entity.setMealType(DietaryRecord.DINNER);    // 晚餐 15:00-21:00
            } else {
                entity.setMealType(DietaryRecord.OTHER);     // 加餐 其他时间
            }
        }
        DietaryRecord record = aiService.fillDietaryRecord(entity);
        return super.save(entity);
    }

    @Override
    public TodayDietaryRecordVo getTodayByStudentNumber(String studentNumber) {
        // 查询今天早餐记录
        List<DietaryRecord> breakfast = dietaryRecordMapper.selectTodayByStudentNumberAndMealType(studentNumber, DietaryRecord.BREAKFAST);
        // 查询今天午餐记录
        List<DietaryRecord> lunch = dietaryRecordMapper.selectTodayByStudentNumberAndMealType(studentNumber, DietaryRecord.LUNCH);
        // 查询今天晚餐记录
        List<DietaryRecord> dinner = dietaryRecordMapper.selectTodayByStudentNumberAndMealType(studentNumber, DietaryRecord.DINNER);
        // 查询今天加餐记录
        List<DietaryRecord> other = dietaryRecordMapper.selectTodayByStudentNumberAndMealType(studentNumber, DietaryRecord.OTHER);
        // 创建一个vo对象
        TodayDietaryRecordVo vo = new TodayDietaryRecordVo();
        vo.setStudentNumber(studentNumber);
        if (breakfast != null && !breakfast.isEmpty()) {
            vo.setBreakfast(getMeal(breakfast));
        }
        if (lunch != null && !lunch.isEmpty()) {
            vo.setLunch(getMeal(lunch));
        }
        if (dinner != null && !dinner.isEmpty()) {
            vo.setDinner(getMeal(dinner));
        }
        if (other != null && !other.isEmpty()) {
            vo.setOther(getMeal(other));
        }
        return vo;
    }

    /**
     * 根据学生学号查询饮食记录
     *
     * @param studentNumber
     * @return
     */
    @Override
    public List<DietaryRecord> getByStudentNumber(String studentNumber) {
        return lambdaQuery()
                .eq(DietaryRecord::getStudentNumber, studentNumber)
                .orderByDesc(DietaryRecord::getCreateTime)
                .list();
    }


    public TodayDietaryRecordVo.Meal getMeal(List<DietaryRecord> records) {
        TodayDietaryRecordVo.Meal meal = new TodayDietaryRecordVo.Meal();
        switch (records.get(0).getMealType()) {
            case 0 -> meal.setMealName("早餐");
            case 1  -> meal.setMealName("午餐");
            case 2 -> meal.setMealName("晚餐");
            case 3 -> meal.setMealName("加餐");
        }
        meal.setFoodList(records.stream().map(DietaryRecord::getFoodName).toList());
        meal.setTotalWeight(records.stream().mapToDouble(DietaryRecord::getFoodWeight).sum());
        meal.setTotalCalories(records.stream().mapToDouble(DietaryRecord::getFoodCalorie).sum());
        meal.setMealTime(records.get(0).getCreateTime());
        return meal;
    }
}