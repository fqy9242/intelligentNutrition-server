package top.codeflux.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/6/3
 */
@Data
public class CountUserVo {
    // 现在的总注册用户数
    private Long total;
    // 上个月的总用户数
    private Long lastMonthTotal;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;
}