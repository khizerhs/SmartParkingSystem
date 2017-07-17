package com.project.team.parking.smart.smartparkingadmin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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
public class StasticFragment extends Fragment {

    private View progressView;
    private PieChart mChart;



    private static JSONObject sensorStatusJSON;
    private static GetSensorStatisticTask sensorStatisticTask;


    private static String[] ParkingStatus = new String[] {
            "Free", "Occupied", "Unavailable", "Inactive" //, "Undefined"
    };
    private static int[] ParkingStatusValues ;

    public StasticFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_stastic, container, false);


        progressView = view.findViewById(R.id.get_sensor_statistic_info_progress);
        mChart = (PieChart) view.findViewById(R.id.pieChart);

        showProgress(true);

        sensorStatisticTask = new GetSensorStatisticTask(this.getContext());
        sensorStatisticTask.execute((Void) null);


        return view;

    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(ParkingStatusValues[i],i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(ParkingStatus[i % ParkingStatus.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Sensor Status");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

      /*  for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);*/

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        /*for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);*/

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        //data.setValueTypeface(tf);

        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
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
        }
    }


    public class GetSensorStatisticTask extends AsyncTask<Void, Void, Boolean> {
        private Context mainContext;

        GetSensorStatisticTask(Context context) {
            mainContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_sensor_status);
            HttpConnectionHelper connectionHelper;
            int returnCode;
            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");
                returnCode = connectionHelper.request_InOutput(HttpConnectionHelper.DEFAULT_READ_TIME_OUT, "");

                if (HttpURLConnection.HTTP_OK == returnCode) {
                    sensorStatusJSON = new JSONObject(connectionHelper.getResponseString());
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
            sensorStatisticTask = null;
            showProgress(false);
            if (success) {
                try {


                    int freeSensors= 0;
                    int occupiedSensors = 0;
                    int unavailableSensors =0;
                    int inactiveSensors = 0;


                        freeSensors = sensorStatusJSON.getInt(getString(R.string.sensor_status_free));
                        occupiedSensors = sensorStatusJSON.getInt(getString(R.string.sensor_status_occupied));
                        unavailableSensors = sensorStatusJSON.getInt(getString(R.string.sensor_status_unavailable));
                        inactiveSensors = sensorStatusJSON.getInt(getString(R.string.sensor_status_inactive));

                        ParkingStatusValues = new int[]{
                                freeSensors,occupiedSensors,unavailableSensors,inactiveSensors
                        };


                        mChart.setUsePercentValues(true);
                        mChart.setDescription("");
                        mChart.setExtraOffsets(5, 10, 5, 5);
                        //        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

                        mChart.setDrawHoleEnabled(true);
                        mChart.setHoleColor(Color.WHITE);

                        mChart.setTransparentCircleColor(Color.WHITE);
                        mChart.setTransparentCircleAlpha(110);

                        mChart.setHoleRadius(58f);
                        mChart.setTransparentCircleRadius(61f);

                        mChart.setDrawCenterText(true);

                        mChart.setRotationAngle(0);
                        // enable rotation of the chart by touch
                        mChart.setRotationEnabled(true);
                        mChart.setHighlightPerTapEnabled(true);

                        setData(3, freeSensors+occupiedSensors+unavailableSensors+inactiveSensors );

                } catch (JSONException e) {

                }
            } else {
                //TODO: Show no content?
            }
        }

        @Override
        protected void onCancelled() {
            sensorStatisticTask = null;
            showProgress(false);
        }
    }


}
