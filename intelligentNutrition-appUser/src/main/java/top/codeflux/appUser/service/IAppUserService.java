package top.codeflux.appUser.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import top.codeflux.common.domain.AppUser;
import top.codeflux.appUser.domain.dto.AppUserLoginDto;
import top.codeflux.appUser.domain.vo.AppUserLoginVo;
import top.codeflux.appUser.domain.vo.AppUserVo;

/**
 * app注册用户Service接口
 * 
 * @author qht
 * @date 2025-05-16
 */
public interface IAppUserService extends IService<AppUser> {
    /**
     * 查询app注册用户
     * 
     * @param id app注册用户主键
     * @return app注册用户
     */
    public AppUser selectAppUserById(String id);

    /**
     * 查询app注册用户列表
     * 
     * @param appUser app注册用户
     * @return app注册用户集合
     */
    public List<AppUserVo> selectAppUserList(AppUser appUser);

    /**
     * 新增app注册用户
     * 
     * @param appUser app注册用户
     * @return 结果
     */
    public int insertAppUser(AppUser appUser);

    /**
     * 修改app注册用户
     * 
     * @param appUser app注册用户
     * @return 结果
     */
    public int updateAppUser(AppUser appUser);

    /**
     * 批量删除app注册用户
     * 
     * @param ids 需要删除的app注册用户主键集合
     * @return 结果
     */
    public int deleteAppUserByIds(String[] ids);

    /**
     * 删除app注册用户信息
     * 
     * @param id app注册用户主键
     * @return 结果
     */
    public int deleteAppUserById(String id);

    /**
     * 用户登录
     * @param dto dto
     * @return vo
     */
    AppUserLoginVo login(AppUserLoginDto dto);
}
