package com.heke.rihappclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.heke.rihappclient.R;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.utils.UIUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by xssx5 on 2018-03-16.
 */

public class zxingActivity extends BaseActivity implements View.OnClickListener{
    ImageView imageView;
    Button button_key;
    EditText inputText;
    Button button_scan;
    private final static int SCANNIN_GREQUEST_CODE = 101;

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x22:
                    imageView.setVisibility(View.VISIBLE);
                    String fileName ="qr_" + System.currentTimeMillis() + ".jpg";/*文件名称=qr_系统时间.jpg*/
                    File file = getFileRoot(fileName);
                    String name=inputText.getText().toString();
                    if(name==null||name.equals("")){
                        Toast.makeText(zxingActivity.this,"请输入内容",Toast.LENGTH_LONG).show();
                        break;
                    }
                    Bitmap bitmap=createQRImage(name, imageView,200,200);/*二维码信息:Welcome!This is a QR_code*/
                    saveImage(zxingActivity.this, bitmap, file, fileName);
                    //button_key.setEnabled(true);
                    break;
                case 0x23:
                    imageView.setVisibility(View.GONE);
                    button_key.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "注销成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);

        imageView = (ImageView) findViewById(R.id.img_qr);
        button_key = (Button) findViewById(R.id.key);
        inputText=(EditText)findViewById(R.id.input_text);
        inputText.setOnClickListener(this);
        button_key.setOnClickListener(this);
        Button button_delete = (Button) findViewById(R.id.delete);
        button_delete.setOnClickListener(this);
        button_scan=(Button)findViewById(R.id.scan);
        button_scan.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.key:
                button_key.setEnabled(false);
                new Thread() {
                    boolean key_flag=true;
                    public void run() {
                        handler.sendEmptyMessage(0x22);
                        Log.e("二维码", "success");
                    }
                }.start();
                break;

            case R.id.delete:
                new Thread() {
                    boolean key_flag=true;
                    String Del;
                    public void run() {
                        Log.e("删除成功：", "ok");
                        handler.sendEmptyMessage(0x23);
                    }
                }.start();
                break;

            case R.id.scan:
                new Thread(){
                    boolean key_flag=true;
                    public  void run(){
                        Intent it = new Intent(zxingActivity.this, CaptureActivity.class);
                        startActivityForResult(it, SCANNIN_GREQUEST_CODE);
                    }
                }.start();
                break;
        }
    }
    //文件存储根目录
    private File getFileRoot(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");/*文件保存在手机根目录:Boohee*/
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, fileName);
            Log.e("路径：", file.toString());
            return file;
        }
        return null;
    }
    //要转换的地址或字符串,可以是中文
    public static Bitmap createQRImage(String url, ImageView sweepIV, int QR_WIDTH, int QR_HEIGHT ) {
        try {//判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else {
                        pixels[y * QR_HEIGHT + x] = 0xffffffff;
                    }
                }
            }//生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            sweepIV.setImageBitmap(bitmap);
            return bitmap;
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    //保存图片
    public static void saveImage(Context context, Bitmap bitmap, File filePath, String fileName){
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {// 把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    filePath.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + "/sdcard/Boohee/image.jpg")));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("scan_result");
                Log.i("", "scan result:" + result);
                if (!result.equals("")) {
                    inputText.setText(result);
                } else {
                    UIUtil.showToast("扫码失败");
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
