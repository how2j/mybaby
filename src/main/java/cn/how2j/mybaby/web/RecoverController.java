package cn.how2j.mybaby.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.how2j.mybaby.pojo.Config;
import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.pojo.Video;
import cn.how2j.mybaby.service.ConfigService;
import cn.how2j.mybaby.service.PictureService;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.service.VideoService;
import cn.hutool.core.date.DateUtil;

@RestController
public class RecoverController {
	@Autowired PostService postService;
	@Autowired PictureService pictureService;
	@Autowired VideoService videoService;
	@Autowired ConfigService configService;
	@Autowired BackupController backupController;
	@Autowired PictureController pictureController;
	@Autowired VideoController videoController;
	
	@GetMapping("/recover/reset")
	public Object reset(@RequestParam("password") String password,HttpServletRequest request) {
		if(!valid(password))
			return null;
		String webFolder = request.getServletContext().getRealPath("");
		postService.reset(webFolder);
		return "success";
	}
	
	@PostMapping("/recover/video")
	public Object recoverVideo(
			@RequestParam("pid") int pid, 
			@RequestParam("index") int index, 
			@RequestParam("name") String name, 
			@RequestParam("status") String status, 
			@RequestParam("createDate") long createTime, 
			@RequestParam("image") MultipartFile image,  
			HttpServletRequest request) {


		
		Video video = new Video();
		
		Date createDate = DateUtil.date(createTime);
		
		Post post = postService.get(pid);
		
		video.setIndex(index);
		video.setName(name);
		video.setCreateDate(createDate);
		
		video.setStatus(status);
		
		video.setPost(post);
		
		videoService.add(video);
		
		try {
			videoController.saveOrUpdateImageFile(video, image, request);
		} catch (IOException e) {
			e.printStackTrace();

			return "fail";
		}
		
		
		return "success";
	}
	
	@PostMapping("/recover/picture")
	public Object recoverPicture(
			@RequestParam("pid") int pid, 
			@RequestParam("index") int index, 
			@RequestParam("name") String name, 
			@RequestParam("status") String status, 
			@RequestParam("createDate") long createTime, 
			@RequestParam("image") MultipartFile image,  
			HttpServletRequest request) {
		Picture picture = new Picture();
		
		Date createDate = DateUtil.date(createTime);
		
		Post post = postService.get(pid);
		
		
		
		picture.setIndex(index);
		picture.setName(name);
		picture.setCreateDate(createDate);
		
		picture.setStatus(status);
		
		picture.setPost(post);
		
		pictureService.add(picture);
		
		try {
			pictureController.saveOrUpdateImageFile(picture, image, request);
		} catch (IOException e) {
			return "fail";
		}
		
		
		return "success";
	}
	
	public boolean valid(String password) {
		return backupController.valid(password);
	}
	
	@PostMapping("/recover/post")
	public Post post(@RequestBody Post post) {
		post.setId(0);
		postService.add(post);

		return post;
	}
	@PostMapping("/recover/config")
	public Config config(@RequestBody Config config) {
		configService.update(config);
		return config;
	}
	

}
