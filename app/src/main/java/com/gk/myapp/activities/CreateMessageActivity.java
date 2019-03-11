package com.gk.myapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Toast;

import com.gk.myapp.R;
import com.gk.myapp.fragments.MessagesFragment;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
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
 * Created by Gk on 18-12-2016.
 */
public class CreateMessageActivity extends BaseActivity implements View.OnClickListener {
    private final int IMAGE1 = 1;
    private final int IMAGE2 = 2;
    private final int IMAGE3 = 3;

    private int selectedImage;

    private ImageView ivAttachment1, ivAttachment2, ivAttachment3;
    private Context mContext;
    private Button btnSend;
    private EditText edMessage;
    private String message;
    private int screenWidth;
    private final int CAMERA_REQUEST = 101;
    private final int GALLERY_REQUEST = 100;
    private String imagepath = null;
    byte[] imgByte = null;
    private Bitmap profilePic = null;
    Long mSystemTime;
    File image1, image2, image3;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.create_message);
    }

    @Override
    protected int setView() {
        return R.layout.activity_create_message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        getIds();
        setListeners();
    }


    private void getIds() {
        ivAttachment1 = (ImageView) findViewById(R.id.iv_attachment1);
        ivAttachment2 = (ImageView) findViewById(R.id.iv_attachment2);
        ivAttachment3 = (ImageView) findViewById(R.id.iv_attachment3);
        btnSend = (Button) findViewById(R.id.btn_send);
        edMessage = (EditText) findViewById(R.id.ed_message);

        setLeftButton(true, R.mipmap.ic_back, new HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                finish();
            }
        });
    }

    private void setListeners() {
        ivAttachment1.setOnClickListener(this);
        ivAttachment2.setOnClickListener(this);
        ivAttachment3.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }


    //get image
    private void imageOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setItems(R.array.image_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        getImageFromGallery();
                        break;
                    case 1:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mSystemTime = System.currentTimeMillis();
                        File f = new File(Environment.getExternalStorageDirectory(), "temp" + mSystemTime + ".jpg");
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
//                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                            File photoFile = null;
//                            try {
//                                photoFile = createImageFile();
//                            } catch (IOException ex) {
//                            }
//                            if (photoFile != null) {
//                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
//                            } else {
//                                U.toast("External Storage is not mounted");
//                            }
//                        }
                        break;
                    case 2:
                        if (selectedImage == IMAGE1) {
                            image1 = null;
                            ivAttachment1.setImageResource(R.mipmap.ic_add);
                        } else if (selectedImage == IMAGE2) {
                            image2 = null;
                            ivAttachment2.setImageResource(R.mipmap.ic_add);
                        } else {
                            image3 = null;
                            ivAttachment3.setImageResource(R.mipmap.ic_add);
                        }
                        break;

                    case 3:
                        dialog.dismiss();
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null) {

                if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
                    Uri selected_MediaUri = data.getData();

                    if (selected_MediaUri.toString().contains("images") || selected_MediaUri.toString().contains("photos")) {
                        ProcessImageData(data);
                    }

                }
            }
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int display_Width = displayMetrics.widthPixels;
                try {
                    File dir = Environment.getExternalStorageDirectory();
                    File f = new File(dir, "temp"+ mSystemTime + ".jpg");
                    imagepath  = f.getAbsolutePath();
                }catch (Exception e){
                    e.printStackTrace();
                }
//								int display_Height = displayMetrics.heightPixels;
                Log.e("displayWIdth", "" + display_Width);
                profilePic = decodeSampledBitmapFromFile(imagepath,
                        display_Width / 2, display_Width / 2);
//                Drawable dd=new BitmapDrawable(getResources(),profilePic);
//                ivProfilePic.setBackgroundDrawable(dd);
//								imagepath=null;
                if (selectedImage == IMAGE1) {
                    image1 = new File(imagepath);
                    ivAttachment1.setBackgroundResource(0);
                    ivAttachment1.setImageBitmap(profilePic);
                } else if (selectedImage == IMAGE2) {
                    image2 = new File(imagepath);
                    ivAttachment2.setBackgroundResource(0);
                    ivAttachment2.setImageBitmap(profilePic);
                } else if (selectedImage == IMAGE3) {
                    image3 = new File(imagepath);
                    ivAttachment3.setBackgroundResource(0);
                    ivAttachment3.setImageBitmap(profilePic);
                }
            }
        } catch (Exception ex) {

        }
