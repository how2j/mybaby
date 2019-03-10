package cn.how2j.mybaby.util;
import java.util.Random;

import cn.hutool.core.convert.Convert;

public class ColorUtil {
 
	/**
	 * @Title:main
	 * @Description:生成随机颜色
	 * @param:@param args
	 * @return: void
	 * @throws
	 */
	public static void main(String[] args) 
	{
		String color;
		color = randomColor();
	}
	
	public static String[] randomColors() {
		String color1;
		String color2;
		
		String red1; 
		String green1;
		String blue1;
		String red2; 
		String green2;
		String blue2;
		
		//生成随机对象
		Random random = new Random();  
		int base = 200;
		int step = 20;
		int randomRed1 = random.nextInt(base);
		int randomGreen1 = random.nextInt(base);
		int randomBlue1 = random.nextInt(base);
		int randomRed2 = randomRed1+step;
		int randomGreen2 = randomGreen1+step;
		int randomBlue2 = randomBlue1+step;
		
		red1 = Integer.toHexString(randomRed1).toUpperCase();
		green1 = Integer.toHexString(randomGreen1).toUpperCase(); 
		blue1 = Integer.toHexString(randomBlue1).toUpperCase();  

		red2 = Integer.toHexString(randomRed2).toUpperCase();
		green2 = Integer.toHexString(randomGreen2).toUpperCase(); 
		blue2 = Integer.toHexString(randomBlue2).toUpperCase();  
	       
		red1 = red1.length()==1 ? "0" + red1 : red1 ;  
		green1 = green1.length()==1 ? "0" + green1 : green1 ; 
		blue1 = blue1.length()==1 ? "0" + blue1 : blue1 ;
		color1 = "#"+red1+green1+blue1;

		red2 = red2.length()==1 ? "0" + red2 : red2 ;  
		green2 = green2.length()==1 ? "0" + green2 : green2 ; 
		blue2 = blue2.length()==1 ? "0" + blue2 : blue2 ;
		color2 = "#"+red2+green2+blue2;
		
		return new String[] {color1,color2};
	}	

	public static String randomColor() {
		String color;
		//红色
		String red; 
		//绿色
		String green;
		//蓝色
		String blue;
		//生成随机对象
		Random random = new Random();  
		//生成红色颜色代码
		red = Integer.toHexString(random.nextInt(256)).toUpperCase();
		//生成绿色颜色代码
		green = Integer.toHexString(random.nextInt(256)).toUpperCase(); 
		//生成蓝色颜色代码
		blue = Integer.toHexString(random.nextInt(256)).toUpperCase();  
	       
		//判断红色代码的位数
		red = red.length()==1 ? "0" + red : red ;  
		//判断绿色代码的位数
		green = green.length()==1 ? "0" + green : green ; 
		//判断蓝色代码的位数
		blue = blue.length()==1 ? "0" + blue : blue ;
		//生成十六进制颜色值
		color = "#"+red+green+blue;
		return color;
	}
	
}