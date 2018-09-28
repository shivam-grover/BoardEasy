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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp extends AppCompatActivity {

    @BindView(R.id.editText2)
    EditText email2;
    @BindView(R.id.editText3)
    EditText password2;
    @BindView(R.id.editText8)
    EditText password3;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.textView)
    TextView textView;
    private FirebaseDatabase mDatabase;
    public String UserID;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance ();
        mDatabase=FirebaseDatabase.getInstance();
        databaseReference=mDatabase.getReference("users");
    }

    @OnClick({R.id.imageView4, R.id.textView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView4:
                setSignupa();
                break;
            case R.id.textView:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }
    public void setSignupa()
    {
        final String email = email2.getText ().toString ().trim ();
        final String PassWord = password2.getText ().toString ().trim ();
        String PassWord2 = password3.getText ().toString ().trim ();

        if(email.isEmpty ())
        {
            email2.setError ("Email is required");
            email2.requestFocus ();
            return;
        }
        if(PassWord.isEmpty ())
        {
            password2.setError ("Password is required");
            password2.requestFocus ();
            return;
        }
        if(PassWord.length ()<6)
        {
            password2.setError ("Minimum length of password required is 6");
            password2.requestFocus ();
            return;
        }
        if(!(PassWord.equals(PassWord2)))
        {
            Toast.makeText(this, "Password does match can't", Toast.LENGTH_SHORT).show();
            return;
        }

//        progressBar.setVisibility (View.VISIBLE);

        mAuth.createUserWithEmailAndPassword (email , PassWord).addOnCompleteListener (new OnCompleteListener<AuthResult>( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressBar.setVisibility (View.GONE);
                if(task.isSuccessful ())
                {       UserID = mAuth.getCurrentUser().getUid();

                    Users users = new Users(email,false,UserID,false);
                    databaseReference.child(UserID).setValue (users);

                    startActivity(new Intent(SignUp.this,MapsActivity.class));}

                else {
                    if (task.getException ( ) instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText (getApplicationContext ( ), "You are already registered", Toast.LENGTH_SHORT).show ( );
                    } else
                    {
                        Toast.makeText (getApplicationContext (),task.getException ().getMessage (),Toast.LENGTH_SHORT).show ();
                    }
                }
            }

        });



    }
}
