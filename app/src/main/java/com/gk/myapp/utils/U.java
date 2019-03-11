package com.gk.myapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gk.myapp.R;
import com.gk.myapp.app.AppController;
import com.gk.myapp.interfaces.ExpEdListener;
import com.gk.myapp.interfaces.QuantityDialogListener;
import com.gk.myapp.interfaces.YesNoDialogConfirmation;
import com.gk.myapp.model.ProductListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class U {


    private static ProgressDialog mProgress;

    public static void toast(String message) {
        Toast toast = Toast.makeText(AppController.getInstance().getContext(), message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    public static void toast(Context mContext,String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void showToastLong(Context mContext, String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    public static Bitmap getCircle(int color, int dia) {
        int diameter = (int) dpToPx(dia);
        Bitmap output = Bitmap.createBitmap(diameter, diameter,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, diameter, diameter, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);
        return output;
    }

    public static Bitmap getCircleRGB(String colorString, int dia) {
        int diameter = (int) dpToPx(dia);
        String colorRGB[] = colorString.split(",");
        int r = Integer.parseInt(colorRGB[0]);
        int g = Integer.parseInt(colorRGB[1]);
        int b = Integer.parseInt(colorRGB[2]);
        String hex = String.format("#%02x%02x%02x", r, g, b);
        int color = Color.parseColor(hex);
        Bitmap output = Bitmap.createBitmap(diameter, diameter,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, diameter, diameter, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);
        return output;
    }

    public static float dpToPx(float _Dp) {
        return _Dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static boolean isConnectedToInternet(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public static String utcDateTime(String date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date myDate = simpleDateFormat.parse(date);
            SimpleDateFormat converted = new SimpleDateFormat(format);
            converted.setTimeZone(TimeZone.getTimeZone("UTC"));
            String utcDate = converted.format(myDate);
            return utcDate.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String normalDateTime(String date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(date);
            SimpleDateFormat converted = new SimpleDateFormat(format);
            converted.setTimeZone(TimeZone.getDefault());
            String utcDate = converted.format(myDate);
            return utcDate.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void commonDialog(Context mContext, final YesNoDialogConfirmation mListener, String title, String msg, final int pos, final String type) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_common);
        mDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = AppController.getInstance().getScreenWidth() - U.width(25);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        TextView txtMessage = (TextView) mDialog.findViewById(R.id.txt_message);
        txtMessage.setText(msg);
        TextView btnOk = (TextView) mDialog.findViewById(R.id.btn_ok);
        txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, U.mediumText());
        btnOk.setTextSize(TypedValue.COMPLEX_UNIT_PX, U.smallText());
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(U.width(30), LinearLayout.LayoutParams.WRAP_CONTENT);
        btnOk.setLayoutParams(btnLp);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                if (mListener != null)
                    mListener.onYesClicked(pos, type);
            }
        });

        mDialog.show();
    }

    public static void yesNoDialog(Context mContext, final YesNoDialogConfirmation mListener, String title, String msg, final int pos, final String type) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_yes_no);
        mDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = AppController.getInstance().getScreenWidth() - U.width(25);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        TextView txtMessage = (TextView) mDialog.findViewById(R.id.txt_message);
//        TextView txtHeading=(TextView)mDialog.findViewById(R.id.txt_heading);
//        txtHeading.setText(title);
        txtMessage.setText(msg);
        TextView btnYes = (TextView) mDialog.findViewById(R.id.btn_yes);
        TextView btnNo = (TextView) mDialog.findViewById(R.id.btn_no);

        txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, U.mediumText());
        btnYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, U.smallText());
        btnNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, U.smallText());
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(U.width(30), LinearLayout.LayoutParams.WRAP_CONTENT);
        btnYes.setLayoutParams(btnLp);
        btnNo.setLayoutParams(btnLp);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                if (mListener != null)
                    mListener.onYesClicked(pos, type);
            }
        });


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    //progress dialog
    public static void showProgress(Context mContext, boolean trans) {
        try {
            if (mProgress == null)
                mProgress = new ProgressDialog(mContext);
            mProgress.show();
            if (trans) {
                mProgress.setContentView(R.layout.blank_layout);
                mProgress.getWindow().setDimAmount(0.0f);
            } else
                mProgress.setContentView(R.layout.dialog_progress);
            mProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgress.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
            mProgress = null;
        }
    }

    public static void showProgress(Context mContext) {
        try {
            if (mProgress == null)
                mProgress = new ProgressDialog(mContext);
            mProgress.show();
            mProgress.setContentView(R.layout.dialog_progress);
            mProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgress.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
            mProgress = null;
        }
    }

    public static void hideProgress() {
        try {
            if (mProgress != null) {
                mProgress.hide();
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgress = null;
        }
    }

    public static int screenWidth(Activity mActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int vvlargeText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.09);
    }

    public static int vlargeText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.07);
    }

    public static int largeText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.045);
    }

    public static int mediumText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.04);
    }

    public static int mediumSmallText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.035);
    }

    public static int smallText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.03);
    }

    public static int vsmallText() {
        return (int) (AppController.getInstance().getScreenWidth() * 0.025);
    }

    public static int width(float w) {
        float s = (float) w / 100;
        return (int) (AppController.getInstance().getScreenWidth() * s);
    }

    public static int height(float h) {
        float s = (float) h / 100;
        return (int) (AppController.getInstance().getScreenHeight() * s);
    }

    public static void showEdDialog(final Context mContext, String msg, final int position, final String type, final QuantityDialogListener mListener) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_ed);
        mDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = AppController.getInstance().getScreenWidth() - U.width(25);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
