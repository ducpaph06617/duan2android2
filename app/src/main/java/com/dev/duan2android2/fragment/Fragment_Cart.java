package com.dev.duan2android2.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.duan2android2.LoginActivity;
import com.dev.duan2android2.R;
import com.dev.duan2android2.adapter.CartAdapter;
import com.dev.duan2android2.user.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class Fragment_Cart extends BaseFragment {

    private RecyclerView recyclerviewcart;
    private DatabaseReference mDatabase;
    private ArrayList<User.cartsp> giohangArray = new ArrayList<>();
    private ArrayList<User.Product> products = new ArrayList<>();
    private String id = "";
    private ArrayList<String> idspham = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    private CartAdapter cartAdapter;
    private ScrollView scrollView;
    private GifImageView loading;
    private Dialog dialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("id");
        recyclerviewcart = view.findViewById(R.id.recyclerviewcart);
        loading = view.findViewById(R.id.loading);
        scrollView = view.findViewById(R.id.scrollView);
        loading.setVisibility(View.VISIBLE);
        scrollView.setAlpha(0.5f);
        cartAdapter = new CartAdapter(Fragment_Cart.this, products, giohangArray);
        recyclerviewcart.setLayoutManager(linearLayoutManager);
        recyclerviewcart.setHasFixedSize(true);
        recyclerviewcart.setAdapter(cartAdapter);
        getcart();
        return view;
    }

    private void getcart() {
        if (id != null) {
            giohangArray.clear();
            products.clear();
            idspham.clear();

            mDatabase.child("id").child("User").child(id).child("cart").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    User.cartsp cartsp = dataSnapshot.getValue(User.cartsp.class);
                    if (dataSnapshot.getKey() != null) {
                        Log.e("CHECK", dataSnapshot.getKey());
                        idspham.add(0,dataSnapshot.getKey());
                        loading.setVisibility(View.INVISIBLE);
                        scrollView.setAlpha(1f);
                    }
                    giohangArray.add(cartsp);
                    mDatabase.child("id").child(cartsp.getIdsp()).child("product").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            User.Product product = dataSnapshot.getValue(User.Product.class);
                            products.add(0, product);
                            cartAdapter.notifyDataSetChanged();
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
                    Log.e("KEY", giohangArray.size() + "");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("KEY", "a");
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("KEY", "b");
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("KEY", "c");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("KEY", "d");
                }
            });
        }
    }

    public void themcart(User.cartsp cartsp, int posion) {
        loading.setVisibility(View.VISIBLE);
        scrollView.setAlpha(0.2f);
        int i = (Integer.parseInt(cartsp.getSoluong()) + 1);
        Log.e("KT", i + "");
        for (String a : idspham) {
            Log.e("KT", a + "");
        }
        User.cartsp cartsp1 = new User.cartsp();
        cartsp1.setSoluong(String.valueOf(i));
        cartsp1.setIdsp(cartsp.getIdsp());
        mDatabase.child("id").child("User").child(id).child("cart").child(idspham.get(posion)).setValue(cartsp1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("KT", "THÀNH CÔNG");
                        loading.setVisibility(View.INVISIBLE);
                        scrollView.setAlpha(1f);
                        cartAdapter.notifyDataSetChanged();
                    }
                });
    }


    public void trucart(User.cartsp cartsp, int posion) {

        int i = (Integer.parseInt(cartsp.getSoluong()));
        int ii = 0;
        if (i == 1) {
            Toast.makeText(getActivity(), "Sô lượng ít nhất là 1", Toast.LENGTH_SHORT).show();
        } else if (i > 1) {
            ii = (Integer.parseInt(cartsp.getSoluong()) - 1);
            loading.setVisibility(View.VISIBLE);
            scrollView.setAlpha(0.2f);
            Log.e("KT", i + "");
            for (String a : idspham) {
                Log.e("KT", a + "");
            }
            User.cartsp cartsp1 = new User.cartsp();
            cartsp1.setSoluong(String.valueOf(ii));
            cartsp1.setIdsp(cartsp.getIdsp());
            mDatabase.child("id").child("User").child(id).child("cart").child(idspham.get(posion)).setValue(cartsp1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("KT", "THÀNH CÔNG");
                            loading.setVisibility(View.INVISIBLE);
                            scrollView.setAlpha(1f);
                            cartAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }


    public void tongtien(String tongtien) {
         dialog=new Dialog(getActivity(),R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tinhtien);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;


        TextView pricetong;
        Button btnxacnhan;
        pricetong =  dialog.findViewById(R.id.pricetong);
        btnxacnhan =  dialog.findViewById(R.id.btnxacnhan);

        pricetong.setText(tongtien);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void deletesp(final int posion, String tensp, final int size){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa sản phẩm\t"+tensp+"\tkhỏi giỏ hàng không?");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
            });

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading.setVisibility(View.VISIBLE);
                scrollView.setAlpha(0.2f);
                mDatabase.child("id").child("User").child(id).child("cart").child(idspham.get(posion)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.setVisibility(View.INVISIBLE);
                        scrollView.setAlpha(1f);
                        getcart();
                        cartAdapter.notifyDataSetChanged();
                        if (size==1){
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }
                });

            }
        });

        builder.show();

    }




}
