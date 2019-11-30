package com.dev.duan2android2;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dev.duan2android2.base.BaseActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;


public class LoginActivity extends BaseActivity {
    public EditText code;
    public GifImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapped();
        method();
        onclick();


    }

    //Ánh xạ
    private void mapped() {
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);

        edtphone = findViewById(R.id.edtphone);
        btnlogin = findViewById(R.id.btnlogin);
        mAuth = FirebaseAuth.getInstance();

    }

    //Các sự kiện onClick
    private void onclick() {

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = edtphone.getText().toString().trim();
                if (phone.equals("")) {
                    edtphone.setError(getString(R.string.error));
                    edtphone.requestFocus();
                    return;
                }
                if (!phone.startsWith("+84") && !phone.startsWith("0")) {
                    edtphone.setError(getString(R.string.error_1));
                    edtphone.requestFocus();
                    return;
                }
                if (phone.length() != 10 && phone.length() != 12) {
                    edtphone.setError(getString(R.string.error_2));
                    edtphone.requestFocus();
                    return;
                }

                sendCode(phone);
                Dialog dialog = dialog(R.layout.codephone, WindowManager.LayoutParams.WRAP_CONTENT);
                Button xacnhan;
                TextView textView = dialog.findViewById(R.id.txtsend);

                code = dialog.findViewById(R.id.code);
                xacnhan = dialog.findViewById(R.id.xacnhan);
                loading = dialog.findViewById(R.id.loading);
                xacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String codeid = code.getText().toString();
                        if (codeid.equals("")) {
                            code.setError(getString(R.string.enter_code));
                            code.requestFocus();
                            return;
                        }
                        if (codeid.length() != 6) {
                            code.setError(getString(R.string.enter_codeid));
                            code.requestFocus();
                            return;
                        }
                        loading.setVisibility(View.VISIBLE);
                        verityCode(codeid);


                    }
                });
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCode(phone);
                    }
                });
                dialog.show();
            }

        });

    }

    private void method() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e("TAG", user.getProviders().get(0));
                    if (user.getProviders().get(0).equals("facebook.com")) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("name", user.getDisplayName());
                        intent.putExtra("phone",user.getPhoneNumber());
                        intent.putExtra("uri", String.valueOf(user.getPhotoUrl()));
                        intent.putExtra("id", user.getUid());
                        intent.putExtra("provider", user.getProviders().get(0));
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("provider", user.getProviders().get(0));
                        intent.putExtra("id", user.getUid());
                        intent.putExtra("phone", user.getPhoneNumber());
                        startActivity(intent);
                        Log.e("Tinh", "B");
                        finish();
                    }
                } else {


                }
            }
        };


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);


    }

    public Dialog dialog(int layoutid, int height) {
        Dialog dialog = new android.app.Dialog(this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutid);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.anim.anim1;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = height;
        window.setAttributes(wlp);
        return dialog;

    }


    private void sendCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verityCode(String code) {
        if (idcode != null) {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(idcode, code);
            signInWithPhoneAuthCredential(phoneAuthCredential);
        } else {
            loading.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Mã xác nhận chưa hợp lệ", Toast.LENGTH_SHORT).show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("TAG", "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            finish();

                            // ...
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.INVISIBLE);
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            idcode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String codeid = phoneAuthCredential.getSmsCode();
            if (codeid != null) {
                code.setText(codeid);
                loading.setVisibility(View.VISIBLE);
                verityCode(codeid);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
    };
}
