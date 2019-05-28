package com.sz.lc.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sz.lc.entity.Clazz;
import com.sz.lc.entity.Grade;
import com.sz.lc.entity.Student;
import com.sz.lc.entity.User;
import com.sz.lc.page.Page;
import com.sz.lc.service.ClazzService;
import com.sz.lc.service.GradeService;
import com.sz.lc.service.StudentService;
import com.sz.lc.util.StringUtil;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private ClazzService clazzService;
	@Autowired
	private StudentService studentService;
	/*
	 * 来到学生列表
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		//相当于获取年级的id
		model.setViewName("student/student_list");
		List<Clazz> clazzList = clazzService.findAll();
		//这里是传值的意思
		model.addObject("clazzList",clazzList);
		model.addObject("clazzListJson",JSONArray.fromObject(clazzList));
		
	   return model;	
	}

/*
* 获取学生列表
* 
*/
@RequestMapping(value="/get_list",method=RequestMethod.POST)
@ResponseBody
public Map<String,Object> getList(
	   @RequestParam(value="name",required=false,defaultValue="") String name,
	   @RequestParam(value="clazzId",required=false,defaultValue="") Long 	clazzId,
	   HttpServletRequest request,
	   Page page
	   ){
   Map<String,Object>ret=new HashMap<String,Object>();
   Map<String,Object>queryMap=new HashMap<String,Object>();
   //如果是学生，会覆盖下面的语句 queryMap.put("username","%"+name+"%");
   queryMap.put("username","%"+name+"%");
   Object attribute = request.getSession().getAttribute("userType");
 
   if("2".equals(attribute.toString())){
	   //说明是学生
	   Student loginStudent =(Student)request.getSession().getAttribute("user");
	   //强制转换
	   queryMap.put("username","%"+loginStudent.getUsername()+"%");
	   
   }
 //这里便是搜索功能
   if(clazzId!=null){
	   queryMap.put("clazzId",clazzId);
   }
   queryMap.put("offset",page.getOffert());
   queryMap.put("pageSize",page.getRows());
   ret.put("rows",studentService.findList(queryMap));
   ret.put("total",studentService.getTotal(queryMap));
   return ret;
}
/*
 * 上传图片	
 */
@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
@ResponseBody
 public Map<String,String> uploadphoto(MultipartFile photo,
		 HttpServletRequest request,
		 HttpServletResponse response
		 
		 )throws IOException{
	  Map<String,String>ret=new HashMap<String,String>();
	  if(photo==null){
		  ret.put("type","error");
		  ret.put("msg","请选择上传文件!");
		  return ret;
	  }
	  if(photo.getSize()>10485760){
		  ret.put("type","error");
		  ret.put("msg","文件大小超过10M,请上传小于10M的图片!");
		  return ret;
	  }
	  //判断是不是图片,先获取图片的后缀
	  String suffix=photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
	  //没有这些格式的图片，不能上传，转为小写
	  if(!"jpg,png,gif,jpeg".contains(suffix.toLowerCase())){
		  ret.put("type","error");
		  ret.put("msg","文件格式不正确，请上传jpg,png,gif,jpeg格式的文件!");
		  return ret;
	  }
	  //成功了，将他保留在某个文件夹下(绝对路径),"/"是根目录
	    
//	     获取路径时添加 throws IOException,抛出异常
//	      String path = request.getServletContext().getContextPath();\
	        //savePath是真正的路径
	      String savePath=request.getServletContext().getRealPath("/") + "\\upload\\";
	      System.out.println(savePath);
	      File savePathFile=new File(savePath);
	      if(!savePathFile.exists()){
	    	  savePathFile.mkdir();// 如果不存在，则创建一个文件夹upload
	    	  
	      }
	      
	      //把文件转存到这个文件夹下(将文件写进去)new Date().getTime()时间戳
	      String filename=new Date().getTime()+"."+suffix;
	      photo.transferTo(new File(savePath+filename));
	      
	      ret.put("type","success");
		  ret.put("msg","上传图片成功!");
		  //返回图片的路径
		  
		  ret.put("src",request.getServletContext().getContextPath()+"/upload/"+filename);
		  return ret;
}

      /*
       * 添加学生信息
       */

