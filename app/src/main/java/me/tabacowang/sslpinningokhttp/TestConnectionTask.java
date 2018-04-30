package me.tabacowang.sslpinningokhttp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestConnectionTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "TestConnectionTask";
    private final CertificatePinner mCertPinner;
    private final Listener mListener;

    public TestConnectionTask(CertificatePinner certPinner, Listener listener) {
        mCertPinner = certPinner;
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        URL url;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to create test URL", e);
            return null;
        }

        try {
            return connect(params[0]);
        } catch (IOException e) {
            return null;
        }
    }

    private String connect(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .certificatePinner(mCertPinner)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if(response.isSuccessful())
            return "ok";
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (null == result) {
            mListener.onConnectionFailure();
        } else {
            mListener.onConnectionSuccess(result);
        }
    }

    public interface Listener {
        public abstract void onConnectionSuccess(String response);
        public abstract void onConnectionFailure();
    }
}
