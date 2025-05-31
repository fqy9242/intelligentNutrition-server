package top.codeflux.appUser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codeflux.appUser.domain.DrinkingWaterRecord;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/31
 */
public interface DrinkingWaterRecordService extends IService<DrinkingWaterRecord> {
    List <DrinkingWaterRecord> getByStudentNumber(String studentNumber, Integer todayOnly);
}
