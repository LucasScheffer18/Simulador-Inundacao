package com.example.simuladorinundao;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    WebView webView;

    public class JSInterface {
        String csvContent;

        JSInterface(String csvContent) {
            this.csvContent = csvContent;
        }

        @JavascriptInterface
        public String getCSV() {
            return csvContent;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String csvData = loadCSVFromAssets();
        webView.addJavascriptInterface(new JSInterface(csvData), "Android");

        webView.loadUrl("file:///android_asset/simulador.html");
    }

    private String loadCSVFromAssets() {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("dados.csv")));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
