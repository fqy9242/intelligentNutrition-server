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
    private String studentNumber;

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
    public void setStudentId(String studentNumber) 
    {
        this.studentNumber = studentNumber;
    }

    public String getStudentId() 
    {
        return studentNumber;
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
            .append("studentNumber", getStudentId())
            .append("allergen", getAllergen())
            .append("createTime", getCreateTime())
            .toString();
    }
}
