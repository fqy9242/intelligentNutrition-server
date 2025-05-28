package top.codeflux.common.domain;

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
    public static Integer BREAKFAST = 0;
    // 午餐
    public static Integer BUNCH = 1;
    //晚餐
    public static Integer DINNER = 2;
    // 加餐
    public static Integer OTHER = 3;
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
    // 食物脂肪
    private Double foodFat;
    // 食物碳水化合物
    private Double foodDietaryFiber;
    // 创建时间
    private LocalDateTime createTime;

}