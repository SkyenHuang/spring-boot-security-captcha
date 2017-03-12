package com.example.captcha.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author skyen
 *
 */
public class CaptchaFactory {

	private static final long DEFAULT_EXPRIED = 300000;

	private static final String DEFAULT_FONT_FILE_PATH = "C:/windows/Fonts/JOKERMAN.TTF";

	private String charRepository = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

	private int width = 180;

	private int height = 60;

	private int fontHeight = height;
	private Font font;
	private int length = 4;

	public String getCharRepository() {
		return charRepository;
	}

	public void setCharRepository(String charRepository) {
		this.charRepository = charRepository;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public CaptchaFactory() throws FontFormatException, IOException, URISyntaxException {
		font = loadFont(DEFAULT_FONT_FILE_PATH, fontHeight);
	}

	public static String getRandomString(int len, String charRepository) {
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			stringBuilder.append(charRepository.charAt(random.nextInt(charRepository.length())));
		}
		return stringBuilder.toString();
	}

	public Captcha createCaptcha() throws FontFormatException, IOException {
		return this.createCaptcha(DEFAULT_EXPRIED);
	}

	public Captcha createCaptcha(long expired) throws FontFormatException, IOException {
		BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = img.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, this.width, this.height);
		graphics2d.setFont(font);
		String value = getRandomString(this.length, this.charRepository);
		Color[] colorArray = new Color[value.length()];
		for (int i = 0; i < value.length(); i++) {
			colorArray[i] = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
			graphics2d.setColor(colorArray[i]);
			graphics2d.drawString(value.charAt(i) + "", i * this.width / value.length(),
					(this.height - this.fontHeight) / 2 + this.fontHeight);
		}

		int x1;
		int x2;
		int y1;
		int y2;
		for (int i = 0; i < 10; i++) {
			x1 = new Random().nextInt(this.width);
			y1 = new Random().nextInt(this.height);
			x2 = new Random().nextInt(this.width);
			y2 = new Random().nextInt(this.height);
			graphics2d.setColor(colorArray[new Random().nextInt(colorArray.length)]);
			graphics2d.drawLine(x1, y1, x2, y2);
			graphics2d.drawLine(x1, y1 + 1, x2, y2 + 1);
		}
		return new Captcha(img, value, expired);
	}

	public static Font loadFont(String path, float fontHeight) throws FontFormatException, IOException {
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		Font font = Font.createFont(Font.TRUETYPE_FONT, fileInputStream);
		fileInputStream.close();
		return font.deriveFont(Font.PLAIN, fontHeight);
	}

	public class Captcha {

		private final BufferedImage img;

		private final String value;

		private final long createTime;

		private final long expired;

		public Captcha(BufferedImage img, String value, long expired) {
			this.img = img;
			this.value = value;
			this.createTime = System.currentTimeMillis();
			this.expired = expired;
		}

		public boolean expired() {
			return System.currentTimeMillis() > (this.createTime + this.expired) ? true : false;
		}

		public void save(String path) throws IOException {
			ImageIO.write(this.getImg(), "PNG", new File(path));
		}

		public String getValue() {
			return this.value;
		}

		public String getBase64Img() throws IOException {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(img, "PNG", byteArrayOutputStream);
			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		}

		public BufferedImage getImg() {
			return this.img;
		}

	}

	@Test
	public void test() {
		Exception exception = null;
		try {
			CaptchaFactory captchaFactory = new CaptchaFactory();
			captchaFactory.createCaptcha().save("D:/test.png");
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			exception = e;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			exception = e;
		}
		Assert.assertNull(exception);
	}

}
