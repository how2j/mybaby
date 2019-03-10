package cn.how2j.mybaby.exception;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GloabalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    	e.printStackTrace();
    	Class constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");
    	if(null!=e.getCause()  && constraintViolationException==e.getCause().getClass()) {
    		return "违反了约束，多半是外键约束";
    	}
        return e.getMessage();
    }

}

