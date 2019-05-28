package com.sz.lc.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sz.lc.entity.User;

@Repository
//这个注解会去相应的mapper配置文件查询
public interface UserDao {
	public User findByUserName(String username);
	public int add(User user);
	public List<User> findList(Map<String,Object> queryMap);
	public int getTotal(Map<String,Object> queryMap);
	public int edit(User user);
	public int delete(String ids);
}
