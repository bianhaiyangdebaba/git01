package com.msb.crm.dao;

import com.msb.crm.base.BaseMapper;
import com.msb.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    Integer countPermissionByRoleId(Integer roleId);

    Integer deletePermissionByRoleId(Integer roleId);

    List<Integer> queryRoleHasModuleByRoleId(Integer roleId);

    List<String> queryUserHasRoleHasPermissionByUserId(int userId);

    Integer countPermissionByModuleId(Integer id);

    Integer deletePermissionByModuleId(Integer id);
}