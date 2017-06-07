package com.qiniu.cdn.test;

import java.util.Map;

import org.junit.Test;

import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.cdn.CdnResult.FluxData;
import com.qiniu.cdn.CdnResult.LogData;
import com.qiniu.common.QiniuException;
import com.qiniu.constant.ConstantsUtils;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * cdn相关功能
 * 
 * @author hktry
 *
 */
public class Test1 {
	@Test
	// 文件刷新
	public void test1() {
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		CdnManager c = new CdnManager(auth);
		// 待刷新的链接列表
		String[] urls = new String[] { "http://javasdk.qiniudn.com/gopher1.jpg",
				"http://javasdk.qiniudn.com/gopher2.jpg" };
		try {
			// 单次方法调用刷新的链接不可以超过100个
			CdnResult.RefreshResult result = c.refreshUrls(urls);
			System.out.println(result.code);
			// 获取其他的回复内容
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}

	@Test
	// 目录刷新
	public void test2() {
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		CdnManager c = new CdnManager(auth);
		// 待刷新的目录
		String[] urls = new String[] { "http://javasdk.qiniudn.com/gopher1／", "http://javasdk.qiniudn.com/gopher2／" };
		try {
			// 单次方法调用刷新的链接不可以超过100个
			CdnResult.RefreshResult result = c.refreshDirs(urls);
			System.out.println(result.code);
			// 获取其他的回复内容
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}

	@Test
	// 文件预取
	public void test3() {
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		CdnManager c = new CdnManager(auth);
		// 待预取的链接列表
		String[] urls = new String[] { "http://javasdk.qiniudn.com/gopher1.jpg",
				"http://javasdk.qiniudn.com/gopher2.jpg" };
		try {
			// 单次方法调用刷新的链接不可以超过100个
			CdnResult.PrefetchResult result = c.prefetchUrls(urls);
			System.out.println(result.code);
			// 获取其他的回复内容
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}

	@Test
	// 获取域名流量
	public void test4() {
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		CdnManager c = new CdnManager(auth);
		// 待刷新的链接列表
		String[] urls = new String[] { "staticimg.xshservice.cn" };
		// 开始日期和结束日期
		String fromDate = "2017-03-02";
		String toDate = "2017-03-09";
		String granularity = "day";
		try {
			// 单次方法调用刷新的链接不可以超过100个
			CdnResult.FluxResult result = c.getFluxData(urls, fromDate, toDate, granularity);
			for (Map.Entry<String, FluxData> entry : result.data.entrySet()) {
				FluxData fluxData = entry.getValue();
				System.out.println(fluxData.china + "------");
			}
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}

	@Test
	// 获取域名带宽
	public void test5() {
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		CdnManager c = new CdnManager(auth);
		// 开始日期和结束日期
		String fromDate = "2017-03-02";
		String toDate = "2017-03-09";
		String granularity = "day";
		String[] urls = new String[] { "staticimg.xshservice.cn" };
		try {
			CdnResult.BandwidthResult bandwidthResult = c.getBandwidthData(urls, fromDate, toDate, granularity);
			System.out.println(bandwidthResult.code);
			// 获取其他的回复内容
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}

	@Test
	// 获取日志下载链接
	public void test6() {
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		CdnManager c = new CdnManager(auth);
		// 域名列表
		String[] urls = new String[] { "staticimg.xshservice.cn" };
		// 具体日期
		String logDate = "2017-03-15";
		try {
			CdnResult.LogListResult logListResult = c.getCdnLogList(urls, logDate);
			// 处理得到的结果数据
			for (Map.Entry<String, LogData[]> entry : logListResult.data.entrySet()) {
				LogData fluxData[] = entry.getValue();
				for (LogData logData : fluxData) {
					System.out.println(logData.url + "------");
				}
			}
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}

	@Test
	// 构建时间戳防盗链访问链接
	public void test7() {
		String host = "http://staticimg.xshservice.cn";
		String fileName = "01-1478068410742.png";
		// 查询参数 假设一个小时
		StringMap queryStringMap = new StringMap();
		queryStringMap.put("name", "七牛");
		queryStringMap.put("year", 2017);
		queryStringMap.put("年龄", 28);
		// 链接过期时间
		long deadline = System.currentTimeMillis() / 1000 + 3600;
		// 签名密钥，从后台域名属性中获取，也就是sk
		String encryptKey = ConstantsUtils.secertKey;
		String signedUrl;
		try {
			signedUrl = CdnManager.createTimestampAntiLeechUrl(host, fileName, queryStringMap, encryptKey, deadline);
			System.out.println(signedUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
