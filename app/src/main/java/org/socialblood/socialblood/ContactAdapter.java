package org.socialblood.socialblood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anuraag on 5/19/15.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
        private TextView name,city,distance,bloodtype;
        private ImageView pfp;
        public ContactAdapter(Context context, ArrayList<Contact> contactArrayList) {
            super(context, 0, contactArrayList);
        }

        @Override
        public View getView(int position, View rootView, ViewGroup parent) {
            // Get the data item for this position
            Contact contact = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (rootView == null) {
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.contactitem, parent, false);
            }
            //Set variables
            name = (TextView)rootView.findViewById(R.id.username);
            city = (TextView)rootView.findViewById(R.id.usercity);
            distance = (TextView)rootView.findViewById(R.id.distancetext);
            bloodtype = (TextView)rootView.findViewById(R.id.bloodtype);
            pfp = (ImageView)rootView.findViewById(R.id.userImage);

            //Change texts
            name.setText(contact.getName());
            city.setText(contact.getLocation());
            distance.setText(contact.getDistance());
            bloodtype.setText(contact.getBloodType());
            pfp.setImageDrawable(contact.getProfilePicture());


        return rootView;
    }
}
