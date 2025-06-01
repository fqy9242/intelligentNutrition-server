package top.codeflux.appUser.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.codeflux.common.domain.AppUser;
import top.codeflux.appUser.domain.Allergen;

/**
 * app注册用户Mapper接口
 * 
 * @author qht
 * @date 2025-05-16
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser>
{
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
    public List<AppUser> selectAppUserList(AppUser appUser);

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
     * 删除app注册用户
     * 
     * @param id app注册用户主键
     * @return 结果
     */
    public int deleteAppUserById(String id);

    /**
     * 批量删除app注册用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppUserByIds(String[] ids);

    /**
     * 批量删除过敏原
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAllergenByStudentNumbers(String[] ids);
    
    /**
     * 批量新增过敏原
     * 
     * @param allergenList 过敏原列表
     * @return 结果
     */
    public int batchAllergen(List<Allergen> allergenList);
    @Select("select * from allergen where student_number = #{studentNumber}")
    List<Allergen> selectAllergenByStudentNumber(String studentNumber);

    /**
     * 通过app注册用户主键删除过敏原信息
     * 
     * @param id app注册用户ID
     * @return 结果
     */
    public int deleteAllergenByStudentNumber(String studentNumber);
    String[] selectStudentNumberByIds(String[] ids);
}
