package top.codeflux.appUser.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codeflux.appUser.domain.dto.AppUserLoginDto;
import top.codeflux.appUser.domain.vo.AppUserLoginVo;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;

/**
 * @author qht
 * @date 2025/5/16
 */
@RestController
@RequestMapping("/client/user")
public class AppUserClientController extends BaseController {
    @Autowired
    private IAppUserService userService;
    // 用户登录
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@RequestBody AppUserLoginDto dto) {
        AppUserLoginVo vo = userService.login(dto);
        return success(vo);
    }
}