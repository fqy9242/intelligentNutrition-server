package top.codeflux.appUser.domain.dto;

import lombok.Data;
import top.codeflux.appUser.domain.Allergen;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/26
 */
@Data
public class AppUserDto {
    // 密码
    private String password;
    /** 学号 */
    private String studentNumber;

    /** 姓名 */
    private String name;
    /** 头像 */
    private String avatar;

    /** 身高 */
    private Double height;

    /** 体重 */
    private Double weight;
    List<Allergen> allergenList;
}