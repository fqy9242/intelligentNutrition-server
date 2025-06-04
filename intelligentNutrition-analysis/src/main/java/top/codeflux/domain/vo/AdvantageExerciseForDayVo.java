package top.codeflux.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/6/4
 */
@Data
public class AdvantageExerciseForDayVo {
    // 这个月用户日均运动时间
    private Double dayAdvExerciseMinute;
    // 上个月用户日均运动时间
    private Double lastMonthDayAdvExerciseMinute;
    // 返回时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;
}