package com.myapplicationdev.android.c302_p07_adressbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ListView lvContact;
    ArrayList<Contact> alContact;
    ArrayAdapter<Contact> aaContact;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = findViewById(R.id.lvContact);
        client = new AsyncHttpClient();
    }

    //refresh with latest contact data whenever this activity resumes
    @Override
    protected void onResume() {
        super.onResume();

        //TODO: call getListOfContacts.php to retrieve all contact details
        alContact = new ArrayList<>();

        client.get("http://10.0.2.2/C302_P07/getListOfContacts.php", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.i("JSON Results: ", response.toString());

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);

                        int contactId = jsonObj.getInt("id");
                        String firstName = jsonObj.getString("firstname");
                        String lastName = jsonObj.getString("lastname");
                        String mobile = jsonObj.getString("mobile");

                        Contact contact = new Contact(contactId, firstName, lastName, mobile);
                        alContact.add(contact);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aaContact = new ContactAdapter(getApplicationContext(), R.layout.contact_row, alContact);
                lvContact.setAdapter(aaContact);

                lvContact.setOnItemClickListener((parent, view, position, id) -> {

                    Contact selectedContact = alContact.get(position);
                    Intent i = new Intent(getBaseContext(), ViewContactDetailsActivity.class);
                    i.putExtra("contact_id", selectedContact.getContactId());
                    startActivity(i);

                });
            }
        });


    }//end onResume


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            Intent intent = new Intent(getApplicationContext(), CreateContactActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}