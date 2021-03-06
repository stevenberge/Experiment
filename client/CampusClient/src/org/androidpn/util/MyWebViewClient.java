package org.androidpn.util;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

	// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		view.loadUrl(url);

		// 如果不需要其他对点击链接事件的处理返回true，否则返回false

		return true;

	}
}