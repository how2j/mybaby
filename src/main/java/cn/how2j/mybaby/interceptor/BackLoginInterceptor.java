package cn.how2j.mybaby.interceptor;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.how2j.mybaby.service.ConfigService;
 
public class BackLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
        String[] requireAuthPages = new String[]{
                "admin_post_list",
                "admin_post_deleted_list",
                "admin_picture_list",
                "admin_video_list",
                "admin_config_list",
        };
  
        String uri = httpServletRequest.getRequestURI();
 
        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
         
        if(begingWith(page, requireAuthPages)){
            String admin_authentication = (String) session.getAttribute(ConfigService.admin_authentication);
            if(!"true".equals(admin_authentication)) {
                httpServletResponse.sendRedirect("admin_login");
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