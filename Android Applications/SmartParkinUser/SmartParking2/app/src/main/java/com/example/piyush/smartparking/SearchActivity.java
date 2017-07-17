package com.example.piyush.smartparking;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = (SearchView)findViewById(R.id.searchView);

    }

    public void findLatLong(View view) {
        String strAddress = searchView.getQuery().toString();

        if (!strAddress.isEmpty()) {
            try {
                Geocoder coder = new Geocoder(this);
                List<Address> address;

                address = coder.getFromLocationName(strAddress, 1);

                if (address == null) {
                    Toast.makeText(getApplicationContext(), "Sorry, can't find this address", Toast.LENGTH_LONG).show();

                    return;
                }

                Address location = address.get(0);

                Intent rangeIntent = new Intent(this, RangeActivity.class);
                rangeIntent.putExtra(getString(R.string.bundle_search_latitude), location.getLatitude());
                rangeIntent.putExtra(getString(R.string.bundle_search_longitude), location.getLongitude());
                startActivity(rangeIntent);

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
