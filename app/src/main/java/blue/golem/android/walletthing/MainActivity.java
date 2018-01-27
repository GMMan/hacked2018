package blue.golem.android.walletthing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    CurrencyConverter currencyConverter = new CurrencyConverter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRefreshClicked(View v) {
        Spinner fromSpinner = findViewById(R.id.fromSpinner);
        Spinner toSpinner = findViewById(R.id.toSpinner);
        Set<String> currencies = currencyConverter.getCurrencies();
        String[] currArr = currencies.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currArr);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }
}
