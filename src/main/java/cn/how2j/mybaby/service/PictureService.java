package cn.how2j.mybaby.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.how2j.mybaby.dao.PictureDAO;
import cn.how2j.mybaby.dao.PostDAO;
import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.util.Page4Navigator;

@Service
public class PictureService {
	public static String status_delete = "delete";
	public static String status_normal = "normal";
	
	@Autowired PostDAO postDAO;
	@Autowired PictureDAO pictureDAO;


	public Page4Navigator<Picture> list(String status,int pid ,int start, int size, int navigatePages) {
		Post post= postDAO.getOne(pid);
		Pageable pageable = new PageRequest(start, size);
		Page pageFromJPA =pictureDAO.findByStatusAndPostOrderByIndexAsc(status,post,pageable);
		return new Page4Navigator<>(pageFromJPA,navigatePages);
	}


	public void add(Picture bean) {
		pictureDAO.save(bean);
	}

	public void delete(int id) {
		pictureDAO.delete(id);
	}

	public Picture get(int id) {
		Picture c= pictureDAO.findOne(id);
		return c;
	}
	public void update(Picture bean) {
		pictureDAO.save(bean);
	}


	public void adjust(int id, int index) {
		Picture p = get(id);
		p.setIndex(index);
		update(p);
		
	}

	public void fillAll(Post post) {
		 List<Picture> beansNormal= list(status_normal,post.getId(),0,1000,5).getContent();
		 List<Picture> beansDelete= list(status_delete,post.getId(),0,1000,5).getContent();
		 List<Picture> beans = new ArrayList<>();
		 beans.addAll(beansNormal);
		 beans.addAll(beansDelete);
		 
		 post.setPictures(beans);
	}
	public void fill(Post post) {
		 List<Picture> beans= list(status_normal,post.getId(),0,1000,5).getContent();
		 post.setPictures(beans);
	}


	public void fillAll(List<Post> posts) {
		for (Post post : posts) {
			fillAll(post);
		}
	}
}
