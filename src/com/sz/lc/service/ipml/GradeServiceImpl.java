package com.sz.lc.service.ipml;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sz.lc.dao.GradeDao;
import com.sz.lc.entity.Grade;
import com.sz.lc.service.GradeService;
@Service
public class GradeServiceImpl  implements GradeService{
    @Autowired
    private GradeDao gradeDao;
	@Override
	public int add(Grade grade) {
		// TODO Auto-generated method stub
		return gradeDao.add(grade);
	}

	@Override
	public int edit(Grade grade) {
		// TODO Auto-generated method stub
		return gradeDao.edit(grade);
	}

	@Override
	public int delete(String ids) {
		// TODO Auto-generated method stub
		return gradeDao.delete(ids);
	}

	@Override
	public List<Grade> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return gradeDao.findList(queryMap);
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return gradeDao.getTotal(queryMap);
	}

	@Override
	public List<Grade> findAll() {
		// TODO Auto-generated method stub
		return gradeDao.findAll();
	}

}
