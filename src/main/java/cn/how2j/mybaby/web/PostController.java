package cn.how2j.mybaby.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.service.PictureService;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.service.VideoService;
import cn.how2j.mybaby.util.Page4Navigator;
import cn.hutool.core.date.DateUtil;
 
@RestController
public class PostController {
	@Autowired PostService postService;
	@Autowired PictureService pictureService;
	@Autowired VideoService vidoeService;

	
	
	@GetMapping("/posts")
	public Page4Navigator<Post> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Post> page =postService.list(PostService.status_normal, start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		
		List<Post> posts= page.getContent();
		for (Post post : posts) {
			pictureService.fill(post);
			vidoeService.fill(post);
		}
		return page;
	}
	@GetMapping("/posts/deleted")
	public Page4Navigator<Post> listDeleted(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Post> page =postService.list(PostService.status_delete, start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		
		List<Post> posts= page.getContent();
		for (Post post : posts) {
			pictureService.fill(post);
			vidoeService.fill(post);
		}
		return page;
	}

	@PostMapping("/posts")
	public Object add(Post bean, MultipartFile image, HttpServletRequest request) throws Exception {
		bean.setStatus(PostService.status_normal);
		postService.add(bean);
		saveOrUpdateImageFile(bean, image, request);
		return bean;
	}
	public void saveOrUpdateImageFile(Post bean, MultipartFile image, HttpServletRequest request)
			throws IOException {
//		File imageFolder= new File(request.getServletContext().getRealPath("img/post"));
//		File file = new File(imageFolder,bean.getId()+".jpg");
//		if(!file.getParentFile().exists())
//			file.getParentFile().mkdirs();
//		image.transferTo(file);
////		BufferedImage img = ImageUtil.change2jpg(file);
//		ImageIO.write(img, "jpg", file);
	}

	@DeleteMapping("/posts/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
		Post post = postService.get(id);
		post.setStatus(PostService.status_delete);
		postService.update(post);
		return null;
	}
	@GetMapping("/posts/recover/{id}")
	public String recover(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
		Post post = postService.get(id);
		post.setStatus(PostService.status_normal);
		postService.update(post);
		return null;
	}

	@GetMapping("/posts/{id}")
	public Post get(@PathVariable("id") int id) throws Exception {
		Post bean=postService.get(id);
		return bean;
	}

	@PutMapping("/posts/{id}")
	public Object update(@PathVariable("id") int id, MultipartFile image,HttpServletRequest request) throws Exception {


		String title = request.getParameter("title");
		String text = request.getParameter("text");
		String createDate = request.getParameter("createDate");
		Post post = postService.get(id);
		post.setTitle(title);
		post.setText(text);
		post.setCreateDate(DateUtil.parse(createDate));
		postService.update(post);
		if(image!=null) {
			saveOrUpdateImageFile(post, image, request);
		}
		return post;
	}
}

