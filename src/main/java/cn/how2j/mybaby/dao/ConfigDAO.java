package cn.how2j.mybaby.dao;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.how2j.mybaby.pojo.Config;

public interface ConfigDAO extends JpaRepository<Config,Integer>{
	List<Config> findByOrderByIdAsc();
	Config getByKey(String key);
}
