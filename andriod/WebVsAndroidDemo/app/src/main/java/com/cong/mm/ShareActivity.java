package com.cong.mm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.cong.mm.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareActivity extends AppCompatActivity {
    private WebView webView;
    private String url = "http://aidenzou.github.io/Hybrid-APP/share.html";
    //    private String url = "https://github.com/aidenzou/Hybrid-APP/blob/master/webapp/example.html";
//    private String url = "www.baidu.com";
//    private String url = "http://beta.html5test.com/";
    private MyJavaScriptInterface myJavaScriptInterface = new MyJavaScriptInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        webView = (WebView) findViewById(R.id.webview);
        enableJavaScript();
        addWebViewClient();
        addJavaScriptInterface();
        loadUrl();
        registShareBroadcast();
    }

    private void enableJavaScript() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(false);
    }

    private void addJavaScriptInterface() {
        /**js调用Android*/
        webView.addJavascriptInterface(myJavaScriptInterface, "bridge");
    }

    private void addWebViewClient() {
        /**如果是WebViewClient则调用alert方法是无效的*/
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                /**message是alert的内容*/
//                Toast.makeText(ShareActivity.this, message, Toast.LENGTH_LONG).show();
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Toast.makeText(ShareActivity.this, "onJsPrompt", Toast.LENGTH_LONG).show();
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Toast.makeText(ShareActivity.this, "onJsConfirm", Toast.LENGTH_LONG).show();
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                Toast.makeText(ShareActivity.this, "onJsBeforeUnload", Toast.LENGTH_LONG).show();
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public boolean onJsTimeout() {
                Toast.makeText(ShareActivity.this, "onJsTimeout", Toast.LENGTH_LONG).show();
                return super.onJsTimeout();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                /**必须在url加载完成之后才可以调用到js方法*/
                if (newProgress >= 100) {
                    /**js能够接收json对象*/
                    String json = "{\"key\":\"i am android\"}";
                    /**调用js方法*/
//                    webView.loadUrl("javascript:callFromActivity(" + json + ")");
                }
                super.onProgressChanged(view, newProgress);
            }
        });

//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");
//                view.loadUrl("javascript:callFromActivity(\"+我是Shen+\")");
//            }
//        });
    }

    private void loadUrl() {
        webView.loadUrl(url);
        /**即使url没有加载完成也可以调用到Android方法，可以在js中声明该方法，这样可以达到分享的目的*/
//        webView.loadUrl("javascript:window.CallToAnAndroidFunction.setVisible()");
    }

    /**
     * Android注册的js方法，被js调用
     */
    class MyJavaScriptInterface {
        Object nObject = new Object();
        private String result;

        /**
         * api 17 or higher 必须要用@JavascriptInterface
         * 因为这个接口允许JavaScript 控制宿主应用程序，这是个很强大的特性，但同时，在4.2的版本前存在重大安全隐患，
         * 因为JavaScript 可以使用反射访问注入webview的java对象的public fields，在一个包含不信任内容的WebView中使用这个方法，
         * 会允许攻击者去篡改宿主应用程序，使用宿主应用程序的权限执行java代码。因此4.2以后，任何为JS暴露的接口，都需要加
         *
         * @param msg js调用Android不能传json对象，会出现undefined错误，需要将字符串转化成json
         * @JavascriptInterface 注释，这样，这个Java对象的fields 将不允许被JS访问。
         */
        @JavascriptInterface
        public String callHandler(final String msg) {
            String value = null;
            try {
                JSONObject jsonObject = new JSONObject(msg);
                value = jsonObject.getString("title");
            } catch (JSONException e) {
                Log.e("jsonexception", e.getLocalizedMessage());
            }
            final String s = value;
            share();
            //线程等待分享
            synchronized (nObject) {
                try {
                    nObject.wait();
                } catch (InterruptedException e) {
                    result = e.getLocalizedMessage();
                }
            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (!TextUtils.isEmpty(s)){
//                        String json = "{\"key\":\"share success\"}";
//                        webView.loadUrl("javascript:shareSuccess(" + json + ")");
//                    }
////                    Toast.makeText(ShareActivity.this, s, Toast.LENGTH_LONG).show();
//                    webView.setVisibility(View.VISIBLE);
//                }
//            });
            return result;
        }
    }

    private void share() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (WXEntryActivity.isWxInstalled(ShareActivity.this) == false) {
                    notifyJsComplete("微信没有被安装");
                    return;
                }
                ShareContent sharecontent = new ShareContent();
                sharecontent.setImageId(R.mipmap.ic_launcher);
                sharecontent.setTargetUrl("https://youcai.shequcun.com/?state=recomitem/1");
                sharecontent.setTitle("有菜，不能说的秘密！");
                sharecontent.setContent("孩子的餐桌我们的标准，走心，连蔬菜都这么有bigger！");
                ShareManager.shareByFrame(ShareActivity.this, sharecontent, new ShareManager.CancelOnTouchOutside() {
                    @Override
                    public void onCancel() {
                        notifyJsComplete("分享取消");
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(shareBroadcastReceiver);
        //释放js
        notifyJsComplete(null);
    }

    /**
     * 唤醒js线程
     */
    private void notifyJsComplete(String result) {
        myJavaScriptInterface.result = result;
        if (myJavaScriptInterface.nObject != null)
            synchronized (myJavaScriptInterface.nObject) {
                myJavaScriptInterface.nObject.notifyAll();
            }
    }

    private void registShareBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXEntryActivity.BROADCAST_SHARE);
        registerReceiver(shareBroadcastReceiver, filter);
    }

    private BroadcastReceiver shareBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            String result = intent.getStringExtra("result");
            int errCode = intent.getIntExtra("errCode", 0);
            Toast.makeText(ShareActivity.this, result + errCode, Toast.LENGTH_LONG).show();
            notifyJsComplete(result);
        }
    };
}
