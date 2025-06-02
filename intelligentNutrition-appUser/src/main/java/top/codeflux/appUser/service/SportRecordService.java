package top.codeflux.appUser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codeflux.appUser.domain.SportRecord;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/2
 */
public interface SportRecordService extends IService<SportRecord> {
    /**
     * 根据学生学号查询运动记录
     *
     * @param studentNumber 学生学号
     * @param onlyToday     是否只查询今天的记录
     * @return 运动记录列表
     */
    List<SportRecord> getByStudentNumber(String studentNumber, Integer onlyToday);
}
