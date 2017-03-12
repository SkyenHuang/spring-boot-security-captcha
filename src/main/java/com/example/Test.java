package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

public class Test {
	public void test() throws IOException {
		FileInputStream inputStream = new FileInputStream(new File("test.html"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-"));
		char[] cbuf = new char[100];
		int len = 0;
		StringBuilder stringBuilder = new StringBuilder();
		while ((len = reader.read(cbuf, 0, cbuf.length)) != -1) {
			stringBuilder.append(cbuf, 0, len);
		}
		String content = stringBuilder.toString().replaceAll("\\s", "");
		System.out.println(content);
		reader.close();
	}

	@org.junit.Test
	public void test2() {
		System.out.println(Base64.getMimeEncoder("Xulei85743007".length(),
				("ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=").getBytes())
				.encodeToString("Xulei85743007".getBytes()));
	}
}
