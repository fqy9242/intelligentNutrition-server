package top.codeflux.appUser.controller;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.codeflux.appUser.domain.dto.AppUserDto;
import top.codeflux.appUser.domain.vo.AppUserVo;
import top.codeflux.common.annotation.Anonymous;
import top.codeflux.common.annotation.Log;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;
import top.codeflux.common.core.page.PageDomain;
import top.codeflux.common.core.page.TableSupport;
import top.codeflux.common.enums.BusinessType;
import top.codeflux.common.domain.AppUser;
import top.codeflux.appUser.service.IAppUserService;
import top.codeflux.common.utils.poi.ExcelUtil;
import top.codeflux.common.core.page.TableDataInfo;

/**
 * app注册用户Controller
 * 
 * @author qht
 * @date 2025-05-16
 */
@RestController
@Slf4j
@RequestMapping("/intelligentNutrition-appUser/appUser")
public class AppUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * 查询app注册用户列表
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppUser appUser)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<AppUserVo> list = appUserService.selectAppUserList(appUser);
        //处理上面查询的list集合
        int num = list.size();
        list = list.stream().skip((pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(list);
        rspData.setTotal(num);
        return rspData;
    }

    /**
     * 导出app注册用户列表
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:export')")
    @Log(title = "app注册用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppUser appUser)
    {
        List<AppUserVo> list = appUserService.selectAppUserList(appUser);
        ExcelUtil<AppUserVo> util = new ExcelUtil<AppUserVo>(AppUserVo.class);
        util.exportExcel(response, list, "app注册用户数据");
    }

    /**
     * 获取app注册用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(appUserService.selectAppUserById(id));
    }

    /**
     * 新增app注册用户
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:add')")
    @Log(title = "app注册用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppUserDto dto)
    {
        return toAjax(appUserService.insertAppUser(dto));
    }

    /**
     * 修改app注册用户
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:edit')")
    @Log(title = "app注册用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppUserDto dto)
    {
        return toAjax(appUserService.updateAppUser(dto));
    }

    /**
     * 删除app注册用户
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:remove')")
    @Log(title = "app注册用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(appUserService.deleteAppUserByIds(ids));
    }

    /**
     * 导出批量导入用户模板
     * @param response
     */
    @GetMapping("/exportInputUserTemplate")
    @Anonymous
    public void exportInputUserTemplate(HttpServletResponse response) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates/用户导入模板.xlsx");
        try {
            XSSFWorkbook sheets = new XSSFWorkbook(inputStream);
            ServletOutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            sheets.close();
            outputStream.close();
        } catch (Exception e) {
            log.error("导出批量导入用户模板失败:{}", e.getMessage());
        }

    }

    /**
     * 解析Excel文件并转换为AppUser列表
     * @param excelFile
     * @return
     */
    @Anonymous
    @PostMapping("/parseExcelToAppUserList")
    public AjaxResult parseExcelToAppUserList(MultipartFile excelFile) {
        List<AppUserDto> list = appUserService.parseExcelToAppUserList(excelFile);
        return success(list);
    }

    /**
     * 批量新增app注册用户
     * @param dtoList
     * @return
     */
    @PostMapping("/addBatch")
    public AjaxResult addBatch(@RequestBody List<AppUserDto> dtoList) {
        return toAjax(appUserService.insertAppUserBatch(dtoList));
    }
}
