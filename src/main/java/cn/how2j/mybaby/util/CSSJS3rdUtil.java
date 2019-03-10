package cn.how2j.mybaby.util;

import java.io.File;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.system.SystemUtil;

public class CSSJS3rdUtil {

	public static void main(String[] args) {
		String dir= SystemUtil.get(SystemUtil.USER_DIR);
		File f= new File(dir,"src/main/resources/templates/include/fore/foreHeader.html");
		File css3rdFile= new File(dir,"src/main/webapp/css/3rd/3rd.css");
		File js3rdFile= new File(dir,"src/main/webapp/js/3rd/3rd.js");
		

		String fc= FileUtil.readUtf8String(f);
		StringBuffer sbCSS =new StringBuffer();
		List<String> csses= ReUtil.findAll("href=\"(.*?)\"", fc, 1);
		for (String css : csses) {
			File cssFile = new File(dir,"src/main/admin_lte/"+css);
			if(cssFile.getName().endsWith("style.css")) {
				continue;
			}
			if(cssFile.getName().endsWith("3rd.css")) {
				continue;
			}
			String cssEach= FileUtil.readUtf8String(cssFile);
			sbCSS.append(cssEach);
			sbCSS.append("\r\n");
		}
		
		FileUtil.writeUtf8String(sbCSS.toString(), css3rdFile);
		
		StringBuffer sbJS =new StringBuffer();
		List<String> jses= ReUtil.findAll("<script src=\"(.*?)\"", fc, 1);
		for (String js : jses) {
			File jsFile = new File(dir,"src/main/admin_lte/"+js);
			if(jsFile.getName().endsWith("3rd.js")) {
				continue;
			}
			String jsEach= FileUtil.readUtf8String(jsFile);
			sbJS.append(jsEach);
			sbJS.append(";");
			sbJS.append("\r\n");
		}
		
		FileUtil.writeUtf8String(sbJS.toString(), js3rdFile);
		
		
		
	}
}
