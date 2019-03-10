package cn.how2j.mybaby.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.how2j.mybaby.pojo.Config;
import cn.how2j.mybaby.service.ConfigService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
 
@RestController
public class ConfigController {
	@Autowired ConfigService configService;

	
	
	@GetMapping("/configs")
	public List<Config> list() throws Exception {
		List<Config> configs =configService.list();
		return configs;
	}
	


	@GetMapping("/configs/{id}")
	public Config get(@PathVariable("id") int id) throws Exception {
		Config bean=configService.get(id);
		return bean;
	}

	@PutMapping("/configs/password")
	public String password(MultipartFile image, HttpServletRequest request, HttpSession session) throws Exception {
		String password = request.getParameter("password");
		String passwordInDb=configService.get(ConfigService.passwordAdmin);
		if(StrUtil.equals(passwordInDb, password)) {
			session.setAttribute(ConfigService.admin_authentication, "true");
			return "success";
		}
		return "fail";
	}
	@PutMapping("/configs/forepassword")
	public String forepassword(MultipartFile image, HttpServletRequest request, HttpSession session) throws Exception {
		String password = request.getParameter("password");
		String passwordInDb=configService.get(ConfigService.passwordFront);
		if(StrUtil.equals(passwordInDb, password)) {
			session.setAttribute(ConfigService.fore_authentication, "true");
			return "success";
		}
		return "fail";
	}

	
	public String getSpace() {
		File currentFile =new File("");
		File[] fs= File.listRoots();
		File root=null;
		for (File file : fs) {
			if(currentFile.getAbsolutePath().startsWith(file.getAbsolutePath())) {
				root = file;
			}
		}
		
		long totalSpace = root.getTotalSpace();
		long freeSpace = root.getFreeSpace();
		
		String total= FileUtil.readableFileSize(totalSpace);
		String free= FileUtil.readableFileSize(freeSpace);
		

		String space = StrUtil.format("硬盘总空间是 {}, 可用空间是 {}", total,free);
		return space;
	}
	@GetMapping("/serverInfo")
	public Object serverInfo() {
		String babyName= configService.get(ConfigService.babyName);
		String space= getSpace();
		
		if(StrUtil.isEmpty(babyName))
			babyName ="宝贝";
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("babyName", babyName);
		map.put("space", space);
		return map;
	}

	
	@PutMapping("/configs/{id}")
	public Object update(@PathVariable("id") int id, MultipartFile image,HttpServletRequest request) throws Exception {
		String value = request.getParameter("value");
		

		Config config = configService.get(id);
		
		if("passwordAdmin".equals(config.getKey()) && StrUtil.isEmpty(value)) {
			
			return null;
		}
		
		config.setValue(value);
		configService.update(config);
		return config;
	}
	
	
	
	
	
	
	
	
	
	
}

