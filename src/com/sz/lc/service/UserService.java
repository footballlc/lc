package com.sz.lc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sz.lc.entity.User;

@Service
public interface UserService {
	
	
	//查询数据功能
	public User findByUserName(String username);
	//添加成员
	public int add(User user);
	//修改员工
	public int edit(User user);
	//删除
	public int delete(String ids);
	//获取列表(分页)
	public List<User> findList(Map<String,Object> queryMap);
	//获取所有
	public int getTotal(Map<String,Object> queryMap);

}
