package com.sz.lc.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sz.lc.entity.Student;
import com.sz.lc.entity.User;
import com.sz.lc.service.StudentService;
import com.sz.lc.service.UserService;
import com.sz.lc.util.CpachaUtil;

/*
 * 系统主页控制器
 */
@Controller
@RequestMapping("/system")
public class SystemController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model){
		//System.out.println("你好");
		//system代表的是路径下的文件夹，index是文件名称
		model.setViewName("system/index");
		return model;
	}
//	登录页面
    @RequestMapping(value="/login",method=RequestMethod.GET)
    public ModelAndView login(ModelAndView model){
    	//System.out.println("你好");
    	model.setViewName("system/login");
    	return model;
    }
//	退出页面
    @RequestMapping(value="/login_out",method=RequestMethod.GET)
    public String loginOut(HttpServletRequest request){
    	//System.out.println("你好");
    	//清空数据
       request.getSession().setAttribute("user",null);
    	return "redirect:/login";
    }
    //登录表单提交
    //ResponseBody是将Map对象字符串转换为json格式字符串到页面
    @RequestMapping(value="/login",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,String> login(
    		@RequestParam(value="username",required=true) String username,
    		@RequestParam(value="password",required=true) String password,
    		@RequestParam(value="vcode",required=true) String vcode,
    		@RequestParam(value="type",required=true) int type,
    		HttpServletRequest request
    		){
    	//System.out.println("你不好!");
    	Map<String,String> ret=new HashMap<String,String>();
    	//进行判断
    	if(StringUtils.isEmpty(username)){
    		ret.put("type","error");
    		ret.put("msg","用户名不能为空！");
    		return ret;
    	}
    	if(StringUtils.isEmpty(password)){
    		ret.put("type","error");
    		ret.put("msg","密码不能为空！");
    		return ret;
    	}
    	if(StringUtils.isEmpty(vcode)){
    		ret.put("type","error");
    		ret.put("msg","验证码不能为空！");
    		return ret;
    	}
    	//验证码不为空(String)是强制转换
    	 String loginCpacha=(String)request.getSession().getAttribute("loginCpacha");
    	 //进行判断
    	 if(StringUtils.isEmpty(loginCpacha)){
    		 ret.put("type","error");
    		 ret.put("msg","长时间未操作，会话已失效，请刷新后重试！");
    	     return ret;
    	 }
    	 //输入的验证码不一致(!vcode.ToUpperCase().equals(loginCpacha.ToUpperCase()))
    	 if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
    		 ret.put("type","error");
    		 ret.put("msg","验证码错误！");
    	     return ret;
    	 }
    	 //到了这一步,验证码正确，登录后清除验证码request.getSession().setAttribute("loginCpacha",null);
    	 request.getSession().setAttribute("loginCpacha", null);
    	 //从数据库中查找用户名和密码
    	 if(type==1){
    		 //1是管理员
    		 User user = userService.findByUserName(username);
        	 if(user==null){
        		 ret.put("type","error");
        		 ret.put("msg","不存在该用户");
        		 return ret;
        	 }
        	 if(!password.equals(user.getPassword())){
        		 //输入的密码和数据库的密码不一致
        		 ret.put("type","error");
        		 ret.put("msg","密码错误");
        		 return ret;
        	 }
        	 //到了这一步，密码跟用户都是正确的
        	 request.getSession().setAttribute("user",user);
    	 }
    	if(type==2){
    		//学生
    		 Student student = studentService.findByUserName(username);
        	 if(student==null){
        		 ret.put("type","error");
        		 ret.put("msg","不存在该学生");
        		 return ret;
        	 }
        	 //if(!password.equals(student.getPassword()))
        	 if(!password.equals(student.getPassword())){
        		 //输入的密码和数据库的密码不一致
        		 ret.put("type","error");
        		 ret.put("msg","密码错误");
        		 return ret;
        	 }
        	 //到了这一步，密码跟用户都是正确的,getSession()是获取当前操作人
        	 request.getSession().setAttribute("user",student);
    	}
    	//userType参数是管理员的，user参数是学生的
    	request.getSession().setAttribute("userType",type);
    	ret.put("type","success");
		ret.put("msg","登录成功！");
    	return ret;
    }
    //获取验证码登录,将验证码设定成自定义的,vl是验证码的个数,defaultValue设定值是4，w是宽度，h是高度
    @RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
    public void getCpacha(HttpServletRequest request,
    		@RequestParam(value="vl",defaultValue="4",required=false) Integer vl,
    		@RequestParam(value="w",defaultValue="98",required=false) Integer w,
    		@RequestParam(value="h",defaultValue="33",required=false) Integer h,
    		HttpServletResponse response){
    	//System.out.println("获取验证码");
//    	第一步，创建生成验证码对象
    	CpachaUtil cpachaUtil =new CpachaUtil(vl,w,h); 
//    	生成一个验证码,generatorVCode表示快速创建一个本地变量
    	
    	String generatorVCode = cpachaUtil.generatorVCode();
    	//在url上建立一个会话getSession
    	request.getSession().setAttribute("loginCpacha",generatorVCode);
//    	获取验证码,还带有旋转的验证码
    	 BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
    	 //将验证码读写出来,然后捕捉异常，BufferedImage是获取验证码,response.getOutputStream是将验证码返还给用户
    	 try {
			ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
