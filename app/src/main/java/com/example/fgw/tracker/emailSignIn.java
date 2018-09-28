package com.example.fgw.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class emailSignIn extends AppCompatActivity {

    @BindView(R.id.editText2)
    EditText email;
    @BindView(R.id.editText3)
    EditText password;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;
    FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;


    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_in);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance ();
    }

    @OnClick({R.id.imageView4, R.id.textView, R.id.textView2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView4:
                login();
                break;
            case R.id.textView:
                break;
            case R.id.textView2:
                startActivity(new Intent(this, SignUp.class));
                break;
        }
    }

    private void login()
        {
            final String emailstring = email.getText ().toString ().trim ();

            // to get Password from user and store it in variable called Password
            final String PassWord = password.getText ().toString ().trim ();

            //to check editText of email should not be empty
            //if it is empty then
            if(emailstring.isEmpty ())
            {
                //set an error
                email.setError ("Email is required");
                //and highlight that box
                email.requestFocus ();
                return;
            }
            if(PassWord.isEmpty ())
            {
                //set an error
                password.setError ("Password is required");
                //it will focus on password
                password.requestFocus ();
                return;
            }
            if(PassWord.length ()<6)
            {
                password.setError ("Minimum length of password required is 6");
                password.requestFocus ();
                return;
            }
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches())
            {
                mAuth.signInWithEmailAndPassword (emailstring,PassWord).addOnCompleteListener (new OnCompleteListener<AuthResult>( ) {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                    progressBar.setVisibility (View.GONE);
                        if (task.isSuccessful ())
                        {
                            //if login is successful then

                            Intent intent = new Intent (emailSignIn.this, TrackerActivity.class);
                            intent .addFlags (intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity (intent);
                            Toast.makeText (getApplicationContext (),"log in",Toast.LENGTH_SHORT).show ();

                        }else
                        {
                            //else
                            Toast.makeText (getApplicationContext (), task.getException ().getMessage (),Toast.LENGTH_SHORT).show ();
                        }
                    }
                });}
    }
}
