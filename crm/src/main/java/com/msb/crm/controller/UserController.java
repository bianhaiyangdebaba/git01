package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.exceptions.ParamsException;
import com.msb.crm.model.UserModel;
import com.msb.crm.query.UserQuery;
import com.msb.crm.service.UserService;
import com.msb.crm.utils.LoginUserUtil;
import com.msb.crm.utils.UserIDBase64;
import com.msb.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @PostMapping("login")
    @ResponseBody
    private ResultInfo userLogin(String userName, String userPwd){
        ResultInfo resultInfo=new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);
        /*try{
            UserModel userModel = userService.userLogin(userName, userPwd);
            resultInfo.setResult(userModel);
        }catch (ParamsException e){
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
            e.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("登陆失败");
        }*/
        return resultInfo;
    }
    @PostMapping("/updatePwd")
    @ResponseBody
    private ResultInfo updateUserPassword(HttpServletRequest req,String oldPassword,String newPassword,String repeatPassword){
        ResultInfo resultInfo=new ResultInfo();
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);
        /*try{
            int userId = LoginUserUtil.releaseUserIdFromCookie(req);
            userService.updatePassWord(userId,oldPassword,newPassword,repeatPassword);

        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败");
            e.printStackTrace();
        }*/
        return resultInfo;
    }

    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "user/user";
    }
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("添加用户成功");
    }
    @RequestMapping("addOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest req){
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            req.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("修改用户成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);
        return success("删除用户成功");
    }
}
