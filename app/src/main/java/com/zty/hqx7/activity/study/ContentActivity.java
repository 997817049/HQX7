package com.zty.hqx7.activity.study;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.alibaba.fastjson.JSONObject;
import com.zty.hqx7.R;
import com.zty.hqx7.util.WebViewUtil;

/**
 * 读取基地内容和study的具体内容
 * */
public class ContentActivity extends AppCompatActivity {
    private static JSONObject para;
    private WebView webView;

    public static void setPara(JSONObject para) {
        ContentActivity.para = para;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        dealBarColor();
        webView = findViewById(R.id.content_webview);
        //设置webview
        WebViewUtil.dealWebView(webView);
        //导入html
        webView.loadUrl(para.getString("htmlUrl"));
        webView.addJavascriptInterface(new ContentJSInterface(), "android");
        System.out.println("导入content界面: " + para.get("model") + "-" + para.get("part") + "-" + para.get("id"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView = WebViewUtil.destroyWebView(webView);
    }

    private void dealBarColor(){
        //除exam为黄色 其余为白色
        if(para.get("model").equals("study") && para.get("part").equals("exam")){
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        } else {
            getWindow().setStatusBarColor(Color.WHITE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(para.get("model").equals("study") && para.get("part").equals("book")){
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    webView.loadUrl("javascript:subPageTurnSpeed()");
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    webView.loadUrl("javascript:addPageTurnSpeed()");
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class ContentJSInterface {
        @JavascriptInterface
        public void finish(){
            ContentActivity.this.finish();
        }

        @JavascriptInterface
        public String getPara(){
            return ContentActivity.para.toJSONString();
        }
    }
}


