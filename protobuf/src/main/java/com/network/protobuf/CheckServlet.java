package com.network.protobuf;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		InputStream is = req.getInputStream();
		Akaxin.Response.Builder arb =Akaxin.Response.newBuilder();
		Akaxin.Request req_data = Akaxin.Request.parseFrom(ByteToInputStream.input2byte(is));
		for(int i=0;i<req_data.getNumbersCount();i++) {
			int num = req_data.getNumbers(i);
			arb.putMsg(i, isPrimeNumber(num));
		}
		Akaxin.Response ar = arb.build();
		//System.out.println(ar.toString());
		ar.writeTo(resp.getOutputStream());
	}
	
	
	//判断一个数是否是质数（素数）  
	public static boolean isPrimeNumber(int num){  
	    if(num == 2) return true;//2特殊处理  
	    if(num < 2 || num % 2 == 0) 
	    	return false;//识别小于2的数和偶数  
	    for(int i=3; i<=Math.sqrt(num); i+=2){  
	        if(num % i == 0){//识别被奇数整除  
	            return false;  
	        }  
	    }  
	    return true;  
	}  

}
