package com.dev.duan2android2.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dev.duan2android2.AccountInformationActivity;
import com.dev.duan2android2.AddProductActivity;
import com.dev.duan2android2.HomeActivity;
import com.dev.duan2android2.LoginActivity;
import com.dev.duan2android2.ProductManagementActivity;
import com.dev.duan2android2.R;
import com.dev.duan2android2.SupportActivity;
import com.dev.duan2android2.user.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;
import spencerstudios.com.bungeelib.Bungee;

import static android.app.Activity.RESULT_OK;

public class Fragment_Menu extends BaseFragment {
    String name = "";
    String uri = "";
    String provice ="";
    String phone = "";
    String id = "";
    String email="";
    String address="";
    String gender="";
    private Dialog dialog;
    private DatabaseReference mDatabase;


    private LinearLayout linearLayout;
    private GifImageView loading;

    ImageView imginfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu,container, false);
        intent = getActivity().getIntent();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://os1221.appspot.com");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences=getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        mapped();
        Support();
        addProduct();
        Exit();

        name = intent.getStringExtra("name");
        uri = intent.getStringExtra("uri");
        provice = intent.getStringExtra("provider");
        id = intent.getStringExtra("id");

        if (provice == null) {
            btnLogin.setVisibility(View.VISIBLE);
            cvAddProduct.setAlpha(0.5f);
            cvManage.setAlpha(0.5f);
            cvAddProduct.setClickable(false);
            cvManage.setClickable(false);
            cvUserinfor.setAlpha(0.5f);
            cvUserinfor.setClickable(false);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();

                }
            });
        } else {
            cvAddProduct.setAlpha(1);
            cvManage.setAlpha(1);
            cvUserinfor.setAlpha(1);
            cvUserinfor.setClickable(true);
            cvManage.setClickable(true);
            cvAddProduct.setClickable(true);
            if (provice.equals("facebook.com")) {
                mDatabase.child("id").child("User").child(id).child("user").child("info").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User.Info user=dataSnapshot.getValue(User.Info.class);
                        name=user.getName();
                        uri=user.getUri();
                        gender=user.getGender();
                        address=user.getAddress();
                        email=user.getEmail();
                        phone=user.getPhone();
                        txtUsername.setText(name);
                        Picasso.get().load(Uri.parse(uri)).resize(50, 50).centerCrop().into(imgUser);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                cvUserinfor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(new Intent(getActivity(), AccountInformationActivity.class));
                        intent.putExtra("name", name);
                        intent.putExtra("uri", uri);
                        intent.putExtra("id", id);
                        intent.putExtra("phone",phone);
                        intent.putExtra("address",address);
                        intent.putExtra("email",email);
                        intent.putExtra("gender",gender);
                        intent.putExtra("provider",provice);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                btnLogin.setText(R.string.logoutfb);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        txtUsername.setText("");
                        imgUser.setImageResource(R.drawable.ic_users);
                        btnLogin.setText(R.string.btn_login);
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finish();
                    }
                });
                Picasso.get().load(Uri.parse(uri)).resize(50, 50).centerCrop().into(imgUser);
                txtUsername.setText(name);
            } else {


                mDatabase.child("id").child("User").child(id).child("user").child("info").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User.Info user=dataSnapshot.getValue(User.Info.class);
                             name=user.getName();
                             uri=user.getUri();
                             gender=user.getGender();
                             address=user.getAddress();
                             email=user.getEmail();
                             phone=user.getPhone();
                            txtUsername.setText(name);
                            Picasso.get().load(Uri.parse(uri)).resize(50, 50).centerCrop().into(imgUser);
                            }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                cvUserinfor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(new Intent(getActivity(), AccountInformationActivity.class));
                        intent.putExtra("name", name);
                        intent.putExtra("phone",phone);
                        intent.putExtra("uri", uri);
                        intent.putExtra("id", id);
                        intent.putExtra("provider",provice);
                        intent.putExtra("address",address);
                        intent.putExtra("email",email);
                        intent.putExtra("gender",gender);
                        startActivity(intent);
                    }
                });
                btnLogin.setText(R.string.logoutfb);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        txtUsername.setText("");
                        imgUser.setImageResource(R.drawable.ic_users);
                        btnLogin.setText(R.string.btn_login);
                        startActivity(new Intent(getActivity(),HomeActivity.class));
                        getActivity().finish();

                    }
                });
                  new Thread(new RunAble1(1,getActivity())).start();

            }

        }
        cvManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(new Intent(getActivity(), ProductManagementActivity.class));
                intent.putExtra("name", name);
                intent.putExtra("phone",phone);
                intent.putExtra("uri", uri);
                intent.putExtra("id", id);
                intent.putExtra("provider",provice);
                intent.putExtra("address",address);
                intent.putExtra("email",email);
                intent.putExtra("gender",gender);
                startActivity(intent);
            }
        });
        return view;
    }


    //ánh xạ

    private void mapped() {
        imgUser = view.findViewById(R.id.imgUser);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnLogin = view.findViewById(R.id.btnLogin);
        cvUserinfor = view.findViewById(R.id.cvUserinfor);
        cvAddProduct = view.findViewById(R.id.cvAddProduct);
        cvManageProduct = view.findViewById(R.id.cvManage);
        cvHelp =  view.findViewById(R.id.cvHelp);
        cvQuit = view.findViewById(R.id.cvQuit);
        cvManage=view.findViewById(R.id.cvManage);
    }

    public void dialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_user);
        final EditText edtphone;
        final EditText edtname;

        Button btninfo;
        loading = dialog.findViewById(R.id.loading);
        linearLayout = dialog.findViewById(R.id.layout);
        edtphone = dialog.findViewById(R.id.edtphone);
        edtname = dialog.findViewById(R.id.edtname);
        imginfo = dialog.findViewById(R.id.imginfo);
        btninfo = dialog.findViewById(R.id.btninfo);
        edtphone.setText(intent.getStringExtra("phone"));
        edtphone.setEnabled(true);
        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtphone.getText().toString();
                final String name1 = edtname.getText().toString();
                if (phone.equals("")) {
                    edtphone.setError(getString(R.string.error));
                    return;
                }
                if (!phone.startsWith("+84") && !phone.startsWith("0")) {
                    edtphone.setError(getString(R.string.error_1));
                    return;
                }
                if (phone.length() != 11 && phone.length() != 12) {
                    edtphone.setError(getString(R.string.error_2));
                    return;
                }
                if (name1.equals("")) {
                    edtname.setError(getString(R.string.error));
                    return;
                }
                if (uri == null) {
                    Toast.makeText(getActivity(), "Chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                User.Info user = new User.Info(name1, phone, uri,"","","");
                loading.setVisibility(View.VISIBLE);
                name=name1;
                linearLayout.setAlpha(0.3f);
                mDatabase.child("id").child("User").child(id).child("user").child("info").child("info").setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                mDatabase.child("id").child("User").child(id).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        //User user1 = dataSnapshot.getValue(User.class);
                                       // uri = user1.getUri();
                                        //name = user1.getName();
                                        txtUsername.setText(name);
                                        new Thread(new RunAble1(1,getActivity())).start();
                                        if (uri!=null){
                                            Picasso.get().load(Uri.parse(uri)).resize(50, 50).centerCrop().into(imgUser);
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });
        dialog.show();
    }

    public void Support(){
        cvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SupportActivity.class));
            }
        });
    }

    public void Exit(){
        cvQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(R.string.logout);
                builder.setMessage(R.string.exit);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.except, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void addProduct(){
        cvAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddProductActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
    }

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
                Toast.makeText(getActivity(), "Hình chưa được lưu", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
                linearLayout.setAlpha(1);
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
                linearLayout.setAlpha(1);
                Toast.makeText(getActivity(), "Hình đã được lưu", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_FOLDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imginfo.setImageURI(uri);
            uploadAnh(imginfo);
            loading.setVisibility(View.VISIBLE);
            linearLayout.setAlpha(0.3f);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public class RunAble1 implements Runnable {
        int seconds;
        Context context;

        public RunAble1(int seconds, Context context) {
            this.seconds = seconds;
            this.context = context;
        }

        @Override
        public void run() {
            for (int i = 0; i <=2; i++) {
                Handler handler = new Handler(Looper.getMainLooper());
                final int intI = i;
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        if (intI==2){
                            if (name==null&&name==null){
                                   dialog();
                                   cvAddProduct.setAlpha(0.5f);
                                   cvAddProduct.setClickable(false);
                                   cvUserinfor.setAlpha(0.5f);
                                   cvUserinfor.setClickable(false);
                                   Bungee.zoom(context);

                            }else {
                                cvAddProduct.setAlpha(1);
                                cvAddProduct.setClickable(true);
                                cvUserinfor.setAlpha(1);
                                cvUserinfor.setClickable(true);

                            }

                        }

                    }

                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }
    }
}
