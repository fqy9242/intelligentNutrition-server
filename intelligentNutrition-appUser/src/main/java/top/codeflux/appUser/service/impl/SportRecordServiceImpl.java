package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.codeflux.ai.service.AiService;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.mapper.SportRecordMapper;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.appUser.service.SportRecordService;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.domain.AppUser;
import top.codeflux.common.exception.base.BaseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author qht
 * @date 2025/6/2
 */
@Service
@RequiredArgsConstructor
public class SportRecordServiceImpl extends ServiceImpl<SportRecordMapper, SportRecord> implements SportRecordService {
    private final AiService aiService;
    private final IAppUserService userService;
    /**
     * 根据学生学号查询运动记录
     *
     * @param studentNumber 学生学号
     * @param onlyToday     是否只查询今天的记录
     * @return 运动记录列表
     */
    @Override
    public List<SportRecord> getByStudentNumber(String studentNumber, Integer onlyToday) {
        if (studentNumber == null || studentNumber.isEmpty()) {
            throw new BaseException(ResponseMessage.STUDENT_NUMBER_NOT_NULL);
        }
        var query = lambdaQuery().eq(SportRecord::getStudentNumber, studentNumber);
        if (onlyToday != null && onlyToday == 1) {
            query.last("AND DATE(exercise_time) = CURDATE()");
        }
        return query.list();
    }

    /**
     * 获取本周运动次数
     *
     * @param studentNumber
     * @return
     */
    @Override
    public long getTotalThisWeekSport(String studentNumber) {
        if (studentNumber == null || studentNumber.isEmpty()) {
            throw new BaseException(ResponseMessage.STUDENT_NUMBER_NOT_NULL);
        }
        // 获取今天是星期几
        int weekValue = LocalDate.now().getDayOfWeek().getValue();
        Long count = lambdaQuery().eq(SportRecord::getStudentNumber, studentNumber)
                .ge(SportRecord::getExerciseTime, LocalDate.now().minusDays(weekValue - 1))
                .le(SportRecord::getExerciseTime, LocalDate.now().plusDays(1))
                .count();
        return count == null ? 0 : count;
    }

    /**
     * 保存运动记录
     *
     * @param entity 运动记录实体
     * @return 是否保存成功
     */
    @Override
    public boolean save(SportRecord entity) {
        if (entity.getStudentNumber() == null || entity.getStudentNumber().isEmpty()) {
            throw new BaseException(ResponseMessage.STUDENT_NUMBER_NOT_NULL);
        }
        entity.setCreateTime(LocalDateTime.now());
        if (entity.getConsumeCalorie() == null) {
             // 如果消耗卡路里为空，则调用AI服务计算
            // 根据学号查询学生信息
            AppUser user = userService.lambdaQuery().eq(AppUser::getStudentNumber, entity.getStudentNumber()).one();
            // 调用ai服务计算运动消耗的卡路里
            double calorie = aiService.calculateSportCalorie(user,
                    entity.getSportName(), entity.getDuration());
            entity.setConsumeCalorie(calorie);
        }
        return super.save(entity);
    }
}