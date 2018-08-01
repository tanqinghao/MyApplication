package com.example.trans.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.trans.myapplication.http.NetRequest;

public class WebUrlActivity extends AppCompatActivity {

    private String url;
    private Bundle bundle;
    private final String WEBVIEWURL = "webViewUrl";
    private final String URL_MAIN = "http://192.168.1.8/HYAK/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_url);
        bundle = this.getIntent().getExtras();
        initView();
    }


    private void initView() {
        WebView webView = findViewById(R.id.wv_webView);
        if (bundle != null) {
            url=bundle.getString(WEBVIEWURL);
        }
        //免责声明:home/disclaimer
        //隐私条款:home/privacyPolicy
     /*   if (TextUtils.isEmpty(url)) {
            url = URL_MAIN + "home/disclaimer";
        }*/

         url=URL_MAIN+ "home/privacyPolicy";

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }

}
