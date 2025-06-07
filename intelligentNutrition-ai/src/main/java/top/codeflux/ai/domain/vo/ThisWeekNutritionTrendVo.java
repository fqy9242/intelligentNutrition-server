package top.codeflux.ai.domain.vo;

import lombok.Data;

/**
 * @author qht
 * @date 2025/6/7
 */
@Data
public class ThisWeekNutritionTrendVo {
    // 例如: 周一
    private String day;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double vitamins;
}