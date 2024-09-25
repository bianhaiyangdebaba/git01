package com.msb.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msb.crm.base.BaseService;
import com.msb.crm.dao.CusDevPlanMapper;
import com.msb.crm.dao.SaleChanceMapper;
import com.msb.crm.query.CusDevPlanQuery;
import com.msb.crm.query.SaleChanceQuery;
import com.msb.crm.utils.AssertUtil;
import com.msb.crm.vo.CusDevPlan;
import com.msb.crm.vo.SaleChance;
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
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map=new HashMap<>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo=new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        checkCusDevPlan(cusDevPlan);
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        Integer integer = cusDevPlanMapper.insertSelective(cusDevPlan);
        AssertUtil.isTrue(integer!=1,"添加营销机会计划失败");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(null==cusDevPlan.getId()||cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId())==null,"数据异常");
        checkCusDevPlan(cusDevPlan);
        cusDevPlan.setUpdateDate(new Date());
        Integer integer = cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan);
        AssertUtil.isTrue(integer!=1,"更新营销机会计划失败");
    }

    private void checkCusDevPlan(CusDevPlan cusDevPlan) {
        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(saleChanceId==null||saleChanceMapper.selectByPrimaryKey(saleChanceId)==null,"数据异常，请重试");
        AssertUtil.isTrue(cusDevPlan.getPlanDate()==null,"请输入计划时间");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划内容不能为空");
}
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id) {
        AssertUtil.isTrue(null==id,"数据不存在");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"删除失败");
    }
}