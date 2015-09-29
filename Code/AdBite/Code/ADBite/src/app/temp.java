package app;

import java.io.*;
import java.net.URL;

public class temp {
	
	public static void test1()
	{
		try {
			ProcessBuilder pb = new ProcessBuilder("SVM.EXE","66");
			pb.redirectErrorStream(true);
						
			//Map<String, String> env = pb.environment();
			System.out.println(pb.directory());
			//pb.directory(new File("C:\\SVM\\"));
			
			System.out.println("PROCESS Started");
			Process p = pb.start();
			
			InputStream is = p.getInputStream();
		       InputStreamReader isr = new InputStreamReader(is);
		       BufferedReader br = new BufferedReader(isr);
		       String line;

		       while ((line = br.readLine()) != null) {
		         System.out.println(line);
		       }
			p.waitFor();
			System.out.println("PROCESS COMPLETE : "+p.exitValue());
			
			
			
			/*
			Runtime run=Runtime.getRuntime();
			run.
			Process p=run.exec("F:\\Final Year Project\\Neural Network\\SEMESTER____8\\PROJECT__CODE\\URL_GRABBER\\html\\TEST.EXE 123");
			*/
			//p.getOutputStream().write("cls".getBytes());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test2()
	{
		try
		{
			System.out.println(ADBiteUtil.getCompleteUrl("http://localhost/criclounge/otherparts/prediction-guru-game.php", "/ss.php"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test3()
	{
		try
		{
			System.out.println(getAbsoluteURL("http://localhost/criclounge/otherparts/prediction-guru-game/", "ss.php"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getAbsoluteURL(String base,String relative)
	{
		try
		{
			URL u=new URL(new URL(base),relative);
			return u.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return base;
		}
	}
	
	public static void main(String[] args)
	{
		test1();
	}
}