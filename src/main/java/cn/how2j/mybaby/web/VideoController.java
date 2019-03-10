package cn.how2j.mybaby.web;

import java.io.File;
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
import cn.how2j.mybaby.pojo.Video;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.service.VideoService;
import cn.how2j.mybaby.util.Page4Navigator;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
 
@RestController
public class VideoController {
	@Autowired VideoService videoService;
	@Autowired PostService postService;
	
	@GetMapping("/posts/{pid}/videos")
	public Page4Navigator<Video> list(@PathVariable("pid") int pid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "100000") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Video> page =videoService.list(VideoService.status_normal,pid,start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		return page;
	}
	@GetMapping("/posts/{pid}/deleted/videos")
	public Page4Navigator<Video> listDeleted(@PathVariable("pid") int pid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "100000") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Video> page =videoService.list(VideoService.status_delete,pid,start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		return page;
	}
	
	@PostMapping("/videos")
	public Object add(@RequestParam("pid") int pid, @RequestParam("name") String name,MultipartFile image, HttpServletRequest request) throws Exception {
		Video bean = new Video();
		bean.setName(name);
		bean.setStatus(VideoService.status_normal);
		bean.setCreateDate(DateUtil.date());
		Post post = postService.get(pid);
		bean.setPost(post);
		videoService.add(bean);
		bean.setIndex(bean.getId()*10);
		videoService.update(bean);
		saveOrUpdateImageFile(bean, image, request);
		return bean;
	}

    public void saveOrUpdateImageFile(Video bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        File imageFolder= new File(request.getServletContext().getRealPath("uploaded/video"));
        File mp4File = new File(imageFolder,bean.getId()+".mp4");
        if(!mp4File.getParentFile().exists())
            mp4File.getParentFile().mkdirs();
        image.transferTo(mp4File);
        
		long size = mp4File.length();
		String md5= SecureUtil.md5(mp4File);
		bean.setSize(size);
		bean.setMd5(md5);
		videoService.update(bean);
		
    	File ffmpegFile=null;
    	
    	if(SystemUtil.getOsInfo().isWindows()) {
    		ffmpegFile = FileUtil.file(request.getServletContext().getRealPath("ffmpeg/ffmpeg.exe"));
    	}
    	else {
    		ffmpegFile = FileUtil.file(request.getServletContext().getRealPath("ffmpeg/ffmpeg"));
    		
    		String cmd = "chmod 777 " + ffmpegFile;
    		Process p= RuntimeUtil.exec(cmd);
    		try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}

		File mp4Folder = FileUtil.file(request.getServletContext().getRealPath("uploaded/video"));
		File previewFolder = FileUtil.file(request.getServletContext().getRealPath("uploaded/preview"));
		File smallMp4Folder = FileUtil.file(request.getServletContext().getRealPath("uploaded/small_video"));
		
		mp4Folder.mkdirs();
		previewFolder.mkdirs();
		smallMp4Folder.mkdirs();
		
		File mp4_small_File =new File(smallMp4Folder,bean.getId()+"_small.mp4");
		File previewFile = new File(previewFolder,bean.getId()+".jpg");
		String cmd_preview = StrUtil.format("{} -i {} -y -ss 1 -vframes 1 -s 200x200 {}", ffmpegFile, mp4File,previewFile);
		
		
		Process p_preview= RuntimeUtil.exec(cmd_preview);
		String result = RuntimeUtil.getResult(p_preview);

//			

		String cmd_small = StrUtil.format("{} -i {} -vf scale=360:-2 {} -y", ffmpegFile, mp4File,mp4_small_File);
		Process p_small= RuntimeUtil.exec(cmd_small);
		//这里不用waitFor 而用 getResult 是因为 waitFor 不起作用。。。
		result = RuntimeUtil.getResult(p_small);

    }	

	@DeleteMapping("/videos/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
		Video video = videoService.get(id);
		video.setStatus(VideoService.status_delete);
		videoService.update(video);
		return null;
	}

	@GetMapping("/videos/{id}")
	public Video get(@PathVariable("id") int id) throws Exception {
		Video bean=videoService.get(id);
		return bean;
	}

	@PutMapping("/videos/{id}")
	public Object update( @PathVariable("id") int id, MultipartFile image,HttpServletRequest request) throws Exception {
		Video bean = videoService.get(id);
		String name = request.getParameter("name");
		bean.setName(name);
		videoService.update(bean);
		return bean;
	}
	@PostMapping("videos/adjustIndex")
	public Object adjust(@RequestParam("indexJsons") String indexJsons) {
		
		JSONArray a=JSONUtil.parseArray(indexJsons);
		
		for (Object object : a) {
			JSONObject o = (JSONObject)object;
			int id= o.getInt("id");
			int index= o.getInt("index");
			videoService.adjust(id,index);
		}
		
		return "success";
		
	}
	@GetMapping("/videos/recover/{id}")
	public Object recover(@PathVariable("id") int id) throws Exception {
		Video bean=videoService.get(id);
		bean.setStatus(VideoService.status_normal);
		
		Page4Navigator<Video> page =videoService.list(VideoService.status_normal,bean.getPost().getId(),0, 10000, 5);  
		List<Video> ps= page.getContent();
		int index = 1;
		if(!ps.isEmpty()) {
			index =ps.get(ps.size()-1).getIndex() + 1;
		}
		bean.setIndex(index);
		videoService.update(bean);
		return null;
	}
	
}

