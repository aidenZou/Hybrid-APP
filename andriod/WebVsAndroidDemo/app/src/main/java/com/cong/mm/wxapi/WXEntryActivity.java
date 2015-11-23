package com.cong.mm.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cong.mm.ShareManager;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends FragmentActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "WXEntryActivity onCreate", Toast.LENGTH_LONG).show();
        api = WXAPIFactory.createWXAPI(this, ShareManager.appId, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        Toast.makeText(this, "onNewIntent", Toast.LENGTH_LONG).show();
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
//        Toast.makeText(this, "onReq", Toast.LENGTH_LONG).show();
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        finish();
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送失败-2";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                result = "一般错误";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                result = "发送失败-3";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "不支持错误";
                break;
            default:
                result = "发送被返回";
                break;
        }
        sendBroadcast(this, result, resp.errCode);
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    public static final String BROADCAST_SHARE = "broadcast_share";

    private void sendBroadcast(Context context, String result, int errCode) {
        Intent intent = new Intent();
        intent.putExtra("result", result);
        intent.putExtra("errCode", errCode);
        intent.setAction(BROADCAST_SHARE);
        context.sendBroadcast(intent);
    }

}
