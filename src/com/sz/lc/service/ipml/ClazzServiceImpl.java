package com.sz.lc.service.ipml;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sz.lc.dao.ClazzDao;
import com.sz.lc.entity.Clazz;
import com.sz.lc.service.ClazzService;
@Service
public class ClazzServiceImpl implements ClazzService{
     @Autowired
     private ClazzDao clazzDao;
	@Override
	public int add(Clazz clazz) {
		// TODO Auto-generated method stub
		return clazzDao.add(clazz);
	}

	@Override
	public int edit(Clazz clazz) {
		// TODO Auto-generated method stub
		return clazzDao.edit(clazz);
	}

	@Override
	public int delete(String ids) {
		// TODO Auto-generated method stub
		return clazzDao.delete(ids);
	}

	@Override
	public List<Clazz> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return clazzDao.findList(queryMap);
	}

	@Override
	public List<Clazz> findAll() {
		// TODO Auto-generated method stub
		return clazzDao.findAll();
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return 0;
	}

}
