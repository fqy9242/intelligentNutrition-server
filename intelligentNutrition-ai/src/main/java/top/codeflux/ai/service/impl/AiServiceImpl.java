package top.codeflux.ai.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
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
}