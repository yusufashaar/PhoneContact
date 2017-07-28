package app.yusuf.com.phonecontact.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.yusuf.com.phonecontact.R;
import app.yusuf.com.phonecontact.db.Contact;
import app.yusuf.com.phonecontact.db.ContactHelper;
/**
 * Created by Asus on 7/27/2017.
 */

public class ViewContactActivity extends AppCompatActivity  {
    private int contact_id;
    private TextView name, organization, phone, address;
    Button btnCall;
    private String phoneStr, nameStr;


    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        name = (TextView) findViewById(R.id.tv_name);
        organization = (TextView) findViewById(R.id.tv_organization);
        phone = (TextView) findViewById(R.id.tv_phone);
        address = (TextView) findViewById(R.id.tv_address);

        btnCall = (Button)  findViewById(R.id.btCall);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_call);
                TextView textv =(TextView)dialog.findViewById(R.id.text);
                textv.setText("Call" +" "+ nameStr + " ?" );
                ImageView image = (ImageView) findViewById(R.id.images);

                Button btnYes = (Button) dialog.findViewById(R.id.btYes);
                Button btnNo = (Button) dialog.findViewById(R.id.btNo);

                dialog.show();

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call(phoneStr);
                        dialog.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });


            }
        });

    }
    @Override
    protected void onResume() {

        Bundle bundle = getIntent().getExtras();
        contact_id = bundle.getInt("contact_id");

        viewContact(contact_id);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.view_contact_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_view_contact:
                Bundle bundle = new Bundle();
                bundle.putInt("contact_id", contact_id);
                Intent updateContactActivity = new Intent(ViewContactActivity.this, EditContactActivity.class);
                updateContactActivity.putExtras(bundle);
                startActivity(updateContactActivity);
                break;

            case R.id.menu_delete_contact:
                /* Delete contact -> load phonebook activity */
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void viewContact(int id) {

        ContactHelper db = new ContactHelper(this);
        Contact contact = db.getContact(id);

        name.setText(contact.getName());
        organization.setText(contact.getOrganization());
        phone.setText(contact.getPhone());
        address.setText(contact.getAddress());
        phoneStr = contact.getPhone();
        nameStr = contact.getName();

    }
    public void call(String phoneStr) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneStr));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(callIntent);
    }


}
