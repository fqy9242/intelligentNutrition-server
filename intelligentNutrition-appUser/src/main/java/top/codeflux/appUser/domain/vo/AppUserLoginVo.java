package top.codeflux.appUser.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author qht
 * @date 2025/5/16
 */
@Data
@Builder
public class AppUserLoginVo {
    // 令牌
    private String token;
}