package top.codeflux.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.codeflux.ai.constants.DefaultSystem;

/**
 * @author qht
 * @date 2025/5/17
 */
@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(DefaultSystem.DEFAULT)
                .build();
    }
}