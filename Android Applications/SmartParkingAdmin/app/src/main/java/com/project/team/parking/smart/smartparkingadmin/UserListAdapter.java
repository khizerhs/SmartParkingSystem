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
 * Created by KhizerHasan on 4/25/2016.
 */
public class UserListAdapter extends BaseAdapter {
    List<String> userID;
    List<String> userName;
    List<String> userAddress;
    List<String> userLoginName;

    private static LayoutInflater layoutInflater = null;

    Context mainContext;

    UserListAdapter(Context context, List<String> id, List<String> name, List<String> address, List<String> loginName) {
        mainContext = context;
        userID = id;
        userName = name;
        userAddress= address;
        userLoginName= loginName;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userID.size();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ListItemHolder listItemHolder = new ListItemHolder();
        View listItemView = layoutInflater.inflate(R.layout.list_user, null);
        listItemHolder.nameTextView = (TextView)listItemView.findViewById(R.id.user_name);
        listItemHolder.usernameTextView= (TextView)listItemView.findViewById(R.id.loginName);
        listItemHolder.usernameTextView.setText(userLoginName.get(position));
        listItemHolder.nameTextView.setText(userName.get(position));
        listItemView.setOnClickListener(new AdminManagementFragment.MyItemOnClickListener(position));
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
        TextView usernameTextView;
        TextView nameTextView;
    }
}


