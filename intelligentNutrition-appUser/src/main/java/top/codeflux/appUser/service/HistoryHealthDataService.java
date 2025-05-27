package top.codeflux.appUser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codeflux.appUser.domain.HistoryHealthData;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/27
 */
public interface HistoryHealthDataService extends IService<HistoryHealthData> {
    List<HistoryHealthData> getByStudentNumber(String studentNumber);
}
