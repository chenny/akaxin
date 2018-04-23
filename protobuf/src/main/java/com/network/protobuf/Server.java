package com.network.protobuf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	public static void main(String[] args) throws Exception {
		logger.info("Server ready");
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
			logger.info("Server start");
			// TODO Auto-generated method stub
			InputStream is = null;
			DataInputStream in = null;
			try {
				is = socket.getInputStream();
				logger.info("Server receive from client");
				in = new DataInputStream(is);
				ZalyHttpClient http = ZalyHttpClient.getInstance();
				byte[] responseBytes = http.postBytes("http://localhost:8080/protobuf/check",
						ByteToInputStream.input2byte(in));
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(responseBytes);
				outputStream.flush();
				logger.info("Server send to client");
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
