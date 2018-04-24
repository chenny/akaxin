package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.network.protobuf.Client;

public class TalkClient {
	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	public static void main(String args[]) {
		logger.info("begin");
		exSocket();
	}

	public static void exSocket() {
		try {
			Socket socket = new Socket("192.168.3.24", 16777);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			File outputFile = new File("out.txt");
			FileWriter fw = new FileWriter(outputFile);
			File inputFile = new File("input.txt");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
			BufferedReader br = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);// 10M缓存
			String line = null;
			while (br.ready()) {
				line = br.readLine();
				String[] split = line.split("!");
				// 将从系统标准输入读入的字符串输出到Server
				out.println(split[1]);
				// 刷新输出流，使Server马上收到该字符串
				out.flush();
				fw.write(split[0] + "  ");
				fw.write(split[1] + "  ");
				fw.write(in.readLine());
				fw.write("\n");
				fw.flush();
			}
			logger.info("finish");
			socket.close(); // 关闭Socket

		} catch (Exception e) {
			System.out.println("Error" + e); // 出错则打印出错信息
		}

	}

}