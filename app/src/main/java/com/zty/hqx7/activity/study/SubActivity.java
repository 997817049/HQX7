package com.zty.hqx7.activity.study;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zty.hqx7.R;
import com.zty.hqx7.model.User;
import com.zty.hqx7.utils.AddressUtil;
import com.zty.hqx7.utils.SharedPreUtil;
import com.zty.hqx7.utils.WebViewUtil;
import com.zty.hqx7.view.IconView;

/**
 * 查看分类子模块
 * */
public class SubActivity extends AppCompatActivity {
    private static final String htmlPath = "file:///android_asset/htmls/";
    private static JSONObject para;
    private RefreshLayout refreshLayout;//刷新布局
    private WebView webView;
    private IconView back;

    public static void setPara(JSONObject para) {
        SubActivity.para = para;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        findElement();
        dealAllElement();
        dealBarColor();
        //设置刷新布局
        WebViewUtil.dealRefreshLayout(SubActivity.this, webView, refreshLayout);
        //设置webview
        WebViewUtil.dealWebView(webView);
        //导入html
        webView.loadUrl(para.getString("htmlUrl"));
        webView.addJavascriptInterface(new SubActivity.SubStudyJSInterface(), "android");
        System.out.println("导入subStudy界面: " + para.get("model") + "-" + para.get("part") + "-" + para.get("sub"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView = WebViewUtil.destroyWebView(webView);
    }

    private void dealBarColor(){
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void findElement() {
        webView = findViewById(R.id.sub_study_webview);
        back = findViewById(R.id.study_sub_back);
        refreshLayout = findViewById(R.id.sub_study_refresh_layout);
    }

    private void dealAllElement() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubActivity.this.finish();
            }
        });
        String title = (String) para.get("title");
        TextView titleView = findViewById(R.id.study_sub_title);
        titleView.setText(title);
        String model = (String) para.get("model");
        String sub = (String) para.get("sub");
        if(model.equals("base")
            || (model.equals("study") && (sub.equals("hot") || sub.equals("recommend") || sub.equals("recent")))){
            refreshLayout.setEnableLoadMore(false);
        }
    }

    private class SubStudyJSInterface {
        @JavascriptInterface
        public String getPara(){
            return SubActivity.para.toJSONString();
        }

        @JavascriptInterface
        public void toContent(String htmlUrl, String model, String part, String id){
            JSONObject obj = new JSONObject();
            if(model.equals("base")){
                obj.put("htmlUrl", htmlUrl);
            } else {
                obj.put("htmlUrl", htmlPath + htmlUrl);
            }
            obj.put("model", model);
            obj.put("part", part);
            obj.put("id", Integer.valueOf(id));
            ContentActivity.setPara(obj);
            Intent intent = new Intent(SubActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void loadNoMoreData(){
            refreshLayout.finishLoadMoreWithNoMoreData();
        }

        @JavascriptInterface
        public void finish(){
            SubActivity.this.finish();
        }

        @JavascriptInterface
        public int getUserId() {
            String userStr = (String) SharedPreUtil.getParam(SubActivity.this, SharedPreUtil.LOGIN_DATA, "");
            User user = JSON.parseObject(userStr, User.class);
            return user.getId();
        }

        @JavascriptInterface
        public String getAddress(){
            return AddressUtil.getInstance().getLocations(SubActivity.this);
        }
    }
}
