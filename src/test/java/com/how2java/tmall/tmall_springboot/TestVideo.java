package com.how2java.tmall.tmall_springboot;

import java.io.File;

import org.junit.Test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RuntimeUtil;
public class TestVideo {

	
    @Test
    public void test() throws Exception {
    	
    	File file = new File("D:/project/mybaby/src/main/webapp/ffmpeg/ffmpeg.exe");
    	
//    	ffmpeg -i D:\ScreenCapture\input.mp4 -y -f  image2  -ss 1 -vframes 1  D:\ScreenCapture\test1.jpg

    	String cmd =file.getAbsolutePath() + " -i D:/ScreenCapture/input.mp4 -y -f  image2  -ss 1 -vframes 1 -s 200x200 D:/ScreenCapture/test1.jpg";
    	Process p= RuntimeUtil.exec(cmd);
    	TimeInterval timer =DateUtil.timer();
    	p.waitFor();
    	
    	
    	
    	
    	
    	
    }
  
}