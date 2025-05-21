package top.codeflux.appUser.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserLoginVo {
    private Integer id;
    private String name;
    private String avatar;
    private String studentNumber;
    private Double height;
    private Double weight;
    private List<String> allergen;
    // 过敏源
    private Double BMI;
    // 本周运动次数
    private Integer countThisWeekSport;
    // 健康打卡
    private Integer countHealthCheckIn;
    // 令牌
    private String token;
}