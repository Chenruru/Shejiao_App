package test.bwie.com.shejiaoapp.utils;

/**
 * Created by muhanxi on 17/7/9.
 */

public class DisanceUtils {


//	  100
    	public static String standedDistance(Float params) {
		String retVal = "";
		try {
			if (params < 500) {
				retVal = "500米内";
			} else if (params < 1000) {
				if (params.toString().contains(".")) {
					// 包含小数点
					String[] content = params.toString().split("\\.");
					retVal = content[0].toString() + "米";
				} else {
					retVal = String.valueOf(params) + "米";
				}
			} else if (params < 10000) {
				Double tempFloat = params / 1000.0;
				if (tempFloat.toString().contains(".")) {
					// 包含小数点
					int length = tempFloat.toString().indexOf(".");
					retVal = tempFloat.toString().substring(0, length + 2) + "公里";
				} else {
					retVal = String.valueOf(params) + "公里";
				}
			} else if (params < 9900000) {
				Double tempFloat = params / 1000.0;
				if (tempFloat.toString().contains(".")) {
					String[] content = tempFloat.toString().split("\\.");
					retVal = content[0] + "公里";
				} else {
					retVal = String.valueOf(tempFloat) + "公里";
				}
			} else if (params >= 9900000){
				retVal = "1万公里外";
			}else{
				retVal = "";
			}
		} catch (NumberFormatException e) {
			retVal = "未知";
		} catch (Exception e) {
			retVal = "未知";
		}
		return retVal;
	}
}
