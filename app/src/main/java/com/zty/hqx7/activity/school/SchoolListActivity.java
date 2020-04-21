package com.zty.hqx7.activity.school;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.zty.hqx7.R;
import com.zty.hqx7.model.Article;
import com.zty.hqx7.utils.MyDatabaseHelper;
import com.zty.hqx7.utils.WebViewUtil;

public class SchoolListActivity extends Activity {
    private WebView webView = null;
    protected static MyDatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.content_webview);
        WebViewUtil.dealWebView(webView);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/schoolActivity.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
        databaseHelper = new MyDatabaseHelper(SchoolListActivity.this, "articls", null, 1);
    }

    public final class JSInterface {

        @JavascriptInterface
        public void goSchoolActivity(String title, String initiator, String detail, String when,
                                     String where, String pic1, String pic2, String pic3, int id) {
            SchoolDetailActivity.id = id;
            SchoolDetailActivity.title = title;
            SchoolDetailActivity.initiator = initiator;
            SchoolDetailActivity.detail = detail;
            SchoolDetailActivity.when = when;
            SchoolDetailActivity.where = where;
            SchoolDetailActivity.pic1 = pic1;
            SchoolDetailActivity.pic2 = pic2;
            SchoolDetailActivity.pic3 = pic3;
            startActivity(new Intent(SchoolListActivity.this, SchoolDetailActivity.class));
        }
        @JavascriptInterface
        public void saveArticle(String part, String articleJson) {
            Article article = JSON.parseObject(articleJson, Article.class);

            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", article.getId());
            values.put("title", article.getTitle());
            values.put("type", article.getType());
            values.put("htmlUrl", article.getHtmlUrl());
            values.put("picUrl", article.getPicUrl());
            values.put("count", article.getCount());
            values.put("recommend", article.getRecommend());
            values.put("date", JSON.toJSONString(article.getDate()));
            database.insert(part, null, values);
        }
    }
}
