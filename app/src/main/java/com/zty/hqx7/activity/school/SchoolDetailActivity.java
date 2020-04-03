package com.zty.hqx7.activity.school;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zty.hqx7.R;
import com.zty.hqx7.activity.my.AboutActivity;
import com.zty.hqx7.activity.my.UserEditActivity;
import com.zty.hqx7.model.Article;
import com.zty.hqx7.model.User;
import com.zty.hqx7.util.MyDatabaseHelper;
import com.zty.hqx7.util.WebViewUtil;
import com.zty.hqx7.util.SharedPreUtil;
import com.zty.hqx7.ztyClass.IconView;

import java.util.List;

public class SchoolDetailActivity extends Activity {
    private WebView webView;
    private TextView titleBar;
    private IconView back;
    private IconView edit;

    protected static MyDatabaseHelper databaseHelper = null;
    private Dialog dialog;

    public static Integer id;
    public static String title;
    public static String initiator;
    public static String detail;
    public static String when;
    public static String where;
    public static String pic1;
    public static String pic2;
    public static String pic3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findElement();
        dealElement();
        WebViewUtil.dealWebView(webView);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/htmls/school/schoolDetail.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
        databaseHelper = new MyDatabaseHelper(SchoolDetailActivity.this, "articls", null, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView = WebViewUtil.destroyWebView(webView);
    }

    private void findElement() {
        webView = findViewById(R.id.my_webview);
        back = findViewById(R.id.my_back);
        titleBar = findViewById(R.id.my_title);
        edit = findViewById(R.id.my_edit);
    }

    private void dealElement(){
        edit.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchoolDetailActivity.this.finish();
            }
        });
        titleBar.setText("活动详情");
    }

    public final class JSInterface {
        int canJiaResult = 0;

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(SchoolDetailActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public int isUserDone() {
            String userStr = (String) SharedPreUtil.getParam(SchoolDetailActivity.this, SharedPreUtil.LOGIN_DATA, "");
            User user = JSON.parseObject(userStr, User.class);
            if (user.getClassStr() == null || user.getClassStr().equals("") ||
                user.getName() == null || user.getName().equals("") ||
                user.getTel() == null || user.getTel().equals("") ||
                user.getSchoolId() == null || user.getSchoolId().equals("")) {
                return 0;
            }
            return 1;
        }

        @JavascriptInterface
        public void dialog() {
            dialog = new AlertDialog.Builder(SchoolDetailActivity.this)
                    .setTitle("小红旗温馨提示：")
                    .setMessage("用户信息不完善，请完善信息后再参与活动~")
                    .setNegativeButton("不参加了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("现在去完善", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            startActivity(new Intent(SchoolDetailActivity.this, UserEditActivity.class));
                        }
                    }).create();
            dialog.show();
        }

        @JavascriptInterface
        public void go() {
            dialog = new AlertDialog.Builder(SchoolDetailActivity.this)
                    .setMessage("是否确定参加？参加活动会将个人信息发送至活动发起方")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            canJiaResult = 1;
                            SchoolDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 调用后端js方法
                                    webView.loadUrl("javascript:sure()");
                                }
                            });
                        }
                    })
                    .setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            canJiaResult = 0;
                        }
                    }).create();
            dialog.show();
        }

        @JavascriptInterface
        public void cancel() {
            dialog = new AlertDialog.Builder(SchoolDetailActivity.this)
                    .setTitle("取消参加")
                    .setMessage("确认取消？机会来之不易，取消会被记录的哦")
                    .setPositiveButton("我不参加了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            canJiaResult = 1;
                            SchoolDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadUrl("javascript:sureCancel()");
                                }
                            });
                        }
                    })
                    .setNegativeButton("我还要参加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            canJiaResult = 0;
                        }
                    }).create();
            dialog.show();
        }

        @JavascriptInterface
        public int getId() {
            return id;
        }

        @JavascriptInterface
        public String getTitle() {
            return title;
        }

        @JavascriptInterface
        public String getInitiator() {
            return initiator;
        }

        @JavascriptInterface
        public String getDetail() {
            return detail;
        }

        @JavascriptInterface
        public String getWhen() {
            return when;
        }

        @JavascriptInterface
        public String getWhere() {
            return where;
        }

        @JavascriptInterface
        public String getPic1() {
            return pic1;
        }

        @JavascriptInterface
        public String getPic2() {
            return pic1;
        }

        @JavascriptInterface
        public String getPic3() {
            return pic1;
        }

        @JavascriptInterface
        public int getStuId() {
            String user = (String) SharedPreUtil.getParam(SchoolDetailActivity.this, SharedPreUtil.LOGIN_DATA, "");
            Integer id = JSON.parseObject(user, User.class).getId();
            return id;
        }

        @JavascriptInterface
        public void toReturn() {
            SchoolDetailActivity.this.finish();
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

//            database.execSQL("INSERT INTO " + part + " " +
//                    "(id, title, htmlUrl, count, recommend, date) " +
//                    "VALUES" +
//                    "(" + id + "," + title + "," + htmlUrl + "," + count + "," + recommend + "," + date +")");
        }

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

    }
}
