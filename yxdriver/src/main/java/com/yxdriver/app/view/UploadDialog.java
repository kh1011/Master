package com.yxdriver.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yxdriver.app.R;

/**
 * Created by mac on 2017/9/12.
 * 自定义弹出框
 */

public class UploadDialog extends Dialog {
    private Context context;
    private int res;// 示例图
    private String title;// 按钮标题
    public ImageView imageView;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doUpload();
    }

    public UploadDialog(Context context, int res, String title) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.res = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.photo_demo, null);
        setContentView(view);
        imageView = (ImageView) view.findViewById(R.id.photo_img);
        Button button = (Button) view.findViewById(R.id.photo_btn);
        button.setText(title);
        imageView.setImageResource(res);
        button.setOnClickListener(new clickListener());
        imageView.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        assert lp != null;
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        lp.height=(int)(d.heightPixels*0.8);
        // 设置透明度
        lp.alpha = 0.6f;
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.photo_btn:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.photo_img:
                    clickListenerInterface.doUpload();
                    break;
            }
        }
    }

    /**
     * add by zhangyun 2017年9月20日 20:58:26 start
     *
     */
    /**
     * 获取imageview 用于测试加载图片
     * @return
     */
    public ImageView getImageView(){
        return imageView;
    }
    /**
     * add by zhangyun 2017年9月20日 20:58:26 end
     *
     */
}
