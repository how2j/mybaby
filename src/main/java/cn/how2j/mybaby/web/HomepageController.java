package cn.how2j.mybaby.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.service.ConfigService;
import cn.how2j.mybaby.service.PictureService;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.service.VideoService;
import cn.how2j.mybaby.util.Page4Navigator;
import cn.hutool.core.date.DateUtil;
 
@RestController
public class HomepageController {
	@Autowired PostService postService;
	@Autowired PictureService pictureService;
	@Autowired VideoService vidoeService;
	@Autowired ConfigService configService;

	@GetMapping("/homepageData")
	public Page4Navigator<Post> homepageData(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Post> page =postService.list(PostService.status_normal, start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		
		String d= configService.get(configService.birthday);
		
		Date birthDay = DateUtil.parse(d);
		
		
		
		List<Post> posts= page.getContent();
		for (Post post : posts) {
			post.setBirthdayDate(birthDay);
			pictureService.fill(post);
			vidoeService.fill(post);
		}
		return page;
	}
}

