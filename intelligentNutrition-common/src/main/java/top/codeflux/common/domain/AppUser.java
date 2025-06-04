package top.codeflux.common.domain;

import java.time.LocalDateTime;
import java.util.List;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.codeflux.common.annotation.Excel;
import top.codeflux.common.core.domain.BaseEntity;

/**
 * app注册用户对象 app_user
 * 
 * @author qht
 * @date 2025-05-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser
{
    private static final long serialVersionUID = 1L;

   @TableId(type = IdType.AUTO)
    private Integer id;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNumber;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 登录密码 */
    private String password;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 身高 */
    @Excel(name = "身高")
    private Double height;

    /** 体重 */
    @Excel(name = "体重")
    private Double weight;
    @Excel(name = "注册时间")
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

//    private LocalDateTime createTime;
//    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studentNumber", getStudentNumber())
            .append("name", getName())
            .append("password", getPassword())
            .append("avatar", getAvatar())
            .append("height", getHeight())
            .append("weight", getWeight())
//            .append("allergenList", getAllergenList())
            .toString();
    }
}
