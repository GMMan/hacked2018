package blue.golem.android.walletthing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    CurrencyConverter currencyConverter = new CurrencyConverter();
    Spinner fromSpinner;
    Spinner toSpinner;
    EditText amountBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        amountBox = findViewById(R.id.amountEdit);
    }

    public void onRefreshClick(View v) {
        Collection<String> currencies = currencyConverter.getCurrencies();
        List<String> sortedCurrencies = new ArrayList<>(currencies);
        Collections.sort(sortedCurrencies);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sortedCurrencies);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    public void onConvertClick(View v) {
        String from = (String)fromSpinner.getSelectedItem();
        String to = (String)toSpinner.getSelectedItem();
        double amount = Double.parseDouble(amountBox.getText().toString());
        BigDecimal fromDec = new BigDecimal(amount);
        BigDecimal toDec = currencyConverter.convert(from, to, fromDec);
        amountBox.setText(toDec.toString());
    }
}
