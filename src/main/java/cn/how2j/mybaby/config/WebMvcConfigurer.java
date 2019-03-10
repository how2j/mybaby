package cn.how2j.mybaby.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.how2j.mybaby.interceptor.BackLoginInterceptor;
import cn.how2j.mybaby.interceptor.ForeLoginInterceptor;
 
@Configuration
class WebMvcConfigurer extends WebMvcConfigurerAdapter{
     
    @Bean
    public BackLoginInterceptor getBackLoginIntercepter() {
        return new BackLoginInterceptor();
    }
    
    @Bean
    public ForeLoginInterceptor getForeLoginIntercepter() {
    	return new ForeLoginInterceptor();
    }

     
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry
        .addInterceptor(getBackLoginIntercepter())
        .addPathPatterns("/**");      
        registry
        .addInterceptor(getForeLoginIntercepter())
        .addPathPatterns("/**");      
     
    }

    
}