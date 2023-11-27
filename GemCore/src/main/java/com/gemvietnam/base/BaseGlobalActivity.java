package com.gemvietnam.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gemvietnam.delegate.OnPickContact;
import com.gemvietnam.utils.ActivityUtils;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Base activity
 * Created by neo on 2/5/2016.
 */
public class BaseGlobalActivity extends AppCompatActivity {
  private static int PICK_CONTACT = 10;
  protected OnPickContact pickContactDelegate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }
}
