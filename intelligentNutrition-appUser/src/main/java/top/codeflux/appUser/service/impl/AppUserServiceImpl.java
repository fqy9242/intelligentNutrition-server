package top.codeflux.appUser.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import top.codeflux.appUser.domain.dto.AppUserLoginDto;
import top.codeflux.appUser.domain.vo.AppUserLoginVo;
import top.codeflux.appUser.domain.vo.AppUserVo;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.core.domain.model.LoginUser;
import top.codeflux.common.exception.base.BaseException;
import top.codeflux.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import top.codeflux.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import top.codeflux.appUser.domain.Allergen;
import top.codeflux.appUser.mapper.AppUserMapper;
import top.codeflux.common.domain.AppUser;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.framework.web.service.TokenService;

/**
 * app注册用户Service业务层处理
 * 
 * @author qht
 * @date 2025-05-16
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService
{
    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询app注册用户
     * 
     * @param id app注册用户主键
     * @return app注册用户
     */
    @Override
    public AppUserVo selectAppUserById(String id)
    {
        // 获取数据库实体对象
        AppUser appUser = appUserMapper.selectAppUserById(id);
        // 查询用户过敏源
        List<Allergen> allergens = appUserMapper.selectAllergenByStudentNumber(appUser.getStudentNumber());
        // 创建一个vo对象 并绑定过敏源
        AppUserVo vo = new AppUserVo();
        vo.setAllergenList(allergens);
        // 拷贝属性
        BeanUtils.copyProperties(appUser, vo);
        return vo;
    }

    /**
     * 查询app注册用户列表
     * 
     * @param appUser app注册用户
     * @return app注册用户
     */
    @Override
    public List<AppUserVo> selectAppUserList(AppUser appUser) {
        List<AppUserVo> vos = new ArrayList<>();
        List<AppUser> users = appUserMapper.selectAppUserList(appUser);
        users.forEach(user -> {
            AppUserVo vo = new AppUserVo();
            // 拷贝属性
            BeanUtils.copyProperties(user, vo);
            // 查询用户过敏源信息 并绑定到vo对象上
            vo.setAllergenList(appUserMapper.selectAllergenByStudentNumber(user.getStudentNumber()));
            vos.add(vo);
        });
        return vos;
    }

    /**
     * 新增app注册用户
     * 
     * @param appUser app注册用户
     * @return 结果
     */
    @Transactional
    @Override
    public int insertAppUser(AppUser appUser) {
        appUser.setCreateTime(DateUtils.getNowDate());
        int rows = appUserMapper.insertAppUser(appUser);
//        insertAllergen(appUser);
        return rows;
    }

    /**
     * 修改app注册用户
     * 
     * @param appUser app注册用户
     * @return 结果
     */
    @Transactional
    @Override
    public int updateAppUser(AppUser appUser)
    {
        appUser.setUpdateTime(DateUtils.getNowDate());
        appUserMapper.deleteAllergenByStudentId(appUser.getId());
//        insertAllergen(appUser);
        return appUserMapper.updateAppUser(appUser);
    }

    /**
     * 批量删除app注册用户
     * 
     * @param ids 需要删除的app注册用户主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAppUserByIds(String[] ids)
    {
        appUserMapper.deleteAllergenByStudentIds(ids);
        return appUserMapper.deleteAppUserByIds(ids);
    }

    /**
     * 删除app注册用户信息
     * 
     * @param id app注册用户主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAppUserById(String id)
    {
        appUserMapper.deleteAllergenByStudentId(id);
        return appUserMapper.deleteAppUserById(id);
    }

    /**
     * 用户登录
     *
     * @param dto dto
     * @return vo
     */
    @Override
    public AppUserLoginVo login(AppUserLoginDto dto) {
        // 获取加密后的密码
        String encryptPassword = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());
        AppUser user = lambdaQuery().eq(AppUser::getStudentNumber, dto.getStudentNumber())
                .eq(AppUser::getPassword, encryptPassword).one();
        if (user == null) {
            throw new BaseException(ResponseMessage.USERNAME_OR_PASSWORD_INCORRECT);
        }
        return getUserLoginVo(user);
    }

    /**
     * 新增过敏原信息
     * 
     * @param appUser app注册用户对象
     */
    public void insertAllergen(AppUserVo appUser) {
        List<Allergen> allergenList = appUser.getAllergenList();
//        List<Allergen> allergenList = null;
        String id = appUser.getId();
        if (StringUtils.isNotNull(allergenList))
        {
            List<Allergen> list = new ArrayList<>();
            for (Allergen allergen : allergenList)
            {
                allergen.setStudentId(id);
                list.add(allergen);
            }
            if (list.size() > 0)
            {
                appUserMapper.batchAllergen(list);
            }
        }
    }

    @NotNull
    private AppUserLoginVo getUserLoginVo(AppUser user) {
        AppUserLoginVo vo = new AppUserLoginVo();
        BeanUtils.copyProperties(user, vo);
        LoginUser loginUser = new LoginUser();
        loginUser.setAppUser(user);
        String token = tokenService.createToken(loginUser);
        vo.setToken(token);
        // 设置IBM
        if (user.getWeight() != null && user.getHeight() != null) {
            double heightInMeters = user.getHeight() / 100.0; // 将身高从厘米转换为米
            double bmi = user.getWeight() / (heightInMeters * heightInMeters);
            BigDecimal formattedBmi = new BigDecimal(bmi).setScale(2, RoundingMode.HALF_UP);
            vo.setBMI(formattedBmi.doubleValue());
        }
        return vo;
    }
}
