package top.codeflux.ai.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRecognitionResult implements Serializable {
    private String name;
    // 多少卡 例如:350kcal
    private String calories;
    // 推荐星级
    private Integer star;
    // 适合人群
    private String suitablePeople;
    private List<nutrient> nutrients;
    @Data
    static class nutrient {
        private String name;
        private String value;
    }
}