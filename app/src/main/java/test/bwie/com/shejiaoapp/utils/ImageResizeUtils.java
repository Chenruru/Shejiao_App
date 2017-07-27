package test.bwie.com.shejiaoapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by muhanxi on 17/7/6.
 */

public class ImageResizeUtils {


    /**
     * 照片路径
     * 压缩后 宽度的尺寸
     * @param path
     * @param specifiedWidth
     */
    public static Bitmap resizeImage(String path,int specifiedWidth) throws Exception{


        Bitmap bitmap = null;
        FileInputStream inStream = null;
        File f = new File(path);
        System.out.println(path);
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        try {
            inStream = new FileInputStream(f);
            int degree = readPictureDegree(path);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            // 获取这个图片的宽和高
            decodeFile(path, opt); // 此时返回bm为空

            int inSampleSize = 1;
            final int width = opt.outWidth;
//           width 照片的原始宽度  specifiedWidth 需要压缩的宽度
//            1000 980
            if (width > specifiedWidth) {
                inSampleSize = (int) (width / (float) specifiedWidth);
            }
            // 按照 565 来采样 一个像素占用2个字节
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
//            图片加载到内存
            opt.inJustDecodeBounds = false;
            // 等比采样
            opt.inSampleSize = inSampleSize;
//            opt.inPurgeable = true;
            // 容易导致内存溢出
            bitmap = BitmapFactory.decodeStream(inStream, null, opt);
            // bitmap = BitmapFactory.decodeFile(path, opt);
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inStream = null;
                }
            }

            if (bitmap == null) {
                return null;
            }
            Matrix m = new Matrix();
            if (degree != 0) {
                //给Matrix 设置旋转的角度
                m.setRotate(degree);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            }
            float scaleValue = (float) specifiedWidth / bitmap.getWidth();
//            等比压缩
            m.setScale(scaleValue, scaleValue);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    public static void copyStream(InputStream inputStream, OutputStream outStream) throws Exception {
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            if(outStream != null){
                outStream.close();
            }
        }

    }



}
