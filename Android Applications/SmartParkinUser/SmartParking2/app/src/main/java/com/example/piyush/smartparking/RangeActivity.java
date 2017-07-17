package com.example.piyush.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class RangeActivity extends AppCompatActivity {

    NumberPicker numberPicker;
    int numPickerVal;

    private double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);

        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        if (null != numberPicker) {
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(3);
            numberPicker.setValue(1);
            numberPicker.setWrapSelectorWheel(false);

            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    numPickerVal = newVal;
                }
            });
        }

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            latitude = bundle.getDouble(getString(R.string.bundle_search_latitude));
            longitude = bundle.getDouble(getString(R.string.bundle_search_longitude));
        }
    }

    public void findParking(View view)
    {
        Intent intent = new Intent(this, SearchResultMapsActivity.class);
        intent.putExtra(getString(R.string.bundle_search_latitude), Double.toString(latitude));
        intent.putExtra(getString(R.string.bundle_search_longitude), Double.toString(longitude));
        intent.putExtra(getString(R.string.bundle_search_range), Integer.toString(numPickerVal));
        startActivity(intent);
    }
}
