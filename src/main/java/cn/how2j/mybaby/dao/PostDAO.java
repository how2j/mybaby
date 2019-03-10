package cn.how2j.mybaby.dao;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.how2j.mybaby.pojo.Post;

public interface PostDAO extends JpaRepository<Post,Integer>{
	Page<Post> findByStatusOrderByCreateDateDescIdDesc(String status,Pageable pageable);
}
