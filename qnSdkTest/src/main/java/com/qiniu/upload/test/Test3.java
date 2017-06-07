package com.qiniu.upload.test;

import org.junit.Test;
import com.qiniu.constant.ConstantsUtils;
import com.qiniu.util.Auth;

/**
 * 业务服务器验证七牛回调
 * 
 * @author hktry
 *
 */
public class Test3 {
	@Test
	public void test1() {

		String callbackUrl = "https://www.hktry.top/";
		String callbackBodyType = "application/json";
		// 这两个参数是根据实际使用的http框架进行获取
		String callbackAuthHeader = "https";
		byte[] callbackBody = null;
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		// 检测是否为合法的七牛云的回调请求
		boolean validCallback = auth.isValidCallback(callbackAuthHeader, callbackUrl, callbackBody, callbackBodyType);
		if (validCallback) {
			System.out.println("a");
		} else {
			System.out.println("b");
		}
	}
}
