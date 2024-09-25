package com.msb.crm.service;

import com.msb.crm.base.BaseService;
import com.msb.crm.dao.PermissionMapper;
import com.msb.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;

    public List<String> queryUserHasRoleHasPermissionByUserId(int i) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(i);
    }
}
