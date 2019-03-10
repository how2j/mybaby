package cn.how2j.mybaby.interceptor;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.how2j.mybaby.service.ConfigService;
import cn.hutool.core.util.StrUtil;
 
public class ResourceInterceptor implements HandlerInterceptor {
	@Autowired ConfigService configService;
	
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    	boolean result = false;
    	String passwordFront=  configService.get(ConfigService.passwordFront);

        HttpSession session = httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
  
        String uri = httpServletRequest.getRequestURI();
 
        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
        
        
        if(StrUtil.containsAny(page, "uploaded")) {
            String admin_authentication = (String) session.getAttribute(ConfigService.admin_authentication);
            String fore_authentication = (String) session.getAttribute(ConfigService.fore_authentication);
            
            if("true".equals(admin_authentication) )
            	result= true;
            
            else {
    	    	if(StrUtil.isEmpty(passwordFront))
    	    		result= true;
    	    	else {
    	    		result= "true".equals(fore_authentication) ;
    	        }
            }
        }
        else {
        	result = true;
        }


        return result;
    }
 
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
 
    }
 
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}