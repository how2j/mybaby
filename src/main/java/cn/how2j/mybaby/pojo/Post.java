package cn.how2j.mybaby.pojo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cn.hutool.core.date.DateUtil;

@Entity
@Table(name = "post")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id")
	private int id;
	private String title;
	private String text;
	private String status;
	private Date createDate;

	@Transient
	private Date birthdayDate;

	@Transient
	private String age;

	@Transient
	private List<Picture> pictures;
	@Transient
	private List<Video> videos;
	
	
	
	public Date getBirthdayDate() {
		return birthdayDate;
	}
	public void setBirthdayDate(Date birthdayDate) {
		this.birthdayDate = birthdayDate;
	}
	public List<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	public List<Video> getVideos() {
		return videos;
	}
	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAge() {
        Date date = this.birthdayDate;
        if(null==date)
        	return "";
        Calendar birthday =         DateUtil.calendar(date);
        Calendar now = Calendar.getInstance();  
        now.setTime(this.getCreateDate());
        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);  
        int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);  
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);  
        if(day<0){  
            month -= 1;  
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。  
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);  
        }  
        if(month<0){  
            month = (month+12)%12;  
            year--;  
        }  
        String age;
        if(0==month&&0==year) {
        	age = day+"天";
        }
        else if(0==year) {
        	age = month+"月"+day+"天";
        }
        else{
        	age =year+"岁"+month+"月"+day+"天";
        }
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", text=" + text + ", status=" + status + ", createDate="
				+ createDate + ", birthdayDate=" + birthdayDate + ", age=" + age + ", pictures=" + pictures
				+ ", videos=" + videos + "]";
	}	
	
	
	
	
	
	
	
	
}
