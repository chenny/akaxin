package com.network.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1041906268716126040L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long httpstart = System.currentTimeMillis();
		InputStream is = req.getInputStream();
		long check = System.currentTimeMillis();
		System.out.println("servise check start");
		Akaxin.Request req_data = Akaxin.Request.parseFrom(is);
		Akaxin.Response.Builder arb = Akaxin.Response.newBuilder();
//		int size = req_data.getNumbersList().size();
//		int threadNum = 8;
//		TaskAction<Map<Integer, Boolean>> ta = new TaskAction<>();
//		for (int i = 0; i < threadNum; i++) {
//			final int tempVal = i;
//			Task<Map<Integer, Boolean>> task = new Task<Map<Integer, Boolean>>() {
//
//				@Override
//				Map<Integer, Boolean> execure() {
//					// TODO Auto-generated method stub
//					Map<Integer, Boolean> map = new HashMap<>();
//					for (int j = tempVal * (size / threadNum) + 1; j <= (tempVal + 1) * (size / threadNum); j++) {
//						map.put(j, isPrimeNumber(req_data.getNumbers(j-1)));
//					}
//					return map;
//				}
//			};
//			ta.addTask(task);
//		}
//		// 汇总多线程计算的结果
//		List<Map<Integer, Boolean>> resultList = ta.doTasks();
//		for (Map<Integer, Boolean> map : resultList) {
//			arb.putAllMsg(map);
//		}

		 int i = 0;
		 for (int num : req_data.getNumbersList()) {
		 arb.putMsg(i, isPrimeNumber(num));
		 i++;
		 }
		Akaxin.Response ar = arb.build();
		System.out.println("servise check cost:" + (System.currentTimeMillis() - check) + "ms");
		ar.writeTo(resp.getOutputStream());
		System.out.println("post cost:" + (System.currentTimeMillis() - httpstart) + "ms");
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
