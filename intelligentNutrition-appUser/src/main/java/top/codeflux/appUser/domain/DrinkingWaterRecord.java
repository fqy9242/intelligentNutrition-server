package top.codeflux.appUser.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/5/31
 */
@Data
public class DrinkingWaterRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String studentNumber;
    // 喝水容量
    private String capacity;
    // 喝水时间
    private LocalDateTime drinkingTime;
}