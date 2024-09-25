package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.model.TreeModule;
import com.msb.crm.service.ModuleService;
import com.msb.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModule> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest req){
        req.setAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryAllModuleList(Integer roleId){
        return moduleService.queryAllModuleList();
    }

    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);

        return success("添加模块成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);

        return success("修改模块成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);

        return success("删除模块成功");
    }

    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade,Integer parentId,HttpServletRequest req){
        req.setAttribute("grade",grade);
        req.setAttribute("parentId",parentId);
        return "module/add";
    }
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id,HttpServletRequest req){
        Module module = moduleService.selectByPrimaryKey(id);
        req.setAttribute("module",module);
        return "module/update";
    }
}
