package test.bwie.com.shejiaoapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;

import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.base.AppManager;
import test.bwie.com.shejiaoapp.base.IActivity;
import test.bwie.com.shejiaoapp.bean.UploadPhotoBean;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.utils.Constants;
import test.bwie.com.shejiaoapp.utils.ImageResizeUtils;
import test.bwie.com.shejiaoapp.utils.SDCardUtils;
import test.bwie.com.shejiaoapp.widget.MyToast;

import static test.bwie.com.shejiaoapp.utils.ImageResizeUtils.copyStream;


@RuntimePermissions
public class PhotoActivity extends IActivity {

//    @BindView(R.id.select_image)
//    ImageView selectImage;
//    @BindView(R.id.select_title)
//    TextView selectTitle;
//    @BindView(R.id.select_login)
//    Button selectLogin;
    @BindView(R.id.upload_photo_camera)
    Button uploadPhotoCamera;
    @BindView(R.id.upload_photo_localphoto)
    Button uploadPhotoLocalphoto;
    @BindView(R.id.photo_circle_image)
    CircleImageView photoCircleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touxiang);
        ButterKnife.bind(this);
//        selectTitle.setVisibility(View.VISIBLE);
//        selectTitle.setText("上传形象照");
    }

    static final int INTENTFORCAMERA = 1;
    static final int INTENTFORPHOTO = 2;


    public String LocalPhotoName;

    public String createLocalPhotoName() {
        LocalPhotoName = System.currentTimeMillis() + "face.jpg";
        return LocalPhotoName;
    }

    /**
     * 打开系统相机
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void toCamera() {
        try {
            Intent intentNow = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentNow.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(SDCardUtils.getMyFaceFile(createLocalPhotoName())));
            startActivityForResult(intentNow, INTENTFORCAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void showRationaleForCamera(final PermissionRequest request) {

        new AlertDialog.Builder(this)
                .setMessage("需要打开您的相机来上传照片并保存照片")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        request.proceed();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }


    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onDenied() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();

    }


    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onNeverAsyAgain() {
        Toast.makeText(this, "不再提示", Toast.LENGTH_SHORT).show();
    }


    /**
     * 打开相册
     */
    public void toPhoto() {
        try {
            createLocalPhotoName();
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType("image/*");
            startActivityForResult(getAlbum, INTENTFORPHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.upload_photo_camera, R.id.upload_photo_localphoto,R.id.photo_circle_image})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.upload_photo_camera:   //立即上传

                toPhoto();
                getLocalImage(102);
                break;
            case R.id.upload_photo_localphoto:  //拍照

                toCamera();
                toCheckPermissionCamera();
                break;
            case R.id.photo_circle_image:
                AlertDialog.Builder builder2 =  new AlertDialog.Builder(PhotoActivity.this);
                builder2.setItems(new String[]{ "相册", "取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 1:
                                break;
                            case 0:
                                getLocalImage(102);
                             //   toCamera();
                                break;
                            case 3:
                                break;
                        }
                    }
                });
                builder2.create().show();
        }

        }



    public void toCheckPermissionCamera() {
       PhotoActivityPermissionsDispatcher.toCameraWithCheck(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      PhotoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null&&requestCode==101&&resultCode==201){
            String name = data.getStringExtra("name");
            // yanzhengma.setText(name);
        }
        if(requestCode==102 && resultCode==RESULT_OK ){
            //读取本地相册
            readLocalImage(data);
        }
        if(requestCode==103 && resultCode==RESULT_OK ){
            //获取拍照返回的数据
            getPaizhaoData(data);
        }


        switch (requestCode) {
            case INTENTFORPHOTO:
                //相册

                try {
                    // 必须这样处理，不然在4.4.2手机上会出问题
                    Uri originalUri = data.getData();
                    File f = null;
                    if (originalUri != null) {
                        f = new File(SDCardUtils.photoCacheDir, LocalPhotoName);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor actualimagecursor = this.getContentResolver().query(originalUri, proj, null, null, null);
                        if (null == actualimagecursor) {
                            if (originalUri.toString().startsWith("file:")) {
                                File file = new File(originalUri.toString().substring(7, originalUri.toString().length()));
                                if (!file.exists()) {
                                    //地址包含中文编码的地址做utf-8编码
                                    originalUri = Uri.parse(URLDecoder.decode(originalUri.toString(), "UTF-8"));
                                    file = new File(originalUri.toString().substring(7, originalUri.toString().length()));
                                }
                                FileInputStream inputStream = new FileInputStream(file);
                                FileOutputStream outputStream = new FileOutputStream(f);
                                copyStream(inputStream, outputStream);
                            }
                        } else {
                            // 系统图库
                            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            actualimagecursor.moveToFirst();
                            String img_path = actualimagecursor.getString(actual_image_column_index);
                            if (img_path == null) {
                                InputStream inputStream = this.getContentResolver().openInputStream(originalUri);
                                FileOutputStream outputStream = new FileOutputStream(f);
                                copyStream(inputStream, outputStream);
                            } else {
                                File file = new File(img_path);
                                FileInputStream inputStream = new FileInputStream(file);
                                FileOutputStream outputStream = new FileOutputStream(f);
                                copyStream(inputStream, outputStream);
                            }

                        }
                        Bitmap bitmap = ImageResizeUtils.resizeImage(f.getAbsolutePath(), Constants.RESIZE_PIC);
                        //获得图片的宽和高
                        int bitWidth = bitmap.getWidth();
                        int bitHeight = bitmap.getHeight();
                        FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
                        if (bitmap != null) {
                            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)) {
                                fos.close();
                                fos.flush();
                            }
                            if (!bitmap.isRecycled()) {
                                bitmap.isRecycled();
                            }

                            uploadFile(f, bitWidth, bitHeight);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }


                break;
            case INTENTFORCAMERA:
//                相机
                try {

                    //file 就是拍照完 得到的原始照片
                    File file = new File(SDCardUtils.photoCacheDir, LocalPhotoName);
                    Bitmap bitmap = ImageResizeUtils.resizeImage(file.getAbsolutePath(), Constants.RESIZE_PIC);
                    int bitmapWidth = bitmap.getWidth();
                    int bitmapHeight = bitmap.getHeight();
                    FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                    if (bitmap != null) {
                        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)) {
                            fos.close();
                            fos.flush();
                        }
                        if (!bitmap.isRecycled()) {
                            //通知系统 回收bitmap
                            bitmap.isRecycled();
                        }
                        uploadFile(file, bitmapWidth, bitmapHeight);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }


    }


    public void uploadFile(File file, int bitmapWith, int bitmapHeight) {


        if (!file.exists()) {
            Toast.makeText(this, "照片不存在", Toast.LENGTH_SHORT);
            return;
        }
        String[] arr = file.getAbsolutePath().split("/");

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        long ctimer = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user.currenttimer", ctimer + "");  //时间戳
        map.put("user.picWidth", bitmapWith + "");
        map.put("user.picHeight", bitmapHeight + "");
        String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign", sign);


        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("image", arr[arr.length - 1], requestFile)
                .build();

//        key = value   addFormDataPart file 00101010110 key = value
//        key = value
//
//         file 00101010110
//        key = value


        RetrofitManager.uploadPhoto(body, map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                try {
                    Gson gson = new Gson();
                    UploadPhotoBean bean = gson.fromJson(result, UploadPhotoBean.class);
                    if (bean.getResult_code() == 200) {
                        MyToast.makeText(PhotoActivity.this, "上传成功", Toast.LENGTH_SHORT);


                    }
                    AppManager.getAppManager().finishActivity(PhotoActivity.class);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }

            @Override
            public void onFailed(int code) {
                MyToast.makeText(PhotoActivity.this, "上传失败" + code, Toast.LENGTH_SHORT);

            }
        });


    }
    private void getPaizhaoData(final  Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        photoCircleImage.setImageBitmap(bitmap);

    }

    //读取图片
    private void readLocalImage(Intent data) {
        if(data==null){
            return;
        }
        Uri uri = data.getData();
        //转化成bitmap
        Bitmap bitmap = getBitmapFromUri(uri);
        //显示图片
        photoCircleImage.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap;
        try {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //得到图片
    private void getLocalImage(int code) {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,code);
    }

}
