package com.zty.hqx7.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.MainActivity;
import com.zty.hqx7.activity.login.LoginActivity;
import com.zty.hqx7.utils.WebViewUtil;
import com.zty.hqx7.utils.SharedPreUtil;
import com.zty.hqx7.view.IconView;

public class SetActivity extends Activity {
    private WebView webView;
    private TextView title;
    private IconView back;
    private IconView edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findElement();
        dealElement();
        WebViewUtil.dealWebView(webView);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/my/set.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
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
                SetActivity.this.finish();
            }
        });
        title.setText("设置");
    }

    public final class JSInterface {
        @JavascriptInterface
        public void back() {
//            startActivity(new Intent(SetActivity.this, UserInfoActivity.class));
//            // 去除滑动动画效果
//            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public void about() {
            startActivity(new Intent(SetActivity.this, AboutActivity.class));
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
        }

        @JavascriptInterface
        public void exit() {
            startActivity(new Intent(SetActivity.this, LoginActivity.class));
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            SharedPreUtil.removeParam(SetActivity.this, SharedPreUtil.IS_LOGIN);
            SharedPreUtil.removeParam(SetActivity.this, SharedPreUtil.LOGIN_DATA);
            finish();
            MainActivity.finishMain();
        }


        @JavascriptInterface
        public void setUser(String userStr) {
            SharedPreUtil.setParam(SetActivity.this, SharedPreUtil.LOGIN_DATA, userStr);
            Toast.makeText(SetActivity.this, "用户信息更新完成", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SetActivity.this, UserInfoActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

    }
}
