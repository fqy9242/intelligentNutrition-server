package top.codeflux.appUser.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.codeflux.common.annotation.Excel;
import top.codeflux.common.core.domain.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 过敏原对象 allergen
 * 
 * @author qht
 * @date 2025-05-16
 */
@Data
@Builder
public class Allergen implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    // 学号
    private String studentNumber;

    private String allergen;
    // 创建时间
    private LocalDateTime createTime;


}
