package com.network.protobuf;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class ZalyHttpClient {
	private static final Logger logger = LoggerFactory.getLogger(ZalyHttpClient.class);
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static OkHttpClient httpClient;

	private static ZalyHttpClient instance = new ZalyHttpClient();

	private ZalyHttpClient() {
	}

	public static ZalyHttpClient getInstance() {
		httpClient  = new OkHttpClient();
		httpClient.setConnectTimeout(150, TimeUnit.SECONDS);
		httpClient.setReadTimeout(150, TimeUnit.SECONDS);
		httpClient.setWriteTimeout(150, TimeUnit.SECONDS);
		return instance;
	}

	public byte[] run(String url) throws Exception {
		Request request = new Request.Builder().url(url).build();
		Response response = httpClient.newCall(request).execute();

		if (response.isSuccessful()) {
			return response.body().bytes();
		} else {
			logger.error("http post error.{}", response.message());
		}

		return null;
	}

	public byte[] get(String url) throws Exception {
		Request request = new Request.Builder().url(url).build();
		Response response = httpClient.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().bytes();
		} else {
			logger.error("http get url={} error.{}", url, response.message());
		}
		return null;
	}

	public byte[] postString(String url, String json) throws IOException {
		RequestBody postBody = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(postBody).build();
		Response response = httpClient.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().bytes();
		} else {
			logger.error("http post error.{}", response.message());
		}
		return null;
	}

	public byte[] postBytes(String url, byte[] bytes) throws IOException {
		RequestBody postBody = RequestBody.create(JSON, bytes);
		Request request = new Request.Builder().url(url).post(postBody).build();
		Response response = httpClient.newCall(request).execute();
	
		if (response.isSuccessful()) {
			return response.body().bytes();
		} else {
			logger.error("http post error.{}", response.message());
		}
		return null;
	}
	}