package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.codeflux.appUser.domain.HistoryHealthData;
import top.codeflux.appUser.mapper.HistoryHealthDataMapper;
import top.codeflux.appUser.service.HistoryHealthDataService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 获取学生的历史健康记录
 * @author qht
 * @date 2025/5/27
 */
@Service
public class HistoryHealthDataServiceImpl extends ServiceImpl<HistoryHealthDataMapper, HistoryHealthData> implements HistoryHealthDataService {
    @Override
//    @Cacheable(value = "userHistoryHealthData", key = "#studentNumber")
    public List<HistoryHealthData> getByStudentNumber(String studentNumber) {
        List<HistoryHealthData> list = lambdaQuery()
                .eq(HistoryHealthData::getStudentNumber, studentNumber)
                .orderByDesc(HistoryHealthData::getCreateTime)
                .list();
        // 计算BMI
        for (HistoryHealthData historyHealthData : list) {
            if (historyHealthData.getWeight() != null && historyHealthData.getHeight() != null && historyHealthData.getHeight() > 0) {
                double heightInMeters = historyHealthData.getHeight() / 100.0; // 将身高从厘米转换为米
                double bmi = historyHealthData.getWeight() / (heightInMeters * heightInMeters);
                BigDecimal formattedBmi = new BigDecimal(bmi).setScale(2, RoundingMode.HALF_UP);
                historyHealthData.setBmi(formattedBmi.doubleValue());
            }
        }
        return list;
    }
}