package com.msb.crm.service;

import com.msb.crm.base.BaseService;
import com.msb.crm.dao.ModuleMapper;
import com.msb.crm.dao.PermissionMapper;
import com.msb.crm.model.TreeModule;
import com.msb.crm.utils.AssertUtil;
import com.msb.crm.vo.Module;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    public List<TreeModule> queryAllModules(Integer roleId){
        List<TreeModule> treeModules = moduleMapper.queryAllModules();
        List<Integer> permissionIds=permissionMapper.queryRoleHasModuleByRoleId(roleId);
        if(permissionIds!=null&&permissionIds.size()>0){
            treeModules.forEach(treeModule -> {
                if(permissionIds.contains(treeModule.getId())){
                    treeModule.setChecked(true);
                }
            });
        }

        return treeModules;
    }
    public Map<String,Object> queryAllModuleList(){
        Map<String,Object> map=new HashMap<>();
        List<Module> modules = moduleMapper.queryAllModuleList();
        map.put("code",0);
        map.put("msg","success");
        map.put("count",modules.size());
        map.put("data",modules);
        return map;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        Integer grade=module.getGrade();
        AssertUtil.isTrue(grade==null||!(grade==1||grade==2||grade==0),"模块层级不合法");

        AssertUtil.isTrue(module.getModuleName()==null,"菜单名不能为空");
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName()),"模块名称已存在");

        if (grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url名不能为空");
            AssertUtil.isTrue(null!=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl()),"url名称已存在");
        }

        if(grade==0){
            module.setParentId(-1);
        }
        if (grade!=0){
            AssertUtil.isTrue(null==module.getParentId(),"父级菜单不能为空");
            AssertUtil.isTrue(null==moduleMapper.selectByPrimaryKey(module.getParentId()),"父级菜单不存在");
        }

        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"缺陷吗不能为空");
        AssertUtil.isTrue(moduleMapper.queryModuleByOpValue(module.getOptValue())!=null,"权限码已存在");

        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());

        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"添加模块失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        AssertUtil.isTrue(module.getId()==null,"待更新记录不存在");
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(temp==null,"待更新记录不存在");

        Integer grade=module.getGrade();
        AssertUtil.isTrue(grade==null||!(grade==1||grade==2||grade==0),"模块层级不合法");

        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        temp=moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName());
        if(temp!=null){
            AssertUtil.isTrue(!temp.getId().equals(module.getId()),"模块名称已存在");
        }

        if (grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url名不能为空");
            temp=moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl());
            if (temp!=null){

                AssertUtil.isTrue(!temp.getId().equals(module.getId()),"url名称已存在");
            }
        }


        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        temp=moduleMapper.queryModuleByOpValue(module.getOptValue());
        if(temp!=null){
            AssertUtil.isTrue(!temp.getId().equals(module.getId()),"权限码已存在");
        }

        module.setUpdateDate(new Date());

        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module)<1,"修改模块失败");
    }

    public void deleteModule(Integer id) {
        AssertUtil.isTrue(id==null,"待删除资源不存在");
        Module temp = moduleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(temp==null,"待删除记录不存在");
        Integer count =moduleMapper.queryModuleByParentId(id);
        AssertUtil.isTrue(count>0,"带删除模块有子记录，不能删除");

        count=permissionMapper.countPermissionByModuleId(id);
        if(count>0){
            permissionMapper.deletePermissionByModuleId(id);
        }
        temp.setIsValid((byte)0);
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp)<1,"删除记录失败");
    }
}
