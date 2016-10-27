package com.szabistcarpool.app.szabistcarpool_application.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.szabistcarpool.app.szabistcarpool_application.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttwyf on 5/15/2016.
 */
public class ContactAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public ContactAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Contacts contacts) {
        super.add(contacts);
        list.add(contacts);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ContactHolder contactHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.ridesrow_layout,parent,false);

            contactHolder = new ContactHolder();
            contactHolder.tx_name = (TextView)row.findViewById(R.id.txt_name);
            contactHolder.tx_email = (TextView)row.findViewById(R.id.txt_email);
            contactHolder.tx_mobile = (TextView)row.findViewById(R.id.txt_mobile);
            row.setTag(contactHolder);
        }
        else{
            contactHolder = (ContactHolder)row.getTag();
        }
        Contacts contacts = (Contacts)this.getItem(position);
        contactHolder.tx_name.setText(contacts.getName());
        contactHolder.tx_email.setText(contacts.getEmail());
        contactHolder.tx_mobile.setText(contacts.getMobielno());
        return row;
    }

    static class ContactHolder{
        TextView tx_name,tx_email,tx_mobile;
    }
}
