package top.codeflux.ai.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.MessageConstraints;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import top.codeflux.ai.constants.MyChatModel;
import top.codeflux.ai.domain.dto.ChatPrompt;
import top.codeflux.ai.domain.vo.FoodRecognitionResult;
import top.codeflux.ai.domain.vo.NutritionIntakeResult;
import top.codeflux.ai.service.AiService;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.domain.AppUser;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.common.domain.vo.chart.PieChartVo;
import top.codeflux.common.exception.base.BaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {
    private final ChatClient chatClient;


    /**
     * 多模态
     *
     * @param prompt
     * @return
     */
    @Override
    public ChatClient.CallResponseSpec chatMultiModel(ChatPrompt prompt) {
        // 获取图片资源
        Media media = new Media(MediaType.ALL, prompt.getFile());
        // 构建message
        UserMessage message = new UserMessage(prompt.getText(), media);
        // 调用大模型 并返回call对象
        return chatClient.prompt(new Prompt(message, DashScopeChatOptions.builder()
                .withModel(MyChatModel.QWEN_VL_MAX)
                .withMultiModel(true)
                .build())).call();
    }

    /**
     * 通过大模型识别食物热量等信息
     *
     * @return
     */
    @Override
    public FoodRecognitionResult getFoodInfo(Resource image) {
        // 构建提示词
        String prompt = "请分析图片中的食物,名字等信息请使用中文描述。";
        // 调用方法分析 并返回call后的对象
        var callRes = chatMultiModel(ChatPrompt.builder().text(prompt).file(image).build());
        // 封装成实体并返回
        return callRes.entity(FoodRecognitionResult.class);
    }

    /**
     * 填充饮食记录信息
     * @param entity
     * @return
     */
    @Override
    public DietaryRecord fillDietaryRecord(DietaryRecord entity) {
        // 需要AI分析的信息字段列表
        List<String> requireFillList = new ArrayList<>();

        // 如果食物重量为null 则抛出异常
        if (entity.getFoodWeight() == null) {
            throw new BaseException(ResponseMessage.WEIGHT_CANNOT_NULL);
        }

        // 检查各项营养成分是否为空，将缺失的添加到分析列表
        if (entity.getFoodCalorie() == null) {
            requireFillList.add("热量");
        }
        if (entity.getFoodProtein() == null) {
            requireFillList.add("蛋白质");
        }
        if (entity.getFoodFat() == null) {
            requireFillList.add("脂肪");
        }
        if (entity.getFoodDietaryFiber() == null) {
            requireFillList.add("膳食纤维");
        }
        if (entity.getFoodCarbohydrate() == null) {
            requireFillList.add("碳水化合物");
        }

        // 如果没有需要填充的信息，直接返回
        if (requireFillList.isEmpty()) {
            return entity;
        }
        // 构建提示词
        String prompt = String.format(
                "请分析这份食物的营养成分。食物名称：%s，重量：%.1f克。请告诉我它的%s含量。请直接返回数值，用逗号分隔。",
                entity.getFoodName(),
                entity.getFoodWeight(),
                String.join("、", requireFillList)
        );

        // 调用AI进行分析
        var response = chatClient.prompt(prompt).call().content();
        String[] values = response.split(",");
        int index = 0;

        // 填充分析结果
        if (entity.getFoodCalorie() == null && index < values.length) {
            entity.setFoodCalorie(Double.parseDouble(values[index++].trim()));
        }
        if (entity.getFoodProtein() == null && index < values.length) {
            entity.setFoodProtein(Double.parseDouble(values[index++].trim()));
        }
        if (entity.getFoodFat() == null && index < values.length) {
            entity.setFoodFat(Double.parseDouble(values[index++].trim()));
        }
        if (entity.getFoodDietaryFiber() == null && index < values.length) {
            entity.setFoodDietaryFiber(Double.parseDouble(values[index].trim()));
        }
        if (entity.getFoodDietaryFiber() == null && index < values.length) {
            entity.setFoodDietaryFiber(Double.parseDouble(values[index].trim()));
        }
        return entity;
    }

    /**
     * 计算运动消耗的卡路里
     *
     * @param user
     * @param sportName
     * @param duration
     * @return 消耗的卡路里
     */
    @Override
    public double calculateSportCalorie(AppUser user, String sportName, double duration) {
        // 构建提示词
        String prompt = String.format(
                "请根据以下信息计算运动消耗的卡路里：\n" +
                        "用户信息：体重 %.1f公斤，身高 %.1f厘米。\n" +
                        "运动名称：%s，持续时间：%.1f分钟。\n" +
                        "请只返回消耗的卡路里数值，不要包含任何单位或其他文字。",
                user.getWeight(), user.getHeight(),
                sportName, duration
        );
        // 调用AI进行分析
        String res = chatClient.prompt(prompt).call().content();
        if (res == null) {
            return 0.0;
        }
        return Double.parseDouble(res);
    }

    /**
     * 获取今日推荐的卡路里摄入量
     *
     * @param userInfo          用户信息
     * @param dietaryRecordInfo 饮食记录信息
     * @param sportRecordInfo   运动记录信息
     * @return 今日推荐的卡路里摄入量
     */
    @Override
    public double getTodayRecommendCalories(String userInfo, String dietaryRecordInfo, String sportRecordInfo) {
        // 构建提示词
        String prompt = String.format(
                "请根据以下信息计算今日推荐的卡路里摄入量：\n" +
                        "用户信息：%s\n" +
                        "饮食记录信息：%s\n" +
                        "运动记录信息：%s\n" +
                        "请只返回推荐的卡路里数值，不要包含任何单位或其他文字。",
                userInfo, dietaryRecordInfo, sportRecordInfo
        );
        String content = chatClient.prompt(prompt).call().content();
        return Double.parseDouble(content);
    }

    /**
     * 获取三条健康建议
     *
     * @param studentInfo       学生信息
     * @param dietaryRecordInfo 饮食信息
     * @param sportRecordInfo   运动记录信息
     * @return 三条健康建议
     */
    @Override
    public List<String> getHealthAdvise(String studentInfo, String dietaryRecordInfo, String sportRecordInfo) {
        // 构建提示词
        String prompt = String.format("请根据以下信息给出三条健康建议: \n" +
                "用户信息:%s \n" +
                "用户运动记录:%s \n" +
                "用户饮食记录: %s \n" +
                "这是个例子:增加蔬菜摄入量,控制油炸食品摄入,保持适量运动 \n" +
                "请严格返回三条建议,每条建议使用英文逗号隔开,每条建议不要超过十五个字。\n" +
                "不要返回多于内容！谢谢。", studentInfo, sportRecordInfo, dietaryRecordInfo);
        // 调用大模型
        String res = chatClient.prompt(prompt).call().content();
//        log.info(res);
        return (res != null && !res.isEmpty()) ? List.of(res.split(",")) : new ArrayList<>();
    }

    /**
     * 分析健康评分
     *
     * @param studentInfo
     * @param sportRecord
     * @param dietaryRecord
     * @return
     */
    @Override
    public double getHealthScore(String studentInfo, String sportRecord, String dietaryRecord) {
        // 构建提示词
        String prompt = String.format("请你根据以下信息,给出1到100的健康评分,\n" +
                "用户信息: %s \n" +
                "运动记录: %s \n" +
                "饮食记录: %s \n" +
                "请注意，你仅需返回一个数字即可，无需返回其他任何信息,谢谢！",
                studentInfo,
                sportRecord,
                dietaryRecord);
        // 调用大模型 并获取结果
        String res = chatClient.prompt(prompt).call().content();
        return res != null ? Double.parseDouble(res): 0.0;
    }

    /**
     * 分析摄入营养
     */
    @Override
    public NutritionIntakeResult nutritionAnalysis(String foodRecord) {
        // 构建提示词
        String prompt = String.format("请分析摄入的营养," +
                "用不足、达标、适中、偏高等词描述,下面是用户的近期饮食信息\n %s",foodRecord);
        // 调用大模型并获取结果
        NutritionIntakeResult result = chatClient.prompt(prompt).call().entity(NutritionIntakeResult.class);
        if (result != null) {
//            result.setResponseDateTime(LocalDateTime.now());
            return result;
        }
        return null;
    }

    /**
     * 分析近期学生摄入营养成分
     *
     * @param dietaryRecordListStr
     */
    @Override
    public PieChartVo analysisNutritional(String dietaryRecordListStr) {
        // 构建提示词
        String prompt = String.format("请根据下列信息，统计用户所摄入的营养成分，例子: name: 碳水化合物, value: 28，value指的是百分比，所有value加起来需要是100\n" +
                "用户近期饮食登记记录列表:%s", dietaryRecordListStr);
        // 调用大模型分析
        return chatClient.prompt(prompt).call().entity(PieChartVo.class);
    }
}