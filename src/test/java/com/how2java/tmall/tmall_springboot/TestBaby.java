package com.how2java.tmall.tmall_springboot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.how2j.mybaby.Application;
import cn.how2j.mybaby.dao.PictureDAO;
import cn.how2j.mybaby.dao.VideoDAO;
import cn.how2j.mybaby.pojo.Picture;
import cn.how2j.mybaby.pojo.Post;
import cn.how2j.mybaby.pojo.Video;
import cn.how2j.mybaby.service.PictureService;
import cn.how2j.mybaby.service.PostService;
import cn.how2j.mybaby.service.VideoService;
import cn.how2j.mybaby.util.ImageUtil;
import cn.how2j.mybaby.util.LogPrintStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.system.SystemUtil;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestBaby {
	public TestBaby() {
		LogPrintStream.init();
	}
  
	
	@Autowired PostService postService;
	@Autowired PictureService pictureService;
	@Autowired VideoService videoService;
	@Autowired PictureDAO pictureDAO;
	@Autowired VideoDAO videoDAO;
	
	
//	@Test
//    public void makeCSSAndJS() throws Exception {
//    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//    	
//    	
//    	
//    	
//    }
    	
    	

//	@Test
    public void test() throws Exception {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	
    	File ffmpegFile=null;
    	
    	if(SystemUtil.getOsInfo().isWindows()) {
    		ffmpegFile = FileUtil.file(request.getServletContext().getRealPath("ffmpeg/ffmpeg.exe"));
    	}
    	else {
    		ffmpegFile = FileUtil.file(request.getServletContext().getRealPath("ffmpeg/ffmpeg"));
    	}

    	
    	List<Video> videos= videoDAO.findAll();
    	for (Video video : videos) {
			File mp4Folder = FileUtil.file(request.getServletContext().getRealPath("uploaded/video"));
			File previewFolder = FileUtil.file(request.getServletContext().getRealPath("uploaded/preview"));
			File smallMp4Folder = FileUtil.file(request.getServletContext().getRealPath("uploaded/small_video"));
			
			mp4Folder.mkdirs();
			previewFolder.mkdirs();
			smallMp4Folder.mkdirs();
			
			File mp4File =new File(mp4Folder,video.getId()+".mp4");
			File mp4_small_File =new File(smallMp4Folder,video.getId()+"_small.mp4");
			File previewFile = new File(previewFolder,video.getId()+".jpg");
			String cmd_preview = StrUtil.format("{} -i {} -y -f  image2  -ss 1 -vframes 1 -s 200x200 {}", ffmpegFile, mp4File,previewFile);
			Process p_preview= RuntimeUtil.exec(cmd_preview);
			p_preview.waitFor();
//			

			String cmd_small = StrUtil.format("{} -i {} -vf scale=360:-2 {} -y", ffmpegFile, mp4File,mp4_small_File);
			System.out.println(cmd_small);
			Process p_small= RuntimeUtil.exec(cmd_small);
			RuntimeUtil.getResult(p_small);
//			p_small.waitFor();
//			break;
		}
	}

    	
	
    @Test
    public void deletePost() throws Exception {
    	List<Post> ps= postService.list(PostService.status_delete, 0, 10000, 5).getContent();
    	for (Post p : ps) {
        	pictureService.fillAll(p);
        	videoService.fillAll(p);
//			System.out.println(p.getTitle());
			
			List<Picture> pictures= p.getPictures();
			for (Picture picture : pictures) {
				String folder = "D:/project/mybaby/src/main/webapp/uploaded";
				File f1 = new File(folder,"picture/"+picture.getId()+".jpg");
				File f2 = new File(folder,"middlePicture/"+picture.getId()+".jpg");
				File f3 = new File(folder,"smallPicture/"+picture.getId()+".jpg");
				System.out.println(f1.exists());
				System.out.println(f2.exists());
				System.out.println(f3.exists());
				f1.delete();
				f2.delete();
				f3.delete();
				pictureService.delete(picture.getId());
			}
			List<Video> vidoes= p.getVideos();
			System.out.println(vidoes.size());
			for (Video video : vidoes) {
				String folder1 = "D:/project/mybaby/src/main/webapp/uploaded";
				File f1 = new File(folder1,"video/"+video.getId()+".mp4");
				f1.delete();
				String folder2 = "D:/project/mybaby/src/main/webapp/uploaded";
				File f2 = new File(folder2,"small_video/"+video.getId()+"_small.mp4");
				f2.delete();
				String folder3 = "D:/project/mybaby/src/main/webapp/uploaded";
				File f3 = new File(folder3,"preview/"+video.getId()+".jpg");
				f3.delete();
				videoService.delete(video.getId());
			}
			postService.delete(p.getId());
		}
    }

    	
//    @Test
    public void resizePictures() throws Exception {
    	List<Picture> ps = pictureDAO.findAll();
    	for (Picture p : ps) {
			if(true)
				break;
			File file = new File("D:/project/mybaby/src/main/webapp/uploaded/picture",p.getId()+".jpg");
			BufferedImage img= cn.hutool.core.util.ImageUtil.read(file);
	        int middleWidth = 500;
	        int middleHeight =  (int) (((float)img.getHeight()*middleWidth)/img.getWidth());
			
	        File middleFile = new File(file.getParentFile().getParentFile(),"middlePicture/"+file.getName());
	        File smallFile = new File(file.getParentFile().getParentFile(),"smallPicture/"+file.getName());

	        if(!middleFile.exists()) {
		        ImageUtil.resizeImage(file, middleWidth, middleHeight, middleFile);

	        }
	        
	        int smallWidth = 100;
	        int smallHeight =  (int) (((float)img.getHeight()*smallWidth)/img.getWidth());
	        
	        if(!smallFile.exists()) {
		        ImageUtil.resizeImage(file, smallWidth, smallHeight, smallFile);
	        }
		}
    }
    
//    @Test
    public void deleteFilesWhichIsNotInDatabase() throws Exception {
    	File folder = FileUtil.file("D:/project/mybaby/src/main/webapp/uploaded/picture");
    	File[] files=  folder.listFiles();
    	for (File f : files) {
			String name = f.getName();
			int id= ReUtil.getFirstNumber(name);
//			System.out.println(id);
			
			Picture picture = pictureService.get(id);
//			System.out.println(picture);
			
			if(null==picture) {
				File file = new File("D:/project/mybaby/src/main/webapp/uploaded/picture",id+".jpg");
		        File middleFile = new File(file.getParentFile().getParentFile(),"middlePicture/"+file.getName());
		        File smallFile = new File(file.getParentFile().getParentFile(),"smallPicture/"+file.getName());
		        
		        System.out.println(file.exists());
		        System.out.println(middleFile.exists());
		        System.out.println(smallFile.exists());
		        
		        
		        file.delete();
		        middleFile.delete();
		        smallFile.delete();
			}
			
		}
    }
//    @Test
    public void calcFileSizeAndMD5() throws Exception {
    	List<Picture> ps = pictureDAO.findAll();
    	for (Picture p : ps) {
    		if(true)
    			break;
    		File file = new File("D:/project/mybaby/src/main/webapp/uploaded/picture",p.getId()+".jpg");
    		long size = file.length();
    		String md5= SecureUtil.md5(file);
    		System.out.println(size);
    		System.out.println(md5);
    		p.setSize(size);
    		p.setMd5(md5);
    		pictureDAO.save(p);
    	}
    	
    	
    	List<Video> vs = videoDAO.findAll();
    	for (Video v: vs) {
    		File file = new File("D:/project/mybaby/src/main/webapp/uploaded/video",v.getId()+".mp4");
    		long size = file.length();
    		String md5= SecureUtil.md5(file);
    		v.setSize(size);
    		v.setMd5(md5);
    		videoDAO.save(v);
    	}
    	
    }

  
}