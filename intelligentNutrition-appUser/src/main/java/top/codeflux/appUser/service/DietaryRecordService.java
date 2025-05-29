package top.codeflux.appUser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codeflux.appUser.domain.vo.TodayDietaryRecordVo;
import top.codeflux.common.domain.DietaryRecord;

/**
 * @author qht
 * @date 2025/5/27
 */
public interface DietaryRecordService extends IService<DietaryRecord> {

    TodayDietaryRecordVo getTodayByStudentNumber(String studentNumber);
}
