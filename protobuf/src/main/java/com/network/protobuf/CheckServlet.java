package com.network.protobuf;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(CheckServlet.class);


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
		InputStream is = req.getInputStream();
		logger.info("Server-Prime-Checker receive msg");
		Akaxin.Request req_data = Akaxin.Request.parseFrom(is);
		Akaxin.Response.Builder arb = Akaxin.Response.newBuilder();
		int i = 0;
		logger.info("Server-Prime-Checker start to change");
		for (int num : req_data.getNumbersList()) {
			arb.putMsg(i, isPrimeNumber(num));
			i++;
		}
		logger.info("Server-Prime-Checker change finish");
		Akaxin.Response ar = arb.build();
		logger.info("Server-Prime-Checker send to server");
		ar.writeTo(resp.getOutputStream());
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
