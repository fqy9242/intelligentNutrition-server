package top.codeflux.appUser.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
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
import top.codeflux.appUser.domain.vo.AppUserVo;
import top.codeflux.common.annotation.Log;
import top.codeflux.common.core.controller.BaseController;
import top.codeflux.common.core.domain.AjaxResult;
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
        startPage();
        List<AppUserVo> list = appUserService.selectAppUserList(appUser);
        return getDataTable(list);
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
    public AjaxResult add(@RequestBody AppUser appUser)
    {
        return toAjax(appUserService.insertAppUser(appUser));
    }

    /**
     * 修改app注册用户
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:edit')")
    @Log(title = "app注册用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppUser appUser)
    {
        return toAjax(appUserService.updateAppUser(appUser));
    }

    /**
     * 删除app注册用户
     */
    @PreAuthorize("@ss.hasPermi('intelligentNutrition-appUser:appUser:remove')")
    @Log(title = "app注册用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(appUserService.deleteAppUserByIds(ids));
    }

}
