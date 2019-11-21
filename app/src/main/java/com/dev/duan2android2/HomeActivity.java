package com.dev.duan2android2;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.dev.duan2android2.base.BaseActivity;
import com.dev.duan2android2.fragment.Fragment_Cart;
import com.dev.duan2android2.fragment.Fragment_Home;
import com.dev.duan2android2.fragment.Fragment_Menu;
import com.dev.duan2android2.fragment.Fragment_Notification;
import com.dev.duan2android2.user.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.roughike.bottombar.OnTabSelectListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class HomeActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private DatabaseReference mDatabase;
    private ArrayList<User.cartsp> giohangArray = new ArrayList<>();
    private ArrayList<User.Product> products = new ArrayList<>();
    private String id="";
    private FloatingSearchView floatingSearchView;
    List<String> strings=new ArrayList<>();
    FirebaseUser user;
    private ListView listView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        Bungee.zoom(this);
        floatingSearchView=findViewById(R.id.floating_search_view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        getnamproduct();
        strings.clear();
        for (int i=0;i<products.size();i++){
            strings.add(products.get(i).getNameproduct());
            }


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.dev.duan2android2",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        mapped();
        onclickView();
        getcart();
    }

    //ánh xạ view
    private void mapped() {

        bottomBar = findViewById(R.id.bottomBar);
        nearby = bottomBar.getTabWithId(R.id.tab_cart);
        nearby1 = bottomBar.getTabWithId(R.id.tab_home);
        nearby2 = bottomBar.getTabWithId(R.id.tab_menu);
        nearby3 = bottomBar.getTabWithId(R.id.tab_notification);

    }

    //các sự kiện click

    private void onclickView() {

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                List<String> lstFound = new ArrayList<String>();
              if (oldQuery!=null&& !oldQuery.isEmpty()){
                  for(String item:strings){
                      if(item.contains(oldQuery))
                          lstFound.add(item);
                  }
                  Log.e("SIZE",strings.size()+"");
                  ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this,android.R.layout.simple_list_item_1,lstFound);

              }

            }
        });

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                fragment(tabId);
            }
        });
    }

    //bottom cilck chuyển đổi giữa các fragment

    public void fragment(int id) {

        switch (id) {
            case R.id.tab_home:

                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Home()).commit();

                break;
            case R.id.tab_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Cart()).commit();

                break;
            case R.id.tab_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Menu()).commit();

                break;
            case R.id.tab_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,
                        new Fragment_Notification()).commit();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        HomeActivity.this.finish();
    }

    private void getcart(){
        if (id!=null){
            giohangArray.clear();
            mDatabase.child("id").child("User").child(id).child("cart").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    User.cartsp cartsp=dataSnapshot.getValue(User.cartsp.class);
                    giohangArray.add(cartsp);
                    nearby.setBadgeCount(giohangArray.size());
                    Log.e("KEY",giohangArray.size()+"");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("KEY","a");
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("KEY","b");
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("KEY","c");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("KEY","d");
                }
            }) ;
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void getnamproduct(){
        products.clear();
        mDatabase.child("id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if (dataSnapshot.getKey() !=null && dataSnapshot.getKey().startsWith("sp:")) {
                    mDatabase.child("id").child(dataSnapshot.getKey()).child("product").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            User.Product product=dataSnapshot.getValue(User.Product.class);
                            products.add(product);
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

    }
}
