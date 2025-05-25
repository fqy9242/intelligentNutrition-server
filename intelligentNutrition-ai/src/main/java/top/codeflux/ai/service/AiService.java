package top.codeflux.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import top.codeflux.ai.domain.dto.ChatPrompt;
import top.codeflux.ai.domain.vo.FoodRecognitionResult;

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

}
