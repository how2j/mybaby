package cn.how2j.mybaby;
import java.io.IOException;
import java.net.URISyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import cn.how2j.mybaby.util.LogPrintStream;
import cn.hutool.core.io.FileUtil; 



@SpringBootApplication
@ServletComponentScan("cn.how2j.mybaby.filter")
public class Application extends SpringBootServletInitializer{
    public static void main(String[] args) throws IOException, URISyntaxException {
    	FileUtil.mkdir("/mybaby/sqlitedbfile");
    	LogPrintStream.init();
    	System. setProperty("java.awt.headless", "false");
    	SpringApplication.run(Application.class, args);
    	System.out.println("http://127.0.0.1:8080/mybaby/admin_post_list");
    	
    }
}
