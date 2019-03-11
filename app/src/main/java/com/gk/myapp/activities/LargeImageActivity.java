package com.gk.myapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.utils.C;

/**
 * Created by Gk on 13-08-2017.
 */
public class LargeImageActivity extends Activity {
    private boolean btnVisibility = false;
    ImageButton iBtnBack;
    private ImageView iv;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_large_image);
        iBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        iv = (ImageView) findViewById(R.id.iv_large_image);

        image = getIntent().getStringExtra("image");

        if (image != null && !image.equals("")) {
            Glide.with(this)
                    .load(image)
                    .into(iv);
        }

        iBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btnVisibility) {
//                    btnVisibility = false;
//                    iBtnBack.setVisibility(View.GONE);
//                } else {
//                    btnVisibility = true;
//                    iBtnBack.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }
}
