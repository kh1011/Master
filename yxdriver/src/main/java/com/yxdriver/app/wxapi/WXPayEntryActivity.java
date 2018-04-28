package com.yxdriver.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.YXRechargeActivity;


/**
 * User: fee(1176610771@qq.com)
 * Date: 2016-07-05
 * Time: 13:49
 * DESC: 微信分享需要接收结果的界面
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, "wx9dd4888fdaa2fc89");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(getApplicationContext(),"onReq",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResp(BaseResp resp) {
        //Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT).show();

        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code, Toast.LENGTH_SHORT).show();
        }

        String result = "";

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "支付成功";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                result = "支付错误";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();

        Intent mIntent = new Intent(YXRechargeActivity.RECEIVER_ACTION);
        mIntent.putExtra("resultCode", resp.errCode);

        //发送广播
        sendBroadcast(mIntent);
    }
}
