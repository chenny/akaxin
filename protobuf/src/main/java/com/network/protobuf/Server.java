package com.network.protobuf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
	                InputStream is  =socket.getInputStream();
	                DataInputStream in = new DataInputStream(is);
	                System.out.println("socket:<----------->receive");
	                ZalyHttpClient http = ZalyHttpClient.getInstance();
	        		
	                byte[] responseBytes = http.postBytes("http://localhost:8080/protobuf/check", ByteToInputStream.input2byte(in));
	        		
	        		
	        		
	        		
//	                Akaxin.Request req_data = Akaxin.Request.parseFrom(ByteToInputStream.input2byte(in));
//	                Akaxin.Response.Builder arb =Akaxin.Response.newBuilder();
//	                for(int i=0;i<req_data.getNumbersCount();i++) {
//	        			int num = req_data.getNumbers(i);
//	        			arb.putMsg(num, isPrimeNumber(num));
//	        		}
//	        		Akaxin.Response ar = arb.build();
	        		//System.out.println(ar.toString());
	        		OutputStream outputStream = socket.getOutputStream();
	        		outputStream.write(responseBytes);
	                System.out.println("socket:<----------->finsh");
	                outputStream.flush();
	                outputStream.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	//判断一个数是否是质数（素数）  


	
	

}
