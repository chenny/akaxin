package com.network.protobuf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client{

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub	
		creatNumbers();
		long time= System.currentTimeMillis();
		List<Num> list = read();
		Akaxin.Request.Builder  arb  = Akaxin.Request.newBuilder();
		for(Num num:list) {
			arb.addNumbers(num.getNum());
		}
		byte[] messageBody = arb.build().toByteArray();
		//InputStream is = ByteToInputStream.byte2Input(messageBody);
		System.out.println("start:"+System.currentTimeMillis());
		Socket socket = new Socket("192.168.3.24",10086);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(messageBody);
        out.flush();
        socket.shutdownOutput();
        InputStream is = socket.getInputStream();
        long i=-1;
        while(i==-1) {	
        	i = writeData(Akaxin.Response.parseFrom(ByteToInputStream.input2byte(is)),list,time);
        	//System.out.println("ms="+(i-time));
        }
    	is.close();
    	socket.close();
	}
	
	public static void creatNumbers() throws IOException {
		File fout = new File("out.txt");  
	    FileOutputStream fos = new FileOutputStream(fout);  
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));  
	    for (int i = 0; i < 1000000; i++) {  
	        bw.write(i+"."+(int)(Math.random()*100000)+"");  
	        bw.newLine();  
	    }  
	    bw.close();  
	}
	
	public static long writeData(Akaxin.Response resp,List<Num>nums,long startTime) throws IOException {
		File fout = new File("outdata.txt");  
	    FileOutputStream fos = new FileOutputStream(fout);  
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	    long finishTime =System.currentTimeMillis();
	    for(int i=0;i<nums.size();i++) {
	    	bw.write(nums.get(i).getId()+"   "+nums.get(i).getNum()+"   "+resp.getMsgMap().get(nums.get(i).getId())+"    "+(finishTime-startTime)+"ms");
	    	bw.newLine();
	    }
//	    bw.write(resp.toString());
	    bw.close();
	    return System.currentTimeMillis();
	}
	private static List<Num> read() throws IOException {  
		List<Num> list = new ArrayList<>();
		
		FileInputStream fis = new FileInputStream(new File("out.txt"));  
	    BufferedReader br = new BufferedReader(new InputStreamReader(fis));  
	    String line = null;  
	    while ((line = br.readLine()) != null) {
	    	int id= Integer.parseInt(line.substring(0, line.lastIndexOf(".")));
	    	int num = Integer.parseInt(line.substring(line.lastIndexOf(".")+1, line.length()));
	        list.add(new Num(id,num));
	    }  
	    return list;
	} 

}
