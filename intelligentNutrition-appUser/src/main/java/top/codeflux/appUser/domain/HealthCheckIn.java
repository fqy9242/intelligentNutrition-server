package top.codeflux.appUser.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qht
 * @date 2025/6/5
 */
@Data
@Builder
public class HealthCheckIn {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String studentNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime createTime;
}