package top.codeflux.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import top.codeflux.ai.domain.dto.ChatPrompt;
import top.codeflux.ai.domain.vo.FoodRecognitionResult;
import top.codeflux.common.domain.AppUser;
import top.codeflux.common.domain.DietaryRecord;

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
}
