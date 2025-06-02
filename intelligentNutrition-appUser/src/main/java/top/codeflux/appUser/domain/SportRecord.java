package top.codeflux.appUser.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/6/2
 */
@Data
@Builder
public class SportRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    // 学号
    private String studentNumber;
    // 运动类型/运动名
    private String sportName;
    // 持续时间（分钟）
    private Double duration;
    // 消耗卡路里
    private Double consumeCalorie;
    // 运动时间
    private LocalDateTime exerciseTime;
    // 创建时间
    private LocalDateTime createTime;
    @Override
    public String toString() {
        return "SportRecord{" +
                "id=" + id +
                ", studentNumber='" + studentNumber + '\'' +
                ", sportName='" + sportName + '\'' +
                ", duration=" + duration +
                ", consumeCalorie=" + consumeCalorie +
                ", exerciseTime=" + exerciseTime +
                ", createTime=" + createTime +
                '}';
    }
}