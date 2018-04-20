package com.network.protobuf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class Server implements Runnable{
	public static void main(String[]args) throws Exception {
		Server server = new Server();
		server.run();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 try {
	            ServerSocket server = new ServerSocket(10086);
	            while (true) {
	                //等待client的请求
	                System.out.println("socket:<----------->start");
	                Socket socket = server.accept();
	                DataInputStream in = new DataInputStream(socket.getInputStream());
	               // Akaxin.Request artemp = Akaxin.Request.parseFrom(ByteToInputStream.input2byte(in)); 
	                System.out.println("socket:<----------->receive");
	                HttpUtil http = new HttpUtil();
	                byte[] bytes = http.post("http://localhost:8080/protobuf/check", ByteToInputStream.input2byte(in));
	                Akaxin.Response res = Akaxin.Response.parseFrom(bytes);
	                System.out.println(res.toString());
	                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	                out.write(bytes);      
	                out.flush();
	                socket.close();
	                System.out.println("socket:<----------->finsh");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	
	public static InputStream post(byte[] bs) throws IOException {
		 URL url = new URL("http://localhost:8080/protobuf/check");  
         HttpURLConnection huc = (HttpURLConnection) url.openConnection();  
         huc.setRequestProperty("encoding", "utf-8");  
         huc.setDoInput(true);  
         huc.setDoOutput(true);  
         huc.setRequestMethod("POST");  
         OutputStream os = huc.getOutputStream();  
         huc.getOutputStream().write(bs);
         os.close();  
         InputStream is = huc.getInputStream();  
         return is;
	}
	
	

}
