package me.tabacowang.sslpinningokhttp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Spinner;
import android.widget.TextView;

import okhttp3.CertificatePinner;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SSLPinningOkHttp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing the actual view.
     */
    public static class PlaceholderFragment extends Fragment implements TestConnectionTask
            .Listener {

        private Spinner mUrlsSpinner;
        private Checkable mEnablePinning;
        private TextView mResultView;

        public PlaceholderFragment() {
            // required default constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_main, container, false);
            if (null != root) {

                mUrlsSpinner = (Spinner) root.findViewById(R.id.urls);
                mEnablePinning = (CheckBox) root.findViewById(R.id.enable_pinning);
                mResultView = (TextView) root.findViewById(R.id.result);

                // listen for clicks on the submit button
                Button submitButton = (Button) root.findViewById(R.id.submit);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSubmit();
                    }
                });
            }
            return root;
        }

        private void onSubmit() {
            mResultView.setText(getString(R.string.loading));

            CertificatePinner certPinner = new CertificatePinner.Builder().build();
            if (mEnablePinning.isChecked()) {
                certPinner = new CertificatePinner.Builder()
                        .add("github.com",
                                "sha256/pL1+qb9HTMRZJmuC/bB/ZI9d302BYrrqiVuRyW+DGrU=")
                        .add("github.com",
                                "sha256/RRM1dGqnDFsCJXBTHky16vi1obOlCgFFn/yOhI/y+ho=")
                        .build();
            }
            String url = (String) mUrlsSpinner.getSelectedItem();
            new TestConnectionTask(certPinner,this).execute(url);
        }

        @Override
        public void onConnectionSuccess(String result) {
            mResultView.setText(getString(R.string.success));
        }

        @Override
        public void onConnectionFailure() {
            mResultView.setText(getString(R.string.refused));
        }
    }
}
