package com.network.protobuf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) throws IOException {
		// Server
		logger.info("Server ready");
		ServerSocket serverSocket = new ServerSocket(10086);
		Socket socket = null;
		while (true) {
			socket = serverSocket.accept();
			new ServerThread(socket).start();
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
			try {
				logger.info("Server conIP:" + socket.getLocalAddress());
				InputStream is = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				PrintWriter bufferedWriter = new PrintWriter(out, true);
				logger.info("server get");
				Akaxin.Request.Builder builder = Akaxin.Request.newBuilder();
				String line;
				int num = 0;
				while ((line = br.readLine()) != null) {
					if (line.equals("end")) {
						break;
					}
					String[] split = line.split("!");
					builder.addNumbers(Integer.parseInt(split[1]));
					// logger.info(builder.getNumbersCount() + "");
					if (builder.getNumbersCount() == 100) {
						ZalyHttpClient http = ZalyHttpClient.getInstance();
						byte[] responseBytes = http.postBytes("http://localhost:8080/protobuf/check",
								builder.build().toByteArray());
						Akaxin.Response response = Akaxin.Response.parseFrom(responseBytes);
						Map<Integer, Boolean> mapMap = response.getMsgMap();
						for (int i : mapMap.keySet()) {
							bufferedWriter.println(mapMap.get(i));
							bufferedWriter.flush();
						}
						builder.clearNumbers();

					}
				}
				out.close();
				br.close();
				is.close();
				bufferedWriter.close();

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static byte[] doPost(byte[] bytes) {

		try {
			Akaxin.Request req_data = Akaxin.Request.parseFrom(bytes);
			Akaxin.Response.Builder arb = Akaxin.Response.newBuilder();
			int i = 0;
			for (int num : req_data.getNumbersList()) {
				arb.putMsg(i, isPrimeNumber(num));
				i++;
			}
			Akaxin.Response ar = arb.build();
			return ar.toByteArray();
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// 判断一个数是否是质数（素数）
	public static boolean isPrimeNumber(int num) {
		if (num == 2)
			return true;// 2特殊处理
		if (num < 2 || num % 2 == 0)
			return false;// 识别小于2的数和偶数
		for (int i = 3; i <= Math.sqrt(num); i += 2) {
			if (num % i == 0) {// 识别被奇数整除
				return false;
			}
		}
		return true;
	}
}
