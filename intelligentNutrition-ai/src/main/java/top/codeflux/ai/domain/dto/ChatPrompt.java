package top.codeflux.ai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author qht
 * @date 2025/5/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatPrompt {
    private String text;
    private Resource file;
}