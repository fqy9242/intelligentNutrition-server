package top.codeflux.common.domain.vo.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qht
 * @date 2025/6/6
 */
@Data
public class PieChartVo implements Serializable {
    private List<String> name;
    private List<Integer> value;
//    private List<Double> percentage;
}