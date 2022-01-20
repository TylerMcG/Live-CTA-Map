package com.McGregor.chicagotraintracker.UI;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.McGregor.chicagotraintracker.MainActivity;
import com.McGregor.chicagotraintracker.R;


public class AlertFragment extends Fragment {
    private static final String TAG = "ALERT_FRAG";
    private Bundle webViewState;
    private WebViewClient webViewClient;
    public static WebView webView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webViewClient = new WebViewClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.bottomNavigationView.findViewById(R.id.trainsFragment).setEnabled(false);
        mainActivity.bottomNavigationView.findViewById(R.id.stations).setEnabled(false);
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        webView = view.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(webViewClient);
        if(webViewState == null) {
            webView.loadUrl("https://www.transitchicago.com/travel-information/railstatus/");
        }
        else {
            webView.restoreState(webViewState);
        }


        return view;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        webViewState = new Bundle();
        Log.d(TAG, "Alert Pause");
        webView.saveState(webViewState);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
