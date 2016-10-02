package com.hoardings.android.college;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoardings.android.college.db.FeedReaderDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class ListActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private static FeedReaderDbHelper mDbHelper;
    private List<StudentDetails> studentDetailses;
    private ListView listView;
    private ArrayAdapter adapter;
    int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE;

    private String photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks);
        mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        studentDetailses = getStudentDetails();
        adapter = new ListAdapter(this, R.layout.student, studentDetailses);
        System.out.println("Printing the list");
        for (StudentDetails studentDetails : studentDetailses) {
            System.out.println(studentDetails.getEmail() + " " + studentDetails.getName() + " " + studentDetails.getPhone());
        }
        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        System.out.println("click" + listView.isClickable());

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doMyThing();
            }
        });
//        LayoutInflater vi;
//        vi = LayoutInflater.from(getApplicationContext());
//        View v = vi.inflate(R.layout.student, null);
//        Button delete = (Button) v.findViewById(R.id.studentDelete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userName = ((TextView)v.findViewById(R.id.studentName)).getText().toString();
//                String email = ((TextView)v.findViewById(R.id.studentEmail)).getText().toString();
//                String phone = ((TextView)v.findViewById(R.id.studentPhone)).getText().toString();
//                System.out.println("Deleting user : " + userName + " email " + email + " phone  " + phone);
//
//                String selection = FeedReaderDbHelper.FeedEntry.COLUMN_NAME_EMAIL + "  = ? and " +
//                        FeedReaderDbHelper.FeedEntry.COLUMN_NAME_USERNAME + "  = ? and " +
//                        FeedReaderDbHelper.FeedEntry.COLUMN_NAME_PHONE + "  = ? ";
//// Specify arguments in placeholder order.
//                String[] selectionArgs = { email,userName,phone };
//// Issue SQL statement.
// SQLiteDatabase db = mDbHelper.getWritableDatabase();
//
//                db.delete(FeedReaderDbHelper.FeedEntry.TABLE_NAME, selection, selectionArgs);
//
//
//            }
//        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                System.out.println(parent);
                System.out.println(view);
                System.out.println(position);
                System.out.println(parent);
                System.out.println(id);
                System.out.println("clicked");
                showUpdateView();
            }
        });
    }

    private List<StudentDetails> getStudentDetails() {

        List<StudentDetails> studentDetailses = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedReaderDbHelper.FeedEntry._ID,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_EMAIL,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_PHONE,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_USERNAME,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_photo
        };

// Filter results WHERE "title" = 'My Title'

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_USERNAME + " ASC";

        Cursor c = db.query(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c != null && c.moveToFirst()) {
            System.out.println(c.getColumnCount());
            System.out.println(c.getCount());

            do {
                StudentDetails studentDetails =new StudentDetails(
                        c.getString(c.getColumnIndexOrThrow(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_EMAIL)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_USERNAME)),
                        c.getString(c.getColumnIndexOrThrow(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_PHONE)));
                studentDetails.setPohto(c.getString(c.getColumnIndexOrThrow(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_photo)));
                studentDetailses.add(studentDetails);
            } while (c.moveToNext());
            c.close();
        }
        System.out.println("Finished");
        return studentDetailses;
    }

    private void doMyThing() {
        Toast.makeText(getApplicationContext(), "Add student", Toast.LENGTH_LONG).show();
        showUpdateView();

    }

    private void showUpdateView() {
        final Dialog commentDialog = new Dialog(this);
        commentDialog.setContentView(R.layout.add_student);
        commentDialog.show();

        LinearLayout linearLayout = (LinearLayout) commentDialog.findViewById(R.id.imageLayout);
        Button buttonLoadImage = null;
        if (linearLayout != null) {
            buttonLoadImage = (Button) linearLayout.findViewById(R.id.buttonLoadPicture);

            buttonLoadImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "WHy null", Toast.LENGTH_LONG).show();

        }
        Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Adding student", Toast.LENGTH_LONG).show();
                String userName = ((TextView) commentDialog.findViewById(R.id.studentName)).getText().toString();
                String email = ((TextView) commentDialog.findViewById(R.id.studentEmail)).getText().toString();
                String phone = ((TextView) commentDialog.findViewById(R.id.studentPhone)).getText().toString();
                saveData(userName, email, phone);

                commentDialog.dismiss();
            }
        });
        Button cancelBtn = (Button) commentDialog.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not adding student", Toast.LENGTH_LONG).show();

                commentDialog.dismiss();
            }
        });
    }

    private void saveData(String userName, String email, String phone) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_USERNAME, userName);
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_PHONE, email);
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_EMAIL, phone);
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_photo, photoURL);
        System.out.println(" saving photl url : "+photoURL);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderDbHelper.FeedEntry.TABLE_NAME, null, values);
        Toast.makeText(getApplicationContext(), "Added id : " + newRowId, Toast.LENGTH_LONG).show();
        studentDetailses = getStudentDetails();
        System.out.println(studentDetailses.size());
        listView.invalidateViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            if ((ContextCompat.checkSelfPermission(ListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(ListActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

            {
                ActivityCompat.requestPermissions
                        (ListActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
//
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            photoURL = Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID().toString() + ".png";
            File file = new File(photoURL);
            System.out.println(Environment.getExternalStorageDirectory() + " directory");
            System.out.println("File name : " + file.getAbsolutePath());
            try

            {
                file.createNewFile();

                FileOutputStream fileoutputstream = new FileOutputStream(file);

                fileoutputstream.write(byteArray);

                fileoutputstream.close();

            } catch (Exception e)

            {

                e.printStackTrace();

            }

            Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_LONG).show();


        }


    }
}
