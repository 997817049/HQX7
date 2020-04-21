package com.zty.hqx7.activity.my;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.widget.TextView;
import com.zty.hqx7.R;
import com.zty.hqx7.service.UpdateManager;
import com.zty.hqx7.utils.AppUtil;
import com.zty.hqx7.utils.WebViewUtil;
import com.zty.hqx7.view.IconView;

public class AboutActivity extends Activity {
    private WebView webView;
    private TextView title;
    private IconView back;
    private IconView edit;

    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findElement();
        dealElement();
        WebViewUtil.dealWebView(webView);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/my/about.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        //Android 6.0以上版本需要临时获取权限
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1&&
                PackageManager.PERMISSION_GRANTED!=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            requestPermissions(perms,PERMS_REQUEST_CODE);
        }
    }

    private void findElement() {
        webView = findViewById(R.id.my_webview);
        back = findViewById(R.id.my_back);
        title = findViewById(R.id.my_title);
        edit = findViewById(R.id.my_edit);
    }

    private void dealElement(){
        edit.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
        title.setText("关于");
    }

    public final class JSInterface {
        @JavascriptInterface
        public void back() {
//            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public void check() {
            new UpdateManager(AboutActivity.this).checkUpdate(1);
        }

        @JavascriptInterface
        public int getVersonCode() {
            return AppUtil.getVersionCode(AboutActivity.this);
        }

        @JavascriptInterface
        public String getVersonName() {
            return AppUtil.getVersionName(AboutActivity.this);
        }
    }
}
