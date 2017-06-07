package com.qiniu.download.test;

import java.io.IOException;
import java.net.URLEncoder;

import org.junit.Test;

import com.qiniu.constant.ConstantsUtils;
import com.qiniu.util.Auth;

/**
 * 下载文件
 * @author hktry
 *
 */
public class Test1 {
	@Test
	//公开空间下载文件
	public void test1() throws Exception{
		String filename="1.jpg";
		String domainOfBucket="http://or2ac0h4a.bkt.clouddn.com";
		String encodeFileName=URLEncoder.encode(filename, "utf-8");
		String finalUrl=String.format("%s/%s", domainOfBucket,encodeFileName);
		System.out.println(finalUrl);
	}
	
	@Test
	//私有空间下载文件,多了一个授权签名
	public void test2() throws IOException{
		String filename="1.jpg";
		String domainOfBucket="http://or4a09vhs.bkt.clouddn.com";
		String encodeFileName=URLEncoder.encode(filename, "utf-8");
		String publicUrl=String.format("%s/%s", domainOfBucket,encodeFileName);
		Auth auth =Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		long expireSeconds=3600;
		String finalUrl=auth.privateDownloadUrl(publicUrl, expireSeconds);
		
		System.out.println(finalUrl);
	}
	
}
