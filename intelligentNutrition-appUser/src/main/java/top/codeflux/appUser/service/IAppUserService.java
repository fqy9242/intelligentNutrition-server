package top.codeflux.appUser.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.codeflux.ai.domain.vo.NutritionIntakeResult;
import top.codeflux.ai.domain.vo.ThisWeekNutritionTrendVo;
import top.codeflux.appUser.domain.PhysicalExaminationPlan;
import top.codeflux.appUser.domain.dto.AppUserDto;
import top.codeflux.appUser.domain.vo.GetHealthScoreVo;
import top.codeflux.appUser.domain.vo.TodayRecommendCalorieVo;
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
    public AppUserVo selectAppUserById(String id);

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
    public int insertAppUser(AppUserDto dto);

    /**
     * 修改app注册用户
     * 
     * @param appUser app注册用户
     * @return 结果
     */
    public int updateAppUser(AppUserDto dto);

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

    /**
     * 解析Excel文件并转换为AppUser列表
     * @param excelFile
     * @return
     */

    List<AppUserDto> parseExcelToAppUserList(MultipartFile excelFile);

    /**
     * 批量插入到AppUser
     * @param dtoList
     * @return
     */
    int insertAppUserBatch(List<AppUserDto> dtoList);

    /**
     * 获取今日推荐摄入卡路里
     * @param studentNumber
     * @return
     */
    TodayRecommendCalorieVo todayRecommendCalories(String studentNumber);

    /**
     *  获取健康建议
     * @param studentNumber
     * @return
     */
    List<String> getHealthAdvise(String studentNumber);

    /**
     * 统计今日摄入热量
     * @param studentNumber 学号
     * @return 今日摄入热量
     */
    double calculateTodayCalorie(String studentNumber);

    /***
     * 用户获取健康评分
     * @return
     */
    GetHealthScoreVo getHealthScore(String studentNumber);

    /**
     * 营养摄入量分析
     * @param studentNumber
     * @return
     */
    NutritionIntakeResult nutritionAnalysis(String studentNumber);

    /**
     *  统计健康打卡天数
     * @param studentNumber
     * @return
     */
    long countHeathCheckInDays(String studentNumber);

    /**
     * 获取本周营养趋势
     * @param studentNumber 学号
     * @return 返回
     */
    List<ThisWeekNutritionTrendVo> getThisWeekNutritionTrend(String studentNumber);
}
