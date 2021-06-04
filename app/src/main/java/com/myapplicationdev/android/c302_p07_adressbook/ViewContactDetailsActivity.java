package com.myapplicationdev.android.c302_p07_adressbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ViewContactDetailsActivity extends AppCompatActivity {

    EditText etFirstName, etLastName, etMobile;
    Button btnUpdate, btnDelete;
    int contactId;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        client = new AsyncHttpClient();

        Intent intent = getIntent();
        contactId = intent.getIntExtra("contact_id", -1);

        //TODO: call getContactDetails.php with the id as a parameter
        //TODO: set the text fields with the data retrieved

        RequestParams params = new RequestParams();
        params.put("id", String.valueOf(contactId));
        client.get("http://10.0.2.2/C302_P07/getContactById.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("JSON Results: ", response.toString());

                    etFirstName.setText(response.getString("firstname"));
                    etLastName.setText(response.getString("lastname"));
                    etMobile.setText(response.getString("mobile"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        btnUpdate.setOnClickListener(this::btnUpdateOnClick);

        btnDelete.setOnClickListener(this::btnDeleteOnClick);
    }//end onCreate


    void btnUpdateOnClick(View v) {

        //TODO: retrieve the updated text fields and set as parameters to be passed to updateContact.php
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(contactId));
        params.add("FirstName", etFirstName.getText().toString());
        params.add("LastName", etLastName.getText().toString());
        params.add("Mobile", etMobile.getText().toString());

        client.post("http://10.0.2.2/C302_P07/updateContact.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("JSON Results: ", response.toString());

                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }//end onSuccess
        });
    }//end btnUpdateOnClick

    void btnDeleteOnClick(View v) {
        //TODO: retrieve the id and set as parameters to be passed to deleteContact.php
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(contactId));

        client.post("http://10.0.2.2/C302_P07/deleteContact.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("JSON Results: ", response.toString());

                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }//end onSuccess
        });

    }//end btnDeleteOnClick

}//end class