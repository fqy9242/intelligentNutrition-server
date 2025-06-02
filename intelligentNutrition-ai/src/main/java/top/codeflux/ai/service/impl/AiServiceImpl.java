package top.codeflux.ai.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
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
import top.codeflux.ai.service.AiService;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.domain.AppUser;
import top.codeflux.common.domain.DietaryRecord;
import top.codeflux.common.exception.base.BaseException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qht
 * @date 2025/5/17
 */
@Service
@RequiredArgsConstructor
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


}