package top.codeflux.appUser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.codeflux.common.domain.DietaryRecord;

import java.util.List;

/**
 * @author qht
 * @date 2025/5/27
 */
@Mapper
public interface DietaryRecordMapper extends BaseMapper<DietaryRecord> {
    @Select("SELECT * FROM dietary_record WHERE student_number = #{studentNumber} AND meal_type = #{mealType} AND CURDATE() = DATE(create_time)")
    List<DietaryRecord> selectTodayByStudentNumberAndMealType(@Param("studentNumber") String studentNumber, @Param("mealType") Integer mealType);
}
