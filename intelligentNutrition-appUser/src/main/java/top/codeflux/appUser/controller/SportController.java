package top.codeflux.appUser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.codeflux.appUser.domain.SportRecord;
import top.codeflux.appUser.service.SportRecordService;
import top.codeflux.common.constant.ResponseMessage;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;

import java.util.List;

/**
 * @author qht
 * @date 2025/6/2
 */
@RestController
@Slf4j
@RequestMapping("/client/sport")
public class SportController extends BaseController {
    @Autowired
    private SportRecordService sportRecordService;
    /**
     * 获取运动记录
     * @param studentNumber 学号
     * @param onlyToday 是否只查询今天的记录
     * @return 运动记录列表
     */
    @GetMapping("/{studentNumber}")
    public AjaxResult sportRecord(@PathVariable String studentNumber, Integer onlyToday) {
        List<SportRecord> list = sportRecordService.getByStudentNumber(studentNumber, onlyToday);
        return success(list);
    }
    /**
     * 保存运动记录
     * @param sportRecord 运动记录
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult save(@RequestBody SportRecord sportRecord) {
        return success(sportRecordService.save(sportRecord));
    }
}