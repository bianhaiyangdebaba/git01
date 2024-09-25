package com.msb.crm.dao;

import com.msb.crm.base.BaseMapper;
import com.msb.crm.model.TreeModule;
import com.msb.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    List<TreeModule> queryAllModules();
    List<Module> queryAllModuleList();

    Module queryModuleByGradeAndModuleName(@Param("grade")Integer grade,@Param("moduleName")String moduleName);

    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade,@Param("url") String url);

    Module queryModuleByOpValue(String optValue);

    Integer queryModuleByParentId(Integer id);
}