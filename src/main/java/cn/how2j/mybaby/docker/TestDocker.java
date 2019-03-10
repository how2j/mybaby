package cn.how2j.mybaby.docker;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;

public class TestDocker {
	public static void main(String[] args) throws IOException {
		String rootDir = "D:/project/mybaby/docker/";
		
		File folder = FileUtil.file(rootDir);
		
		List<File> fs= FileUtil.loopFiles(folder);
		int count = 0;
		for (File file : fs) {
			String path = FileUtil.subPath(rootDir, file.getParentFile());
			System.out.println(path);
			System.out.println(file);
			upload(path, file );
			System.out.println(++count + "/" + fs.size() + "/t" + file);
		}
		System.out.println("success!");
		
	}
	
	public static void  upload(String path, File file) throws IOException{
		Ftp ftp = new Ftp("192.168.2.2",21,"ftptest","paw123###");
		ftp.upload(path, file);
		ftp.close();
	}
}
