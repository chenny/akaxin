package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.protobuf.Akaxin;
import com.network.protobuf.Client;
import com.network.protobuf.ZalyHttpClient;

public class TalkServer {

	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(16777);
		while (true) {
			Socket socket = server.accept();
			exSocketServer(socket);
		}
	}

	public static void exSocketServer(final Socket socket) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BufferedReader in = null;
				PrintWriter out = null;
				try {
					Akaxin.Request.Builder builder = Akaxin.Request.newBuilder();
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream());
					while (true) {
						String msg = in.readLine();
						System.out.println("from client:" + msg);
						if (msg == null) {
							break;
						}
						builder.addNumbers(Integer.parseInt(msg));
						ZalyHttpClient http = ZalyHttpClient.getInstance();
						byte[] responseBytes = http.postBytes("http://localhost:8080/protobuf/check",
								builder.build().toByteArray());
						Akaxin.Response response = Akaxin.Response.parseFrom(responseBytes);
						Map<Integer, Boolean> mapMap = response.getMsgMap();
						for (int i : mapMap.keySet()) {
							out.println(mapMap.get(i));
							out.flush();
						}
						
						
						builder.clear();
						
					

					}
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("Error:" + e);
				} finally {
					try {
						in.close();
						out.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();
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