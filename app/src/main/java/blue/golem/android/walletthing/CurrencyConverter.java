package blue.golem.android.walletthing;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by cyanic on 2018-01-27.
 */

public class CurrencyConverter {
    private static final String TAG = "CurrencyConverter";
    private static final String CURRENCY_XML_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    private HashMap<String, Double> currencies;

    public CurrencyConverter() {
        currencies = new HashMap<>();
        currencies.put("EUR", 1.0);
        downloadCurrencyDataThreaded();
    }

    public BigDecimal convert(String from, String to, BigDecimal amount) {
        double toRate = currencies.get(to);
        if (toRate == 0) return null;
        double fromRate = currencies.get(from);
        if (fromRate == 0) return null;
        BigDecimal toEuro = amount.setScale(10).divide(new BigDecimal(fromRate), BigDecimal.ROUND_HALF_EVEN);
        BigDecimal toTarget = toEuro.multiply(new BigDecimal(toRate));
        BigDecimal finalNum = toTarget.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return finalNum;
    }

    public Set<String> getCurrencies() {
        return currencies.keySet();
    }

    public void clearCurrencies() {
        currencies.clear();
        currencies.put("EUR", 1.0);
    }

    public void refresh() {
        clearCurrencies();
        downloadCurrencyDataThreaded();
    }

    private void downloadCurrencyDataThreaded() {
        new Thread() {
            @Override
            public void run() {
                downloadCurrencyData();
            }
        }.start();
    }

    private void downloadCurrencyData() {
        try {
            // Download the file into a string
            URL url = new URL(CURRENCY_XML_URL);
            URLConnection conn = url.openConnection();
            InputStream stream = conn.getInputStream();

            // Load XML into reader
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser parser = saxFactory.newSAXParser();


            DefaultHandler currencyHandler = new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (localName.equals("Cube")) {
                        String currencyName = null;
                        String currencyValue = null;
                        for (int i = 0; i < attributes.getLength(); ++i) {
                            String attrName = attributes.getLocalName(i);
                            if (attrName.equals("currency")) {
                                currencyName = attributes.getValue(i);
                            } else if (attrName.equals("rate")) {
                                currencyValue = attributes.getValue(i);
                            }
                        }
                        if (currencyName != null && currencyValue != null) {
                            double rate = Double.parseDouble(currencyValue);
                            currencies.put(currencyName, rate);
                        }
                    }
                }
            };

            parser.parse(stream, currencyHandler);
        } catch (Exception ex) {
            Log.e(TAG, "Error loading currencies.", ex);
        }
    }
}
