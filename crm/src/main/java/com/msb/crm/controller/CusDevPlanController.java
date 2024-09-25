package com.msb.crm.controller;

import com.msb.crm.base.BaseController;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.query.CusDevPlanQuery;
import com.msb.crm.query.SaleChanceQuery;
import com.msb.crm.service.CusDevPlanService;
import com.msb.crm.service.SaleChanceService;
import com.msb.crm.utils.LoginUserUtil;
import com.msb.crm.vo.CusDevPlan;
import com.msb.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer sid, HttpServletRequest req){
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        req.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("添加计划成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("更新计划成功");
    }
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer sid,HttpServletRequest req,Integer id){
        req.setAttribute("sid",sid);
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        req.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("删除计划成功");
    }

}
