package com.qiniu.upload.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.constant.ConstantsUtils;
import com.qiniu.constant.MyPutRet;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * 服务器直传demo
 * 
 * @author hktry
 *
 */
public class Test2 {
	@Test
	// 最简单的上传本地文件
	public void test1() {
		// 构造一个带指定的zone对象的配置类
		Configuration configuration = new Configuration(Zone.zone0());
		// 上传工具
		UploadManager uploadManager = new UploadManager(configuration);

		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		String localFilePath = "/Users/hktry/images/a.jpg";
		String key = null;
		String upToken = auth.uploadToken(ConstantsUtils.bucket);
		System.out.println(upToken);
		try {
			Response response = uploadManager.put(localFilePath, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Response r = e.response;
			System.out.println(r.toString());
			try {
				System.out.println(r.bodyString());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

	@Test
	// 字节数组上传，可以支持将内存中的字节数组上传到空间中去。在七牛存储的格式是字节流
	public void test2() throws Exception {
		// 构造一个带指定的zone对象的配置类
		Configuration configuration = new Configuration(Zone.zone0());
		// 上传工具
		UploadManager uploadManager = new UploadManager(configuration);

		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		String key = null;
		String upToken = auth.uploadToken(ConstantsUtils.bucket);
		System.out.println(upToken);
		try {
			byte[] uploadBytes = "i love lvqu".getBytes("utf-8");
			Response response = uploadManager.put(uploadBytes, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Response r = e.response;
			System.out.println(r.toString());
			try {
				System.out.println(r.bodyString());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	@Test
	// 数据流上传，
	public void test3() throws Exception {
		// 构造一个带指定的zone对象的配置类
		Configuration configuration = new Configuration(Zone.zone0());
		// 上传工具
		UploadManager uploadManager = new UploadManager(configuration);

		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		String key = null;
		String upToken = auth.uploadToken(ConstantsUtils.bucket);
		// System.out.println(upToken);
		try {
			byte[] uploadBytes = "i love lvqu very much".getBytes("utf-8");
			// for (byte b : uploadBytes) {
			// System.out.println(b);
			// }
			ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadBytes);
			Response response = uploadManager.put(inputStream, key, upToken, null, null);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Response r = e.response;
			System.out.println(r.toString());
			try {
				System.out.println(r.bodyString());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

	@Test
	// 断点续上传
	public void test4() throws IOException {
		// 构造一个带指定的zone对象的配置类
		Configuration configuration = new Configuration(Zone.zone0());
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		String key = null;
		//当然也可以自定义回复
		StringMap putPolicy=new StringMap();
		putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
		String upToken = auth.uploadToken(ConstantsUtils.bucket,null, 3600,putPolicy);
		String uploadFile="/Users/hktry/images/faerzi.mp3";
		//System.getenv是获取默认的临时文件路径
		String localTempDir =Paths.get(System.getenv("java.io.tmpdir"),ConstantsUtils.bucket).toString();
		try {
			//设置断点续传文件进度保存目录；
			FileRecorder fileRecorder=new FileRecorder(localTempDir);
			UploadManager uploadManager=new UploadManager(configuration,fileRecorder);
			Response response = uploadManager.put(uploadFile, key, upToken);
			// 解析上传成功的结果
			MyPutRet myPutRet =response.jsonToObject(MyPutRet.class);
			System.out.println(myPutRet.toString());
			//DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//			System.out.println(putRet.key);
//			System.out.println(putRet.hash);
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Response r = e.response;
			System.out.println(r.toString());
			try {
				System.out.println(r.bodyString());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
