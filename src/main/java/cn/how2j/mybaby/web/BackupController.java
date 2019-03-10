package cn.how2j.mybaby.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.how2j.mybaby.dao.ConfigDAO;
import cn.how2j.mybaby.dao.PostDAO;
import cn.how2j.mybaby.pojo.Config;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.service.ConfigService;
import cn.how2j.mybaby.service.PictureService;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.service.VideoService;
import cn.hutool.core.util.StrUtil;

@RestController
public class BackupController {
	@Autowired PostService postService;
	@Autowired PostDAO postDAO;
	@Autowired ConfigDAO configDAO;
	@Autowired PictureService pictureService;
	@Autowired VideoService videoService;
	@Autowired ConfigService configService;
	
	
	@GetMapping("/backup/allinfo")
	public Object allInfo(@RequestParam("password") String password) {
		if(!valid(password))
			return null;
		List<Post> posts=postDAO.findAll();
		pictureService.fillAll(posts);
		videoService.fillAll(posts);
		return posts;
	}
	@GetMapping("/backup/config")
	public Object config(@RequestParam("password") String password) {
		if(!valid(password))
			return null;
		List<Config> configs=configDAO.findAll();
		return configs;
	}
	@GetMapping("/backup/valid")
	public boolean valid(@RequestParam("password") String password) {
		String passwordInDb=configService.get(configService.passwordAdmin);
		return StrUtil.equals(password, passwordInDb);
	}
//	
//	@GetMapping("/backup/picture")
//	public Object allInfo( @RequestParam("password") String password,@RequestParam("pid") int pid,HttpServletRequest request,HttpServletResponse response) throws Exception {
//		if(!valid(password))
//			return null;
//		
//		File imageFolder= new File(request.getServletContext().getRealPath("uploaded/picture"));
//		
//        File file = new File(imageFolder,pid+".jpg");
//        
//        if(!file.exists())
//        	return null;
//        
//        
//        IoUtil.copy(new FileInputStream(file), response.getOutputStream());
//        
//        
//        return null;
//        
//        
//        
//        
//        
//		
//		
//	}
	
	
	
	
	
	
	

}
