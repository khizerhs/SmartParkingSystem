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
public class AdminManagementFragment extends Fragment {

    private static final int ACTIVITYREQUESTCODE_EDIT = 0;

    private GetUserInfoTask getUserInfoTask = null;

    private View progressView;
    private View formView;
    private ListView listView;

    private static UserListAdapter userListAdapter;

    private static JSONObject userInfoJSON;

    private static AdminManagementFragment adminManagementFragment;

    public AdminManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_management, container, false);
        progressView = view.findViewById(R.id.get_user_info_progress);
        formView = view.findViewById(R.id.user_form);
        listView = (ListView)view.findViewById(R.id.userlistView);
        adminManagementFragment = this;
        showProgress(true);
        getUserInfoTask = new GetUserInfoTask(this.getContext());
        getUserInfoTask.execute((Void) null);

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
            Intent intent = new Intent(adminManagementFragment.getContext(), EditAdminActivity.class);
            intent.putExtra(adminManagementFragment.getString(R.string.intent_launch_action), adminManagementFragment.getString(R.string.intent_launch_action_view));
            intent.putExtra(adminManagementFragment.getString(R.string.user_list_data_id), userListAdapter.userID.get(position));
            intent.putExtra(adminManagementFragment.getString(R.string.user_list_data_name), userListAdapter.userName.get(position));
            intent.putExtra(adminManagementFragment.getString(R.string.user_list_data_address),userListAdapter.userAddress.get(position));
            intent.putExtra(adminManagementFragment.getString(R.string.user_list_data_loginName), userListAdapter.userLoginName.get(position));

            adminManagementFragment.startActivityForResult(intent, AdminManagementFragment.ACTIVITYREQUESTCODE_EDIT);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ACTIVITYREQUESTCODE_EDIT == requestCode && Activity.RESULT_OK == resultCode) {
            //TODO: refresh list
            showProgress(true);
            getUserInfoTask = new GetUserInfoTask(this.getContext());
            getUserInfoTask.execute((Void) null);
        }

    }

    public class GetUserInfoTask extends AsyncTask<Void, Void, Boolean> {
        private Context mainContext;
        GetUserInfoTask(Context context) {
            mainContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_user_getAllUsers);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");
                returnCode = connectionHelper.request_InOutput(HttpConnectionHelper.DEFAULT_READ_TIME_OUT, "");


                if (HttpURLConnection.HTTP_OK == returnCode) {
                    userInfoJSON = new JSONObject(connectionHelper.getResponseString());
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
            getUserInfoTask = null;
            showProgress(false);
            if (success) {
                //Update list
                try {
                    JSONArray userListArr = userInfoJSON.getJSONArray(getString(R.string.user_list_root));
                    List<String> idList = new ArrayList<>();
                    List<String> nameList = new ArrayList<>();
                    List<String> addressList = new ArrayList<>();
                    List<String> loginNameList = new ArrayList<>();

                    for(int userIndex = 0; userIndex < userListArr.length(); ++userIndex) {
                        JSONObject userData = userListArr.getJSONObject(userIndex);
                        idList.add(userData.getString(getString(R.string.user_list_data_id)));
                        nameList.add(userData.getString(getString(R.string.user_list_data_name)));
                        addressList.add(userData.getString(getString(R.string.user_list_data_address)));
                        loginNameList.add(userData.getString(getString(R.string.user_list_data_loginName)));
                    }
                    userListAdapter = new UserListAdapter(mainContext, idList, nameList, addressList,loginNameList);
                    listView.setAdapter(userListAdapter);
                } catch (JSONException e) {

                }
            } else {
                //TODO: Show no content?
            }
        }

        @Override
        protected void onCancelled() {
            getUserInfoTask = null;
            showProgress(false);
        }
    }
}
