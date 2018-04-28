package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.utils.ACache;
import com.yxdriver.app.utils.UtilImags;
import com.yxdriver.app.view.NonScrollGridView;
import com.yxdriver.app.view.UploadDialog;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/9/11.
 * 上传照片信息
 */

public class YXSetInfoPhotoActivity extends BaseActivity {
    private static final int RESULT_CAMARA = 1;
    private static final int RESULT_ALBUMS = 2;
    private static final String TAG = "YXSetInfoPhotoActivity";
    //身份证
    private static final String KEY_IDCARD = "cardImage";
    //手持身份证
    private static final String KEY_IDCARD_HAND = "holdCardImage";
    //驾驶证
    private static final String KEY_DRIVER = "driverImage";
    //行驶证
    private static final String KEY_DRIVERING = "vehicleImage";
    //车辆信息
    private static final String KEY_CAR = "carImage";


    @BindView(R.id.gv)
    NonScrollGridView gridView;
    ImageView imageView;

    String URL = "url";
    PopupWindow pop;
    LinearLayout ll_popup;
    Intent intent;
    String urlsf = "";
    int img = 1;
    ACache aCache;

    //存储需要上传的图片文件
    Map<String, String> imageMaps;

    String [] s;
    String [] ss;
    String mapKey = null;

