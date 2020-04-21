package com.zty.hqx7.utils;

import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zty.hqx7.R;

public class WebViewUtil {
    public static WebView destroyWebView(WebView webView) {
        webView.removeAllViews();
        webView.clearHistory();
        webView.clearCache(true);
        webView.freeMemory();
        webView = null;
        return webView;
    }
    /**
     * 设置webview
     * */
    public static void dealWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //允许执行JavaScript脚本，默认false
        webSettings.setJavaScriptEnabled(true);
        //在WebView内部是否允许访问文件，默认允许访问
        webSettings.setAllowFileAccess(true);
//        设置WebView运行中的脚本可以是否访问任何原始起点内容，默认true
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //设置WebView运行中的一个文件方案被允许访问其他文件方案中的内容，默认值true
        webSettings.setAllowFileAccessFromFileURLs(true);
        //设置滚动条风格，也可在配置文件中android:scrollbarStyle="insideInset"
        //该ScrollBar显示在视图(view)的边缘,不增加view的padding,该ScrollBar将被半透明覆盖
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //开启可重新加载
        //调用js的重新加载
        webView.loadUrl("javascript:window.location.reload( true )");
    }
    //        webSettings.setUserAgentString("User-Agent");
    //        //开启本地DOM存储
    //        webSettings.setDomStorageEnabled(true);
    //        //禁止缓存
    //        webSettings.setAppCacheEnabled(false);
    //        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {}

    /**
     * 设置下拉加载的layout
     * */
    public static void dealRefreshLayout(Context context, final WebView webView, RefreshLayout refreshlayout) {
        //是否在列表不满一页时候开启上拉加载功能
        refreshlayout.setEnableLoadMoreWhenContentNotFull(false);
        //设置主题颜色
        refreshlayout.setPrimaryColorsId(R.color.orange);
        //设置刷新布局
        refreshlayout.setRefreshHeader(new PhoenixHeader(context));
        refreshlayout.setRefreshFooter(new BallPulseFooter(context));
        refreshlayout.setOnRefreshListener(new OnRefreshListener() { //下拉刷新
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                System.out.println("我正在刷新***********************************");
                webView.loadUrl("javascript:refresh()");
            }
        });

        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() { //上拉加载更多
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                System.out.println("我正在加载***********************************");
                webView.loadUrl("javascript:loadContent()");

            }
        });
    }

    /**
     * 设置下拉加载的layout
     * 禁止加载和刷新
     * */
//    search static void dealRefreshLayoutFalse(final WebView webView, RefreshLayout refreshlayout) {
//        dealRefreshLayout(webView, refreshlayout);
//        refreshlayout.setEnableRefresh(false);
//        refreshlayout.setEnableLoadMore(false);
//    }
}
