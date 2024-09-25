package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.service.PermissionService;
import com.msb.crm.service.UserService;
import com.msb.crm.utils.LoginUserUtil;
import com.msb.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Resource
    private PermissionService permissionService;
    @Resource
    private UserService userService;
    /**
     * 系统登录⻚
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    // 系统界⾯欢迎⻚
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主⻚⾯
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest req){
        int i = LoginUserUtil.releaseUserIdFromCookie(req);
        User user= userService.selectByPrimaryKey(i);
        req.getSession().setAttribute("user",user);

        List<String> permissions=permissionService.queryUserHasRoleHasPermissionByUserId(i);
        req.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
