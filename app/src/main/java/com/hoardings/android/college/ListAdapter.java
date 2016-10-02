package com.hoardings.android.college;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ListAdapter extends ArrayAdapter<StudentDetails> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<StudentDetails> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println("twist");
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.mobile, null);
        }

        StudentDetails p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.user_name);
            TextView email = (TextView) v.findViewById(R.id.email);
            TextView phone = (TextView) v.findViewById(R.id.phone_number);

            name.setText(p.getName());
            email.setText(p.getEmail());
            phone.setText(p.getEmail());

            try {
                ImageView tt2 = (ImageView) v.findViewById(R.id.image);
                System.out.println(" image url : "+ p.getPohto());
                tt2.setImageURI(Uri.parse(new File(p.getPohto()).toString()));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return v;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("", "Error getting bitmap", e);
        }
        return bm;
    }

}