package com.gemvietnam.base;

import com.gemvietnam.utils.ActivityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Base activity
 * Created by neo on 2/5/2016.
 */
public abstract class BaseActivity extends BaseGlobalActivity {

  private OnBackStackChangedListener mOnBackStackChangedListener = new OnBackStackChangedListener() {
    @Override
    public void onBackStackChanged() {
      onFragmentDisplay();
    }
  };

  public static Fragment getTopFragment(FragmentManager manager) {
    if (manager == null) {
      return null;
    }
    if (manager.getBackStackEntryCount() > 0) {
      String fragmentName = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
      List<Fragment> fragments = manager.getFragments();
      if (fragments != null && !fragments.isEmpty()) {
        Fragment topFragment = null;
        int i = 1;
        while (i < fragments.size() &&
            (topFragment == null || !isSameClass(topFragment, fragmentName))) {

          topFragment = fragments.get(fragments.size() - i);
          i++;
        }
        return topFragment;
      }
    } else {
      List<Fragment> fragments = manager.getFragments();
      if (fragments != null && !fragments.isEmpty()) {
        return fragments.get(0);
      }
    }
    return null;
  }

  private static boolean isSameClass(Fragment topFragment, String fragmentName) {
    String simpleName = topFragment.getClass().getSimpleName();
    return simpleName.equals(fragmentName);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    // Inject views
    ButterKnife.bind(this);

    // Prepare layout
    initLayout();
    initLayout(savedInstanceState);
    getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
  }

  protected void initLayout(Bundle savedInstanceState) {

  }

  @Override
  protected void onResume() {
    super.onResume();
    onFragmentDisplay();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
  }

  private void onFragmentDisplay() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        Fragment fragment = getTopFragment(getSupportFragmentManager());
        if (fragment instanceof BaseFragment) {
          ((BaseFragment) fragment).onDisplay();
        }
      }
    });
  }
  public void back() {
    FragmentManager manager = getSupportFragmentManager();
    if (manager.getBackStackEntryCount() > 0) {
      manager.popBackStack();
    } else {
      if (manager.getBackStackEntryCount() > 0) {
        manager.popBackStack();
      } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn thoát ứng dụng không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
            android.os.Process.killProcess(android.os.Process.myPid());
          }
        });
        builder.setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();

          }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
      }
    }
  }


  @Override
  public void onBackPressed() {
    back();
  }
  public abstract void initLayout();

//  public abstract void showAlertDialog(String message);
//
//  public abstract void showProgress();
//
//  public abstract void hideProgress();
//
//  public abstract void onRequestError(String errorCode, String errorMessage);
//
//  public abstract void showErrorAlert(Context context, String string);
//
//  public abstract void showNetworkErrorDialog(Activity activity);

  public void onRequestSuccess() {
//    hideProgress();
  }

  /**
   * Return layout resource id for activity
   */
  protected abstract int getLayoutId();

  /**
   * Hide keyboard of current focus view
   */
  public void hideKeyboard() {
    View view = this.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm =
          (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
  }

  /**
   * Hide keyboard of current focus view
   */
  public void hideKeyboard(View view) {
    if (view != null) {
      InputMethodManager imm =
          (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
  }

  /**
   * Show keyboard for {@link EditText}
   */
  public void showKeyboard(EditText editText) {
    editText.requestFocus();
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  public void replaceFragment(int containerId, BaseFragment fragment, boolean addToBackStack, String tag){
    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), fragment, containerId,
            addToBackStack, tag, false);
  }

  public void addFragment(int containerId, BaseFragment fragment, boolean addToBackStack,
                          String tag) {
    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, containerId,
        addToBackStack, tag, false);
  }

  public void addChildFragment(int containerId, FragmentManager fragmentManager, BaseFragment fragment, boolean addToBackStack,
                               String tag, boolean loadExisted) {
    ActivityUtils.addFragmentToActivity(fragmentManager, fragment, containerId,
        addToBackStack, tag, loadExisted);
  }

  public void addFragment(int containerId, BaseFragment fragment, boolean addToBackStack, boolean loadExisted) {
    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, containerId,
        addToBackStack, fragment.getClass().getSimpleName(), loadExisted);
  }

  public void addFragment(int containerId, BaseFragment fragment, boolean addToBackStack) {
    addFragment(containerId, fragment, addToBackStack, fragment.getClass().getSimpleName());
  }

  public void replaceFragment(int containerId, BaseFragment fragment) {
    replaceFragment(containerId, fragment, false, fragment.getClass().getSimpleName());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    FragmentManager manager = getSupportFragmentManager();
    if (manager != null && manager.getFragments() != null && !manager.getFragments().isEmpty()) {
      for (Fragment fragment : manager.getFragments()) {
        if (fragment != null) {
          fragment.onActivityResult(requestCode, resultCode, data);
        }
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    FragmentManager manager = getSupportFragmentManager();
    if (manager != null && manager.getFragments() != null && !manager.getFragments().isEmpty()) {
      for (Fragment fragment : manager.getFragments()) {
        if (fragment != null) {
          fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
      }
    }
  }
}
