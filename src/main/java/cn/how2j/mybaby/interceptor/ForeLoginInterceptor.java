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
 
public class ForeLoginInterceptor implements HandlerInterceptor {
	
	@Autowired ConfigService configService;
	
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

    	
    	String passwordFront=  configService.get(ConfigService.passwordFront);
    	
    	if(StrUtil.isEmpty(passwordFront))
    		return true;
    	
    	
        HttpSession session = httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
        String[] requireAuthPages = new String[]{
                "homepage"
        };
  
        String uri = httpServletRequest.getRequestURI();
 
        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
         
        if(begingWith(page, requireAuthPages)){
            String fore_authentication = (String) session.getAttribute(ConfigService.fore_authentication);
            if(!"true".equals(fore_authentication)) {
                httpServletResponse.sendRedirect("fore_login");
                return false;
            }
        }
        return true;  
    }
 
    private boolean begingWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            if(StringUtils.startsWith(page, requiredAuthPage)) {
                result = true; 
                break;
            }
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