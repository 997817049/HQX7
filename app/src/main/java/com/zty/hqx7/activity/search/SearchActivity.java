package com.zty.hqx7.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.news.ArticleActivity;
import com.zty.hqx7.activity.study.ContentActivity;
import com.zty.hqx7.model.User;
import com.zty.hqx7.utils.SharedPreUtil;
import com.zty.hqx7.utils.WebViewUtil;

/**
 * 获取全部查找内容
 * */
public class SearchActivity extends AppCompatActivity {
    private static final String rootPath = "file:///android_asset/htmls/";
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.content_webview);
        //设置webview
        WebViewUtil.dealWebView(webView);
        //导入html
        webView.loadUrl(rootPath + "search/search.html");
        System.out.println("导入search界面");
        webView.addJavascriptInterface(new SearchJSInterface(), "android");
    }

    private class SearchJSInterface {
        @JavascriptInterface
        public void toContent(String htmlUrl, String model, String part, String id){
            JSONObject obj = new JSONObject();
            if(model.equals("base") || model.equals("news")){
                obj.put("htmlUrl",htmlUrl);
            } else {
                obj.put("htmlUrl",rootPath + htmlUrl);
            }
            obj.put("model", model);
            obj.put("part", part);
            obj.put("id", Integer.valueOf(id));
            System.out.println(obj);
            ContentActivity.setPara(obj);
            Intent intent = new Intent(SearchActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void toArticle(String htmlUrl, int aid) {
            ArticleActivity.setHtmlUrl(htmlUrl);
            ArticleActivity.setArtId(aid);
            startActivity(new Intent(SearchActivity.this, ArticleActivity.class));
        }

        @JavascriptInterface
        public void getMore(String model, String part, String word){
            JSONObject obj = new JSONObject();
            obj.put("model", model);
            obj.put("part", part);
            obj.put("word", word);
            SearchContentActivity.setPara(obj);
            Intent intent = new Intent(SearchActivity.this, SearchContentActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void finish(){
            SearchActivity.this.finish();
        }

        @JavascriptInterface
        public int getUserId() {
            String userStr = (String) SharedPreUtil.getParam(SearchActivity.this, SharedPreUtil.LOGIN_DATA, "");
            User user = JSON.parseObject(userStr, User.class);
            return user.getId();
        }

    }
}
