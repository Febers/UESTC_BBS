/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-8-5 下午1:42
 *
 */

package com.febers.uestc_bbs.http;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewConfigure {

    private static final String TAG = "WebViewConfigure";
    private WebViewConfigure(){}

    public static class Builder {

        private Context context;
        private WebView webView;
        private WebSettings webSettings;
        private WebViewClient webViewClient;
        private Boolean acceptAllRequest = true;
        private Boolean noImage = false;
        private Boolean openUrlOut = true;
        private Boolean processHtml = false;
        private Boolean enableJS = true;

        private WebChromeClient webChromeClient;
        private ProgressBar progressBar;
        private Boolean supportLoadingBar = false;

        public Builder(Context context, WebView webView) {
            this.context = context;
            this.webView = webView;
            webSettings = webView.getSettings();
        }

        public Builder disabledJS() {
            enableJS = false;
            return this;
        }

        public Builder setJSEnable(Boolean enable) {
            webSettings.setJavaScriptEnabled(true);
            return this;
        }

        public Builder addJSInterface(Object object, String name) {
            setJSEnable(true);
            webView.addJavascriptInterface(object, name);
            return this;
        }

        public Builder setCacheMode(int mode) {
            webSettings.setCacheMode(mode);
            return this;
        }

        public Builder setAppCacheEnabled(Boolean b) {
            webSettings.setAppCacheEnabled(b);
            return this;
        }

        public Builder setAppCachePath(String path) {
            webSettings.setAppCachePath(path);
            return this;
        }

        public Builder setUseWideViewPort(Boolean useWideViewPort) {
            webSettings.setUseWideViewPort(useWideViewPort);
            return this;
        }

        public Builder setLoadWithOverviewMode(Boolean loadWithOverviewMode) {
            webSettings.setLoadWithOverviewMode(loadWithOverviewMode);
            return this;
        }

        public Builder setSupportZoom(Boolean supportZoom) {
            webSettings.setSupportZoom(supportZoom);
            return this;
        }

        public Builder setBuiltInZoomControls(Boolean builtInZoomControls) {
            webSettings.setBuiltInZoomControls(builtInZoomControls);
            return this;
        }

        public Builder setDisplayZoomControls(Boolean displayZoomControls) {
            webSettings.setDisplayZoomControls(displayZoomControls);
            return this;
        }

        public Builder setSupportWindow(Boolean supportWindow) {
            webSettings.setJavaScriptCanOpenWindowsAutomatically(supportWindow);
            return this;
        }

        public Builder setDomEnabled(Boolean domEnabled) {
            webSettings.setDatabaseEnabled(true);
            webSettings.setDomStorageEnabled(domEnabled);
            return this;
        }

        public Builder setClientWithoutImage() {
            noImage = true;
            return this;
        }

        public Builder acceptAnyRequest(Boolean accepter) {
            acceptAllRequest = accepter;
            return this;
        }

        public Builder setBlockNetworkImage(Boolean block) {
            webSettings.setBlockNetworkImage(block);
            return this;
        }

        public Builder setSupportLoadingBar(Boolean support, ProgressBar progressBar) {
            supportLoadingBar = support;
            this.progressBar = progressBar;
            return this;
        }

        public Builder setOpenUrlOut(Boolean openUrlOut) {
            this.openUrlOut = openUrlOut;
            return this;
        }

        public Builder setProcessHtml(Boolean processHtml, Object object, String name) {
            this.processHtml = processHtml;
            addJSInterface(object, name);
            return this;
        }

        public WebView builder() {
            webViewClient = new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    if (acceptAllRequest) {
                        handler.proceed();
                    }
                }

                /**
                 *  webveiw只能打开以http/https开头的网页
                 *  遇到其他开头的url，比如一些scheme，比如打开其他应用
                 *  以openapp开头的url时，就会报错，需要手动处理
                 */
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (openUrlOut) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        if (intent.resolveActivity(context.getPackageManager())!=null) {
                            context.startActivity(intent);
                        }
                        return true;
                    } else {
                        if (url.startsWith("openapp")) {
                            return true;
                        }
                        view.loadUrl(url);
                        return true;
                    }
                }

                @TargetApi(21)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (openUrlOut) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
                        if (intent.resolveActivity(context.getPackageManager())!=null) {
                            context.startActivity(intent);
                        }
                        return true;
                    } else {
                        if (request.getUrl().toString().startsWith("openapp")) {
                            return true;
                        }
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }
                }

                @TargetApi(21)
                @Nullable
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    if (noImage) {
                        if (request.getUrl().toString().contains("image")||
                                request.getUrl().toString().contains("png")||
                                request.getUrl().toString().contains("jpg")) {
                            return new WebResourceResponse(null, null, null);
                        }
                    }
                    return super.shouldInterceptRequest(view, request);
                }

                @Nullable
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                    if (noImage) {
                        if (url.contains("image")||
                                url.contains("png")||
                                url.contains("jpg")) {
                            return new WebResourceResponse(null, null, null);
                        }
                    }
                    return super.shouldInterceptRequest(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!webSettings.getLoadsImagesAutomatically()) {
                        webSettings.setLoadsImagesAutomatically(true);
                    }
                    if (processHtml) {
                        //解析网页源码
                        view.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);");
                    }
                    super.onPageFinished(view, url);
                }

            };

            webChromeClient = new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (supportLoadingBar && progressBar!=null) {
                        if (newProgress == 100) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(newProgress);
                        }
                    }
                }
            };

            CookieSyncManager.createInstance(webView.getContext());
            CookieSyncManager.getInstance().sync();
            //延迟加载图片,对于4.4直接加载
            if (Build.VERSION.SDK_INT >= 19) {
                webSettings.setLoadsImagesAutomatically(true);
            } else {
                webSettings.setLoadsImagesAutomatically(false);
            }
            webView.setWebViewClient(webViewClient);
            webView.setWebChromeClient(webChromeClient);
            return webView;
        }
    }
}
