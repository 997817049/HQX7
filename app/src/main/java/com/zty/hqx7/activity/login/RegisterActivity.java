package com.zty.hqx7.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.zty.hqx7.R;
import com.zty.hqx7.util.MyDatabaseHelper;
import com.zty.hqx7.util.WebViewUtil;

public class RegisterActivity extends Activity {
    private MyDatabaseHelper dbHelper;

    private WebView webView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);
        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.content_webview);
        WebViewUtil.dealWebView(webView);
        webView.loadUrl( "javascript:window.location.reload( true )" );
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/login/register.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
    }

    public final class JSInterface {
        @JavascriptInterface
        public void register() {

//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put("id", id);
//            values.put("mail", mail);
//
//            db.insert("User", null, values);

            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            finish();
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
