package com.qiniu.persist.test;

import org.junit.Test;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.constant.ConstantsUtils;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

/**
 * 持久化冲突
 * 
 * @author hktry
 *
 */

public class Test1 {
	@Test
	// 发送数据处理请求
	public void test1() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		String key ="your persisit filename";
		// 数据处理指令，支持多个指令
		String saveMp4Entry = String.format("%s:test.mp4", ConstantsUtils.bucket);
		String saveJpgEntry = String.format("%s:test.jpg", ConstantsUtils.bucket);
		String avthumbMp4Fop = String.format("test/test/%s", UrlSafeBase64.encodeToString(saveMp4Entry));
		String vframeJpgFop = String.format("test/1|saveas/%s", UrlSafeBase64.encodeToString(saveJpgEntry));
		// 将多个数据处理指令拼接起来
		String persistentOpfs = StringUtils.join(new String[] { avthumbMp4Fop, vframeJpgFop }, ";");
		// 数据处理队列名称，必须
		String persistentPipeline = "mps-pipe1";
		// 数据处理完成结果通知地址
		String persistentNotifyUrl = "http://api.example.com/qiniu/pfop/notify";
		// ...其他参数参考类注释
		// 构建持久化数据处理对象
		OperationManager operationManager = new OperationManager(auth, cfg);
		try {
			String persistentId = operationManager.pfop(ConstantsUtils.bucket, key, persistentOpfs, persistentPipeline,
					persistentNotifyUrl, true);
			System.out.println(persistentId);
			// 可以根据该 persistentId 查询任务处理进度
			OperationStatus operationStatus = operationManager.prefop(persistentId);
			// 解析 operationStatus 的结果
			System.out.println(operationStatus.toString());
		} catch (QiniuException e) {
			System.err.println(e.response.toString());
		}
	}
}
