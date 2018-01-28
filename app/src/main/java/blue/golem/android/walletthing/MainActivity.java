package blue.golem.android.walletthing;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner fromSpinner;
    Spinner toSpinner;
    EditText fromAmountView;
    EditText toAmountView;
    boolean isEditMode;
    Drawable editableBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromAmountView = findViewById(R.id.fromAmountView);
        toAmountView = findViewById(R.id.toAmountView);
        editableBackground = fromAmountView.getBackground();

        // Restore instance variables
        if (savedInstanceState != null) {
            isEditMode = savedInstanceState.getBoolean("edit_mode");
        }

        setEditMode(isEditMode);
        
        // Setup currency spinners
        Collection<String> currencies = CurrencyConverter.getInstance().getCurrencies();
        List<String> sortedCurrencies = new ArrayList<>(currencies);
        Collections.sort(sortedCurrencies);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sortedCurrencies);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        // TODO: load user settings (from/to currencies)
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("edit_mode", isEditMode);
    }

    public void onModeClick(View v) {
        setEditMode(!isEditMode);
    }

    private void setEditMode(boolean editable) {
        isEditMode = editable;
        setTextViewEditable(fromAmountView, isEditMode);
        setTextViewEditable(toAmountView, isEditMode);
    }

    private void setTextViewEditable(EditText v, boolean editable) {
        if (editable) {
            v.setBackground(editableBackground);
            v.setFocusableInTouchMode(true);
        }
        else
        {
            v.clearFocus();
            v.setBackground(null);
        }
        v.setFocusable(editable);
    }

    /*
    public void onConvertClick(View v) {
        String from = (String)fromSpinner.getSelectedItem();
        String to = (String)toSpinner.getSelectedItem();
        double amount = Double.parseDouble(fromAmount.getText().toString());
        BigDecimal fromDec = new BigDecimal(amount);
        BigDecimal toDec = currencyConverter.convert(from, to, fromDec);
        fromAmount.setText(toDec.toString());
    }
    */
}
