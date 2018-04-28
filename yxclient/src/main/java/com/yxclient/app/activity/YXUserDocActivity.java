package com.yxclient.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIDateTimeSaveListener;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.dou361.dialogui.listener.DialogUIListener;
import com.dou361.dialogui.widget.DateSelectorWheelView;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.UserModel;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.utils.UtilImags;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.ImageUtils;

/**
 * Created by mac on 2017/9/18.
 * 个人中心
 */

public class YXUserDocActivity extends BaseActivity {
    ////////////////////////////////////////////////////////////////////
//                            _ooOoo_                             //
//                           o8888888o                            //
//                           88" . "88                            //
//                           (| ^_^ |)                            //
//                           O\  =  /O                            //
//                        ____/`---'\____                         //
//                      .'  \\|     |//  `.                       //
//                     /  \\|||  :  |||//  \                      //
//                    /  _||||| -:- |||||-  \                     //
//                    |   | \\\  -  /// |   |                     //
//                    | \_|  ''\---/''  |   |                     //
//                    \  .-\__  `-`  ___/-. /                     //
//                  ___`. .'  /--.--\  `. . ___                   //
//                ."" '<  `.___\_<|>_/___.'  >'"".                //
//              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
//              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
//        ========`-.____`-.___\_____/___.-`____.-'========       //
//                             `=---='                            //
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
//         佛祖保佑       永无BUG        永不修改                    //
////////////////////////////////////////////////////////////////////

    @BindView(R.id.doc_userimage)
    CircleImageView heaer;
    //@BindView(R.id.icon)
    //CircleImageView icon;

    @BindView(R.id.doc_nickname)
    TextView tv_Nick;
    @BindView(R.id.doc_sex)
    TextView tv_Sex;
    @BindView(R.id.doc_bd)
    TextView tv_Bd;

