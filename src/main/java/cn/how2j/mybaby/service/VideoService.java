package cn.how2j.mybaby.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.how2j.mybaby.dao.VideoDAO;
import cn.how2j.mybaby.dao.PostDAO;
import cn.how2j.mybaby.pojo.Video;
import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.util.Page4Navigator;

@Service
public class VideoService {
	public static String status_delete = "delete";
	public static String status_normal = "normal";
	
	@Autowired PostDAO postDAO;
	@Autowired VideoDAO videoDAO;


	public Page4Navigator<Video> list(String status,int pid ,int start, int size, int navigatePages) {
		Post post= postDAO.getOne(pid);
		Pageable pageable = new PageRequest(start, size);
		Page pageFromJPA =videoDAO.findByStatusAndPostOrderByIndexAsc(status,post,pageable);
		return new Page4Navigator<>(pageFromJPA,navigatePages);
	}


	public void add(Video bean) {
		videoDAO.save(bean);
	}

	public void delete(int id) {
		videoDAO.delete(id);
	}

	public Video get(int id) {
		Video c= videoDAO.findOne(id);
		return c;
	}
	public void update(Video bean) {
		videoDAO.save(bean);
	}


	public void adjust(int id, int index) {
		Video p = get(id);
		p.setIndex(index);
		update(p);
		
	}
	public void fill(Post post) {
		 List<Video> beans= list(status_normal,post.getId(),0,1000,5).getContent();
		 post.setVideos(beans);
	}


	public void fillAll(Post post) {
		 List<Video> beansNormal= list(status_normal,post.getId(),0,1000,5).getContent();
		 List<Video> beansDelete= list(status_delete,post.getId(),0,1000,5).getContent();
		 List<Video> beans = new ArrayList<>();
		 beans.addAll(beansNormal);
		 beans.addAll(beansDelete);
		 post.setVideos(beans);
	}
	public void fillAll(List<Post> posts) {
		for (Post post : posts) {
			fillAll(post);
		}
	}
}
