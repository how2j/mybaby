package cn.how2j.mybaby.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.service.PictureService;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.util.ImageUtil;
import cn.how2j.mybaby.util.Page4Navigator;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
 
@RestController
public class PictureController {
	
	
	
	@Autowired PictureService pictureService;
	@Autowired PostService postService;
	
	@GetMapping("/posts/{pid}/pictures")
	public Page4Navigator<Picture> list(@PathVariable("pid") int pid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "100000") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Picture> page =pictureService.list(PictureService.status_normal,pid,start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		return page;
	}
	@GetMapping("/posts/{pid}/deleted/pictures")
	public Page4Navigator<Picture> listDeleted(@PathVariable("pid") int pid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "100000") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Picture> page =pictureService.list(PictureService.status_delete,pid,start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		return page;
	}
	
	@PostMapping("/pictures")
	public synchronized Object add(@RequestParam("pid") int pid, @RequestParam("image") MultipartFile images[], HttpServletRequest request) throws Exception {
		for (MultipartFile image : images) {
			Picture bean = new Picture();
			bean.setName("");
			bean.setStatus(PictureService.status_normal);
			bean.setCreateDate(DateUtil.date());
			Post post = postService.get(pid);
			bean.setPost(post);
			pictureService.add(bean);
			bean.setIndex(bean.getId()*10);
			pictureService.update(bean);
			saveOrUpdateImageFile(bean, image, request);
		}
		return null;
	}

    public void saveOrUpdateImageFile(Picture bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        File imageFolder= new File(request.getServletContext().getRealPath("uploaded/picture"));
        File file = new File(imageFolder,bean.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        
        File middleFile = new File(file.getParentFile().getParentFile(),"middlePicture/"+file.getName());
        File smallFile = new File(file.getParentFile().getParentFile(),"smallPicture/"+file.getName());
        
        int middleWidth = 500;
        int middleHeight =  (int) (((float)img.getHeight()*middleWidth)/img.getWidth());
        
        
        ImageUtil.resizeImage(file, middleWidth, middleHeight, middleFile);
        int smallWidth = 100;
        int smallHeight =  (int) (((float)img.getHeight()*smallWidth)/img.getWidth());
        
        
        ImageUtil.resizeImage(file, smallWidth, smallHeight, smallFile);
        
		long size = file.length();
		String md5= SecureUtil.md5(file);
		
		BufferedImage theimage= cn.hutool.core.util.ImageUtil.read(file);
		bean.setWidth(theimage.getWidth());
		bean.setHeight(theimage.getHeight());

		bean.setSize(size);
		bean.setMd5(md5);
        pictureService.update(bean);
        
    }	

	@DeleteMapping("/pictures/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
		Picture picture = pictureService.get(id);
		picture.setStatus(PictureService.status_delete);
		pictureService.update(picture);
		return null;
	}

	@GetMapping("/pictures/{id}")
	public Picture get(@PathVariable("id") int id) throws Exception {
		Picture bean=pictureService.get(id);
		return bean;
	}

	@PutMapping("/pictures/{id}")
	public Object update( @PathVariable("id") int id, MultipartFile image,HttpServletRequest request) throws Exception {
		Picture bean = pictureService.get(id);
		String name = request.getParameter("name");
		bean.setName(name);
		pictureService.update(bean);
		return bean;
	}
	@PostMapping("pictures/adjustIndex")
	public Object adjust(@RequestParam("indexJsons") String indexJsons) {
		
		JSONArray a=JSONUtil.parseArray(indexJsons);
		
		for (Object object : a) {
			JSONObject o = (JSONObject)object;
			int id= o.getInt("id");
			int index= o.getInt("index");
			pictureService.adjust(id,index);
		}
		
		return "success";
		
	}
	@GetMapping("/pictures/recover/{id}")
	public Object recover(@PathVariable("id") int id) throws Exception {
		Picture bean=pictureService.get(id);
		bean.setStatus(PictureService.status_normal);
		
		Page4Navigator<Picture> page =pictureService.list(PictureService.status_normal,bean.getPost().getId(),0, 10000, 5);  
		List<Picture> ps= page.getContent();
		int index = 1;
		if(!ps.isEmpty()) {
			index =ps.get(ps.size()-1).getIndex() + 1;
		}
		bean.setIndex(index);
		pictureService.update(bean);
		return null;
	}
	
}

