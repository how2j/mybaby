package cn.how2j.mybaby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.how2j.mybaby.service.ConfigService;

@Component
@Order(1)
public class AutoStartRunner implements ApplicationRunner {
	@Autowired ConfigService configService;
	
	
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
    	configService.init();
    }
}