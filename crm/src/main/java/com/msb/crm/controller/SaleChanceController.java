package com.msb.crm.controller;

import com.msb.crm.annoation.RequiredPermission;
import com.msb.crm.base.BaseController;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.query.SaleChanceQuery;
import com.msb.crm.service.SaleChanceService;
import com.msb.crm.utils.CookieUtil;
import com.msb.crm.utils.LoginUserUtil;
import com.msb.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest req){
        if(flag!=null&&flag==1){
            saleChanceQuery.setState(1);
            int i = LoginUserUtil.releaseUserIdFromCookie(req);
            saleChanceQuery.setAssignMan(i);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }
    @RequiredPermission(code = "101002")
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest req){
        String userName = CookieUtil.getCookieValue(req, "userName");
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功");
    }
    @RequiredPermission(code = "101004")
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据修改成功");
    }
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id,HttpServletRequest req){
        if(id!=null){
            SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
            req.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }
    @RequiredPermission(code = "101003")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会删除成功");
    }
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer sid,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(sid,devResult);
        return success("开发状态更新成功");
    }
}
