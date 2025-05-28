package top.codeflux.appUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.codeflux.ai.service.AiService;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.appUser.mapper.DietaryRecordMapper;
import top.codeflux.appUser.service.DietaryRecordService;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/5/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DietaryRecordServiceImpl extends ServiceImpl<DietaryRecordMapper, DietaryRecord> implements DietaryRecordService {
    private final AiService aiService;
    @Override
    public boolean save(DietaryRecord entity) {
        // 设置默认值
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(LocalDateTime.now());
        }
        DietaryRecord record = aiService.fillDietaryRecord(entity);
        return super.save(entity);
    }
}