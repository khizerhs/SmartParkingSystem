package com.project.team.parking.smart.smartparkingadmin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.HttpURLConnection;


public class EditSensorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String id;
    private String location;
    private String [] latlong;
    private String status;
    private String zip;
    private String cost;

    private AutoCompleteTextView latitudeTextEdit;
    private AutoCompleteTextView longitudeTextEdit;
    private AutoCompleteTextView costTextEdit;

    private Spinner statusSpinner;

    private View progressView;
    private View formView;

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;


    private UpdateSensorInfoTask updateSensorInfoTask = null;
    private DeleteSensorInfoTask deleteSensorInfoTask = null;
    private AddSensorInfoTask addSensorInfoTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressView = findViewById(R.id.update_sensor_info_progress);
        formView = findViewById(R.id.edit_sensor_info_form);

        latitudeTextEdit = (AutoCompleteTextView)findViewById(R.id.sensor_latitude_edit);
        longitudeTextEdit = (AutoCompleteTextView)findViewById(R.id.sensor_longitude_edit);
        costTextEdit = (AutoCompleteTextView)findViewById(R.id.sensor_cost_edit);
        statusSpinner = (Spinner)findViewById(R.id.sensor_status_spinner);

        addButton = (Button)findViewById(R.id.sensor_add_button);
        updateButton = (Button)findViewById(R.id.sensor_update_button);
        deleteButton = (Button)findViewById(R.id.sensor_delete_button);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            String action = bundle.getString(getString(R.string.intent_launch_action));

            if (action.equals(getString(R.string.intent_launch_action_add))) {

                setTitle("Add Sensor");

                addButton.setVisibility(View.VISIBLE);

                //default SJSU location
                latlong = new String [] {"37.3351916", "-121.8832655"};

            } else if (action.equals(getString(R.string.intent_launch_action_view))) {
                id = bundle.getString(getString(R.string.sensor_list_data_id));
                location = bundle.getString(getString(R.string.sensor_list_data_location));
                status = bundle.getString(getString(R.string.sensor_list_data_status));
                zip = bundle.getString(getString(R.string.sensor_list_data_zip));
                cost = bundle.getString(getString(R.string.sensor_list_data_cost));

                setTitle("Sensor ID: " + id);

                latlong = location.split(", ");
                latitudeTextEdit.setText(latlong[0]);
                longitudeTextEdit.setText(latlong[1]);
                costTextEdit.setText(cost);
                statusSpinner.setSelection(Integer.parseInt(status));

                updateButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        }

        latitudeTextEdit.addTextChangedListener(new myLocationTextWatcher());
        longitudeTextEdit.addTextChangedListener(new myLocationTextWatcher());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapMarker(latlong[0], latlong[1]);
    }

    private void setMapMarker(String lat, String lon) {
        mMap.clear();
        LatLng parkingSpot = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
        mMap.addMarker(new MarkerOptions().position(parkingSpot).title("Cost: " + cost));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingSpot, mMap.getMaxZoomLevel()));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            formView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onUpdateBtnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.prompt_warning_update_sensor_title))
                .setMessage(getString(R.string.prompt_warning_update_sensor_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newLatitude = latitudeTextEdit.getText().toString();
                        String newLongitude = longitudeTextEdit.getText().toString();
                        String newStatus = Integer.toString(statusSpinner.getSelectedItemPosition());
                        String newCost = costTextEdit.getText().toString();

                        showProgress(true);
                        updateSensorInfoTask = new UpdateSensorInfoTask(newLatitude, newLongitude, newStatus, newCost);
                        updateSensorInfoTask.execute((Void) null);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void onDeleteBtnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.prompt_warning_delete_sensor_title))
                .setMessage(getString(R.string.prompt_warning_delete_sensor_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        deleteSensorInfoTask = new DeleteSensorInfoTask();
                        deleteSensorInfoTask.execute((Void) null);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void onAddBtnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.prompt_warning_add_sensor_title))
                .setMessage(getString(R.string.prompt_warning_add_sensor_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String addLatitude = latitudeTextEdit.getText().toString();
                        String addLongitude = longitudeTextEdit.getText().toString();
                        String addStatus = Integer.toString(statusSpinner.getSelectedItemPosition());
                        String addCost = costTextEdit.getText().toString();

                        showProgress(true);
                        addSensorInfoTask = new AddSensorInfoTask(addLatitude, addLongitude, addStatus, addCost);
                        addSensorInfoTask.execute((Void) null);
                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class myLocationTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Check current input are both correct floating numbers
            try {
                Float.parseFloat(latitudeTextEdit.getText().toString());
                Float.parseFloat(longitudeTextEdit.getText().toString());
            } catch (NumberFormatException e) {
                return;
            }

            setMapMarker(latitudeTextEdit.getText().toString(), longitudeTextEdit.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class UpdateSensorInfoTask extends AsyncTask<Void, Void, Boolean> {
        private String latitude;
        private String longitude;
        private String status;
        private String cost;

        UpdateSensorInfoTask(String lat, String lon, String sta, String cos) {
            latitude = lat;
            longitude = lon;
            status = sta;
            cost = cos;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_sensor_updateSensor);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");

                JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                jsonObjectHelper.add(getString(R.string.sensor_list_data_id), id);
                jsonObjectHelper.add(getString(R.string.sensor_list_data_location), latitude + ", " + longitude);
                jsonObjectHelper.add(getString(R.string.sensor_list_data_status), status);
                jsonObjectHelper.add(getString(R.string.sensor_list_data_cost), cost);
                jsonObjectHelper.add(getString(R.string.sensor_list_data_zip), zip);

                returnCode = connectionHelper.request_Output(jsonObjectHelper.getResult());

            } catch (IOException e) {
                return false;
            }

            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            updateSensorInfoTask = null;
            showProgress(false);

            if (success) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                //TODO: Show error?
            }
        }

        @Override
        protected void onCancelled() {
            updateSensorInfoTask = null;
            showProgress(false);
        }
    }

    public class DeleteSensorInfoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_sensor_deleteSensor);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");

                JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                jsonObjectHelper.add(getString(R.string.sensor_list_data_id), id);

                returnCode = connectionHelper.request_Output(jsonObjectHelper.getResult());

            } catch (IOException e) {
                return false;
            }

            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            deleteSensorInfoTask = null;
            showProgress(false);

            if (success) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            } else {
                //TODO: Show error?
            }
        }

        @Override
        protected void onCancelled() {
            deleteSensorInfoTask = null;
            showProgress(false);
        }
    }

    public class AddSensorInfoTask extends AsyncTask<Void, Void, Boolean> {
        private String latitude;
        private String longitude;
        private String status;
        private String cost;

        AddSensorInfoTask(String lat, String lon, String sta, String cos) {
            latitude = lat;
            longitude = lon;
            status = sta;
            cost = cos;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_sensor_addSensor);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");

                JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                jsonObjectHelper.add(getString(R.string.sensor_list_data_location), latitude + ", " + longitude);
                jsonObjectHelper.add(getString(R.string.sensor_list_data_status), status);
                jsonObjectHelper.add(getString(R.string.sensor_list_data_cost), cost);
                //TODO: how to handle zip
                jsonObjectHelper.add(getString(R.string.sensor_list_data_zip), "1");

                returnCode = connectionHelper.request_Output(jsonObjectHelper.getResult());

            } catch (IOException e) {
                return false;
            }

            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            addSensorInfoTask = null;
            showProgress(false);

            if (success) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            } else {
                //TODO: Show error?
            }
        }

        @Override
        protected void onCancelled() {
            addSensorInfoTask = null;
            showProgress(false);
        }
    }
}
