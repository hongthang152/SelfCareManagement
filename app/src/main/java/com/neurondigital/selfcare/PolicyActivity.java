package com.neurondigital.selfcare;

import android.content.Context;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.neurondigital.selfcare.Configurations.SERVER_URL;

public class PolicyActivity extends AppCompatActivity {
    Context context;

    WebView infoWebview;
    SwipeRefreshLayout swipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);
        infoWebview = (WebView) findViewById(R.id.webview_info);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);

        context = this;

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.privacy));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //go back
                onBackPressed();

            }
        });


        // Swipe to Refresh
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refreshes the WebView
                refresh();
            }
        });

        //stop refresh
        infoWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                swipeLayout.setRefreshing(false);
            }
        });



        refresh();
    }


    public void refresh() {
        infoWebview.loadUrl(SERVER_URL+"privacypolicy");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        infoWebview.destroy();
    }
    //added after v41
    @Override
    public void onPause() {
        super.onPause();
        infoWebview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        infoWebview.onResume();
    }

}
