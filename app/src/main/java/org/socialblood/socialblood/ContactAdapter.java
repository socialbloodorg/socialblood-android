package org.socialblood.socialblood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Anuraag on 5/19/15.
 */
public class ContactAdapter extends ArrayAdapter<Contact>{
        private TextView name,city,distance,bloodtype;
        private ImageView pfp;
        public ContactAdapter(Context context, List<Contact> contactArrayList) {
            super(context, 0, contactArrayList);
        }

    public static Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
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

//        if(contact.getProfilePicture()!= null) {
//                pfp.setImageURI(contact.getProfilePicture());
//                Log.d("Pro",contact.getProfilePicture().toString());
            try {
                Bitmap bmp = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(contact.getProfilePicture()));
                pfp.setImageBitmap(getclip(bmp));

            }catch (FileNotFoundException f) {
                Log.d(f.toString(),f.toString());
                pfp.setImageResource(R.drawable.user);

            }catch (NullPointerException n) {
                Log.d(n.toString(),n.toString());
                pfp.setImageResource(R.drawable.user);

            }
//        }else{
//                pfp.setImageResource(R.drawable.user);
//            }


        return rootView;
    }



}
