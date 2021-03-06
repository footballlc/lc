package com.sz.lc.service.ipml;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sz.lc.dao.UserDao;
import com.sz.lc.entity.User;
import com.sz.lc.service.UserService;
@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private UserDao userDao;
	
	
	
	@Override
	public User findByUserName(String username) {
		// TODO Auto-generated method stub 
		return userDao.findByUserName(username);
	}



	@Override
	public int add(User user) {

		return userDao.add(user);
	}



	@Override
	//查询数据
	public List<User> findList(Map<String,Object> queryMap) {
		// TODO Auto-generated method stub
		return userDao.findList(queryMap);
	}



	@Override
	public int getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return userDao.getTotal(queryMap);
	}



	@Override
	public int edit(User user) {
		// TODO Auto-generated method stub
		return userDao.edit(user);
	}



	@Override
	public int delete(String ids) {
		// TODO Auto-generated method stub
		return userDao.delete(ids);
	}

}
