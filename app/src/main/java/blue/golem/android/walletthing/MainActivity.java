package blue.golem.android.walletthing;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

        // Setup some views
        setEditMode(isEditMode);
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return MainActivity.this.onEditorAction(textView, i, keyEvent);
            }
        };
        fromAmountView.setOnEditorActionListener(editorActionListener);
        toAmountView.setOnEditorActionListener(editorActionListener);

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
        } else {
            v.clearFocus();
            v.setBackground(null);
        }
        v.setFocusable(editable);
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && event == null) {
            if (v == fromAmountView) {
                convertHomeToForeign();
                return true;
            } else if (v == toAmountView) {
                convertForeignToHome();
                return true;
            }
        }
        return false;
    }

    private void convertHomeToForeign() {
        String from = (String) fromSpinner.getSelectedItem();
        String to = (String) toSpinner.getSelectedItem();
        double amount = Double.parseDouble(fromAmountView.getText().toString());
        BigDecimal fromDec = new BigDecimal(amount);
        BigDecimal toDec = CurrencyConverter.getInstance().convert(from, to, fromDec);
        toAmountView.setText(toDec.toString());
    }

    private void convertForeignToHome() {
        String to = (String) fromSpinner.getSelectedItem();
        String from = (String) toSpinner.getSelectedItem();
        double amount = Double.parseDouble(toAmountView.getText().toString());
        BigDecimal fromDec = new BigDecimal(amount);
        BigDecimal toDec = CurrencyConverter.getInstance().convert(from, to, fromDec);
        fromAmountView.setText(toDec.toString());
    }
}
