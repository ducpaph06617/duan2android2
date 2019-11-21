package com.dev.duan2android2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.duan2android2.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class AccountInformationActivity extends AppCompatActivity {

    private GifImageView loading;
    private LinearLayout layout;
    private CircularImageView imginfo;
    private EditText edtnameinfo;
    private RadioButton rdman;
    private RadioButton rdwomen;
    private EditText edtaddress;
    private EditText edtphoneinfo;
    private EditText edtemailinfo;
    private Button btnSaveuserinfo;

    private DatabaseReference mDatabase;
    private int REQUEST_CODE_FOLDER = 123;
    private StorageReference storageRef;
    private FirebaseStorage storage;

    String uri="";
    String name = "";
    String provice ="";
    String phone = "";
    String id = "";
    String email="";
    String address="";
    String gender="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        getSupportActionBar().setTitle(getString(R.string.information));
        mapped();
        method();
        oncick();
        }


    //Anh Xa

    private void mapped(){
        loading = findViewById(R.id.loading);
        layout =  findViewById(R.id.layout);
        imginfo =  findViewById(R.id.imginfo);
        edtnameinfo =  findViewById(R.id.edtnameinfo);
        rdman =  findViewById(R.id.rdman);
        rdwomen = findViewById(R.id.rdwomen);
        edtaddress = findViewById(R.id.edtaddress);
        edtphoneinfo =  findViewById(R.id.edtphoneinfo);
        edtemailinfo =  findViewById(R.id.edtemailinfo);
        btnSaveuserinfo =  findViewById(R.id.btnSaveuserinfo);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://os1221.appspot.com");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void method(){
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        name=intent.getStringExtra("name");
        gender=intent.getStringExtra("gender");
        email=intent.getStringExtra("email");
        uri=intent.getStringExtra("uri");
        address=intent.getStringExtra("address");
        provice = intent.getStringExtra("provider");
        id = intent.getStringExtra("id");
        if (provice.equals("facebook.com")){
            edtnameinfo.setText(name);
            edtemailinfo.setText(email);
            edtaddress.setText(address);
            edtphoneinfo.setText(phone);
            if (gender.equals("man")){
                rdman.setChecked(true);
            }
            if (gender.equals("woman")){
                rdwomen.setChecked(true);
            }

            Picasso.get().load(Uri.parse(uri)).resize(50, 50).centerCrop().into(imginfo);
             }else {
            edtnameinfo.setText(name);
            edtemailinfo.setText(email);
            edtaddress.setText(address);
            edtphoneinfo.setText(phone);
              edtnameinfo.setText(name);
            if (gender.equals("man")){
                rdman.setChecked(true);
            }
            if (gender.equals("woman")){
                rdwomen.setChecked(true);
            }
            Picasso.get().load(Uri.parse(uri)).resize(50, 50).centerCrop().into(imginfo);

        }
    }

    //View onclcik
    private void oncick(){
        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        btnSaveuserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edtnameinfo.getText().toString();
                phone=edtphoneinfo.getText().toString();
                address=edtaddress.getText().toString();
                email=edtemailinfo.getText().toString();
                layout.setAlpha(0.3f);
                loading.setVisibility(View.VISIBLE);
                if (name.equals("")){
                    edtnameinfo.setError(getString(R.string.error));
                    edtnameinfo.requestFocus();
                    return;
                }
                if (!rdman.isChecked()&&!rdwomen.isChecked()){
                    Toast.makeText(AccountInformationActivity.this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (address.equals("")){
                    edtaddress.setError(getString(R.string.error));
                    edtaddress.requestFocus();
                    return;
                }
                if (phone.equals("")) {
                    edtphoneinfo.setError(getString(R.string.error));
                    edtemailinfo.requestFocus();
                    return;
                }
                if (!phone.startsWith("+84") && !phone.startsWith("0")) {
                    edtphoneinfo.setError(getString(R.string.error_1));
                    edtemailinfo.requestFocus();
                    return;
                }
                if (phone.length() != 10 && phone.length() != 12) {
                    edtphoneinfo.setError(getString(R.string.error_2));
                    edtemailinfo.requestFocus();
                    return;
                }
                if (email.equals("")){
                    edtemailinfo.setError(getString(R.string.error));
                    edtemailinfo.requestFocus();
                    return;
                }

                if (rdman.isChecked()){
                    User.Info user=new User.Info();
                    user.setName(name);
                    user.setUri(uri);
                    user.setAddress(address);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setGender("man");
                    mDatabase.child("id").child("User").child(id).child("user").child("info").child("info").setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    Toast.makeText(AccountInformationActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    startActivity();
                                }
                            });
                }else {
                    User.Info user=new User.Info();
                    user.setName(name);
                    user.setUri(uri);
                    user.setAddress(address);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setGender("woman");
                    mDatabase.child("id").child("User").child(id).child("user").child("info").child("info").setValue(user) .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(AccountInformationActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                            startActivity();
                        }
                    });
                }


            }
        });



    }
  //Upload imgage firebase
    public void uploadAnh(ImageView imageView) {
        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + "");
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AccountInformationActivity.this, "Hình chưa được lưu", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
                layout.setAlpha(1);
                imginfo.setImageResource(R.drawable.ic_user);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                Uri downloadUrl = urlTask.getResult();
                uri = String.valueOf(downloadUrl);
                loading.setVisibility(View.INVISIBLE);
                layout.setAlpha(1);
                Toast.makeText(AccountInformationActivity.this, "Hình đã được lưu", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //imgage album
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_FOLDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imginfo.setImageURI(uri);
            uploadAnh(imginfo);
            loading.setVisibility(View.VISIBLE);
            layout.setAlpha(0.3f);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        startActivity();
        }

   public void startActivity(){
        Intent intent=new Intent(AccountInformationActivity.this,HomeActivity.class);
       intent.putExtra("name", name);
       intent.putExtra("uri", uri);
       intent.putExtra("id", id);
       intent.putExtra("phone",phone);
       intent.putExtra("address",address);
       intent.putExtra("email",email);
       intent.putExtra("gender",gender);
       intent.putExtra("provider",provice);
       startActivity(intent);
       finish();
   }
}
