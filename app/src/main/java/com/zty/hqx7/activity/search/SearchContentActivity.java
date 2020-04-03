package com.zty.hqx7.activity.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.study.ContentActivity;
import com.zty.hqx7.util.WebViewUtil;
import com.zty.hqx7.ztyClass.IconView;

/**
 * 具体看某一项的查找内容
 * */
public class SearchContentActivity extends AppCompatActivity {
    private static final String htmlPath = "file:///android_asset/htmls/search/searchContent.html";
    private static JSONObject para;
    private RefreshLayout refreshLayout;//刷新布局
    private WebView webView;
    private IconView back;
    private TextView titleView;

    public static void setPara(JSONObject para) {
        SearchContentActivity.para = para;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        findElement();
        dealAllElement();
        dealBarColor();
        //设置刷新布局
        WebViewUtil.dealRefreshLayout(SearchContentActivity.this, webView, refreshLayout);
        //设置webview
        WebViewUtil.dealWebView(webView);
        //导入html
        webView.loadUrl(htmlPath);
        webView.addJavascriptInterface(new SearchContentJSInterface(), "android");
        System.out.println("导入subStudy界面:" + htmlPath);
    }

    private void dealBarColor(){
        getWindow().setStatusBarColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void findElement() {
        webView = findViewById(R.id.sub_study_webview);
        back = findViewById(R.id.study_sub_back);
        titleView = findViewById(R.id.study_sub_title);
        refreshLayout = findViewById(R.id.sub_study_refresh_layout);
    }

    private void dealAllElement() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchContentActivity.this.finish();
            }
        });
        String part = (String) para.get("part");
        if(part.equals("base")){
            titleView.setText("基地查找");
            return;
        }
        String sub = (String) para.get("sub");
        String title = "";
        switch (sub){
            case "exam": title = "答题"; break;
            case "book": title = "书籍"; break;
            case "teleplay": title = "电视"; break;
            case "film": title = "电影"; break;
            case "variety": title = "综艺"; break;
            case "documentary": title = "纪录片"; break;
            case "drama": title = "答题"; break;
        }
        titleView.setText(title + "查找");
    }

    private class SearchContentJSInterface {
        @JavascriptInterface
        public String getPara(){
            return SearchContentActivity.para.toJSONString();
        }

        @JavascriptInterface
        public void toContent(String htmlUrl, String model, String part, String id){
            JSONObject obj = new JSONObject();
            obj.put("htmlUrl", htmlUrl);
            obj.put("model", model);
            obj.put("part", part);
            obj.put("id", Integer.valueOf(id));
            ContentActivity.setPara(obj);
            Intent intent = new Intent(SearchContentActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void loadNoMoreData(){
            refreshLayout.finishLoadMoreWithNoMoreData();
        }

        @JavascriptInterface
        public void finish(){
            SearchContentActivity.this.finish();
        }
    }
}