//        TextView txtMessage=(TextView)mDialog.findViewById(R.id.txt_message);
//        TextView txtHeading=(TextView)mDialog.findViewById(R.id.txt_heading);
//        txtHeading.setText(title);
//        txtMessage.setText(msg);

        final EditText ed = (EditText) mDialog.findViewById(R.id.ed_txt);
        ed.setHint(msg);
        Button btnOk = (Button) mDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = ed.getText().toString().trim();
                if (!txt.equals("")) {
                    mDialog.dismiss();
                    if (mListener != null)
                        mListener.onOkClicked(position, type, txt);
                }else{
                    U.toast("Enter a value");
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    public static void showEdDialogExpendable(final Context mContext, String msg, final ProductListResponse.ProductAttributes obj, final String type, final ExpEdListener mListener) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_ed);
        mDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = AppController.getInstance().getScreenWidth() - U.width(25);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
//        TextView txtMessage=(TextView)mDialog.findViewById(R.id.txt_message);
//        TextView txtHeading=(TextView)mDialog.findViewById(R.id.txt_heading);
//        txtHeading.setText(title);
//        txtMessage.setText(msg);

        final EditText ed = (EditText) mDialog.findViewById(R.id.ed_txt);
        ed.setHint(msg);
        Button btnOk = (Button) mDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = ed.getText().toString().trim();
                if (!txt.equals("")) {
                    int t=Integer.parseInt(txt);
                    mDialog.dismiss();
                    if (mListener != null)
                        mListener.onOkClick(obj, type, t);
                }else{
                    U.toast("Enter a value");
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    //    public static String getUrl(ArrayList<NameValuePair> prms,String type){
//        String url=C.BASE_URL+type;
//        String paramString = URLEncodedUtils
//                .format(prms, "utf-8");
//        url += "?" + paramString;
//        url=url.replace("+", "%20");
//        return url;
//    }


    //gson array
//    Gson gson = new Gson();
//    String jsonOutput = "Your JSON String";
//    Type listType = new TypeToken<List<Post>>(){}.getType();
//    List<Post> posts = (List<Post>) gson.fromJson(jsonOutput, listType);

    //gson object
//    Gson gson = new Gson();
//    UserData data = gson.fromJson(mPrefrences.getLoginData(), UserData.class);


//    gson code
//    Gson gson = new Gson();
//    String jsonOutput =mainJson.getJSONArray("data").toString();
//    Type listType = new TypeToken<List<MyShipmentModel>>(){}.getType();
//    mList= (ArrayList<MyShipmentModel>) gson.fromJson(jsonOutput, listType);
//
//    normal
//    Gson gson = new Gson();
//    UserData data = gson.fromJson(mPrefrences.getLoginData(), UserData.class);


//    glide      compile 'com.github.bumptech.glide:glide:3.7.0'
//    Glide.with(getActivity()).load(image.getLarge())
//            .thumbnail(0.5f)
//    .crossFade()
//    .diskCacheStrategy(DiskCacheStrategy.ALL)
//    .into(imageViewPreview);

//    Glide.with(mContext).load(imgUrl)
//    .thumbnail(0.5f)
//    .crossFade()
//    .diskCacheStrategy(DiskCacheStrategy.ALL)
//    .into(imageView);

}