@RequestMapping(value="/add",method=RequestMethod.POST)
@ResponseBody
public Map<String,String> add(Student student){
	Map<String,String> ret=new HashMap<String,String>();
	if(StringUtils.isEmpty(student.getUsername())){
		ret.put("type","error");
		ret.put("msg","学生姓名不能为空!");
		return ret;
		
	}
	if(StringUtils.isEmpty(student.getPassword())){
		ret.put("type","error");
		ret.put("msg","密码不能为空!");
		return ret;
	}
	//班级id不能为空
	
	if(student.getClazzId()==null){
		ret.put("type","error");
		ret.put("msg","请选择所属班级!");
		return ret;
	}
	//不让添加(当添加的名字相同时，不允许在添加)
	if(isExits(student.getUsername(),null)){
		ret.put("type","error");
		ret.put("msg","该姓名已经存在，不可再添加!");
		return ret;
	}
	//前缀prefix是S,后缀suffix是空，调用student的方法和StringUtil.geneTatSn()
	student.setSn(StringUtil.generateSn("S",""));
	if(studentService.add(student)<=0){
		ret.put("type","error");
		ret.put("msg","添加学生失败!");
		return ret;
	}
	//添加学生失败
	ret.put("type","success");
	ret.put("msg","添加学生成功!");
	return ret;
}
   private boolean isExits(String username,Long id){
	   Student student = studentService.findByUserName(username);
	   if(student!=null){
		   //不等于空，有两种情况,第一种id==null是新增，根据用户名查出学生说明是存在的
		   if(id==null){
			   return true;
		   }
		   if(student.getId().longValue()!=id.longValue()){
			   return true;
		   }
	   }
	   //默认返回false
	   return false;
   }
   //修改学生信息
   @RequestMapping(value="/edit",method=RequestMethod.POST)
   @ResponseBody
   public Map<String,String> edit(Student student){
	   
	   Map<String,String> ret=new HashMap<String,String>();
	   if(StringUtils.isEmpty(student.getUsername())){
		   ret.put("type","error");
		   ret.put("msg","学生名字不能为空!");
		   return ret;
	   }
	   if(StringUtils.isEmpty(student.getPassword())){
		   ret.put("type","error");
		   ret.put("msg","密码不能为空!");
		   return ret;
	   }
	   if(isExits(student.getUsername(),student.getId())){
			ret.put("type","error");
			ret.put("msg","该姓名已经存在，不可再添加!");
			return ret;
		}
		//前缀prefix是S,后缀suffix是空，调用student的方法和StringUtil.geneTatSn()
		student.setSn(StringUtil.generateSn("S",""));
		if(studentService.edit(student)<=0){
			ret.put("type","error");
			ret.put("msg","修改学生信息失败!");
			return ret;
		}
		
	   if(student.getClazzId()==null){
		   ret.put("type","error");
		   ret.put("msg","请选择所属班级!");
		   return ret;
	   }
	   
	   ret.put("type","success");
	   ret.put("msg","学生信息修改成功!");
	   return ret;
   }
   /*
    * 删除学生信息
    */
   @RequestMapping(value="/delete",method=RequestMethod.POST)
   @ResponseBody
    public Map<String,String> delete(
    		@RequestParam(value="ids[]",required=true)Long[] ids
    		){
	   Map<String,String> ret=new HashMap<String,String>();
	   if(ids==null||ids.length==0){
		   ret.put("type","error");
		   ret.put("msg","请选择你要删除的!");
		   return ret;
	   }
	   try{
		   if(studentService.delete(StringUtil.joinString(Arrays.asList(ids),","))<=0){
			   ret.put("type","error");
			   ret.put("msg","删除失败!");
			   return ret;
		   }
	   }catch(Exception e){
		   ret.put("type","error");
		   ret.put("msg","该班级下存在学生信息，请勿冲动!");
		   return ret;
	   }
     ret.put("type","success");
     ret.put("msg","删除信息成功!");
     return ret;
}
   }
