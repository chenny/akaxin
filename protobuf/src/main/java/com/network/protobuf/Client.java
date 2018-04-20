package com.network.protobuf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub	
		creatNumbers();
		List<Integer> list = read();
		Akaxin.Request.Builder  arb  = Akaxin.Request.newBuilder();
		arb.addAllNumbers(list);
		byte[] messageBody = arb.build().toByteArray();
		//InputStream is = ByteToInputStream.byte2Input(messageBody);
		long start = System.currentTimeMillis();
		System.out.println("client开始"+start);
		Socket socket = new Socket("192.168.3.24",10086);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(messageBody);
        out.flush();
        while(true) {
        	DataInputStream is = new DataInputStream(socket.getInputStream());
        	Akaxin.Request req_data = Akaxin.Request.parseFrom(ByteToInputStream.input2byte(is));
        	long finish = System.currentTimeMillis();
        	writeData(ByteToInputStream.input2byte(is));
            //System.out.println(req_data.toString());
            //System.out.println("client Finsh"+finish);
            socket.close();
        }
        
	}
	
	public static void creatNumbers() throws IOException {
		File fout = new File("out.txt");  
	    FileOutputStream fos = new FileOutputStream(fout);  
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));  
	    for (int i = 0; i < 1000; i++) {  
	        bw.write((int)(Math.random()*100000)+"");  
	        bw.newLine();  
	    }  
	    bw.close();  
	}
	
	public static void writeData(byte[] bytes) throws IOException {
		File fout = new File("outdata.txt");  
	    FileOutputStream fos = new FileOutputStream(fout);  
	  //  BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));  
	    fos.write(bytes);
	    System.out.println(System.currentTimeMillis());
	}
	
	
	private static List<Integer> read() throws IOException {  
		List<Integer> list = new ArrayList<>();
		FileInputStream fis = new FileInputStream(new File("out.txt"));  
	    BufferedReader br = new BufferedReader(new InputStreamReader(fis));  
	    String line = null;  
	    while ((line = br.readLine()) != null) {    
	        list.add(Integer.parseInt(line));
	    }  
	    return list;
	} 

}
