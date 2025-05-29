package top.codeflux.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/5/27
 */
@Data
@Builder
public class DietaryRecord {
    // 早餐
    public final static Integer BREAKFAST = 0;
    // 午餐
    public final static Integer LUNCH = 1;
    //晚餐
    public final static Integer DINNER = 2;
    // 加餐
    public final static Integer OTHER = 3;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String studentNumber;
    // 餐次
    private Integer mealType;
    // 食物名称
    private String foodName;
    // 食物重量
    private Double foodWeight;
    // 食物热量
    private Double foodCalorie;
    // 食物蛋白质
    private Double foodProtein;
    // 膳食纤维
    private Double foodDietaryFiber;
    // 食物脂肪
    private Double foodFat;
    // 食物碳水化合物
    private Double foodCarbohydrate;
    // 创建时间
    private LocalDateTime createTime;

}