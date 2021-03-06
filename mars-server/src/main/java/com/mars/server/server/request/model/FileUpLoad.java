package com.mars.server.server.request.model;

import java.io.InputStream;

/**
 * 文件参数实体类
 * @author yuye
 *
 */
public class FileUpLoad {

	/**
	 * 请求name
	 */
	private String name;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件流
	 */
	private InputStream inputStream;

	/**
	 * 二进制流
	 */
	private byte[] bytes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
