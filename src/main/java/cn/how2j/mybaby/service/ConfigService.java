package cn.how2j.mybaby.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.how2j.mybaby.dao.ConfigDAO;
import cn.how2j.mybaby.pojo.Config;

@Service
public class ConfigService {
	public static final String passwordFront = "passwordFront";
	public static final String passwordAdmin = "passwordAdmin";
	public static final String babyName = "babyName";
	public static final String birthday = "birthday";

	public static final String admin_authentication = "admin_authentication";
	public static final String fore_authentication = "fore_authentication";
	
	
	public static String initInfo[][] = {
			{"前台密码",passwordFront,"前台访问使用的密码，如果是空，则表示无须密码也可以访问",""},
			{"后台密码",passwordAdmin,"后台访问必须使用的密码，不能够为空，默认是admin","admin"},

			{"宝宝名字",babyName,"宝贝的名字","宝贝"},
			{"出生日期",birthday,"用于计算当前记录的时候宝宝有多大了","2019-01-01"},
	};

	
	
	@Autowired ConfigDAO configDAO;



	public List<Config> list() {
		List<Config> configs=configDAO.findByOrderByIdAsc();
		return configs;
	}

	public void init() {
		for (String[] init : initInfo) {
			String name = init[0];
			String key = init[1];
			String desc = init[2];
			String defaultValue = init[3];
			
			Config config= configDAO.getByKey(key);
			if(null!=config)
				continue;
			
			Config bean= new  Config();
			bean.setName(name);
			bean.setKey(key);
			bean.setDesc(desc);
			bean.setValue(defaultValue);
			configDAO.save(bean);
		}
	}

	public void add(Config bean) {
		configDAO.save(bean);
	}

	public void delete(int id) {
		configDAO.delete(id);
	}

	public Config get(int id) {
		Config c= configDAO.findOne(id);
		return c;
	}
	public String get(String key) {
		Config c= configDAO.getByKey(key);
		if(null==c)
			return null;
		return c.getValue();
				
	}
	public void update(Config bean) {
		configDAO.save(bean);
	}
}
