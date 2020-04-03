package com.zty.hqx7.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.MainActivity;
import com.zty.hqx7.model.User;
import com.zty.hqx7.util.MyDatabaseHelper;
import com.zty.hqx7.util.WebViewUtil;
import com.zty.hqx7.util.SharedPreUtil;
import com.zty.hqx7.util.TimeCount;

public class LoginActivity extends Activity {
    private WebView webView = null;

    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);
        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.content_webview);
        WebViewUtil.dealWebView(webView);
        webView.loadUrl( "javascript:window.location.reload( true )" );
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/login/login.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
    }

    public final class JSInterface {
        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void loginSuccess(String userinfo) {

            // 初始化用户信息
            User user = JSONObject.parseObject(userinfo, User.class);
            if (user.getNick() == null || user.getNick().equals("")) {
                user.setNick("小红旗-" + user.getId());
            }
            if (user.getShowing() == null || user.getShowing().equals("")) {
                user.setShowing("此人很懒，暂无个性签名");
            }
            userinfo = JSONObject.toJSONString(user);
            // 缓存用户信息(json字符串)
            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.IS_LOGIN, true);
            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.LOGIN_DATA, userinfo);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            TimeCount.getInstance().setTime(System.currentTimeMillis());
            startActivity(intent);
            finish();
        }


        @JavascriptInterface
        public void toRegister() {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void login(String mail) {

            // 登录成功后保存用户身份
//            SQLiteDatabase db = databaseHelper.getReadableDatabase();
//            Cursor cursor = db.rawQuery("select * from User where name=?", new String[]{mail});
//
//            if (cursor.moveToFirst()) {
//                String dbPass = cursor.getString(cursor.getColumnIndex("password"));
//
//            }
        }
    }
}
