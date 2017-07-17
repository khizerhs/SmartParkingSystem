package com.project.team.parking.smart.smartparkingadmin;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorManagementFragment extends Fragment {

    private static final int ACTIVITYREQUESTCODE_EDIT = 0;

    private GetSensorInfoTask getSensorInfoTask = null;

    private View progressView;
    private View formView;
    private ListView listView;

    private static JSONObject sensorInfoJSON;

    private static SensorListAdapter sensorListAdapter;

    private static SensorManagementFragment sensorManagementFragment;

    public SensorManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_sensor_management, container, false);
        progressView = view.findViewById(R.id.get_sensor_info_progress);
        formView = view.findViewById(R.id.sensor_form);
        listView = (ListView)view.findViewById(R.id.sensorlistView);
        sensorManagementFragment = this;
        showProgress(true);
        getSensorInfoTask = new GetSensorInfoTask(this.getContext());
        getSensorInfoTask.execute((Void) null);
        return view;
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

    public static class MyItemOnClickListener implements View.OnClickListener {
        private int position;

        MyItemOnClickListener(int pos) {
           position = pos;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(sensorManagementFragment.getContext(), EditSensorActivity.class);
            intent.putExtra(sensorManagementFragment.getString(R.string.intent_launch_action), sensorManagementFragment.getString(R.string.intent_launch_action_view));
            intent.putExtra(sensorManagementFragment.getString(R.string.sensor_list_data_id), sensorListAdapter.sensorID.get(position));
            intent.putExtra(sensorManagementFragment.getString(R.string.sensor_list_data_location), sensorListAdapter.sensorLocation.get(position));
            intent.putExtra(sensorManagementFragment.getString(R.string.sensor_list_data_status), sensorListAdapter.sensorStatus.get(position));
            intent.putExtra(sensorManagementFragment.getString(R.string.sensor_list_data_zip), sensorListAdapter.sensorZip.get(position));
            intent.putExtra(sensorManagementFragment.getString(R.string.sensor_list_data_cost), sensorListAdapter.sensorCost.get(position));
            sensorManagementFragment.startActivityForResult(intent, SensorManagementFragment.ACTIVITYREQUESTCODE_EDIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ACTIVITYREQUESTCODE_EDIT == requestCode && Activity.RESULT_OK == resultCode) {
            //TODO: refresh list
            showProgress(true);
            getSensorInfoTask = new GetSensorInfoTask(this.getContext());
            getSensorInfoTask.execute((Void) null);
        }
    }

    public class GetSensorInfoTask extends AsyncTask<Void, Void, Boolean> {
        private Context mainContext;

        GetSensorInfoTask(Context context) {
            mainContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_sensor_getAllSensors);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");
                returnCode = connectionHelper.request_InOutput(HttpConnectionHelper.DEFAULT_READ_TIME_OUT, "");
                if (HttpURLConnection.HTTP_OK == returnCode) {
                    sensorInfoJSON = new JSONObject(connectionHelper.getResponseString());
                }
            } catch (IOException e) {
                return false;
            } catch (JSONException e) {
                return false;
            }
            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getSensorInfoTask = null;
            showProgress(false);

            if (success) {
                //Update list
                try {
                    JSONArray sensorListArr = sensorInfoJSON.getJSONArray(getString(R.string.sensor_list_root));
                    List<String> idList = new ArrayList<>();
                    List<String> locationList = new ArrayList<>();
                    List<String> statusList = new ArrayList<>();
                    List<String> zipList = new ArrayList<>();
                    List<String> costList = new ArrayList<>();

                    for(int sensorIndex = 0; sensorIndex < sensorListArr.length(); ++sensorIndex) {
                        JSONObject sensorData = sensorListArr.getJSONObject(sensorIndex);

                        //status equal 0 means it had been deleted, don't show
                        if (!sensorData.getString(getString(R.string.sensor_list_data_status)).equals("0")) {
                            idList.add(sensorData.getString(getString(R.string.sensor_list_data_id)));
                            locationList.add(sensorData.getString(getString(R.string.sensor_list_data_location)));
                            statusList.add(sensorData.getString(getString(R.string.sensor_list_data_status)));
                            zipList.add(sensorData.getString(getString(R.string.sensor_list_data_zip)));
                            costList.add(sensorData.getString(getString(R.string.sensor_list_data_cost)));
                        }
                    }

                    sensorListAdapter = new SensorListAdapter(mainContext, idList, locationList, statusList, zipList, costList);
                    listView.setAdapter(sensorListAdapter);
                } catch (JSONException e) {

                }
            } else {
                //TODO: Show no content?
            }
        }

        @Override
        protected void onCancelled() {
            getSensorInfoTask = null;
            showProgress(false);
        }
    }

}
