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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by KhizerHasan on 4/29/2016.
 */
public class EditAdminActivity extends AppCompatActivity {

    private String id;
    private String name;
    private String address;
    private String loginName;

    private View progressView;
    private View formView;

    private AutoCompleteTextView nameTextEdit;
    private AutoCompleteTextView addressTextEdit;
    private AutoCompleteTextView loginNameTextEdit;
    private AutoCompleteTextView passwordTextEdit;
    private AutoCompleteTextView passwordAgainTextEdit;

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;

    private UpdateAdminInfoTask updateAdminInfoTask = null;
    private DeleteAdminInfoTask deleteAdminInfoTask = null;
    private AddAdminInfoTask addAdminInfoTask = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin);

        progressView = findViewById(R.id.update_admin_info_progress);
        formView = findViewById(R.id.edit_admin_info_form);

        nameTextEdit = (AutoCompleteTextView)findViewById(R.id.admin_name_edit);
        addressTextEdit = (AutoCompleteTextView)findViewById(R.id.admin_address_edit);
        loginNameTextEdit = (AutoCompleteTextView)findViewById(R.id.admin_loginName_edit);
        passwordTextEdit = (AutoCompleteTextView)findViewById(R.id.admin_password_edit);
        passwordAgainTextEdit = (AutoCompleteTextView)findViewById(R.id.admin_password_edit_again);

        addButton = (Button)findViewById(R.id.admin_add_button);
        updateButton = (Button)findViewById(R.id.admin_update_button);
        deleteButton = (Button)findViewById(R.id.admin_delete_button);


        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {

            String action = bundle.getString(getString(R.string.intent_launch_action));

            if (action.equals(getString(R.string.intent_launch_action_add))) {
                setTitle("Add Admin User");
                addButton.setVisibility(View.VISIBLE);

            } else if (action.equals(getString(R.string.intent_launch_action_view))) {
                id = bundle.getString(getString(R.string.user_list_data_id));
                name = bundle.getString(getString(R.string.user_list_data_name));
                address = bundle.getString(getString(R.string.user_list_data_address));
                loginName = bundle.getString(getString(R.string.user_list_data_loginName));

                setTitle("Admin ID: " + id);
                nameTextEdit.setText(name);
                addressTextEdit.setText(address);
                loginNameTextEdit.setText(loginName);

                updateButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        }
    }

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
                .setTitle(getString(R.string.prompt_warning_update_admin_title))
                .setMessage(getString(R.string.prompt_warning_update_admin_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = nameTextEdit.getText().toString();
                        String newAddress= addressTextEdit.getText().toString();
                        String newLoginName = loginNameTextEdit.getText().toString();
                        String newPassword = passwordTextEdit.getText().toString();
                        String newPasswordAgain = passwordAgainTextEdit.getText().toString();
                        showProgress(true);

                        updateAdminInfoTask = new UpdateAdminInfoTask(newName,newAddress,newLoginName, newPassword);
                        updateAdminInfoTask.execute((Void) null);
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
                .setTitle(getString(R.string.prompt_warning_add_admin_title))
                .setMessage(getString(R.string.prompt_warning_add_admin_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String addName = nameTextEdit.getText().toString();
                        String addAddress = addressTextEdit.getText().toString();
                        String addLoginName = loginNameTextEdit.getText().toString();
                        String addPassword = passwordTextEdit.getText().toString();
                        String addPasswordAgain = passwordAgainTextEdit.getText().toString();
                        showProgress(true);

                        addAdminInfoTask = new AddAdminInfoTask(addName, addAddress, addLoginName, addPassword);
                        addAdminInfoTask.execute((Void) null);
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
                .setTitle(getString(R.string.prompt_warning_delete_admin_title))
                .setMessage(getString(R.string.prompt_warning_delete_admin_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        deleteAdminInfoTask = new DeleteAdminInfoTask();
                        deleteAdminInfoTask.execute((Void) null);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public class AddAdminInfoTask extends AsyncTask<Void, Void, Boolean> {
        private String name;
        private String address;
        private String loginName;
        private String password;

        AddAdminInfoTask(String nam, String addr, String login, String pass) {
            name = nam;
            address = addr;
            loginName = login;
            password = pass;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_user_addUser);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");

                JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                jsonObjectHelper.add(getString(R.string.user_list_data_name), name);
                jsonObjectHelper.add(getString(R.string.user_list_data_address), address);
                jsonObjectHelper.add(getString(R.string.user_list_data_loginName), loginName);
                jsonObjectHelper.add(getString(R.string.user_list_data_password), password);

                returnCode = connectionHelper.request_Output(jsonObjectHelper.getResult());

            } catch (IOException e) {
                return false;
            }

            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            addAdminInfoTask = null;
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
            addAdminInfoTask = null;
            showProgress(false);
        }
    }

    public class UpdateAdminInfoTask extends AsyncTask<Void, Void, Boolean> {
        private String name;
        private String address;
        private String loginName;
        private String password;

        UpdateAdminInfoTask(String nam, String addrss, String login, String pass) {
            name = nam;
            address = addrss;
            loginName = login;
            password = pass;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_user_updateUser);
            HttpConnectionHelper connectionHelper;
            int returnCode;
            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");
                JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                jsonObjectHelper.add(getString(R.string.user_list_data_id), id);
                jsonObjectHelper.add(getString(R.string.user_list_data_name), name);
                jsonObjectHelper.add(getString(R.string.user_list_data_address), address);
                jsonObjectHelper.add(getString(R.string.user_list_data_loginName), loginName);
                jsonObjectHelper.add(getString(R.string.user_list_data_password), password);
                returnCode = connectionHelper.request_Output(jsonObjectHelper.getResult());
            } catch (IOException e) {
                return false;
            }
            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            updateAdminInfoTask = null;
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
            updateAdminInfoTask = null;
            showProgress(false);
        }
    }

    public class DeleteAdminInfoTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.server_api_root) + getString(R.string.server_api_user_deleteUser);
            HttpConnectionHelper connectionHelper;
            int returnCode;

            try {
                connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                connectionHelper.setRequestProperty("Content-type", "application/json");

                JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                jsonObjectHelper.add(getString(R.string.user_list_data_id), id);

                returnCode = connectionHelper.request_Output(jsonObjectHelper.getResult());

            } catch (IOException e) {
                return false;
            }

            return HttpURLConnection.HTTP_OK == returnCode;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            deleteAdminInfoTask = null;
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
            deleteAdminInfoTask = null;
            showProgress(false);
        }
    }
}
