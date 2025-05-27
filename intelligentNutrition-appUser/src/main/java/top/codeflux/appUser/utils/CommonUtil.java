package top.codeflux.appUser.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author qht
 * @date 2025/5/27
 */
public class CommonUtil {
    /**
     *  计算 BMI
     * @param weight 体重 单位kg
     * @param height 身高 单位cm
     * @return bmi值
     */
    public static Double getBmi(Double weight, Double height) {
        double heightInMeters = height / 100.0; // 将身高从厘米转换为米
        double bmi = weight / (heightInMeters * heightInMeters);
        BigDecimal formattedBmi = new BigDecimal(bmi).setScale(2, RoundingMode.HALF_UP);
        return formattedBmi.doubleValue();
    }
}