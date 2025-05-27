package top.codeflux.appUser.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/5/26
 */
@Data
public class PhysicalExaminationPlan implements Serializable {
    private Integer id;
    private String place;
    private LocalDateTime physicalExaminationTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}