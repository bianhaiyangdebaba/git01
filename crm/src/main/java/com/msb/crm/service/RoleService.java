package com.msb.crm.service;

import com.msb.crm.base.BaseService;
import com.msb.crm.dao.ModuleMapper;
import com.msb.crm.dao.PermissionMapper;
import com.msb.crm.dao.RoleMapper;
import com.msb.crm.utils.AssertUtil;
import com.msb.crm.vo.Permission;
import com.msb.crm.vo.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RoleMapper roleMapper;
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(role.getRoleName()==null,"角色名不能为空");
        AssertUtil.isTrue(roleMapper.selectByRoleName(role.getRoleName())!=null,"角色名已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"添加角色失败");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(role.getId()==null,"角色id不能为空");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(role.getId())==null,"角色id不存在");
        AssertUtil.isTrue(role.getRoleName()==null,"角色名不能为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!=null&&!temp.getId().equals(role.getId()),"角色名已存在");

        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"修改角色失败");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        AssertUtil.isTrue(roleId==null,"角色id不能为空");
        Role role=roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role==null,"待删除记录不存在");
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除角色失败");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mIds, Integer roleId) {
        Integer count=permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        if(mIds!=null&&mIds.length>0){
            List<Permission> list=new ArrayList<>();
            for (Integer mId : mIds) {
                Permission permission=new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                list.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(list)!=list.size(),"授权失败");
        }
    }
}
