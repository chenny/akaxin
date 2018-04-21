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
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
	Socket socket = null;
	static List<Integer> nums = null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		List<Num> list = null;
		InputStream is = null;
//		try {
//			list = read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		long time = System.currentTimeMillis();

		Akaxin.Request.Builder arb = Akaxin.Request.newBuilder();
//		for (Num num : list) {
//			arb.addNumbers(num.getNum());
//		}
		arb.addAllNumbers(nums);
		byte[] messageBody = arb.build().toByteArray();
		System.out.println(messageBody.length);
		try {
			socket = new Socket("192.168.3.24", 10086);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.write(messageBody);
			out.flush();
			socket.shutdownOutput();
			is = socket.getInputStream();
			long i = -1;
			while (i == -1) {
				Akaxin.Response ar =Akaxin.Response.parseFrom(ByteToInputStream.input2byte(is));
				if (ar.getMsgCount()>0) {
					System.out.println("socket receive num:"+ar.getMsgCount());
					i=0;
					System.out.println("it is cost:" + (System.currentTimeMillis() - time) + "ms");
				}
				//i = writeData(Akaxin.Response.parseFrom(ByteToInputStream.input2byte(is)), list, time);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//creatNumbers();
		nums = createInMemory(30000000);
		new Client().start();
	}

	public static void creatNumbers() throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("create start:"+start);
		File fout = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		for (int i = 0; i < 100000000; i++) {
			bw.write(i + "." + ((int) (Math.random() * 100000)) + "");
			bw.newLine();
		}
		System.out.println("create finishe:"+(System.currentTimeMillis()-start)+"ms");
		bw.close();
	}

	public static int writeData(Akaxin.Response resp, List<Num> nums, long startTime) throws IOException {
		File fout = new File("outdata.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		long finishTime = System.currentTimeMillis();
		System.out.println("it is cost:" + (finishTime - startTime) + "ms");
		for (int i = 0; i < nums.size(); i++) {
			bw.write(nums.get(i).getId() + "   " + nums.get(i).getNum() + "   "
					+ resp.getMsgMap().get(nums.get(i).getId()) + "    " + (finishTime - startTime) + "ms");
			bw.newLine();
		}
		// bw.write(resp.toString());
		bw.close();
		return 1;
	}

	private static List<Num> read() throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("read start:"+start);
		List<Num> list = new ArrayList<>();
		FileInputStream fis = new FileInputStream(new File("out.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		// int thisGetNum = 0;
		while ((line = br.readLine()) != null) {
			// thisGetNum++;
			int id = Integer.parseInt(line.substring(0, line.lastIndexOf(".")));
			int num = Integer.parseInt(line.substring(line.lastIndexOf(".") + 1, line.length()));
			list.add(new Num(id, num));
			//
		}
		System.out.println("read finishe"+(System.currentTimeMillis()-start)+"ms");
		return list;
	}

	private static List<Integer> createInMemory(int createNum) {
		long start = System.currentTimeMillis();
		System.out.println("createInMemory");
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < createNum; i++) {
			list.add(((int) (Math.random() * 100000)));
		}
		System.out.println("createInMemory"+(System.currentTimeMillis()-start)+"ms");
		return list;
	}

}
