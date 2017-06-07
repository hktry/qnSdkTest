package com.qiniu.upload.test;

import org.junit.Test;

import com.qiniu.constant.ConstantsUtils;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

/**
 * 生成凭证的demo
 * @author hktry
 *
 */
public class Test1 {
	@Test
	//获取凭证
	public void test1(){
		//创建权限
		Auth auth =Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		//获取上传凭证
		String upToken =auth.uploadToken(ConstantsUtils.bucket);
		System.out.println(upToken);
		//result:XNfRj8ywAvRmhHd39-_BI9BkBZP-vxfe6H5IoENJ:RxF8DU0tagRFvMNKMopuHRNABBo=:eyJzY29wZSI6ImhrdHJ5IiwiZGVhZGxpbmUiOjE0OTY3MTc2MzB9
	}
	@Test
	//覆盖上传凭证
	public void test2(){
		//覆盖文件的名称，这个名称同客户端上传代码中的指定的文件名一致
		String key="1.jpg";
		Auth auth =Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		//获取上传凭证
		String upToken =auth.uploadToken(ConstantsUtils.bucket,key);
		System.out.println(upToken);
//		result:XNfRj8ywAvRmhHd39-_BI9BkBZP-vxfe6H5IoENJ:weF1N1F2A6zy9pZQgXthRrzCM2Q=:eyJzY29wZSI6ImhrdHJ5OjEuanBnIiwiZGVhZGxpbmUiOjE0OTY3MTgwNjF9
	}
	@Test
	//自定义上传回复凭证，默认情况下，在没有设置returnBody或者回调相关的参数情况下，七牛回复格式为hash和key，适用于上传端和七牛服务器之间进行直接交互
	public void test3(){
		Auth auth =Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		//七牛官方工具内
		StringMap putPolicy=new StringMap();
		putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
		long expireSeconds=3600;
		String upToken=auth.uploadToken(ConstantsUtils.bucket, null, expireSeconds, putPolicy);
		System.out.println(upToken);
		
	}
	@Test
	//带回调业务服务器的凭证，客户上传文件到七牛后，业务服务器获取回调，并且将信息传回到客户端中去。
	public void test4(){
		Auth auth =Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		StringMap putPolicy=new StringMap();
		//你自己业务服务接受回调的接口
		putPolicy.put("callbackUrl", "http://youu.server.ip/qiniu/upload/callback");
		putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
		long expireSeconds=3600;
		String upToken=auth.uploadToken(ConstantsUtils.bucket, null, expireSeconds, putPolicy);
		System.out.println(upToken);
	}
	
	@Test
	//带数据处理的凭证,文件上传到七牛中，立即对起进行进行多种指令的数据数据处理，这个只需要生成的上传凭证中指定相关的参数即可。
	public void test5(){
		Auth auth =Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		StringMap putPolicy=new StringMap();
		//数据处理指令
		String saveMp4Entry=String.format("%s:1.mp4", ConstantsUtils.bucket);
		String saveJpgEntry=String.format("%s:1.jpg", ConstantsUtils.bucket);
		String avthnumbMp4Fop=String.format("avthnumb/mp4|saveas/%s", UrlSafeBase64.encodeToString(saveMp4Entry));
		String avthnumbJpgFop=String.format("avthnumb/jpg|saveas/%s", UrlSafeBase64.encodeToString(saveJpgEntry));
		//将多个数据吹指令拼接起来
		String persistentOpfs =StringUtils.join(new String[]{avthnumbMp4Fop,avthnumbJpgFop}, ";");
		putPolicy.put("persistentOpfs", persistentOpfs);
		//数据处理队列名称
		putPolicy.put("persistentPipeline", "mps-pipel");
		//数据处理完成后结构通知地址
		putPolicy.put("persistentPipeline", "http://youu.server.ip/qiniu/pfop/notify");
		long expireSeconds=3600;
		String upToken=auth.uploadToken(ConstantsUtils.bucket, null, expireSeconds, putPolicy);
		System.out.println(upToken);
	}
	
}
