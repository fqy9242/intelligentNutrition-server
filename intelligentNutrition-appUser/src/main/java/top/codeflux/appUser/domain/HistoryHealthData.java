package top.codeflux.appUser.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/5/27
 */
@Data
public class HistoryHealthData {
    private Integer id;
    private String studentNumber;
    private Double weight;
    private Double height;
    @TableField(exist = false)
    private Double bmi;
    private LocalDateTime createTime;
}