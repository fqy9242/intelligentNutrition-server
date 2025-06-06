package top.codeflux.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/6
 */
@Data
public class ChartVo<T> {
    private List<String> xAxis;
    private List<T> yXis;
}