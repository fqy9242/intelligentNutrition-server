package top.codeflux.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import top.codeflux.ai.domain.dto.ChatPrompt;

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

}
