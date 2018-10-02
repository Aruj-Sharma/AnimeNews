package com.ar_j.animenews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipe=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

     load();


    }

    public void load(){
        if (isOnline()) {
            final WebView webView = findViewById(R.id.webView);


            webView.getSettings().setJavaScriptEnabled(true);

            webView.loadUrl("https://www.animenewsnetwork.com/news/");

            swipe.setRefreshing(true);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    swipe.setRefreshing(false);
                }

                @Override
                public void onLoadResource(WebView view, String url) {

                    try {
                        webView.loadUrl("javascript:(window.onload = function() { " +
                                "(elem1 = document.getElementById('header')); elem1.parentNode.removeChild(elem1); "
                                +"document.getElementById('mega').style.display='none';"+
                                "document.querySelector('.controls').style.display='none';"
                                +
                                "})()");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    Intent intent = new Intent(getApplicationContext(),urlActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                    return true;
                }
            });

        }
    }


    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();

                            }
                        }).setNegativeButton("No", null).show();
    }

}





