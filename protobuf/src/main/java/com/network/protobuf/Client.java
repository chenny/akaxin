package com.network.protobuf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
	private static final Logger logger = LoggerFactory.getLogger(Client.class);
	Socket socket = null;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("192.168.3.24", 10086);
		new SendThread(socket).start();

	}

	public static class SendThread extends Thread {
		private Socket socket;
		public List<Integer> mList = new ArrayList<>();

		public SendThread(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					logger.info("client start");
					OutputStream outputStream = socket.getOutputStream();
					File inputFile = new File("input.txt");
					PrintWriter bufw = new PrintWriter(outputStream, true);
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
					BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);// 10M缓存
					String line = null;
					while (in.ready()) {
						line = in.readLine();
						String[] split = line.split("!");
						mList.add(Integer.parseInt(split[1]));
						// logger.info(line);
						bufw.println(line);
						bufw.flush();
					}
					bufw.println("end");
					bufw.flush();
					in.close();

					File outputFile = new File("out.txt");
					FileWriter fw = new FileWriter(outputFile);

					InputStream inputStream = socket.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					int i = 0;
					while ((line = bufferedReader.readLine()) != null) {
						logger.info(""+i);
						fw.write(i + "!" + mList.get(i) + "!" + line);
						fw.write("\n");
						fw.flush();
						i++;
					}
					fw.close();
					logger.info("client finished");
					socket.shutdownOutput();
					socket.close();
					break;
				} catch (Exception e) {

				}
			}
		}

	}
	
	

}