//		}
    }


    //from gallery
    private void ProcessImageData(Intent data) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int display_Width = displayMetrics.widthPixels;
        // Get the Image from data
        final Uri selectedImageURI = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getContentResolver().query(selectedImageURI,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        final String imgDecodableString = cursor.getString(columnIndex);
        imagepath = imgDecodableString;
        Log.e("imagePath ", " " + imagepath);
        profilePic = decodeSampledBitmapFromFile(imagepath,
                display_Width / 2, display_Width / 2);
//TODO  set image to image view
//        Drawable dd = new BitmapDrawable(getResources(), profilePic);
        if (selectedImage == IMAGE1) {
            image1 = new File(imagepath);
            ivAttachment1.setBackgroundResource(0);
            ivAttachment1.setImageBitmap(profilePic);
        } else if (selectedImage == IMAGE2) {
            image2 = new File(imagepath);
            ivAttachment2.setBackgroundResource(0);
            ivAttachment2.setImageBitmap(profilePic);
        } else if (selectedImage == IMAGE3) {
            image3 = new File(imagepath);
            ivAttachment3.setBackgroundResource(0);
            ivAttachment3.setImageBitmap(profilePic);
        }
        cursor.close();
    }


    //from camera
    private File createImageFile() throws IOException {
        String state = Environment.getExternalStorageState();
        Log.i("state", "" + state);
        String imageFileName = "image" + System.currentTimeMillis();
        File myAliiDirctory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Tiwana");
        File image = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (!myAliiDirctory.mkdirs()) {

            }
            image = new File(myAliiDirctory + File.separator + imageFileName + ".jpg");
            imagepath = image.getAbsolutePath();
            if (imagepath != null) {
            }
        }
        return image;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.i("height", "" + height);
        Log.i("width", "" + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @SuppressLint("NewApi")
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathName, options);// decodeResource(res,
        // resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        Log.i("Sample_Size", "" + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return getbitmap(pathName, options);
    }

    public static Bitmap getbitmap(String image_Path, BitmapFactory.Options options) {
        Bitmap bm;
        Bitmap bitmap = null;
        try {
            bm = BitmapFactory.decodeStream(new FileInputStream(new File(
                    image_Path)), null, options);
            bitmap = bm;

            ExifInterface exif = new ExifInterface(image_Path);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);

            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "3");
                exif.saveAttributes();

                m.postRotate(180);

                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);

                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "6");
                exif.saveAttributes();

                m.postRotate(90);

                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);

                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "8");
                exif.saveAttributes();

                m.postRotate(270);

                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            Log.i("IOException:", e.toString());
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            imageOptions();
        }
    }

    /***
     * end of get bitmap
     ***/


    private void sendBroadcastMessage() {
        if (U.isConnectedToInternet(getCont())) {
            showProgress();
            // Parsing any Media type file
//            RequestBody requestBody1=null, requestBody2=null, requestBody3=null;
            MultipartBody.Part fileToUpload1 = null, fileToUpload2 = null, fileToUpload3 = null;

            if (image1 != null) {
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/from-data"), image1);
                fileToUpload1 = MultipartBody.Part.createFormData("image1", "image1", requestBody1);
            }

            if (image2 != null) {
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/from-data"), image2);
                fileToUpload2 = MultipartBody.Part.createFormData("image2", "image2", requestBody2);
            }

            if (image3 != null) {
                RequestBody requestBody3 = RequestBody.create(MediaType.parse("multipart/from-data"), image3);
                fileToUpload3 = MultipartBody.Part.createFormData("image3", "image3", requestBody3);
            }

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), getPreferences().getStringPref(P.USER_ID));
            RequestBody mes = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody to = RequestBody.create(MediaType.parse("text/plain"), "ALL");
            if (getPreferences().getStringPref(P.USER_TYPE).equals(C.ASM))
                to = RequestBody.create(MediaType.parse("text/plain"), "ALL SO");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.sendBroadCastMessage(fileToUpload1, fileToUpload2, fileToUpload3, userId, mes, to);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data != null) {
                        if (data.getSuccess().equals("1")) {
                            MessagesFragment.update = true;
                            finish();
                        }
                        showToast(data.getMessage());
                    } else {
//                        assert serverResponse != null;
                        showToast(getString(R.string.error_message));
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    showToast(getString(R.string.error_message));
                    hideProgress();
                }
            });
        } else {
            showToast(getString(R.string.no_internet));
        }
    }

    private void sendMessage() {
        if (U.isConnectedToInternet(getCont())) {
            showProgress();
            // Parsing any Media type file
            RequestBody requestBody1, requestBody2, requestBody3;
            MultipartBody.Part fileToUpload1 = null, fileToUpload2 = null, fileToUpload3 = null;

            if (image1 != null) {
                requestBody1 = RequestBody.create(MediaType.parse("multipart/from-data"), image1);
                fileToUpload1 = MultipartBody.Part.createFormData("image1", "image1", requestBody1);
            }

            if (image2 != null) {
                requestBody2 = RequestBody.create(MediaType.parse("multipart/from-data"), image2);
                fileToUpload2 = MultipartBody.Part.createFormData("image2", "image2", requestBody2);
            }

            if (image3 != null) {
                requestBody3 = RequestBody.create(MediaType.parse("multipart/from-data"), image3);
                fileToUpload3 = MultipartBody.Part.createFormData("image3", "image3", requestBody3);
            }

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), getPreferences().getStringPref(P.USER_ID));
            RequestBody mes = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody to = RequestBody.create(MediaType.parse("text/plain"), "ASM");
            if (getPreferences().getStringPref(P.USER_TYPE).equals(C.ASM))
                to = RequestBody.create(MediaType.parse("text/plain"), "GM");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.sendMessage(fileToUpload1, fileToUpload2, fileToUpload3, userId, mes, to);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data != null) {
                        if (data.getSuccess().equals("1")) {
                            MessagesFragment.update = true;
                            finish();
                        }
                        showToast(data.getMessage());
                    } else {
//                        assert serverResponse != null;
                        showToast(getString(R.string.error_message));
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    showToast(getString(R.string.error_message));
                    hideProgress();
                }
            });
        } else {
            showToast(getString(R.string.no_internet));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_attachment1:
                selectedImage = IMAGE1;
                U.hideKeyboard(mContext);
                if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
                    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(CreateMessageActivity.this);
                    if (marshMallowPermission.checkPermissionForExternalStorage()) {
                        imageOptions();
                    } else {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    }
                } else {
                    imageOptions();
                }
                break;

            case R.id.iv_attachment2:
                selectedImage = IMAGE2;
                U.hideKeyboard(mContext);
                if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
                    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(CreateMessageActivity.this);
                    if (marshMallowPermission.checkPermissionForExternalStorage()) {
                        imageOptions();
                    } else {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    }
                } else {
                    imageOptions();
                }
                break;

            case R.id.iv_attachment3:
                selectedImage = IMAGE3;
                U.hideKeyboard(mContext);
                if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
                    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(CreateMessageActivity.this);
                    if (marshMallowPermission.checkPermissionForExternalStorage()) {
                        imageOptions();
                    } else {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    }
                } else {
                    imageOptions();
                }
                break;

            case R.id.btn_send:
                message = edMessage.getText().toString().trim();
                if (message.equals("")) {
                    showToast(getString(R.string.enter_text));
                } else {
                    if (getPreferences().getStringPref(P.USER_TYPE).equals(C.GM)) {
                        sendBroadcastMessage();
                    } else if (getPreferences().getStringPref(P.USER_TYPE).equals(C.ASM)) {
                        sendToDialog();
                    } else {
                        sendMessage();
                    }
                }
                break;
        }
    }

    //get image
    private void sendToDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setItems(R.array.send_message_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        sendBroadcastMessage();
                        break;
                    case 1:
                        dialog.dismiss();
                        sendMessage();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

}

