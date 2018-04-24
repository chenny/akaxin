package com.network.protobuf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CreateNum {
	public static void main(String[] args) throws IOException {
		File fout = new File("input.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		for (int i = 0; i < 100000; i++) {
			bw.write(i + "!" + ((int) (Math.random() * 100000)) + "");
			bw.newLine();
		}
		bw.close();
	}
}
