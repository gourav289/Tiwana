package com.gk.myapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.MessageModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.MarshMallowPermission;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 13-08-2017.
 */
public class MessageDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivAttachment1, ivAttachment2, ivAttachment3,ivAttachment4,ivAttachment5,ivAttachment6;
    private TextView txtMessage;

    private MessageModel data;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.message_details);
    }

    @Override
    protected int setView() {
        return R.layout.activity_message_details;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (MessageModel) getIntent().getSerializableExtra("data");
        getIds();
        setListeners();
        setData();
    }



    private void getIds() {
        ivAttachment1 = (ImageView) findViewById(R.id.iv_attachment1);
        ivAttachment2 = (ImageView) findViewById(R.id.iv_attachment2);
        ivAttachment3 = (ImageView) findViewById(R.id.iv_attachment3);
        ivAttachment4= (ImageView) findViewById(R.id.iv_attachment4);
        ivAttachment5= (ImageView) findViewById(R.id.iv_attachment5);
        ivAttachment6= (ImageView) findViewById(R.id.iv_attachment6);

        txtMessage = (TextView) findViewById(R.id.txt_message);

    }

    private void setListeners() {
        ivAttachment1.setOnClickListener(this);
        ivAttachment2.setOnClickListener(this);
        ivAttachment3.setOnClickListener(this);
        ivAttachment4.setOnClickListener(this);
        ivAttachment5.setOnClickListener(this);
        ivAttachment6.setOnClickListener(this);

    }

    private void setData() {
        txtMessage.setText(data.getMessage());
        if (data.getImage1() != null && !data.getImage1().equals("")) {
            Glide
                    .with(this)
                    .load(C.BASE_URL_FILES + data.getImage1())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_image)
                    .crossFade()
                    .into(ivAttachment1);
        }

        if (data.getImage2() != null && !data.getImage2().equals("")) {
            Glide
                    .with(this)
                    .load(C.BASE_URL_FILES + data.getImage2())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_image)
                    .crossFade()
                    .into(ivAttachment2);
        }

        if (data.getImage3() != null && !data.getImage3().equals("")) {
            Glide
                    .with(this)
                    .load(C.BASE_URL_FILES + data.getImage3())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_image)
                    .crossFade()
                    .into(ivAttachment3);
        }

        if (data.getImage4() != null && !data.getImage4().equals("")) {
            Glide
                    .with(this)
                    .load(C.BASE_URL_FILES + data.getImage4())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_image)
                    .crossFade()
                    .into(ivAttachment4);
        }

        if (data.getImage5() != null && !data.getImage5().equals("")) {
            Glide
                    .with(this)
                    .load(C.BASE_URL_FILES + data.getImage5())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_image)
                    .crossFade()
                    .into(ivAttachment5);
        }

        if (data.getImage6() != null && !data.getImage6().equals("")) {
            Glide
                    .with(this)
                    .load(C.BASE_URL_FILES + data.getImage6())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_image)
                    .crossFade()
                    .into(ivAttachment6);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_attachment1:
                if (data.getImage1() != null && !data.getImage1().equalsIgnoreCase("")) {
                    Intent image1 = new Intent(MessageDetailsActivity.this, LargeImageActivity.class);
                    image1.putExtra("image", C.BASE_URL_FILES+data.getImage1());
                    startActivity(image1);
                }
                break;

            case R.id.iv_attachment2:
                if (data.getImage2() != null && !data.getImage2().equalsIgnoreCase("")) {
                    Intent image1 = new Intent(MessageDetailsActivity.this, LargeImageActivity.class);
                    image1.putExtra("image",C.BASE_URL_FILES+ data.getImage2());
                    startActivity(image1);
                }
                break;

            case R.id.iv_attachment3:
                if (data.getImage3() != null && !data.getImage3().equalsIgnoreCase("")) {
                    Intent image1 = new Intent(MessageDetailsActivity.this, LargeImageActivity.class);
                    image1.putExtra("image", C.BASE_URL_FILES+data.getImage3());
                    startActivity(image1);
                }
                break;

            case R.id.iv_attachment4:
                if (data.getImage4() != null && !data.getImage4().equalsIgnoreCase("")) {
                    Intent image1 = new Intent(MessageDetailsActivity.this, LargeImageActivity.class);
                    image1.putExtra("image", C.BASE_URL_FILES+data.getImage4());
                    startActivity(image1);
                }
                break;

            case R.id.iv_attachment5:
                if (data.getImage5() != null && !data.getImage5().equalsIgnoreCase("")) {
                    Intent image1 = new Intent(MessageDetailsActivity.this, LargeImageActivity.class);
                    image1.putExtra("image",C.BASE_URL_FILES+ data.getImage5());
                    startActivity(image1);
                }
                break;

            case R.id.iv_attachment6:
                if (data.getImage6() != null && !data.getImage6().equalsIgnoreCase("")) {
                    Intent image1 = new Intent(MessageDetailsActivity.this, LargeImageActivity.class);
                    image1.putExtra("image", C.BASE_URL_FILES+data.getImage6());
                    startActivity(image1);
                }
                break;
        }
    }

}
