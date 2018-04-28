package com.yxdriver.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;

import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.PostArticleImgAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.listener.MyCallBack;
import com.yxdriver.app.listener.OnRecyclerItemClickListener;
import com.yxdriver.app.utils.StringUtil;
import com.yxdriver.app.utils.UtilImags;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.GridAdapter;

import static com.yxdriver.app.R.id.gridView;
import static com.yxdriver.app.R.id.imageView;

/**
 * 功能：发布二手车信息
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXPublishCarActivity extends BaseActivity {
    public static final String FILE_DIR_NAME = "com.yxdriver.app";//应用缓存地址
    public static final String FILE_IMG_NAME = "images";//放置图片缓存
    public static final int IMAGE_SIZE = 9;//可添加图片最大数
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;


    private ArrayList<String> imagePaths = new ArrayList<>();//原始图片
    private ArrayList<String> newimagePaths = new ArrayList<>();//压缩长宽后图片
    private GridAdapter gridAdapter;
    // 图片存放位置
    @BindView(R.id.rcv_img)
    GridView gridView;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.et_content)
    EditText ed_Content;
    @BindView(R.id.price)
    EditText ed_Price;
    @BindView(R.id.et_title)
    EditText ed_title;
    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context, ArrayList<String> data) {
        return new Intent(context, YXPublishCarActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.yx_old_car_push_activity);
        ButterKnife.bind(this);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs)) {
                    PhotoPickerIntent intent = new PhotoPickerIntent(YXPublishCarActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(9); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                } else {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(YXPublishCarActivity.this);
                    intent.setCurrentItem(position);
                    if (imagePaths.get(imagePaths.size()-1).contains("000000")){
                        imagePaths.remove(imagePaths.remove("000000"));
                        intent.setPhotoPaths(imagePaths);
                    }else {
                        intent.setPhotoPaths(imagePaths);
                    }
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        //
    }


    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if (listUrls.size() == 10) {
                listUrls.remove(listUrls.size() - 1);
            }
            inflater = LayoutInflater.from(YXPublishCarActivity.this);
        }

        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image_, parent, false);
                holder.image = (ImageView) convertView.findViewById(imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String path = listUrls.get(position);
            if (path.equals("000000")) {
                holder.image.setImageResource(R.drawable.add_picture);
            } else {
               /* Glide.with(CaseLoadActivity.this)
                        .load(path)
                        //.placeholder(R.drawable.xx)
                        //.error(R.drawable.xx)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);*/
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(YXPublishCarActivity.this).load(path).apply(options).into(holder.image);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView image;
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains("000000")) {
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        try {
            //JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", imagePaths.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    final ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d("ListExtra", "list: " + "list = [" + list.size());
                    for (int i=0;i<list.size();i++){
                        final int finalI = i;
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
                        newimagePaths.clear();
                        Tiny.getInstance().source(list.get(i)).asFile().withOptions(compressOptions).compress(new FileCallback() {
                            @Override
                            public void callback(boolean isSuccess, String outfile, Throwable t) {
                                if (!isSuccess) {
                                    Log.e("zxy", "error: " + t.getMessage());
                                    //mCompressTv.setText("compress file failed!");
                                    return;
                                }
                                //File file = new File(outfile);
                                newimagePaths.add(outfile);
                                if (newimagePaths.size()==list.size()){
                                    loadAdpater(newimagePaths);
                                }

                                //setupCompressInfo(outfile, file.length());
                            }
                        });
                    }
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d("ListExtra", "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }
    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);

        // 发布
        String content = ed_Content.getText().toString();
        String price = ed_Price.getText().toString();
        String title= ed_title.getText().toString();
        if (com.yxdriver.app.utils.StringUtil.isNullOrEmpty(content)) {
            DialogUIUtils.showToast("请输入信息内容");
        } else if (StringUtil.isNullOrEmpty(title)) {
            DialogUIUtils.showToast("请输入主题");
        } else if (StringUtil.isNullOrEmpty(price)) {
            DialogUIUtils.showToast("请输入备注信息");
        } else {
            Map<String, RequestBody> params = new HashMap<>();
            params.put("title", convertToRequestBody(title));
            params.put("content", convertToRequestBody(content));
            params.put("note", convertToRequestBody(price));
            for (int i = 0; i < newimagePaths.size(); i++) {
                if (i != newimagePaths.size() - 1) {
                    // 过滤最后一张图片（加号图）
                    File file = new File(newimagePaths.get(i));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    params.put("images\"; filename=\"" + file.getName(), requestFile);
                }
            }
            publishData(DemoApplication.getInstance().getMyToken(), params);
        }

    }

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }


    //------------------图片相关-----------------------------





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //动态申请权限
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.CAMERA",

        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    /**
     * 发布信息
     *
     * @param token
     * @param params
     */
    private void publishData(String token, Map<String, RequestBody> params) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).publishCarInfo(token, params).enqueue(new Callback<ResponseBody>() {
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
                                System.out.println("发布同城信息:" + result);
                                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    // 发布信息成功
                                    onBackPressed();
                                    finish();
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            }
                        } catch (IOException e) {
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
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }
}