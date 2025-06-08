package top.codeflux.appUser.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/6/4
 */
@Data
public class TodayRecommendCalorieVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 推荐值
    private Double recommendValue;
    // 今日摄入量
    private Double todayCalorie;
    // 返回时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;
}