package top.codeflux.appUser.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/6/4
 */
@Data
@Builder
public class GetHealthScoreVo {
    // 现在的分数
    private Double nowScore;
    // 上次分数
    private Double lastScore;
    // 返回时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;
}