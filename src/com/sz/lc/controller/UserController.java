package com.sz.lc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sz.lc.entity.User;
import com.sz.lc.page.Page;
import com.sz.lc.service.UserService;

/*
 * 用户管理员
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	/*
	 * 用户管理页面
	 * 
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	
	public ModelAndView list(ModelAndView model){
		//user代表的是文件夹，user_list代表的是.jsp文件
		model.setViewName("user/user_list");
		return model;
	}
   /*
    * 添加用户
    */
	@RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
	public Map<String,String> add(User user){
		//得到一个返回值
		Map<String,String> ret=new HashMap<String,String>();
		if(user==null){
			ret.put("type","error");
			ret.put("msg","数据绑定失败，请重新绑定!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type","error");
			ret.put("msg","用户名不能为空!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())){
			ret.put("type","error");
			ret.put("msg","密码不能为空!");
			return ret;
		}
		    User exitsUser = userService.findByUserName(user.getUsername());
		    if(exitsUser!=null){
		    	ret.put("type","error");
				ret.put("msg","该用户名已经存在，请重新添加!");
				return ret;
		    }
		    
		if(userService.add(user)<=0){
			ret.put("type","error");
			ret.put("msg","添加用户失败!");
			return ret;
		}
		ret.put("type","success");
		ret.put("msg","添加用户成功!");
		return ret;
	}
/*
 * 查询用户和获取用户列表
 */
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getlist(
			@RequestParam(value="username",required=false,defaultValue="") String username,
			Page page
			){
	   Map<String,Object> ret=new HashMap<String,Object>();
	   Map<String,Object> queryMap=new HashMap<String,Object>();
        // like模糊查询时是两个百分号，%%代表空。
	   queryMap.put("username","%"+username+"%");
        //从0开始
	   queryMap.put("offset",page.getOffert());
	   //每页10条信息
	   queryMap.put("pageSize",page.getRows());
	   //这是根据查询的格式获取的列表,也是获取数据库的信息列表
	   ret.put("rows",userService.findList(queryMap));
	   ret.put("total",userService.getTotal(queryMap));
	   return ret;
	}
	 /*
	    * 修改用户
	    */
		@RequestMapping(value="/edit",method=RequestMethod.POST)
	    @ResponseBody
		public Map<String,String> edit(User user){
			//得到一个返回值
			Map<String,String> ret=new HashMap<String,String>();
			if(user==null){
				ret.put("type","error");
				ret.put("msg","数据绑定失败，请重新绑定!");
				return ret;
			}
			if(StringUtils.isEmpty(user.getUsername())){
				ret.put("type","error");
				ret.put("msg","用户名不能为空!");
				return ret;
			}
			if(StringUtils.isEmpty(user.getPassword())){
				ret.put("type","error");
				ret.put("msg","密码不能为空!");
				return ret;
			}
			    User exitsUser = userService.findByUserName(user.getUsername());
			    if(exitsUser!=null){
			    	if(user.getId()!=exitsUser.getId()){
			    	 	ret.put("type","error");
						ret.put("msg","请重新修改!");
						return ret;
				    
			    	}
			
			    }
			    
			if(userService.edit(user)<=0){
				ret.put("type","error");
				ret.put("msg","修改用户失败!");
				return ret;
			}
			ret.put("type","success");
			ret.put("msg","修改用户成功!");
			return ret;
		}
		/*
		    * 删除用户
		    */
			@RequestMapping(value="/delete",method=RequestMethod.POST)
		    @ResponseBody
			public Map<String,String> delete(
					@RequestParam(value="ids[]",required=true) Long[] ids
					
					){
				//得到一个返回值
				Map<String,String> ret=new HashMap<String,String>();
//				ret.put("ids",ids.toString());
				if(ids==null){
					ret.put("type","error");
					ret.put("msg","请选择您要删除的数据!");
					return ret;
				}
//				已经勾选我们所要选择的数据后，进行遍历
			
				String idsString="";
				for(Long id:ids){
					idsString+=id+ ",";
				}
//				这个是截取字符串的方法，后面第一个参数0代表，从字符串的第一个字符开始截取，
//				后面一个参数sb.Length - 1代表截取的字符串长度，也就是从第一个字符截取到最后一个字符前一位
				idsString=idsString.substring(0,idsString.length()-1);
				//在Service定义的方法，记得在对应的controller调用
				if(userService.delete(idsString)<=0){
					ret.put("type","error");
					ret.put("msg","删除失败!");
					return ret;
				}
				ret.put("type","success");
				ret.put("msg","删除用户成功!");
				return ret;
			}
}
