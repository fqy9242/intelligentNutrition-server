package top.codeflux.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/7
 */
@Data
public class BarChartVo {
    /**
     * 横轴数据（时间维度）
     */
    private List<String> xAxis;

    /**
     * 系列数据
     */
    private List<SeriesData> series;
    @Data

    // 内部类：系列数据
    public static class SeriesData {
        /**
         * 系列名称
         */
        private String name;

        /**
         * 数据值
         */
        private List<Integer> data;
    }

}