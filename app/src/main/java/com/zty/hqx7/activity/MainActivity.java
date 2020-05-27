package com.zty.hqx7.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.my.CollectionActivity;
import com.zty.hqx7.activity.my.SetActivity;
import com.zty.hqx7.activity.my.UserInfoActivity;
import com.zty.hqx7.activity.news.ArticleActivity;
import com.zty.hqx7.activity.school.SchoolDetailActivity;
import com.zty.hqx7.activity.search.SearchActivity;
import com.zty.hqx7.activity.study.ContentActivity;
import com.zty.hqx7.activity.study.SubActivity;
import com.zty.hqx7.model.Article;
import com.zty.hqx7.model.User;
import com.zty.hqx7.service.UpdateManager;
import com.zty.hqx7.utils.*;
import com.zty.hqx7.view.IconView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String htmlPath = "file:///android_asset/htmls/";
    private RadioGroup modelRg;//模块按钮组
    private RadioGroup studySubRg;//study模块的子模块按钮组
    private HorizontalScrollView studySelect;//study模块的子模块按钮组
    private TextView baseLabel;//base模块的头部标签  红色基地
    private RefreshLayout mainRefreshLayout;//刷新布局
    private WebView mainWebView;//主体内容
    private IconView search;//查询按钮

    private TextView myTxtView;
    private TextView baseTxtView;
    private TextView homeTxtView;
    private TextView studyTxtView;
    private TextView schoolTxtView;

    private String para;

    private static MyDatabaseHelper databaseHelper = null;
    private static MainActivity mainActivity;

    private static boolean isChecked = false;
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findElement();
        dealElement();
        //设置刷新布局
        WebViewUtil.dealRefreshLayout(MainActivity.this, mainWebView, mainRefreshLayout);
        //设置webview
        WebViewUtil.dealWebView(mainWebView);
        //导入html
        mainWebView.loadUrl(htmlPath + "news/news.html");
        mainWebView.addJavascriptInterface(new MainJSInterface(), "android");

        mainActivity = this;

