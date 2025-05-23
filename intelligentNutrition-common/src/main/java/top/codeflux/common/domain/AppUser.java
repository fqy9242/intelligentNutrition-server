package top.codeflux.common.domain;

import java.util.List;

import lombok.Data;
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
public class AppUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

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
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
//            .append("allergenList", getAllergenList())
            .toString();
    }
}
