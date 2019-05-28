package com.sz.lc.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sz.lc.entity.User;

import net.sf.json.JSONObject;

/*
 * 登录过滤拦截器
 */
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	//请求完成之后
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	//请求正在发生的时候
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	//请求完成之前
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		//获取路径
		String url=request.getRequestURI();
		//System.out.println("进入拦截器:url="+url);
		//强制转换(User)
		Object user=request.getSession().getAttribute("user");
		if(user==null){
			System.out.println("未登录或者登录失效:url="+url);
			if("XMLHttpRequest".equals(request.getHeader("X-Request-With"))){
				//ajax请求
				Map<String,String> ret=new HashMap<String,String>();
				ret.put("type", "error");
				ret.put("msg", "登录状态已失效，请重新登录!");
//				response.getWriter().write(JSONObject.fromObject(ret).toString());
				response.getWriter().write(JSONObject.fromObject(ret).toString());
				return false;
				
			}
		    //没有登录的话，直接先转到登录页面(重定向会登录界面)
			//拿到我们网站的根目录+后面的url,response.sendRedirect(request.getContextPath()+"/system/login");
			//response.sendRedirect(request.getContextPath()+"url");
			response.sendRedirect(request.getContextPath()+"/system/login");
			return false;
		}
		return true;
	}
}