    String[] arrRes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_infophoto);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, YXSetInfoPhotoActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        gridView.setAdapter(new MyAdapter(context));
        aCache = ACache.get(this);
        imageMaps = new HashMap<>();
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.upload_photo_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.upload_photo_btn:
                //上传图片信息
                if (checkFile()) {
                    //upLoadFaile1(DemoApplication.getInstance().getMyToken());
                   upLoadFaile(DemoApplication.getInstance().getMyToken());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mapKey = KEY_IDCARD;
                        showIDCardDialog(view, R.drawable.shen_fen_zheng);
                        imageView = (ImageView) gridView.getChildAt(position).findViewById(R.id.iv);
                        break;
                    case 1:
                        mapKey = KEY_IDCARD_HAND;
                        showIDCardDialog(view, R.drawable.shou_chi_shen_fen_zheng);
                        imageView = (ImageView) gridView.getChildAt(position).findViewById(R.id.iv);
                        //imageView.setImageResource(R.mipmap.entrance1);
                        break;
                    case 2:
                        mapKey = KEY_DRIVER;
                        showIDCardDialog(view, R.drawable.jia_shi_zheng);
                        imageView = (ImageView) gridView.getChildAt(position).findViewById(R.id.iv);
                        //imageView.setImageResource(R.mipmap.entrance1);
                        break;
                    case 3:
                        mapKey = KEY_DRIVERING;
                        showIDCardDialog(view, R.drawable.xing_shi_zheng);
                        imageView = (ImageView) gridView.getChildAt(position).findViewById(R.id.iv);
                        //imageView.setImageResource(R.mipmap.entrance1);
                        break;
                    case 4:
                        mapKey = KEY_CAR;
                        showIDCardDialog(view, R.drawable.che_liang);
                        imageView = (ImageView) gridView.getChildAt(position).findViewById(R.id.iv);
                        //imageView.setImageResource(R.mipmap.entrance1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //自定义适配器（通过继承BaseAdapter）
    class MyAdapter extends BaseAdapter {
        Context context;//声明适配器中引用的上下文
        //将需要引用的图片和文字分别封装成数组
        int[] images = {R.drawable.add_icon, R.drawable.add_icon, R.drawable.add_icon, R.drawable.add_icon, R.drawable.add_icon};
        String[] names = {"身份证照片", "手持身份证照片", "驾驶证照片", "行驶证照片", "车辆45度角侧面照"};

        //通过构造方法初始化上下文
        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return names.length;//images也可以
        }

        @Override
        public Object getItem(int position) {
            return names[position];//images也可以
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //通过布局填充器LayoutInflater填充网格单元格内的布局
            View v = LayoutInflater.from(context).inflate(R.layout.yx_item_setphoto, null);
            //使用findViewById分别找到单元格内布局的图片以及文字
            ImageView iv = (ImageView) v.findViewById(R.id.iv);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            //引用数组内元素设置布局内图片以及文字的内容
            iv.setImageResource(images[position]);
            tv.setText(names[position]);
            //返回值一定为单元格整体布局v
            return v;
        }
    }

    /**
     * 显示身份证的弹出框
     *
     * @param view
     */
    private void showIDCardDialog(final View view, int res) {
        final UploadDialog dialog = new UploadDialog(context, res, "立即上传");
        dialog.show();
        dialog.setClicklistener(new UploadDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                dialog.dismiss();
                //dialog.imageView.setImageResource(R.mipmap.ic_launcher);
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        context, R.anim.activity_translate_in));
                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }

            @Override
            public void doUpload() {
                dialog.dismiss();
                //把图片加载进来
                /*showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        context, R.anim.activity_translate_in));
                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);*/
            }
        });
    }

    /****
     * 头像提示框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(context);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, RESULT_CAMARA);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, RESULT_ALBUMS);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照中获取结果
        if (requestCode == RESULT_CAMARA && resultCode == Activity.RESULT_OK
                && null != data) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bmp = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            String filename = null;
            try {
                filename = UtilImags.SHOWFILEURL(context) + "/" + name;
            } catch (IOException e) {
            }
            try {
                fout = new FileOutputStream(filename);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            } catch (FileNotFoundException e) {
                //showToastShort("上传失败");
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    //showToastShort("上传失败");
                }
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            final String finalFilename = filename;

            Tiny.getInstance().source(finalFilename).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile, Throwable t) {
                    if (!isSuccess) {
                        Log.e("zxy", "error: " + t.getMessage());
                        //mCompressTv.setText("compress file failed!");
                        return;
                    }
                    Bitmap bmp = BitmapFactory.decodeFile(outfile);
                    // 获取图片并显示
                    imageView.setImageBitmap(bmp);
                    //将选中的图片添加到map中
                    imageMaps.put(mapKey, outfile);
//             staffFileupload(new File(filename));

                }
            });

        }
        //相册中获取结果
        if (requestCode == RESULT_ALBUMS && resultCode == Activity.RESULT_OK
                && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage,
                        filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
                final String finalFilename = picturePath;

                Tiny.getInstance().source(picturePath).asFile().withOptions(compressOptions).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        if (!isSuccess) {
                            Log.e("zxy", "error: " + t.getMessage());
                            //mCompressTv.setText("compress file failed!");
                            return;
                        }
                        Bitmap bmp = BitmapFactory.decodeFile(outfile);
                        // 获取图片并显示
                        imageView.setImageBitmap(bmp);
                        // zqRoundOvalImageView.setImageBitmap(bmp);
                        try {
                            saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(context) + "/stscname.jpg");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //将选中的图片添加到map中
                        imageMaps.put(mapKey, outfile);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                //showToastShort("上传失败");
            }
        }
    }

    /**
     * 文件上传 add bu zhangyun 2017年9月20日 22:10:51 start
     */
    /**
     * 上传文件，在此界面中指上传图片
     */
    private void upLoadFaile(String token){
        s= new String[]{imageMaps.get(KEY_IDCARD), imageMaps.get(KEY_IDCARD_HAND), imageMaps.get(KEY_DRIVER), imageMaps.get(KEY_DRIVERING), imageMaps.get(KEY_CAR)};
        ss= new String[]{KEY_IDCARD, KEY_IDCARD_HAND, KEY_DRIVER, KEY_DRIVERING, KEY_CAR};
        upLoadFaile2(s[0],ss[0],token,0);
    }
    private void upLoadFaile2(String path, String type, final String token, final int log) {
        final int log_=log+1;
        File file=new File(path);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("file\"; filename=\"" + file.getName(), fileBody);
        params.put("type",convertToRequestBody(type));
        //请求接口 上传图片
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "文件上传中", false, true, false, true).show();
        //将文件上传到服务器中

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).uploadSingleDriverImages(token,
                params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                //Log.i(TAG, "文件上传成功 --> " + response.body().toString());

                switch (response.code()) {
                    case 200:
                        try {
                            if (response.body() == null) {
                                DialogUIUtils.showToast(response.message());
                                Log.e(TAG, "解析失败 -- > 请求返回结果未空");
                            } else {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    if (log_<5){
                                        upLoadFaile2(s[log_],ss[log_],token,log_);
                                    }else{
                                        DialogUIUtils.dismiss(dialog);
                                        DialogUIUtils.showToast("司机相关照片上传成功");
                                        toInformation();
                                    }

                                }
                            }

                        } catch (IOException e) {
                            Log.e(TAG, "解析失败 -- > " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        DialogUIUtils.dismiss(dialog);
                        try {
                            String result = response.errorBody().string();
                            DialogUIUtils.showToast(JSON.parseObject(result).getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast(t.getMessage());
                Log.i(TAG, "文件上传失败 --> " + t.getMessage());
            }
        });
    }

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }
    private void toInformation() {
        //跳转到信息审核界面
        toActivity(YXInformationActivity.createIntent(context));
        finish();
    }

    /**
     * 检查文件
     */
    private boolean checkFile() {
        if (imageMaps.get(KEY_IDCARD) == null) {
            DialogUIUtils.showToast("请选择身份证照片");
            return false;
        }
        if (imageMaps.get(KEY_IDCARD_HAND) == null) {
            DialogUIUtils.showToast("请选择手持身份证照片");
            return false;
        }
        if (imageMaps.get(KEY_DRIVER) == null) {
            DialogUIUtils.showToast("请选择驾驶证照片");
            return false;
        }
        if (imageMaps.get(KEY_DRIVERING) == null) {
            DialogUIUtils.showToast("请选择行驶证照片");
            return false;
        }
        if (imageMaps.get(KEY_CAR) == null) {
            DialogUIUtils.showToast("请选择车辆照片");
            return false;
        }
        return true;
    }

    /**
     * 文件上传 add bu zhangyun 2017年9月20日 22:10:51 end
     */

    public void saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upLoadFaile1(String token) {
        String cardImagePath = imageMaps.get(KEY_IDCARD);
        String holdCardImagePath = imageMaps.get(KEY_IDCARD_HAND);
        String driverImagePath = imageMaps.get(KEY_DRIVER);
        String vehicleImagePath = imageMaps.get(KEY_DRIVERING);
        String carImagePath = imageMaps.get(KEY_CAR);

        File cardImageFile = new File(cardImagePath);
        File holdCardImageFile = new File(holdCardImagePath);
        File driverImageFile = new File(driverImagePath);
        File vehicleImageFile = new File(vehicleImagePath);
        File carImageFile = new File(carImagePath);

        RequestBody cardImageBody = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), cardImageFile);
        RequestBody holdCardImageBody = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), holdCardImageFile);
        RequestBody driverImageBody = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), driverImageFile);
        RequestBody vehicleImageBody = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), vehicleImageFile);
        RequestBody carImageBody = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), carImageFile);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("cardImage\"; filename=\"" + cardImageFile.getName(), cardImageBody);
        params.put("holdCardImage\"; filename=\"" + holdCardImageFile.getName(), holdCardImageBody);
        params.put("driverImage\"; filename=\"" + driverImageFile.getName(), driverImageBody);
        params.put("vehicleImage\"; filename=\"" + vehicleImageFile.getName(), vehicleImageBody);
        params.put("carImage\"; filename=\"" + carImageFile.getName(), carImageBody);
        //请求接口 上传图片
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "文件上传中", false, true, true, true).show();
        //将文件上传到服务器中
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).uploadDriverImages(token,
                params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                //Log.i(TAG, "文件上传成功 --> " + response.body().toString());
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        try {
                            if (response.body() == null) {
                                DialogUIUtils.showToast(response.message());
                                Log.e(TAG, "解析失败 -- > 请求返回结果未空");
                            } else {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    DialogUIUtils.showToast("图片全部上传成功!");
                                    toInformation();
                                }
                            }

                        } catch (IOException e) {
                            Log.e(TAG, "解析失败 -- > " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        try {
                            String result = response.errorBody().string();
                            DialogUIUtils.showToast(JSON.parseObject(result).getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast(t.getMessage());
                Log.i(TAG, "文件上传失败 --> " + t.getMessage());
            }
        });
    }
}
