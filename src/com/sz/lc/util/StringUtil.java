package com.sz.lc.util;

import java.util.Date;
import java.util.List;

/*
 * 实体类工具
 * 
 */
public class StringUtil {
	/*
	 * 将给定的list按照指定的分隔符分割成字符串返回
	 * 
	 */
	//split分隔符,joinString支持传入一个list
	public static String joinString(List<Long> list,String split){
	    String ret="";
	     for(Long l:list){
	    	 ret+=l+split;
	     }	
	     //里面有内容(不为空)
		  if(!"".equals(ret)){
			 ret=ret.substring(0,ret.length()-split.length());
		 }
		return ret;
	}
 //添加学号(属于student)静态方法
	public static String generateSn(String prefix,String suffix){
		return prefix+new Date().getTime()+suffix;
	}
}
