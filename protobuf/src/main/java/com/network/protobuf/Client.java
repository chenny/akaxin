package com.network.protobuf;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(Client.class);
	Socket socket = null;
	private static Akaxin.Request request;
	private static Akaxin.Response.Builder responseBuilder = Akaxin.Response.newBuilder();
	private static final int TIME = 4;
	private int ti;

	public Client(int ti) {
		super();
		this.ti = ti;
	}

	@Override
	public void run() {
		logger.info("client start");
		InputStream is = null;
		int size = request.getNumbersCount();
		try {
			socket = new Socket("192.168.3.24", 10086);
			Akaxin.Request.Builder builder = Akaxin.Request.newBuilder();
			builder.addAllNumbers(request.getNumbersList().subList(size / TIME * ti, size / TIME * (ti + 1)));
			byte[] messageBody = builder.build().toByteArray();
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.write(messageBody);
			out.flush();
			socket.shutdownOutput();
			int receiveTimes = 0;
			is = socket.getInputStream();
			while (receiveTimes == 0) {
				Akaxin.Response ar = Akaxin.Response.parseFrom(is);
				if (ar.getMsgCount() > 0) {
					responseBuilder.putAllMsg(ar.getMsgMap());
					logger.info("client msg receive from server and the size is" + ar.getMsgCount());
					receiveTimes++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
				if (is != null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// creatNumbers();
		request = createInMemory(30000000);
		new Client(0).start();
		new Client(1).start();
		new Client(2).start();
		new Client(3).start();
//		Executor executor = Executors.newFixedThreadPool(4);
//		executor.execute(new Client(0));
//		executor.execute(new Client(1));
//		executor.execute(new Client(2));
//		executor.execute(new Client(3));

	}

	// public static void creatNumbers() throws IOException {
	// long start = System.currentTimeMillis();
	// System.out.println("create start:"+start);
	// File fout = new File("out.txt");
	// FileOutputStream fos = new FileOutputStream(fout);
	// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	// for (int i = 0; i < 100000000; i++) {
	// bw.write(i + "." + ((int) (Math.random() * 100000)) + "");
	// bw.newLine();
	// }
	// System.out.println("create
	// finishe:"+(System.currentTimeMillis()-start)+"ms");
	// bw.close();
	// }
	//
	// public static int writeData(Akaxin.Response resp, List<Num> nums, long
	// startTime) throws IOException {
	// File fout = new File("outdata.txt");
	// FileOutputStream fos = new FileOutputStream(fout);
	// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	// long finishTime = System.currentTimeMillis();
	// System.out.println("it is cost:" + (finishTime - startTime) + "ms");
	// for (int i = 0; i < nums.size(); i++) {
	// bw.write(nums.get(i).getId() + " " + nums.get(i).getNum() + " "
	// + resp.getMsgMap().get(nums.get(i).getId()) + " " + (finishTime - startTime)
	// + "ms");
	// bw.newLine();
	// }
	// // bw.write(resp.toString());
	// bw.close();
	// return 1;
	// }
	//
	// private static List<Num> read() throws IOException {
	// long start = System.currentTimeMillis();
	// System.out.println("read start:"+start);
	// List<Num> list = new ArrayList<>();
	// FileInputStream fis = new FileInputStream(new File("out.txt"));
	// BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	// String line = null;
	// // int thisGetNum = 0;
	// while ((line = br.readLine()) != null) {
	// // thisGetNum++;
	// int id = Integer.parseInt(line.substring(0, line.lastIndexOf(".")));
	// int num = Integer.parseInt(line.substring(line.lastIndexOf(".") + 1,
	// line.length()));
	// list.add(new Num(id, num));
	// //
	// }
	// System.out.println("read finishe"+(System.currentTimeMillis()-start)+"ms");
	// return list;
	// }

	private static Akaxin.Request createInMemory(int createNum) {
		logger.info("createInMemory");
		Akaxin.Request.Builder builder = Akaxin.Request.newBuilder();
		for (int i = 0; i < createNum; i++) {
			builder.addNumbers(((int) (Math.random() * 100000)));
		}
		logger.info("createInMemory finish");
		return builder.build();
	}
}
