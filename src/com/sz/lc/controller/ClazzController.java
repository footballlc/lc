package com.sz.lc.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sz.lc.entity.Clazz;
import com.sz.lc.entity.Grade;
import com.sz.lc.page.Page;
import com.sz.lc.service.ClazzService;
import com.sz.lc.service.GradeService;
import com.sz.lc.util.StringUtil;

import net.sf.json.JSONArray;

/*
 * 班级信息管理
 * 
 */
@Controller
@RequestMapping("/clazz")
public class ClazzController {
	@Autowired
	private GradeService gradeService;
	@Autowired
	private ClazzService clazzService;
	/*
	 * 来到年级列表页面
	 */
    @RequestMapping(value="/list",method=RequestMethod.GET)
    public ModelAndView list(ModelAndView model){
    	model.setViewName("clazz/clazz_list");
    	List<Grade> findAll = gradeService.findAll();
    	
    	model.addObject("gradeList",findAll);
    	model.addObject("gradeListJson",JSONArray.fromObject(findAll));
    	return model;
    }
/*
 * 获取年级列表
 * 
 */
   @RequestMapping(value="/get_list",method=RequestMethod.POST)
   @ResponseBody
   public Map<String,Object> getList(
		   @RequestParam(value="name",required=false,defaultValue="") String name,
		   @RequestParam(value="gradeId",required=false,defaultValue="") Long gradeId,
		   Page page
		   ){
	   Map<String,Object>ret=new HashMap<String,Object>();
	   Map<String,Object>queryMap=new HashMap<String,Object>();
	   queryMap.put("name","%"+name+"%");
	   if(gradeId!=null){
		   queryMap.put("gradeId",gradeId);
	   }
	   queryMap.put("offset",page.getOffert());
	   queryMap.put("pageSize",page.getRows());
	   ret.put("rows",clazzService.findList(queryMap));
	   ret.put("total",clazzService.getTotal(queryMap));
	   return ret;
   }
   /*
    * 添加班级信息
    * 
    */
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(Clazz clazz){
    	  Map<String,String> ret=new HashMap<String,String>();
    	  if(StringUtils.isEmpty(clazz.getName())){
    		  ret.put("type","error");
    		  ret.put("mag","班级名称不能为空!");
    		  return ret;
    	  }
    	  if(clazz.getGradeId()==null){
    		  ret.put("type","error");
    		  ret.put("msg", "请选择所属年级!");
    		  return ret;
    	  }
    	  if(clazzService.add(clazz)<=0){
    		  ret.put("type","error");
    		  ret.put("msg","班级添加失败");
    		  return ret;
    		   
    	  }
    	  ret.put("type","success");
    	  ret.put("msg","添加成功");
    	  return ret;
    }
   
   /*
    * 修改班级信息
    * 
    */
   @RequestMapping(value="/edit",method=RequestMethod.POST)
   @ResponseBody
   public Map<String,String> edit(Clazz clazz){
	   Map<String,String> ret=new HashMap<String,String>();
	   if(StringUtils.isEmpty(clazz.getName())){
		   ret.put("type","error");
		   ret.put("msg","班级名称不能为空!");
		   return ret;
	   }
	   
	   if(clazz.getGradeId()==null){
		   ret.put("type","error");
		   ret.put("msg","所属年级不能为空!");
		   return ret;
	   }
	   
	   
	   if(clazzService.edit(clazz)<=0){
		   ret.put("type","error");
		   ret.put("msg", "年级修改失败");
		   return ret;
	   }
	   
	   
	   ret.put("type","success");
	   ret.put("msg","修改成功!");
	   return ret;
	   
   }
   
   /*
    * 删除
    */
   @RequestMapping(value="/delete",method=RequestMethod.POST)
   @ResponseBody
   public Map<String,String> delete(
		   @RequestParam(value="ids[]",required=true) Long[] ids
		   ){
	   Map<String,String> ret=new HashMap<String,String>();
	   if(ids==null||ids.length==0){
		   ret.put("type","error");
		   ret.put("msg","请选择你要删除的数据");
		   return ret;
	   }
	    try{
	       if(clazzService.delete(StringUtil.joinString(Arrays.asList(ids),","))<=0){
	    	   ret.put("type","error");
			   ret.put("msg","删除数据失败!");
			   return ret; 
	       }	
	     }catch(Exception e){
	       ret.put("type","error");
	       ret.put("msg","该班级下存在学生信息，不能删除");
	       return ret;
	    	
	    }
	   
	    ret.put("type","success");
	       ret.put("msg","成功删除信息");
	   return ret;
   }
}
