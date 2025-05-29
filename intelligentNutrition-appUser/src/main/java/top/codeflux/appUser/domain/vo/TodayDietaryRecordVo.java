package top.codeflux.appUser.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayDietaryRecordVo {
    private String studentNumber;
    private Meal breakfast;
    private Meal lunch;
    private Meal dinner;
    private Meal other;
    @Data
    @NoArgsConstructor
    static
    public class Meal {
        private String mealName;
        private List<String> foodList;
        // 进餐时间
        private LocalDateTime mealTime;
        // 总重量
        private Double totalWeight;
        // 总热量
        private Double totalCalories;
    }
}
