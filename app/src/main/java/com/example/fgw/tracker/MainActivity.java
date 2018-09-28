package com.example.fgw.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //    public ImageView emailwith, withgoogle;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView3)
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.imageView2, R.id.imageView3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView2:
                startActivity(new Intent(this,emailSignIn.class));
                break;
            case R.id.imageView3:
                startActivity(new Intent(this,GoogleSigninn.class));

                break;
        }
    }
}