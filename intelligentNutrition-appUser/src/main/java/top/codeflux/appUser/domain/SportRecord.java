package top.codeflux.appUser.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author qht
 * @date 2025/6/2
 */
@Data
@Builder
public class SportRecord {
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
    private String exerciseTime;
    // 创建时间
    private String createTime;
}