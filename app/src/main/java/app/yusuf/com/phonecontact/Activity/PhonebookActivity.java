package app.yusuf.com.phonecontact.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import app.yusuf.com.phonecontact.R;
import app.yusuf.com.phonecontact.db.ContactHelper;
/**
 * Created by Asus on 7/27/2017.
 */

public class PhonebookActivity extends AppCompatActivity {

    private ListView lv_contacts;
    private Cursor contacts;
    private ContactCursorAdapter contactCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook);

        lv_contacts = (ListView) findViewById(R.id.lv_contacts);

    }



    @Override
    protected void onResume() {

        ContactHelper db = new ContactHelper(this);
        contacts = db.getContacts();

        contactCursorAdapter = new ContactCursorAdapter(this, contacts, 0);

        lv_contacts.setAdapter(contactCursorAdapter);

        registerForContextMenu(lv_contacts);

        lv_contacts.setOnItemClickListener(onItemClickListener);

        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_add_contact:
                startActivity(new Intent(PhonebookActivity.this, AddContactActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            int _id = contacts.getInt(contacts.getColumnIndex(ContactHelper._ID));
            Bundle bundle = new Bundle();
            bundle.putInt("contact_id", _id);
            Intent viewActivity = new Intent(PhonebookActivity.this, ViewContactActivity.class);
            viewActivity.putExtras(bundle);
            startActivity(viewActivity);
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        switch (v.getId()){

            case R.id.lv_contacts:
                String name = contacts.getString(contacts.getColumnIndex(ContactHelper.COL_NAME));
                menu.setHeaderTitle(name);
                String [] menuItems = getResources().getStringArray(R.array.context_menu);
                for (int i=0;i<menuItems.length;i++){
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                }
                break;

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int menuItemIndex = item.getItemId();
        String [] menuItems = getResources().getStringArray(R.array.context_menu);
        String menuItemName = menuItems[menuItemIndex];

        int _id = contacts.getInt(contacts.getColumnIndex(ContactHelper._ID));
        String name = contacts.getString(contacts.getColumnIndex(ContactHelper.COL_NAME));

        switch (menuItemName) {

            case "Edit":
                Bundle bundle = new Bundle();
                bundle.putInt("contact_id", _id);
                Intent updateContactActivity = new Intent(PhonebookActivity.this, EditContactActivity.class);
                updateContactActivity.putExtras(bundle);
                startActivity(updateContactActivity);
                break;

            case "Delete":
                confirmationDelete(_id, name).show();
                break;

        }

        return true;
    }

    private class ContactCursorAdapter extends CursorAdapter {

        public ContactCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.row_contact, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // Find fields to populate in inflated template
            TextView tv_contact_id = (TextView) view.findViewById(R.id.tv_contact_id);
            TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            TextView tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);

            // Extract properties from cursor
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ContactHelper._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactHelper.COL_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactHelper.COL_PHONE));

            // Populate fields with extracted properties
            tv_contact_id.setText(String.valueOf(id));
            tv_contact_name.setText(name);
            tv_contact_phone.setText(phone);

        }
    }

    public AlertDialog.Builder confirmationDelete(final int id, String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("You're about to delete '"+name+"'; \nThis is action irreversible");

        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                ContactHelper db = new ContactHelper(getApplicationContext());
                db.deleteContact(id);

                contacts = db.getContacts();
                contactCursorAdapter.swapCursor(contacts);
                lv_contacts.setAdapter(contactCursorAdapter);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return alertDialog;
    }

}
