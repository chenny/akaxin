package com.network.protobuf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(10086);
		Socket socket = null;
		Executor executor = Executors.newFixedThreadPool(4);
		while(true) {
			socket =server.accept();
			executor.execute(new ServerThread(socket));
		}
	}
	public static class ServerThread extends Thread {
		private Socket socket;

		public ServerThread(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			long start = System.currentTimeMillis();
			InputStream is = null;
			DataInputStream in = null;
			try {
				is = socket.getInputStream();
				in = new DataInputStream(is);
				ZalyHttpClient http = ZalyHttpClient.getInstance();
				byte[] responseBytes = http.postBytes("http://localhost:8080/protobuf/check",
						ByteToInputStream.input2byte(in));
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(responseBytes);
				outputStream.flush();
				System.out.println(Thread.currentThread()+"server cost:"+(System.currentTimeMillis()-start)+"ms");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
					if (in!=null) {
						in.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}
}
