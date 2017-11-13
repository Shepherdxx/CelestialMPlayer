package com.shepherdxx.celestialmp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.shepherdxx.celestialmp.MyWebViewClient;
import com.shepherdxx.celestialmp.R;

/**
 * Created by Shepherdxx on 10.11.2017.
 */

public class Fragment_KrasView
        extends Fragment
        implements View.OnClickListener
{

    public Fragment_KrasView(){
    }

    public static Fragment_KrasView newInstance(String HTML_path) {
        Fragment_KrasView fragment = new Fragment_KrasView();
        Bundle args = new Bundle();
        args.putString("HTML_path", HTML_path);
        fragment.setArguments(args);
        Log.i(Log_Tag, "newInstance " + HTML_path);
        return fragment;
    }

    public WebView getKras_viewer() {
        return kras_viewer;
    }

    public String getPath() {
        if (kras_viewer==null)return path;
        Log.i(Log_Tag, kras_viewer.getUrl());
        return  kras_viewer.getUrl();
    }

    private WebView kras_viewer = null;
    private static String Log_Tag= Fragment_KrasView.class.getSimpleName();
    public LinearLayout panelLayout;
    View fragmentView;
    ImageButton imageButton;
    String path;
    boolean panelVisibility = false;

    public boolean getPanelVisibility(){
        if (panelLayout.getVisibility()==View.VISIBLE) return true;
        else  return false;
    }

    public void setPanelVisibility(boolean panelVisibility) {
        this.panelVisibility = panelVisibility;
        if (panelVisibility) panelLayout.setVisibility(View.VISIBLE);
        else panelLayout.setVisibility(View.GONE);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            path = getArguments().getString("HTML_path");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_detail, container, false);
        panelLayout = fragmentView.findViewById(R.id.k_v_panel);
        setPanelVisibility(false);
        imageButton= fragmentView.findViewById(R.id.k_v_iBut);
        imageButton.setOnClickListener(this);
        return fragmentView;
    }

    @Override
    public void onResume() {
        goToLink(getPath());
        super.onResume();
    }

    public void goToLink(String item){
        kras_viewer = getView().findViewById(R.id.k_v_webView1);
        kras_viewer.setWebViewClient(new MyWebViewClient());
        kras_viewer.setWebChromeClient(new WebChromeClient());
        kras_viewer.getSettings().setJavaScriptEnabled(false);
        kras_viewer.loadUrl(item);
    }

    private String getLink(){
        String Link;
        EditText editText= getView().findViewById(R.id.k_v_edit);
        if (editText.toString().isEmpty())Link = path;
        else Link = editText.getText().toString();
        return Link;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==imageButton.getId()) goToLink(getLink());

    }

    public class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(Uri.parse(url).getHost().length() == 0) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }


    }


}
