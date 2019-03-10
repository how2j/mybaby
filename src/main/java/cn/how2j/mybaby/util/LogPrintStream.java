package cn.how2j.mybaby.util;



import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPrintStream extends PrintStream{

	public static boolean log = true;
	
	
	
	
	
	
	public void println() {
		
		println("");
	}
	public void println(boolean x) {
		println(String.valueOf(x));
	}
	public void println(char x) {
		println(String.valueOf(x));
	}
	public void println(int x) {
		println(String.valueOf(x));
	}
	public void println(long x) {
		println(String.valueOf(x));
	}
	public void println(float x) {
		println(String.valueOf(x));
	}
	public void println(double x) {
		println(String.valueOf(x));
	}
	public void println(char[] x) {
		println(String.valueOf(x));
	}
	public LogPrintStream(OutputStream out) {
		super(out);
	}
	public LogPrintStream() {
		this(System.out);
	}
	
	public static void closeLog(){
		log= false;
	}
	

	public void println(Object x){
		println(String.valueOf(x));
	}
	
	public void println(String msg){
		if(!log){
			super.println(msg);
			return;
		}
		try {
			throw new Exception();
		} catch (Exception e) {
			StackTraceElement[] stes = e.getStackTrace();
			String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
			StackTraceElement s= getTriggerTraceElement(stes);
			String logFormat = "%s (%s:%d) %s() - %s%n";
			
			super.printf(logFormat, time,s.getFileName(), s.getLineNumber(), s.getMethodName(), msg);

		}
		
		
		

		
		
	}
	private StackTraceElement getTriggerTraceElement(StackTraceElement[] stes) {
		for (StackTraceElement s : stes) {
			String clazzName = s.getClassName();
			if(clazzName.equals(LogPrintStream.class.getName()))
			{
				continue;
			}
			return s;

		}
		return null;
		
	}
	
	 public static void init() {
	        System.setOut(new LogPrintStream());
	         
	    }	

}
