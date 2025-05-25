package top.codeflux.ai.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.codeflux.ai.constants.MyChatModel;
import top.codeflux.ai.domain.vo.FoodRecognitionResult;
import top.codeflux.ai.service.AiService;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.utils.file.MimeTypeUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author qht
 * @date 2025/5/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class ChatController {
    private final AiService aiService;

    @PostMapping("/getFoodRecognitionResult")
    @Anonymous
    public FoodRecognitionResult getFoodRecognitionResult(MultipartFile image) {
        return aiService.getFoodInfo(image.getResource());
    }
}