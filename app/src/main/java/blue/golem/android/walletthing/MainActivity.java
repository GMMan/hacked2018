package blue.golem.android.walletthing;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
    Drawable fromEditableBackground;
    Drawable toEditableBackground;
    String prevFromCurrency;
    String prevToCurrency;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromAmountView = findViewById(R.id.fromAmountView);
        toAmountView = findViewById(R.id.toAmountView);
        fromEditableBackground = fromAmountView.getBackground();
        toEditableBackground = toAmountView.getBackground();

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
        AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.onSpinnerItemSelected(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        fromSpinner.setOnItemSelectedListener(spinnerSelectedListener);
        toSpinner.setOnItemSelectedListener(spinnerSelectedListener);

        prefs = getSharedPreferences("app", MODE_PRIVATE);
        String savedFromCurrency = prefs.getString("from_curr", null);
        String savedToCurrency = prefs.getString("to_curr", null);
        if (savedFromCurrency != null) {
            int pos = ((ArrayAdapter<String>) fromSpinner.getAdapter()).getPosition(savedFromCurrency);
            if (pos >= 0) fromSpinner.setSelection(pos);
        }
        if (savedToCurrency != null) {
            int pos = ((ArrayAdapter<String>) toSpinner.getAdapter()).getPosition(savedToCurrency);
            if (pos >= 0) toSpinner.setSelection(pos);
        }

        prevFromCurrency = (String) fromSpinner.getSelectedItem();
        prevToCurrency = (String) toSpinner.getSelectedItem();
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
        setTextViewEditable(fromAmountView, isEditMode, fromEditableBackground);
        setTextViewEditable(toAmountView, isEditMode, toEditableBackground);
    }

    private void setTextViewEditable(EditText v, boolean editable, Drawable editableBackground) {
        if (editable) {
            v.setBackground(editableBackground);
            v.setFocusableInTouchMode(true);
        } else {
            v.clearFocus();
            v.setBackground(null);
        }
        v.setFocusable(editable);
    }

    private boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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

    private void onSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == fromSpinner) {
            String newCurrency = (String) fromSpinner.getItemAtPosition(position);
            convertAndSet(prevFromCurrency, newCurrency, fromAmountView, fromAmountView);
            prevFromCurrency = newCurrency;
            SharedPreferences.Editor e = prefs.edit();
            e.putString("from_curr", newCurrency);
            e.commit();
        } else if (parent == toSpinner) {
            String newCurrency = (String) toSpinner.getItemAtPosition(position);
            convertAndSet(prevToCurrency, newCurrency, toAmountView, toAmountView);
            prevToCurrency = newCurrency;
            SharedPreferences.Editor e = prefs.edit();
            e.putString("to_curr", newCurrency);
            e.commit();
        }
    }

    private void convertHomeToForeign() {
        String from = (String) fromSpinner.getSelectedItem();
        String to = (String) toSpinner.getSelectedItem();
        convertAndSet(from, to, fromAmountView, toAmountView);
    }

    private void convertForeignToHome() {
        String to = (String) fromSpinner.getSelectedItem();
        String from = (String) toSpinner.getSelectedItem();
        convertAndSet(from, to, toAmountView, fromAmountView);
    }

    private void convertAndSet(String from, String to, EditText srcView, EditText destView) {
        double amount = Double.parseDouble(srcView.getText().toString());
        BigDecimal fromDec = new BigDecimal(amount);
        BigDecimal toDec = CurrencyConverter.getInstance().convert(from, to, fromDec);
        destView.setText(toDec.toString());
    }
}
