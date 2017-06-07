package com.qiniu.manager.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.constant.ConstantsUtils;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

/**
 * 文件资源管理
 * 
 * @author hktry
 *
 */
public class Test1 {
	@Test
	// 获取文件信息
	public void test1() {
		Configuration cfg = new Configuration(Zone.zone0());
		String key = "1.jpg";
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		try {
			// 获取文件的详细信息
			FileInfo fileInfo = bucketManager.stat(ConstantsUtils.pBucket, key);
			System.out.println(fileInfo.hash);
			System.out.println(fileInfo.fsize);
			System.out.println(fileInfo.mimeType);
			System.out.println(fileInfo.putTime);
		} catch (QiniuException e) {
			e.printStackTrace();
		}
	}

	@Test
	// 修改文件类型
	public void test2() {
		Configuration cfg = new Configuration(Zone.zone0());
		String key = "1.jpg";
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);

		try {
			bucketManager.changeMime(ConstantsUtils.pBucket, key, "mp3");
		} catch (QiniuException e) {
			System.out.println(e.response.toString());
			e.printStackTrace();
		}
	}

	@Test
	// 移动或重命名文件，移动本身支持文件移动到不同空间中，移动的同时也支持文件重命名，但是前提必须是移动的源空间和目标空间必须在同一机房
	public void test3() {
		Configuration cfg = new Configuration(Zone.zone0());
		String key = "1.jpg";
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		String toKey = "avc.jpg";
		try {
			bucketManager.move(ConstantsUtils.bucket, key, ConstantsUtils.pBucket, toKey);
		} catch (QiniuException ex) {
			// 如果遇到异常，说明移动失败
			System.err.println(ex.code());
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 复制文件副本
	public void test4() {
		Configuration cfg = new Configuration(Zone.zone0());
		String key = "abc.jpg";
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		String toKey = "abc.jpg";
		try {
			bucketManager.copy(ConstantsUtils.bucket, key, ConstantsUtils.pBucket, toKey);
		} catch (QiniuException ex) {
			// 如果遇到异常，说明移动失败
			System.err.println(ex.code());
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 删除空间中的文件
	public void test5() {
		Configuration cfg = new Configuration(Zone.zone0());
		String key = "abc.jpg";
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		try {
			bucketManager.delete(ConstantsUtils.bucket, key);
		} catch (QiniuException ex) {
			// 如果遇到异常，说明移动失败
			System.err.println(ex.code());
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 设置或者更新文件的生存时间
	public void test6() {
		Configuration cfg = new Configuration(Zone.zone0());
		String key = "abc.jpg";
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 默认单位为天。
		int days = 10;
		try {
			bucketManager.deleteAfterDays(ConstantsUtils.pBucket, key, days);
		} catch (QiniuException ex) {
			// 如果遇到异常，说明移动失败
			System.err.println(ex.code());
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 获取空间文件列表
	public void test7() {
		Configuration cfg = new Configuration(Zone.zone0());
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 文件名前缀
		String prefix = "a";
		// 每次迭代的长度限制
		int limit = 1000;
		// 指定目录分隔符，列出所有公共前缀
		String delimiter = "";
		BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(ConstantsUtils.pBucket,
				prefix, limit, delimiter);
		while (fileListIterator.hasNext()) {
			FileInfo[] items = fileListIterator.next();
			for (FileInfo item : items) {
				System.out.println(item.key);
				System.out.println(item.hash);
				System.out.println(item.fsize);
				System.out.println(item.mimeType);
				System.out.println(item.putTime);
				System.out.println(item.endUser);
				System.out.println("-------");
			}

		}
	}

	@Test
	// 抓取网络资源到空间
	public void test8() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		String remoteUrl = "http://devtools.qiniu.com/qiniu.png";
		// 网络资源重新命名
		String key = "qiniu";
		FetchRet fetchRet;
		try {
			fetchRet = bucketManager.fetch(remoteUrl, ConstantsUtils.pBucket, key);
			System.out.println(fetchRet.key);
			System.out.println(fetchRet.hash);
			System.out.println(fetchRet.fsize);
			System.out.println(fetchRet.mimeType);
		} catch (QiniuException e) {
			System.out.println(e.response.toString());
			e.printStackTrace();
		}
	}

	@Test
	// 批量获取文件信息
	public void test9() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		try {
			// 单次批量请求的文件数量不得超过1000
			String[] keyList = new String[] { "qiniu", "abc.jpg", "avc.jpg", };
			// 批量处理工具
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			batchOperations.addStatOps(ConstantsUtils.pBucket, keyList);
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			for (int i = 0; i < keyList.length; i++) {
				BatchStatus status = batchStatusList[i];
				String key = keyList[i];
				System.out.print(key + "\t");
				if (status.code == 200) {
					// 文件存在
					System.out.println(status.data.hash);
					System.out.println(status.data.mimeType);
					System.out.println(status.data.fsize);
					System.out.println(status.data.putTime);
				} else {
					System.out.println(status.data.error);
				}
				System.out.println("--------");
			}
		} catch (QiniuException ex) {
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 批量修改文件类型
	public void test10() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		try {
			// 单次批量请求的文件数量不得超过1000
			HashMap<String, String> keyMimeMap = new HashMap<>();
			keyMimeMap.put("qiniu", "image/jpg");
			keyMimeMap.put("abc.jpg", "image/png");
			// 批量处理工具
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			// 添加指令
			for (Map.Entry<String, String> entry : keyMimeMap.entrySet()) {
				String key = entry.getKey();
				String newMimeType = entry.getValue();
				batchOperations.addChgmOp(ConstantsUtils.pBucket, key, newMimeType);
			}
			// 执行批处理修改
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			int index = 0;
			for (Map.Entry<String, String> entry : keyMimeMap.entrySet()) {
				String key = entry.getKey();
				System.out.print(key + "\t");
				BatchStatus status = batchStatusList[index];
				if (status.code == 200) {
					System.out.println("change mime success");
				} else {
					System.out.println(status.data.error);
				}
				index += 1;
			}
		} catch (QiniuException ex) {
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 批量删除文件
	public void test11() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		try {
			// 单次批量请求的文件数量不得超过1000
			String[] keyList = new String[] { "qiniu", "abc.jpg", "avc.jpg", };
			// 批量处理工具
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			// 这里面有多中批处理操作，会一一展现在后面展出来
			batchOperations.addDeleteOp(ConstantsUtils.pBucket, keyList);
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			for (int i = 0; i < keyList.length; i++) {
				BatchStatus status = batchStatusList[i];
				String key = keyList[i];
				System.out.print(key + "\t");
				if (status.code == 200) {
					System.out.println("delete success");
				} else {
					System.out.println(status.data.error);
				}
			}
		} catch (QiniuException ex) {
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 批量移动或重命名文件
	public void test12() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		try {
			// 单次批量请求的文件数量不得超过1000
			String[] keyList = new String[] { "1.jpg", "b.jpg", "c.jpg", };
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			for (String key : keyList) {
				batchOperations.addMoveOp(ConstantsUtils.pBucket, key, ConstantsUtils.bucket, key + "_move");
			}
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			for (int i = 0; i < keyList.length; i++) {
				BatchStatus status = batchStatusList[i];
				String key = keyList[i];
				System.out.print(key + "\t");
				if (status.code == 200) {
					System.out.println("move success");
				} else {
					System.out.println(status.data.error);
				}
			}
		} catch (QiniuException ex) {
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 批量复制文件
	public void test13() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		try {
			// 单次批量请求的文件数量不得超过1000
			String[] keyList = new String[] { "1.jpg_move", "b.jpg_move", "c.jpg_move", };
			// 批量处理工具
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			for (String key : keyList) {
				batchOperations.addCopyOp(ConstantsUtils.bucket, key, ConstantsUtils.pBucket, key);
			}
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			for (int i = 0; i < keyList.length; i++) {
				BatchStatus status = batchStatusList[i];
				String key = keyList[i];
				System.out.print(key + "\t");
				if (status.code == 200) {
					System.out.println("copy success");
				} else {
					System.out.println(status.data.error);
				}
			}
		} catch (QiniuException ex) {
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 批量混合指令操作
	public void test14() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		try {
			// 单次批量请求的文件数量不得超过1000
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			// 添加混合指令
			batchOperations.addStatOps(ConstantsUtils.bucket, "qiniu.png");
			batchOperations.addCopyOp(ConstantsUtils.bucket, "qiniu.png", ConstantsUtils.pBucket, "qiniu_copy1.png");
			batchOperations.addMoveOp(ConstantsUtils.pBucket, "qiniu2.png", ConstantsUtils.bucket, "qiniu3.png");
			batchOperations.addDeleteOp(ConstantsUtils.pBucket, "qiniu4.png");
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			for (BatchStatus status : batchStatusList) {
				if (status.code == 200) {
					System.out.println("operation success");
				} else {
					System.out.println(status.data.error);
				}
			}
		} catch (QiniuException ex) {
			System.err.println(ex.response.toString());
		}
	}

	@Test
	// 更新镜像存储空间的文件内容。镜像的理解：https://developer.qiniu.com/kodo/kb/1376/seven-cattle-image-storage-instruction-manuals
	public void test15() {
		Configuration cfg = new Configuration(Zone.zone0());
		// bucket资源管理工具类
		Auth auth = Auth.create(ConstantsUtils.accessKey, ConstantsUtils.secertKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		// 网络资源地址
		String key ="your filename";
		try {
			bucketManager.prefetch(ConstantsUtils.pBucket, key);
		} catch (QiniuException ex) {
			// 如果遇到异常，说明更新失败
			System.err.println(ex.code());
			System.err.println(ex.response.toString());
		}
	}
}
