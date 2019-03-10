package cn.how2j.mybaby.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import cn.how2j.mybaby.service.ConfigService;

@Controller
public class AdminPageController {
	@GetMapping(value="/admin")
    public String admin(){
		return "redirect:admin_post_list";
    }
	@GetMapping(value="/admin_category_list")
	public String listCategory(){
		return "admin/listCategory";
	}
	@GetMapping(value="/admin_index_list")
	public String listIndex(){
		return "admin/listIndex";
	}
	@GetMapping(value="/admin_post_list")
	public String listPost(){
		return "admin/listPost";
	}
	@GetMapping(value="/admin_post_deleted_list")
	public String listPostDeleted(){
		return "admin/listPostDeleted";
	}
	@GetMapping(value="/admin_fund_list")
	public String listFund(){
		return "admin/listFund";
	}
	@GetMapping(value="/admin_user_list")
	public String listUser(){
		return "admin/listUser";
	}
	@GetMapping(value="/admin_stastic")
	public String stastic(){
		return "admin/stastic";
	}
	@GetMapping(value="/admin_picture_list")
	public String listPicture(){
		return "admin/listPicture";
	}
	@GetMapping(value="/admin_video_list")
	public String listVideo(){
		return "admin/listVideo";
	}
	@GetMapping(value="/")
	public String index(){
		return "redirect:/homepage";
	}
	
	@GetMapping(value="/homepage")
	public String homepage(){
		return "fore/homepage";
	}
	
	@GetMapping(value="/homepage4mobile")
	public String homepage4mobile(){
		return "fore/homepage4mobile";
	}

	@GetMapping(value="/admin_config_list")
	public String config(){
		return "admin/listConfig";
	}
	@GetMapping(value="/admin_login")
	public String admin_login(){
		return "admin/login";
	}
	@GetMapping(value="/admin_about")
	public String admin_about(){
		return "admin/about";
	}
	
	@GetMapping(value="/fore_login")
	public String fore_login(){
		return "fore/login";
	}
	
	@GetMapping(value="/test")
	public String test(){
		return "fore/test";
	}
	@GetMapping(value="/admin_logout")
    public String admin_logout(HttpSession session){
		session.removeAttribute(ConfigService.admin_authentication);
		return "redirect:admin_login";
    }
}
