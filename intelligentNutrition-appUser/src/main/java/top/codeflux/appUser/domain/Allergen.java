package top.codeflux.appUser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.codeflux.common.annotation.Excel;
import top.codeflux.common.core.domain.BaseEntity;

/**
 * 过敏原对象 allergen
 * 
 * @author qht
 * @date 2025-05-16
 */
public class Allergen extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 学号 */
    private String studentId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allergen;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }
    public void setAllergen(String allergen) 
    {
        this.allergen = allergen;
    }

    public String getAllergen() 
    {
        return allergen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studentId", getStudentId())
            .append("allergen", getAllergen())
            .append("createTime", getCreateTime())
            .toString();
    }
}
