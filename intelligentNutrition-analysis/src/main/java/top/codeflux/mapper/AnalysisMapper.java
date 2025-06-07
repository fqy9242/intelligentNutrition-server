package top.codeflux.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @author qht
 * @date 2025/6/7
 */
@Mapper
public interface AnalysisMapper {
    @Select("WITH student_records AS ( " +
            "    SELECT " +
            "        student_number, " +
            "        new_height, " +
            "        new_weight, " +
            "        change_time " +
            "    FROM bmi_change_log " +
            "    WHERE new_height > 0 AND new_weight > 0 " +
            "    AND change_time BETWEEN #{startTime} AND #{endTime} " +
            "), " +
            "latest_records AS ( " +
            "    SELECT " +
            "        student_number, " +
            "        new_height, " +
            "        new_weight, " +
            "        ROW_NUMBER() OVER ( " +
            "            PARTITION BY student_number " +
            "            ORDER BY change_time DESC " +
            "        ) AS rn " +
            "    FROM student_records " +
            ") " +
            "SELECT AVG(new_weight / POW(new_height / 100, 2)) AS avg_bmi " +
            "FROM latest_records " +
            "WHERE rn = 1")
    Double getLatestAverageBmiByTimeRange(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );
}
