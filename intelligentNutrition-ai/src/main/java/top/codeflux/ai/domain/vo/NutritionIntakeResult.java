package top.codeflux.ai.domain.vo;

import lombok.Data;
import top.codeflux.common.domain.vo.BaseVo;

import java.io.Serializable;

/**
 * @author qht
 * @date 2025/6/4
 */
@Data
public class NutritionIntakeResult implements Serializable {
    // 蛋白质
    private String protein;
    // 脂肪
    private String fat;
    // 碳水化合物
    private String carbohydrate;
    // 维生素
    private String vitamin;

}