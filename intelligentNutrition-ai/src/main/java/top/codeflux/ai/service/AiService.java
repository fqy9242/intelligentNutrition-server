package top.codeflux.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import top.codeflux.ai.domain.dto.ChatPrompt;
import top.codeflux.ai.domain.vo.FoodRecognitionResult;
import top.codeflux.ai.domain.vo.NutritionIntakeResult;
import top.codeflux.common.domain.AppUser;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.common.domain.vo.chart.PieChartVo;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/17
 */
public interface AiService {
    /**
     * 多模态
     * @return
     */
    ChatClient.CallResponseSpec chatMultiModel(ChatPrompt prompt);

    /**
     *  通过大模型识别食物热量等信息
     * @return
     */
    FoodRecognitionResult getFoodInfo(Resource image);

    DietaryRecord fillDietaryRecord(DietaryRecord entity);

    /**
     * 计算运动消耗的卡路里
     * @return 消耗的卡路里
     */
    double calculateSportCalorie(AppUser user, String sportName, double duration);
    /**
     * 获取今日推荐的卡路里摄入量
     * @param userInfo 用户信息
     * @param dietaryRecordInfo 饮食记录信息
     * @param sportRecordInfo 运动记录信息
     * @return 今日推荐的卡路里摄入量
     */
    double getTodayRecommendCalories(String userInfo, String dietaryRecordInfo, String sportRecordInfo);

    /**
     * 获取三条健康建议
     * @param studentInfo 学生信息
     * @param dietaryRecordInfo 饮食信息
     * @param sportRecordInfo 运动记录信息
     * @return 三条健康建议
     */
    List<String> getHealthAdvise(String studentInfo, String dietaryRecordInfo, String sportRecordInfo);

    /**
     *  分析健康评分
     * @param studentInfo
     * @param sportRecord
     * @param dietaryRecord
     * @return
     */
    double getHealthScore(String studentInfo, String sportRecord, String dietaryRecord);

    /**
     *  分析摄入营养
     * @param string
     * @return
     */
    NutritionIntakeResult nutritionAnalysis(String foodRecord);

    /**
     * 分析近期学生摄入营养成分
     */
    PieChartVo analysisNutritional(String dietaryRecordListStr);
}
