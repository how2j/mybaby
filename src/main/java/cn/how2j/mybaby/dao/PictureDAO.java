package cn.how2j.mybaby.dao;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;

public interface PictureDAO extends JpaRepository<Picture,Integer>{
	Page<Picture> findByStatusAndPostOrderByIndexAsc(String status,Post post,Pageable pageable);
}