    String token;
    String name;
    String sex;
    String birthday;
    private UserModel userModel;// 用户对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_userdoc);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, YXUserDocActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        token = DemoApplication.getInstance().getMyToken();
    }

    @Override
    public void initData() {
        getUserInfo();
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.doc_re_header, R.id.doc_re_nick, R.id.doc_re_sex, R.id.doc_re_berthday, R.id.doc_re_oldcar, R.id.doc_re_city})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.doc_re_header:
                showPopupWindow();
                //showChoosePicDialog();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        context, R.anim.activity_translate_in));
                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.doc_re_nick:
                // 修改昵称
                showNickDialog();
                break;
            case R.id.doc_re_sex:
                showSexDialog();
                break;
            case R.id.doc_re_berthday:
                showBertherDayDialog();
                break;
            case R.id.doc_re_oldcar:
                // 二手车信息管理
                toActivity(YXMyOldCarActivity.createIntent(context, ""));
                break;
            case R.id.doc_re_city:
                // 同城信息管理
                toActivity(YXMySameCityActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    ////////////////////////////////////////////////////////////
    /////头像相关
    //////////////////////////////////////////////
    PopupWindow pop;
    LinearLayout ll_popup;
    Intent intent;
    private File headFile;

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
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(YXUserDocActivity.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(YXUserDocActivity.this,new String[]{Manifest.permission.CAMERA},222);
                        return;
                    }else{
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(camera, 1);
                        pop.dismiss();
                        ll_popup.clearAnimation();
                    }
                } else {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, 1);
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }

            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, 2);
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
        if (requestCode == 1 && resultCode == Activity.RESULT_OK
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
                e.printStackTrace();
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
            heaer.setImageBitmap(bmp);
            try {
                saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(context) + "/stscname.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 修改头像
            //uploadHeadImage(DemoApplication.getInstance().getMyToken(), headFile);
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK
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

                Bitmap bmp = ImageUtils.getSmallBitmap(picturePath);
                // 获取图片并显示
                heaer.setImageBitmap(bmp);
                // zqRoundOvalImageView.setImageBitmap(bmp);
                saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(context) + "/stscname.jpg");
                // 修改头像
                //uploadHeadImage(DemoApplication.getInstance().getMyToken(), headFile);
            } catch (Exception e) {
                e.printStackTrace();
                //showToastShort("上传失败");
            }
        }
    }

    public void saveBitmapFile(Bitmap bitmap, String path) {
        headFile = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(headFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改昵称
    private void showNickDialog() {
        DialogUIUtils.showAlert(context, "修改昵称", "", "请输入昵称", "", "修改", "取消", false, true, true, new DialogUIListener() {
            @Override
            public void onPositive() {

            }

            @Override
            public void onNegative() {

            }

            @Override
            public void onGetInput(CharSequence input1, CharSequence input2) {
                super.onGetInput(input1, input2);
                if (input1 != null && !StringUtil.isNullOrEmpty(input1.toString())) {
                    tv_Nick.setText(input1);
                    name = input1.toString();
                }
            }
        }).show();
    }

    // 修改性别
    private void showSexDialog() {
        List<TieBean> strings = new ArrayList<TieBean>();
        strings.add(new TieBean("男"));
        strings.add(new TieBean("女"));
        strings.add(new TieBean("取消"));
//        strings.add(new TieBean("其他"));
        DialogUIUtils.showSheet(context, strings, "", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                //showToast(text);
                // text即为所修改的性别
                if (text != null && !"其他".equals(text.toString())) {
                    tv_Sex.setText(text);
                    sex = text.toString();
                }
            }
        }).show();
    }

    // 修改生日
    private void showBertherDayDialog() {
        DialogUIUtils.showDatePick(context, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
            @Override
            public void onSaveSelectedDate(int tag, String selectedDate) {
                // selectedDate为参数修改生日
                if (!StringUtil.isNullOrEmpty(selectedDate)) {
                    tv_Bd.setText(selectedDate);
                    birthday = selectedDate;
                }
            }
        }).show();
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    private void getUserInfo() {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getUserInfo(token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 绘制页面
                                            userModel = JSON.parseObject(jsonObject.getString("data"), UserModel.class);
                                            drawView(userModel);
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                DialogUIUtils.showToast(response.message());
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast("网络不给力");
                    }
                });
    }

    private void drawView(UserModel model) {
        name = model.getName();
        sex = model.getSex();
        birthday = model.getBirthday();
        tv_Nick.setText(name);
        tv_Sex.setText(sex);
        tv_Bd.setText(birthday);
        Glide.with(context).load(model.getHeadImage()).into(heaer);
    }

    /**
     * 修改用户信息
     */
    public static final String MULTIPART_FORM_DATA = "image/jpg";  // 上传的文件格式

    private void updateUserInfo() {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Map<String, RequestBody> params = new HashMap<>();
        if (!StringUtil.isNullOrEmpty(name)) {
            params.put("name", convertToRequestBody(name));
        }
        if (!StringUtil.isNullOrEmpty(sex)) {
            params.put("sex", convertToRequestBody(sex));
        }
        if (!StringUtil.isNullOrEmpty(birthday)) {
            params.put("birthday", convertToRequestBody(birthday));
        }
        RequestBody requestFile = null;
        if (headFile != null) {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
            params.put("headImage\"; filename=\"" + headFile.getName(), requestFile);
        }
        RetrofitHttp.getRetrofit(builder.build()).updateUserInfo(token, params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()){
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        //response.errorBody().string();
                                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("修改用户信息返回结果：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 绘制页面
                                            drawView(JSON.parseObject(jsonObject.getString("data"), UserModel.class));
                                            finish();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                try {
                                    DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast("网络不给力");
                    }
                });
    }


    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    private RequestBody convertToRequestBody1(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        return requestBody;
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /**
     * 提交更改
     *
     * @param v
     */
    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        updateUserInfo();
    }
}
