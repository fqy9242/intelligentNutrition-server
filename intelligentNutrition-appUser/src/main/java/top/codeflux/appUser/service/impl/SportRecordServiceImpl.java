package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.codeflux.ai.service.AiService;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.mapper.SportRecordMapper;
import top.codeflux.appUser.service.SportRecordService;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.exception.base.BaseException;

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
        entity.setCreateTime(new Date().toString());
        if (entity.getConsumeCalorie() == null) {
            // TODO 调用ai计算
            // 如果消耗卡路里为空，则调用AI服务计算
//            double calorie = aiService.calculateSportCalorie();
//            entity.setConsumeCalorie(calorie);
        }
        return super.save(entity);
    }
}