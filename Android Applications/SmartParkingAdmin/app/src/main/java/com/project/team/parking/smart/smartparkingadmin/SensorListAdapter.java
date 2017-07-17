package com.project.team.parking.smart.smartparkingadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by ChenYu Wu on 4/22/2016.
 */
public class SensorListAdapter extends BaseAdapter {
    List<String> sensorID;
    List<String> sensorLocation;
    List<String> sensorStatus;
    List<String> sensorZip;
    List<String> sensorCost;

    private static LayoutInflater layoutInflater = null;

    Context mainContext;

    SensorListAdapter(Context context, List<String> id, List<String> location, List<String> status, List<String> zip, List<String> cost) {
        mainContext = context;

        sensorID = id;
        sensorLocation = location;
        sensorStatus = status;
        sensorZip = zip;
        sensorCost = cost;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sensorID.size();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ListItemHolder listItemHolder = new ListItemHolder();
        View listItemView = layoutInflater.inflate(R.layout.list_sensor, null);
        listItemHolder.idTextView = (TextView)listItemView.findViewById(R.id.sensor_id);
        listItemHolder.locationTextView = (TextView)listItemView.findViewById(R.id.sensor_location);
        listItemHolder.statusImageView = (ImageView)listItemView.findViewById(R.id.sensor_status);


        listItemHolder.idTextView.setText("ID: " + sensorID.get(position));

        String location = sensorLocation.get(position);
        String [] lonlat = location.split(", ");
        listItemHolder.locationTextView.setText(String.format("%3.6f, %3.6f", Float.parseFloat(lonlat[0]), Float.parseFloat(lonlat[1])));

        listItemHolder.statusImageView.setImageResource(listItemHolder.statusImg[Integer.parseInt(sensorStatus.get(position))]);

        listItemView.setOnClickListener(new SensorManagementFragment.MyItemOnClickListener(position));

        return listItemView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ListItemHolder {
        TextView idTextView;
        TextView locationTextView;
        ImageView statusImageView;

        //TODO: add rest of the images
        int [] statusImg = {R.drawable.sensor_free, R.drawable.sensor_free, R.drawable.sensor_occupied, R.drawable.sensor_unavailable, R.drawable.sensor_inactive};
    }
}
