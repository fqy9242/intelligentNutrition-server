package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.mapper.SportRecordMapper;
import top.codeflux.appUser.service.SportRecordService;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.exception.base.BaseException;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/2
 */
@Service
public class SportRecordServiceImpl extends ServiceImpl<SportRecordMapper, SportRecord> implements SportRecordService {

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
        // TODO 完成这个方法
        return super.save(entity);
    }
}