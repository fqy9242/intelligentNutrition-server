package top.codeflux.appUser.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import top.codeflux.ai.domain.vo.NutritionIntakeResult;
import top.codeflux.ai.service.AiService;
import top.codeflux.appUser.domain.*;
import top.codeflux.appUser.domain.dto.AppUserLoginDto;
import top.codeflux.appUser.domain.dto.AppUserDto;
import top.codeflux.appUser.domain.vo.AppUserLoginVo;
import top.codeflux.appUser.domain.vo.AppUserVo;
import top.codeflux.appUser.domain.vo.GetHealthScoreVo;
import top.codeflux.appUser.domain.vo.TodayRecommendCalorieVo;
import top.codeflux.appUser.service.*;
import top.codeflux.appUser.utils.CommonUtil;
import top.codeflux.common.constant.OptionConstants;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.core.domain.model.LoginUser;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.common.exception.base.BaseException;
import top.codeflux.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Objects;

import top.codeflux.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import top.codeflux.appUser.mapper.AppUserMapper;
import top.codeflux.common.domain.AppUser;
import top.codeflux.framework.web.service.TokenService;

/**
 * app注册用户Service业务层处理
 * 
 * @author qht
 * @date 2025-05-16
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {
    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AiService aiService;
    @Autowired
    @Lazy
    private SportRecordService sportRecordService;
    @Autowired
    private DietaryRecordService dietaryRecordService;
    @Autowired
    private HealthScoreService healthScoreService;
    @Autowired
    private HealthCheckInService healthCheckInService;

    /**
     * 查询app注册用户
     * 
     * @param id app注册用户主键
     * @return app注册用户
     */
    @Override
    public AppUserVo selectAppUserById(String id) {
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
     */
    @Transactional
    @Override
    public int insertAppUser(AppUserDto dto) {
        AppUser user = new AppUser();
        BeanUtils.copyProperties(dto, user);
        if (user.getPassword() == null) {
            // 设置默认密码
            user.setPassword(DigestUtils.md5DigestAsHex(user.getStudentNumber().getBytes()));
        } else {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        appUserMapper.insertAppUser(user);
        insertAllergen(dto.getStudentNumber(), dto.getAllergenList());
        return 1;
    }

    /**
     * 修改app注册用户
     */
    @Transactional
    @Override
    public int updateAppUser(AppUserDto dto) {
        // 创建一个实体对象 并将dto的属性拷贝过去
        AppUser user = new AppUser();
        BeanUtils.copyProperties(dto, user);
        user.setUpdateTime(LocalDateTime.now());
        // 调用接口更新用户表
        appUserMapper.updateAppUser(user);
        // 先删除该用户所有的过敏源信息
        appUserMapper.deleteAllergenByStudentNumber(user.getStudentNumber());
        // 在批量插入该用户的过敏源信息
        insertAllergen(dto.getStudentNumber(),dto.getAllergenList());
        return 1;
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
        // 根据id查询学号
        String[] studentNumbers = appUserMapper.selectStudentNumberByIds(ids);
        // 删除所有过敏源信息
        appUserMapper.deleteAllergenByStudentNumbers(studentNumbers);
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
        appUserMapper.deleteAllergenByStudentNumber(id);
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
     * 解析Excel文件并转换为AppUser列表
     *
     * @param excelFile
     * @return
     */
    @Override
    public List<AppUserDto> parseExcelToAppUserList(MultipartFile excelFile) {
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet sheet = xssfWorkbook.getSheet("template");
            if (sheet == null) {
                throw new BaseException(ResponseMessage.EXCEL_FORMAT_ERROR);
            }
            List<AppUserDto> list = new ArrayList<>();
            // 获取最后一行的索引
            int rowNum = sheet.getLastRowNum();
            for (int i = 3; i <= rowNum; i++) {
                // 从第四行开始 因为前三行是表头
                // 获取行
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                // 构建user对象
                AppUserDto user = new AppUserDto();
                user.setStudentNumber(row.getCell(0).getStringCellValue());
                user.setName(row.getCell(1).getStringCellValue());
                if (row.getCell(2) != null) {
                    user.setPassword(row.getCell(2).getStringCellValue());
                } else {
                    // 如果密码为空 则设置默认密码为学号
                    user.setPassword(row.getCell(0).getStringCellValue());
                }
                if (row.getCell(3) != null) {
                    user.setAvatar(row.getCell(3).getStringCellValue());
                }
                if (row.getCell(4) != null) {
                    user.setHeight(row.getCell(4).getNumericCellValue());
                }
                if (row.getCell(5) != null) {
                    user.setWeight(row.getCell(5).getNumericCellValue());
                }
                if (row.getCell(6) != null) {
                    // 设置过敏源
                    String[] s = row.getCell(6).getStringCellValue().split(",");
                    List<Allergen> allergenList = new ArrayList<>();
                    for (String string : s) {
                        String allergenString = string.trim();
                        if (StringUtils.isEmpty(allergenString)) {
                            // 跳过空字符串
                            continue;
                        }
                        allergenList.add(
                                Allergen.builder()
                                        .allergen(string.trim())
                                        .studentNumber(user.getStudentNumber())
                                        .createTime(LocalDateTime.now())
                                        .build()
                        );
                    }
                    user.setAllergenList(allergenList);
                }

                list.add(user);

            }
            xssfWorkbook.close();
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量插入到AppUser
     *
     * @param dtoList
     * @return
     */
    @Override
    @Transactional
    public int insertAppUserBatch(List<AppUserDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }
        List<AppUser> userList = new ArrayList<>();
        dtoList.forEach(dto -> {
            AppUser user = new AppUser();
            BeanUtils.copyProperties(dto, user);
            if (user.getPassword() == null) {
                // 设置默认密码
                user.setPassword(DigestUtils.md5DigestAsHex(user.getStudentNumber().getBytes()));
            } else {
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            }
            userList.add(user);
            if (dto.getAllergenList() != null && !dto.getAllergenList().isEmpty()) {
                // 设置过敏源
                insertAllergen(dto.getStudentNumber(), dto.getAllergenList());
            }

        });
        saveBatch(userList);
        return userList.size();
    }

    /**
     * 获取今日推荐摄入卡路里
     *
     * @param studentNumber
     * @return
     */
    @Override
    public TodayRecommendCalorieVo todayRecommendCalories(String studentNumber) {
        // 根据学号查询用户信息
        AppUser user = lambdaQuery().eq(AppUser::getStudentNumber, studentNumber).one();
        // 根据学号查询运动记录信息
        List<SportRecord> sportRecords = sportRecordService.getByStudentNumber(studentNumber, OptionConstants.FALSE);
        // 根据学号查询饮食记录信息
        List<DietaryRecord> dietaryRecords = dietaryRecordService.getByStudentNumber(studentNumber);
        // 拿数据喂给ai 分析今日推荐摄入卡路里
        double recommendValue = aiService.getTodayRecommendCalories(user.toString(),dietaryRecords.toString(), sportRecords.toString());
        TodayRecommendCalorieVo vo = new TodayRecommendCalorieVo();
        vo.setRecommendValue(recommendValue);
        // 计算今日摄入量
        vo.setTodayCalorie(calculateTodayCalorie(studentNumber));
        vo.setReturnTime(LocalDateTime.now());
        // 如果今日摄入卡路里 <= 推荐摄入卡路里 则代表今日已经健康打卡 => 插入数据库
        if (vo.getRecommendValue() < vo.getTodayCalorie()) {
            healthCheckInService.save(
                    HealthCheckIn.builder()
                            .studentNumber(studentNumber)
                            .createTime(LocalDateTime.now())
                            .build()
            );
        }


        return vo;
    }

    /**
     * 获取健康建议
     * TODO 1. 优化代码 将公共部分抽取出去 2025-6-3  2.不要获取全部记录 而是最近xx条的记录
     * @param studentNumber
     * @return
     */
    @Override
    public List<String> getHealthAdvise(String studentNumber) {
        if (studentNumber == null) {throw new BaseException(ResponseMessage.STUDENT_NUMBER_NOT_NULL);}
        // 根据学号查询用户信息
        AppUser user = lambdaQuery().eq(AppUser::getStudentNumber, studentNumber).one();
        // 根据学号查询运动记录信息
        List<SportRecord> sportRecords = sportRecordService.getByStudentNumber(studentNumber, OptionConstants.FALSE);
        // 根据学号查询饮食记录信息
        List<DietaryRecord> dietaryRecords = dietaryRecordService.getByStudentNumber(studentNumber);
        // 拿数据喂给ai 分析今日推荐摄入卡路里
        return aiService.getHealthAdvise(user.toString(),dietaryRecords.toString(), sportRecords.toString());
    }

    /**
     * 统计今日摄入热量
     *
     * @param studentNumber 学号
     * @return 今日摄入热量
     */
    @Override
    public double calculateTodayCalorie(String studentNumber) {
        // 统计用户今日进食了摄入了多少热量
        List<DietaryRecord> dietaryRecordList = dietaryRecordService.lambdaQuery()
                .eq(DietaryRecord::getStudentNumber, studentNumber)
                .last("AND DATE(create_time) = CURRENT_DATE")
                .list();
        double foodCalorie = 0.0;
        if (dietaryRecordList != null && ! dietaryRecordList.isEmpty()) {
            foodCalorie = dietaryRecordList.stream().mapToDouble(DietaryRecord::getFoodCalorie).sum();
        }
        // 统计用户运动消耗了多少热量
        List<SportRecord> sportRecordList = sportRecordService.lambdaQuery()
                .eq(SportRecord::getStudentNumber, studentNumber)
                .last("AND DATE(exercise_time) = CURRENT_DATE")
                .list();
        double consumeCalorie = 0.0;
        if (sportRecordList != null && !sportRecordList.isEmpty()) {
            consumeCalorie = sportRecordList.stream().mapToDouble(SportRecord::getConsumeCalorie).sum();
        }
        // 最终摄入: 吃的 - 消耗的
        double res = foodCalorie - consumeCalorie;
        return res >= 0 ? res : 0.0;
    }

    /***
     * 用户获取健康评分
     * @return
     */
    @Override
    public GetHealthScoreVo getHealthScore(String studentNumber) {
        // 查询上次的评分
        HealthScore lastRecord = healthScoreService.lambdaQuery().eq(HealthScore::getStudentNumber, studentNumber)
                .orderByDesc(HealthScore::getCreateTime)
                .last("limit 1")
                .one();
        double lastScore = 0.0;
        if (lastRecord != null) {
            LocalDate recordDate = lastRecord.getCreateTime().toLocalDate();
            LocalDate today = LocalDate.now();
            // 获取本周的开始日期（周一）
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            lastScore = lastRecord.getScore();
            // 如果上次的评分记录就是这周的，则直接返回
            if (recordDate.isEqual(startOfWeek) || recordDate.isAfter(startOfWeek)) {
                return GetHealthScoreVo.builder()
                        .lastScore(lastScore)
                        .nowScore(lastScore) // 本周的分数就是上次的分数
                        .returnTime(LocalDateTime.now())
                        .build();
            }
        }
        // 查询今天的评分
        // 根据学号查询用户信息
        AppUser user = lambdaQuery().eq(AppUser::getStudentNumber, studentNumber).one();
        // 根据学号查询运动记录信息
        List<SportRecord> sportRecords = sportRecordService.getByStudentNumber(studentNumber, OptionConstants.FALSE);
        // 根据学号查询饮食记录信息
        List<DietaryRecord> dietaryRecords = dietaryRecordService.getByStudentNumber(studentNumber);
        // 喂给ai 获取本次评分
        double score = aiService.getHealthScore(user.toString(), sportRecords.toString(), dietaryRecords.toString());
        // 插入到数据库中
        HealthScore healthScore = HealthScore.builder()
                .score(score)
                .studentNumber(studentNumber)
                .createTime(LocalDateTime.now())
                .build();
        healthScoreService.save(healthScore);
        // 创建一个vo对象并返回
        return GetHealthScoreVo.builder()
                .lastScore(lastScore)
                .nowScore(score)
                .returnTime(LocalDateTime.now())
                .build();
    }


    /**
     * 新增过敏原信息
     */
    public void insertAllergen(String studentNumber, List<Allergen> allergenList) {
        if (StringUtils.isNotNull(allergenList) && !allergenList.isEmpty()) {
            List<Allergen> list = new ArrayList<>();
            allergenList.forEach(a -> {
                a.setCreateTime(LocalDateTime.now());
                a.setStudentNumber(studentNumber);
                list.add(a);
            });
                appUserMapper.batchAllergen(list);
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
            vo.setBMI(CommonUtil.getBmi(user.getWeight(), user.getHeight()));
        }
        // 查询用户过敏源等信息并封装到vo对象
        List<String> simpleAllergenInfoList = new ArrayList<>();
        List<Allergen> allergensDetailList = appUserMapper.selectAllergenByStudentNumber(user.getStudentNumber());
        allergensDetailList.forEach(a -> {
            simpleAllergenInfoList.add(a.getAllergen());
        });
        vo.setAllergen(simpleAllergenInfoList);
        return vo;
    }

    /**
     * 营养摄入量分析
     *
     * @param studentNumber
     * @return
     */
    @Override
    public NutritionIntakeResult nutritionAnalysis(String studentNumber) {
        // 查询用户近期进食信息
        List<DietaryRecord> dietaryRecordList = dietaryRecordService.lambdaQuery()
                .eq(DietaryRecord::getStudentNumber, studentNumber)
                .orderByDesc(DietaryRecord::getCreateTime)
                .last("limit 50").list();
        if (dietaryRecordList == null) {
            return null;
        }
        return aiService.nutritionAnalysis(dietaryRecordList.toString());
    }

    /**
     * 统计健康打卡天数
     *
     * @param studentNumber
     * @return
     */
    @Override
    public long countHeathCheckInDays(String studentNumber) {
        return healthCheckInService.lambdaQuery()
                .eq(HealthCheckIn::getStudentNumber, studentNumber)
                .count();
    }
}
