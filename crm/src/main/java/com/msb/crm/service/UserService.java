package com.msb.crm.service;

import com.msb.crm.base.BaseService;
import com.msb.crm.dao.UserMapper;
import com.msb.crm.dao.UserRoleMapper;
import com.msb.crm.model.UserModel;
import com.msb.crm.utils.AssertUtil;
import com.msb.crm.utils.Md5Util;
import com.msb.crm.utils.PhoneUtil;
import com.msb.crm.utils.UserIDBase64;
import com.msb.crm.vo.User;
import com.msb.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserMapper userMapper;
    public UserModel userLogin(String userName, String userPwd){
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(user==null,"用户姓名不存在");
        checkUserPwd(userPwd,user.getUserPwd());

        return buildUserModel(user);

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        User user=userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user==null,"待更新记录不存在");
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        user.setUserPwd(Md5Util.encode(newPwd));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");
    }

    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空");
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致");
    }

    private UserModel buildUserModel(User user) {
        UserModel userModel=new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());
        return userModel;
    }

    private void checkUserPwd(String userPwd, String userPwd1) {
        userPwd= Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码不正确");
    }

    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"用户添加失败");

        relationUserRole(user.getId(),user.getRoleIds());
    }

    private void relationUserRole(Integer id, String roleIds) {
        Integer count = userRoleMapper.countUserRoleByUserId(id);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(id)!=count,"用户角色分配失败");
        }
        if(!StringUtils.isBlank(roleIds)){
            ArrayList<UserRole> list=new ArrayList<>();
            String[] split = roleIds.split(",");
            for (String s : split) {
                UserRole userRole=new UserRole();
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setUserId(id);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                list.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(list)!=list.size(),"用户角色分配失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        AssertUtil.isTrue(null==user.getId(),"用户id不能为空");
        AssertUtil.isTrue(null==userMapper.selectByPrimaryKey(user.getId()),"用户id不存在");
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)!=1,"用户修改失败");

        relationUserRole(user.getId(),user.getRoleIds());
    }

    private void checkUserParams(String userName, String email, String phone,Integer id) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(userMapper.queryUserByName(userName)!=null&&!userMapper.queryUserByName(userName).getId().equals(id),"用户名以存在");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        AssertUtil.isTrue(ids==null||ids.length==0,"待删除记录不存在");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"用户删除失败");

        for (Integer id : ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(id);
            if (count > 0) {
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(id) != count, "用户删除失败");
            }
        }
    }
}
