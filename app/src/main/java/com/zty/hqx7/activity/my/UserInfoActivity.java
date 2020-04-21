package com.zty.hqx7.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.widget.TextView;
import com.zty.hqx7.R;
import com.zty.hqx7.utils.WebViewUtil;
import com.zty.hqx7.utils.SharedPreUtil;
import com.zty.hqx7.view.IconView;

public class UserInfoActivity extends Activity {
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
        webView.loadUrl("file:///android_asset/htmls/my/userdata.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
    }


    private void findElement() {
        webView = findViewById(R.id.my_webview);
        back = findViewById(R.id.my_back);
        title = findViewById(R.id.my_title);
        edit = findViewById(R.id.my_edit);
    }

    private void dealElement(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });
        title.setText("个人信息");
        edit.setVisibility(View.VISIBLE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, UserEditActivity.class));
            }
        });
    }

    public final class JSInterface {
        @JavascriptInterface
        public void edit() {
            startActivity(new Intent(UserInfoActivity.this, UserEditActivity.class));
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public String getUser() {
            return  (String) SharedPreUtil.getParam(UserInfoActivity.this, SharedPreUtil.LOGIN_DATA, "");
        }

        @JavascriptInterface
        public void back() {
//            startActivity(new Intent(UserInfoActivity.this, MeActivity.class));
//            // 去除滑动动画效果
//            overridePendingTransition(0, 0);
            UserInfoActivity.this.finish();
        }
    }
}
