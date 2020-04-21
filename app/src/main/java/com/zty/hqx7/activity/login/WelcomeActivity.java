package com.zty.hqx7.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.MainActivity;
import com.zty.hqx7.utils.WebViewUtil;
import com.zty.hqx7.utils.SharedPreUtil;

public class WelcomeActivity extends Activity {

    private WebView webView = null;

    public WelcomeActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        // 定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        // 获得当前窗体对象
        Window window = WelcomeActivity.this.getWindow();
        // 设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.content_webview);
        WebViewUtil.dealWebView(webView);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/login/welcome.html");

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    // 检查用户是否登录 是：直接首页  否：登录页
                    boolean isLogin = (boolean) SharedPreUtil.getParam
                            (WelcomeActivity.this, SharedPreUtil.IS_LOGIN, false);
                    if (isLogin) {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    overridePendingTransition(0, 0);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }



    //由于安全原因 需要加 @JavascriptInterface
//    @JavascriptInterface
//    public void startFunction(){
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(WelcomeActivity.this,"Toast",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    @JavascriptInterface
//    public void startFunction(final String text){
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                new AlertDialog.Builder(WelcomeActivity.this).setMessage(text).show();
//
//            }
//        });
//
//
//    }
}
