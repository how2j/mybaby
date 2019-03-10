package cn.how2j.mybaby.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.how2j.mybaby.service.ConfigService;
import cn.how2j.mybaby.util.SpringUtils;
import cn.hutool.core.util.StrUtil;


@WebFilter(filterName="first",urlPatterns="/*")
public class ResourceFilter implements  Filter{
	
	public ResourceFilter() {
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		ConfigService configService = SpringUtils.getBean(ConfigService.class);
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		
    	boolean result = false;

        HttpSession session = httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
  
        String uri = httpServletRequest.getRequestURI();
 
        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
        


    	
        if(StrUtil.containsAny(page, "uploaded/")) {
        	String passwordFront=  configService.get(ConfigService.passwordFront);
        	String passwordAdmin=  configService.get(ConfigService.passwordAdmin);
    		String passwordInParameter = request.getParameter("password");
    		


    		
        	//先判断提交参数里是否有密码，如果密码匹配就进行
        	if(null!=passwordInParameter && StrUtil.equals(passwordAdmin, passwordInParameter)) {
        		result = true;
        	}
        	else {

                String admin_authentication = (String) session.getAttribute(ConfigService.admin_authentication);
                String fore_authentication = (String) session.getAttribute(ConfigService.fore_authentication);
                
                if("true".equals(admin_authentication) ) {
                	result= true;
                }
                
                else {
 
        	    	if(StrUtil.isEmpty(passwordFront)) {
        	    		result= true;
        	    		
        	    	}
        	    	else {
        	    		result= "true".equals(fore_authentication) ;
        	        }
                }        		
        	}
        }
        else {
        	result = true;
        }
        
        if(result) {
    		chain.doFilter(request, response);
    		return;
        }

        else
        	return;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}