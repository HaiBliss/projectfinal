package com.timnhatro1.asus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.timnhatro1.asus.view.activity.R;

import java.util.Calendar;
import java.util.Date;

public class Tools {

    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }
    public static int getActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }
    public static String howLongFrom(String time_post) {
        try {
            Date fromTime = new Date(Long.valueOf(time_post));
            String result;
            long time = (Calendar.getInstance().getTime().getTime() - fromTime.getTime()) / 1000;
            if (time / 60 < 60) {
                result = (time / 60) + " phút trước";
            } else if (time / 3600 < 24) {
                result = (time / 3600) + " giờ trước";
            } else if (time / 86400 < 30) {
                result = (time / 86400) + " ngày trước";
            } else {
                result = (time / 2592000) + " tháng trước";
            }
            return result;
        } catch (Exception e) {
            Log.e("khoado",e.toString());
            return "Không xác định";
        }


    }
    public static void displayImageRound(final Context ctx, final ImageView img, @DrawableRes int drawable) {
//        try {
//            Glide.with(ctx).load(drawable).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    img.setImageDrawable(circularBitmapDrawable);
//                }
//            });
//        } catch (Exception e) {
//        }

        try {
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(ctx).asBitmap().load(drawable).apply(options).into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        }catch (Exception e){

        }
    }
    public static void displayImageRound(final Context ctx, final ImageView img, String url) {
//        try {
//            Glide.with(ctx).load(url).asBitmap().placeholder(R.drawable.default_image_thumbnail).error(R.drawable.default_image_thumbnail).centerCrop().into(new BitmapImageViewTarget(img) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    img.setImageDrawable(circularBitmapDrawable);
//                }
//            });
//        } catch (Exception e) {
//        }
        try {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.default_image_thumbnail)
                    .error(R.drawable.default_image_thumbnail);
            Glide.with(ctx).asBitmap().load(url).apply(options).into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        }catch (Exception e){

        }


    }

    public static void loadImage(Context context, String linkImage, ImageView imageView, int placeHolderId, int errorId, boolean fit) {
        String imageUrl = linkImage;
        if (StringUtils.isEmpty(imageUrl)) {
            imageUrl = null;
        }
        imageUrl = imageUrl.replaceAll(" ","%20");

        if (!fit) {
//            Glide
//                    .with(context)
//                    .load(imageUrl)
//                    .placeholder(placeHolderId)
//                    .error(errorId)
//                    .centerCrop()
//                    .dontAnimate()
//                    .into(imageView);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(placeHolderId)
                    .error(errorId)
                    .dontAnimate();
            Glide.with(context).load(imageUrl).apply(options).into(imageView);
        } else {
//            Glide
//                    .with(context)
//                    .load(imageUrl)
//                    .fitCenter()
//                    .placeholder(placeHolderId)
//                    .error(errorId)
//                    .dontAnimate()
//                    .into(imageView);

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(placeHolderId)
                    .error(errorId)
                    .dontAnimate();
            Glide.with(context).load(imageUrl).apply(options).into(imageView);
        }
    }
    public static void animationRoundImage(ImageView imageView) {
        float deg = (imageView.getRotation() == 360f) ? 0F : 360f;
        imageView.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public static void setupVerticalRecyclerView(Context context, RecyclerView mRecyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setHasFixedSize(true);
    }

    public static void setupHorizontalRecyclerView(Context context, RecyclerView mRecyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setHasFixedSize(true);
    }
    public static Bitmap getBitmapFromView(View view) {
        if (view.getMeasuredHeight() <= 0) {
            view.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(c);
            return b;
        } else {
            //Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
            //Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);
            //Get the view's background
            Drawable bgDrawable =view.getBackground();
            if (bgDrawable!=null)
                //has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            else
                //does not have background drawable, then draw white background on the canvas
                canvas.drawColor(Color.WHITE);
            // draw the view on the canvas
            view.draw(canvas);
            //return the bitmap
            return returnedBitmap;
        }
    }
    public static void hideKeyboard(Activity context) {
        try {
            if (context != null) {
                context.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }

        } catch (Exception e) {
            Log.e("Tools.hideKeyboard", e.toString());
        }
    }
    public static void hideKeyboard(View focusingView, Activity context) {
        try {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (focusingView != null) {
                imm.hideSoftInputFromWindow(focusingView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                imm.hideSoftInputFromWindow(context.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            Log.e("Tools.hideKeyboard", e.toString());
        }
    }

}
