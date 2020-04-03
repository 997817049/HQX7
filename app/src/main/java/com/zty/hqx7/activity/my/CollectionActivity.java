package com.zty.hqx7.activity.my;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.news.ArticleActivity;
import com.zty.hqx7.model.User;
import com.zty.hqx7.util.MyDatabaseHelper;
import com.zty.hqx7.util.WebViewUtil;
import com.zty.hqx7.util.SharedPreUtil;
import com.zty.hqx7.ztyClass.IconView;

public class CollectionActivity extends Activity {
    private WebView webView;
    private TextView title;
    private IconView back;
    private IconView edit;

    protected static MyDatabaseHelper databaseHelper = null;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findElement();
        dealElement();
        WebViewUtil.dealWebView(webView);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/my/collection.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        //databaseHelper = new MyDatabaseHelper(CollectionActivity.this, "articls", null, 1);
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
                CollectionActivity.this.finish();
            }
        });
        title.setText("我的收藏");
    }

    public final class JSInterface {

        @JavascriptInterface
        public void toReturn() {
            CollectionActivity.this.finish();
        }

        @JavascriptInterface
        public void toArticle(String htmlUrl, int aid) {
            ArticleActivity.setHtmlUrl(htmlUrl);
            ArticleActivity.setArtId(aid);
            startActivity(new Intent(CollectionActivity.this, ArticleActivity.class));
        }

        @JavascriptInterface
        public int getUid() {
            String userStr = (String) SharedPreUtil.getParam(CollectionActivity.this, SharedPreUtil.LOGIN_DATA, "");
            if (!userStr.equals("")) {
                User user = JSON.parseObject(userStr, User.class);
                if (user != null) {
                    return user.getId();
                }
            }
            return -1;
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(CollectionActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
