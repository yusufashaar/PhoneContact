package app.yusuf.com.phonecontact.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.yusuf.com.phonecontact.R;

import app.yusuf.com.phonecontact.db.Contact;
import app.yusuf.com.phonecontact.db.ContactHelper;

/**
 * Created by Asus on 7/27/2017.
 */

public class EditContactActivity extends AppCompatActivity {

    private int contact_id;
    private EditText name, organization, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        name = (EditText) findViewById(R.id.ed_name);
        organization = (EditText) findViewById(R.id.ed_organization);
        phone = (EditText) findViewById(R.id.ed_phone);
        address = (EditText) findViewById(R.id.ed_address);
        Button btn_save = (Button) findViewById(R.id.btn_update);

        btn_save.setOnClickListener(onClickUpdateBtn);

    }
    @Override
    protected void onResume() {

        Bundle bundle = getIntent().getExtras();
        contact_id = bundle.getInt("contact_id");

        loadContact(contact_id);

        super.onResume();
    }
    private View.OnClickListener onClickUpdateBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Contact contact = new Contact();
            contact.set_id(contact_id);
            contact.setName(name.getText().toString());
            contact.setOrganization(organization.getText().toString());
            contact.setPhone(phone.getText().toString());
            contact.setAddress(address.getText().toString());

            ContactHelper db = new ContactHelper(getApplicationContext());
            db.updateContact(contact);
            finish();
        }
    };

    private void loadContact(int id) {
        ContactHelper db = new ContactHelper(this);
        Contact contact = db.getContact(id);

        name.setText(contact.getName());
        organization.setText(contact.getOrganization());
        phone.setText(contact.getPhone());
        address.setText(contact.getAddress());

    }
}
