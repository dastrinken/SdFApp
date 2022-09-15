package de.sdf.companion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import de.sdf.companion.preferences.PreferencesActivity;


public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://www.sdf-rg.de/wordpress";
    private ProgressBar webViewProgress;
    private LinearLayout menuLayout;
    private WebView newsView;

    // Debugging
    private static final String TAG = MainActivity.class.getName();
    private static final boolean D = true; //debugging konstante -> if (D)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        menuLayout = findViewById(R.id.menuLayout);
        loadViews();
        loadPreferences();
    }

    /* News display and logic */
    public void loadViews() {
        webViewProgress = findViewById(R.id.webViewProgress);
        newsView = findViewById(R.id.newsView);
        newsView.setBackgroundColor(Color.parseColor("#66564E"));
        newsView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if (menuLayout.getVisibility() != View.GONE) menuLayout.setVisibility(View.GONE);
        });
        newsView.setWebViewClient(new WebViewClient());
        newsView.loadUrl(URL);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webViewProgress.setVisibility(View.GONE);
        }
    }

    /* Menu display and logic */
    public void displayMenu(View view) {
        if(menuLayout.getVisibility() != View.VISIBLE) {
            menuLayout.setVisibility(View.VISIBLE);
        } else {
            menuLayout.setVisibility(View.GONE);
        }
    }

    public void menuBtnPressed(View view) {
        if (D) Log.d(TAG, String.valueOf(view.getId()));
        Class<?> activity;
        switch(view.getId()) {
            case R.id.loginBtn:
                activity = LoginActivity.class;
                break;
            case R.id.prefsBtn:
                //TODO: preferences
                activity = PreferencesActivity.class;
                break;
            case R.id.raidBtn:
                //TODO: raidActivity
                activity = RaidActivity.class;
                break;
            default:
                activity = MainActivity.class;
                break;
        }
        startActivity(new Intent(MainActivity.this, activity));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (newsView.canGoBack()) {
                        newsView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void loadPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = preferences.getString("auth_token", "");
        if(!token.equalsIgnoreCase(""))
        {
            Log.w(TAG, token);
        } else {
            Log.w(TAG, "kein token verfügbar");
        }
    }
}