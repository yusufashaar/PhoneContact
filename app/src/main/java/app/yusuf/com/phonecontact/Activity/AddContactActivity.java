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

public class AddContactActivity extends AppCompatActivity {


    private EditText name, organization, phone, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        name = (EditText) findViewById(R.id.tv_name);
        organization = (EditText) findViewById(R.id.tv_organization);
        phone = (EditText) findViewById(R.id.tv_phone);
        address = (EditText) findViewById(R.id.tv_address);
        Button save = (Button) findViewById(R.id.btn_save);

        save.setOnClickListener(onClickRegisterBtn);

    }
    private View.OnClickListener onClickRegisterBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addContact();
        }
    };
    private void addContact() {

        Contact contact = new Contact();
        contact.setName(name.getText().toString());
        contact.setOrganization(organization.getText().toString());
        contact.setPhone(phone.getText().toString());
        contact.setAddress(address.getText().toString());

        ContactHelper db = new ContactHelper(this);
        db.addContact(contact);
        finish();

    }


}