//        overridePendingTransition(0, 0);

        databaseHelper = new MyDatabaseHelper(MainActivity.this, "articls", null, 1);

        if (!isChecked) {
            //Android 6.0以上版本需要临时获取权限
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 &&
                    PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                int PERMS_REQUEST_CODE = 200;
                requestPermissions(perms, PERMS_REQUEST_CODE);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    new UpdateManager(MainActivity.this).checkUpdate(0);
                    isChecked = true;
                    Looper.loop();
                }
            }).start();
        }
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "未开启定位权限，请手动到设置去开去权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void finishMain() {
        if (mainActivity != null) {
            mainActivity.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainWebView = WebViewUtil.destroyWebView(mainWebView);
    }

    //查找全部控件
    private void findElement() {
        mainRefreshLayout = findViewById(R.id.main_refresh_layout);
        mainWebView = findViewById(R.id.main_webview);
        search = findViewById(R.id.search);
        studySelect = findViewById(R.id.study_select);
        modelRg = findViewById(R.id.model_rg);
        studySubRg = findViewById(R.id.study_sub_buttonGroup);
        baseLabel = findViewById(R.id.base_label);

        myTxtView = findViewById(R.id.txt_my);
        baseTxtView = findViewById(R.id.txt_base);
        homeTxtView = findViewById(R.id.txt_home);
        studyTxtView = findViewById(R.id.txt_study);
        schoolTxtView = findViewById(R.id.txt_school);
    }

    //处理控件
    private void dealElement() {
        modelRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                刷新加载禁止
                mainRefreshLayout.setEnableRefresh(false);
                mainRefreshLayout.setEnableLoadMore(false);
                //标题都不显示
                baseLabel.setVisibility(View.GONE);
                studySelect.setVisibility(View.GONE);
                //设置查找
                search.setText(getResources().getText(R.string.label_search));
                search.setTextSize(30);
                dealModelTxt(checkedId);
                String path = "";
                switch (checkedId) {
                    case R.id.rg_school:
                        path = "school/schoolActivity.html";
                        break;
                    case R.id.rg_study:
                        studySelect.setVisibility(View.VISIBLE);
                        path = "study/book/book.html";
                        studySubRg.check(R.id.literature);
                        break;
                    case R.id.rg_home:
                        path = "news/news.html";
                        break;
                    case R.id.rg_base:
                        mainRefreshLayout.setEnableLoadMore(true);
                        baseLabel.setVisibility(View.VISIBLE);
                        path = "base/base.html";
                        break;
                    case R.id.rg_my:
                        search.setText(getResources().getText(R.string.label_set));
                        search.setTextSize(25);
                        path = "my/me.html";
                        break;
                }
                mainWebView.loadUrl(htmlPath + path);
            }
        });
        studySubRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String rs = "video/video.html";
                switch (checkedId) {
                    case R.id.exam: rs = "exam/exam.html";break;
                    case R.id.literature: rs = "book/book.html";break;
                    case R.id.teleplay:para = "teleplay"; break;
                    case R.id.film:para = "film"; break;
                    case R.id.variety:para = "variety"; break;
                    case R.id.documentary:para = "documentary"; break;
                    case R.id.drama:para = "drama"; break;
                }
                mainWebView.loadUrl(htmlPath + "study/" + rs);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int button = modelRg.getCheckedRadioButtonId();
                Intent intent;
                if(button == R.id.rg_my){
                    intent = new Intent(MainActivity.this, SetActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, SearchActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    private void  dealModelTxt(int id) {
        myTxtView.setTextColor(Color.BLACK);
        baseTxtView.setTextColor(Color.BLACK);
        homeTxtView.setTextColor(Color.BLACK);
        studyTxtView.setTextColor(Color.BLACK);
        schoolTxtView.setTextColor(Color.BLACK);
        switch (id) {
            case R.id.rg_school: schoolTxtView.setTextColor(getResources().getColor(R.color.red)); break;
            case R.id.rg_study:studyTxtView.setTextColor(getResources().getColor(R.color.red)); break;
            case R.id.rg_home:homeTxtView.setTextColor(getResources().getColor(R.color.red)); break;
            case R.id.rg_base:baseTxtView.setTextColor(getResources().getColor(R.color.red)); break;
            case R.id.rg_my:myTxtView.setTextColor(getResources().getColor(R.color.red));break;
        }
    }

    private class MainJSInterface {
//---------------------------------------我的---------------------------------------------------

        @JavascriptInterface
        public void goCollection() {
            startActivity(new Intent(MainActivity.this, CollectionActivity.class));
        }

        @JavascriptInterface
        public void goSet() {
            startActivity(new Intent(MainActivity.this, SetActivity.class));
        }

        @JavascriptInterface
        public void goUserData() {
            startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
        }

        @JavascriptInterface
        public String getUser() {
            return  (String) SharedPreUtil.getParam(MainActivity.this, SharedPreUtil.LOGIN_DATA, "");
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

//---------------------------------------学校---------------------------------------------------

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
            startActivity(new Intent(MainActivity.this, SchoolDetailActivity.class));
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
            values.put("date", JSON.toJSONString(article.getDate()));//article.getDate()
            database.insert(part, null, values);
        }

//---------------------------------------新闻---------------------------------------------------

        @JavascriptInterface
        public void toArticle(String htmlUrl, int id) {
            ArticleActivity.setHtmlUrl(htmlUrl);
            ArticleActivity.setArtId(id);
            startActivity(new Intent(MainActivity.this, ArticleActivity.class));
        }

        //saveArticle(String part, String articleJson)
        //和学校中的方法相同 用一个

        @JavascriptInterface
        public void cleanArticles(String part) {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            database.execSQL("DELETE * from " + part );
        }

        @JavascriptInterface
        public String getTopArticle(String part) {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * from " + part + " WHERE recommend = 2 ORDER BY id DESC LIMIT 1", null);

            if (cursor == null) {
                return null;
            }
            Article article = buildArticle(cursor);
            return JSON.toJSONString(article);
        }

        @JavascriptInterface
        public String getArticles(String part) {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            List<Article> articles = null;
            Cursor cursor = null;
            cursor = database.rawQuery("SELECT * from " + part +
                    " ORDER BY id DESC ", null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Article article = buildArticle(cursor);
                    articles.add(article);
                }
                return JSON.toJSONString(articles);
            }
            return null;
        }

        private Article buildArticle(Cursor cursor) {
            Article article = new Article();
            article.setId(cursor.getInt(cursor.getColumnIndex("id")));
            article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            article.setType(cursor.getString(cursor.getColumnIndex("type")));
            article.setHtmlUrl(cursor.getString(cursor.getColumnIndex("htmlUrl")));
            article.setPicUrl(cursor.getString(cursor.getColumnIndex("picUrl")));
            article.setCount(cursor.getInt(cursor.getColumnIndex("count")));
            article.setRecommend(cursor.getInt(cursor.getColumnIndex("recommed")));
            article.setDate(cursor.getString(cursor.getColumnIndex("date")));
            return article;
        }

//---------------------------------------我的---------------------------------------------------

        @JavascriptInterface
        public String getAddress(){
            return AddressUtil.getLocations(MainActivity.this);
        }

        @JavascriptInterface
        public String getPara(){
            return para;
        }

        //转到study子模块
        @JavascriptInterface
        public void toSub(String model, String part, String sub, String subTitle, String htmlUrl){
            JSONObject obj = new JSONObject();
            obj.put("model", model);
            obj.put("part", part);
            obj.put("sub", sub);
            obj.put("title", subTitle);
            obj.put("htmlUrl", htmlPath + htmlUrl);
            SubActivity.setPara(obj);
            Intent intent = new Intent(MainActivity.this, SubActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void loadNoMoreData(){
            mainRefreshLayout.finishLoadMoreWithNoMoreData();
        }

        @JavascriptInterface
        public int getUserId() {
            String userStr = (String) SharedPreUtil.getParam(MainActivity.this, SharedPreUtil.LOGIN_DATA, "");
            User user = JSON.parseObject(userStr, User.class);
            return user.getId();
        }
    }
}
