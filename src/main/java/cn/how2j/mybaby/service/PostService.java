package cn.how2j.mybaby.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.how2j.mybaby.dao.PictureDAO;
import cn.how2j.mybaby.dao.PostDAO;
import cn.how2j.mybaby.dao.VideoDAO;
import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.pojo.Video;
import cn.how2j.mybaby.util.Page4Navigator;
import cn.hutool.core.io.FileUtil;

@Service
public class PostService {
	public static String status_delete = "delete";
	public static String status_normal = "normal";
	
	@Autowired PostDAO postDAO;
	@Autowired PictureDAO pictureDAO;
	@Autowired VideoDAO videoDAO;
	@Autowired PictureService pictureService;
	@Autowired VideoService videoService;


	public Page4Navigator<Post> list(String status,int start, int size, int navigatePages) {
		Pageable pageable = new PageRequest(start, size);
		Page pageFromJPA =postDAO.findByStatusOrderByCreateDateDescIdDesc(status,pageable);

		return new Page4Navigator<>(pageFromJPA,navigatePages);
	}


	public void add(Post bean) {
		postDAO.save(bean);
	}

	public void delete(int id) {
		postDAO.delete(id);
	}

	public Post get(int id) {
		Post c= postDAO.findOne(id);
		return c;
	}
	public void update(Post bean) {
		postDAO.save(bean);
	}


	public void reset(String webFolder) {
		List<Picture> pictures= pictureDAO.findAll();
		for (Picture picture : pictures) {
			pictureDAO.delete(picture.getId());
		}
		List<Video> videos= videoDAO.findAll();
		for (Video video : videos) {
			videoDAO.delete(video.getId());
		}
		
		List<Post> posts= postDAO.findAll();
		for (Post post : posts) {
			postDAO.delete(post.getId());
		}
		

		
		File uploadedFolder = new File(webFolder,"uploaded");
		FileUtil.clean(uploadedFolder);
		
		
		
		
		
		
		
		
		
	}
}
