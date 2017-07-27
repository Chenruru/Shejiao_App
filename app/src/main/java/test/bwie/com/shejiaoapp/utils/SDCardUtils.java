package test.bwie.com.shejiaoapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;


import java.io.File;
import java.io.IOException;


public class SDCardUtils {


	public static final String DLIAO = "dliao" ;

	public static File photoCacheDir = SDCardUtils.createCacheDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DLIAO);
	public static String cacheFileName = "myphototemp.jpg";



	public static boolean isSDCardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static boolean deleteOldAllFile(final String path) {
		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					delAllFile(Environment.getExternalStorageDirectory() + File.separator + DLIAO);
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 给定字符串获取文件夹
	 * 
	 * @param dirPath
	 * @return 创建的文件夹的完整路径
	 */
	public static File createCacheDir(String dirPath) {
		File dir = new File(dirPath);;
		if(isSDCardExist()){
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		return dir;
	}

	public static File createNewFile(File dir, String fileName) {
		File f = new File(dir, fileName);
		try {
			// 出现过目录不存在的情况，重新创建
			if (!dir.exists()) {
				dir.mkdirs();
			}
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static File getCacheFile() {
		return createNewFile(photoCacheDir, cacheFileName);
	}

	public static File getMyFaceFile(String fileName) {
		return createNewFile(photoCacheDir, fileName);
	}

	/**
	 * 获取SDCARD剩余存储空间
	 *
	 * @return 0 sd已被挂载占用  1 sd卡内存不足 2 sd可用
	 */
	public static int getAvailableExternalStorageSize() {
		if (isSDCardExist()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			long memorySize = availableBlocks * blockSize;
			if(memorySize < 10*1024*1024){
				return 1;
			}else{
				return 2;
			}
		} else {
			return 0;
		}
	}











}
