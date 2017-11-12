package com.shepherdxx.celestialmp;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Shepherdxx on 10.11.2017.
 */

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }


}
