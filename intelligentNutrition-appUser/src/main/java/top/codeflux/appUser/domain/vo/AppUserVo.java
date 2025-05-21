package top.codeflux.appUser.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codeflux.appUser.domain.Allergen;
import top.codeflux.common.annotation.Excel;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private String id;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNumber;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;
    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 身高 */
    @Excel(name = "身高")
    private Long height;

    /** 体重 */
    @Excel(name = "体重")
    private Long weight;
    List<Allergen> allergenList;
}