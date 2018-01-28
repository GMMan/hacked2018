package blue.golem.android.walletthing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StartupActivity extends AppCompatActivity {

    private static final String TAG = "StartupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        waitForConverterLoad();
    }

    private void waitForConverterLoad() {
        new Thread() {
            @Override
            public void run() {
                try {
                    CurrencyConverter inst = CurrencyConverter.getInstance();
                    while (!inst.getReady()) {
                        Thread.sleep(100);
                    }
                    Intent launchIntent = new Intent(StartupActivity.this, MainActivity.class);
                    startActivity(launchIntent);
                    finish();
                } catch (InterruptedException ex) {
                    Log.e(TAG, "Launch interrupted", ex);
                }
            }
        }.start();
    }
}
