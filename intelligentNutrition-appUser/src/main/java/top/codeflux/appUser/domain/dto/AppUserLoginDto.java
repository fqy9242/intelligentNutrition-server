package top.codeflux.appUser.domain.dto;


import lombok.Data;

/**
 * @author qht
 * @date 2025/5/16
 */
@Data
public class AppUserLoginDto {
    private String studentNumber;
    private String password;
}