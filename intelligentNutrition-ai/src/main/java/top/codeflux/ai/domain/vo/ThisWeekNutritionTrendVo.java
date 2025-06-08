package top.codeflux.ai.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qht
 * @date 2025/6/7
 */
@Data
public class ThisWeekNutritionTrendVo implements Serializable {
    // 例如: 周一
    private String day;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double vitamins;
}