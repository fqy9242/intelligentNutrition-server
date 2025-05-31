package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codeflux.appUser.domain.DrinkingWaterRecord;
import top.codeflux.appUser.mapper.DrinkingWaterRecordMapper;
import top.codeflux.appUser.service.DrinkingWaterRecordService;

import java.time.LocalDate;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/31
 */
@Service
public class DrinkingWaterRecordServiceImpl extends ServiceImpl<DrinkingWaterRecordMapper, DrinkingWaterRecord> implements DrinkingWaterRecordService {

    @Override
    public List<DrinkingWaterRecord> getByStudentNumber(String studentNumber, Integer todayOnly) {
        var query = lambdaQuery()
                .eq(DrinkingWaterRecord::getStudentNumber, studentNumber);
        if (todayOnly == 1) {
            // 如果只查询今天的饮水记录
            LocalDate today = LocalDate.now();
            query = query.ge(DrinkingWaterRecord::getDrinkingTime, today.atStartOfDay())
                         .lt(DrinkingWaterRecord::getDrinkingTime, today.plusDays(1).atStartOfDay());
        }
        return query.list();
    }
}