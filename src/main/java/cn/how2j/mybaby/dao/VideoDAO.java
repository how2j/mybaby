package cn.how2j.mybaby.dao;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.how2j.mybaby.pojo.Video;
import cn.how2j.mybaby.pojo.Post;

public interface VideoDAO extends JpaRepository<Video,Integer>{
	Page<Video> findByStatusAndPostOrderByIndexAsc(String status,Post post,Pageable pageable);
}
