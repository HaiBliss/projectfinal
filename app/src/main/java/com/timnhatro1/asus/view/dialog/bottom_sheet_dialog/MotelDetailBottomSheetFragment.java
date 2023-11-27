package com.timnhatro1.asus.view.dialog.bottom_sheet_dialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.interactor.model.motel.MotelDAO;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.view.adapter.AdapterImageSlider;
import com.timnhatro1.asus.view.dialog.DialogAddNote;
import com.timnhatro1.asus.view.dialog.DialogConfrim;
import com.timnhatro1.asus.view.dialog.DialogReport;
import com.timnhatro1.asus.model.event_bus.EventBusMotelModelSave;
import com.timnhatro1.asus.model.event_bus.EventBusUpdateListMotelPost;
import com.timnhatro1.asus.view.dialog.fragment_dialog.ImageFullSreenFragmentDialog;
import com.timnhatro1.asus.view.dialog.fragment_dialog.PostMotelFragmentDialog;
import com.timnhatro1.asus.model.UserModel;
import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.StringUtils;
import com.timnhatro1.asus.utils.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MotelDetailBottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.image)
    ImageView mIvAvatar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_space)
    TextView mTvSpace;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_time_post)
    TextView mTvTimePost;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.layout_dots)
    LinearLayout mLayoutDots;
    @BindView(R.id.tv_price_1)
    TextView mTvPrice1;
    @BindView(R.id.ll_note)
    View mLlNote;
    @BindView(R.id.edit_note)
    ImageView mEditNote;
    @BindView(R.id.et_note)
    EditText mEtNote;
    @BindView(R.id.ll_edit_note)
    View llEditNote;
    @BindView(R.id.iv_done)
    ImageView mIvDone;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.ll_add_note)
    View mLLAddNote;
    @BindView(R.id.tv_add_note)
    TextView mTvAddNote;
    @BindView(R.id.iv_saved)
    ImageView mIvSaved;
    @BindView(R.id.iv_phone)
    ImageView mIvPhone;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.iv_direction)
    ImageView mIvDirection;
    @BindView(R.id.source_internet)
    TextView mTvSourceInternet;
    @BindView(R.id.ll_khep_kin)
    View mLlKhepKin;
    @BindView(R.id.tv_khep_kin)
    TextView mTvKhepKin;
    @BindView(R.id.ll_chung_chu)
    View mLlChungChu;
    @BindView(R.id.tv_chung_chu)
    TextView mTvChungChu;
    @BindView(R.id.ll_cho_de_xe)
    View mLlChoDeXe;
    @BindView(R.id.tv_cho_de_xe)
    TextView mTvChoDeXe;
    @BindView(R.id.ll_cho_phoi_quan_ao)
    View mLlChoPhoiQuanAo;
    @BindView(R.id.tv_cho_phoi_quan_ao)
    TextView mTvChoPhoiQuanAo;
    @BindView(R.id.iv_edit)
    ImageView mIvEdit;
    @BindView(R.id.iv_remove)
    ImageView mIvRemove;
    @BindView(R.id.iv_static_map)
    ImageView mIvStaticMap;
    @BindView(R.id.iv_report)
    ImageView mIvReport;
    @BindView(R.id.tv_error)
    TextView mTvError;
    private AdapterImageSlider adapterImageSlider;
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    private LinearLayout lyt_profile;
    private MotelModel mMotelModel;
    private boolean canEdit = false;
    private SharedPreferences mPrefs;
    private UserModel userModel;
    private FirebaseStorage mFirebaseStorage;
    private boolean showStaticMap = false;
    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public void setmMotelModel(MotelModel mMotelModel) {
        this.mMotelModel = mMotelModel;
    }

    public void setShowStaticMap(boolean showStaticMap) {
        this.showStaticMap = showStaticMap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_detail_motel, null);
        dialog.setContentView(view);
        ButterKnife.bind(this,view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        mPrefs = getActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        try {
            if (mPrefs.getString(Constant.KEY_USER_MODEL, null) != null) {
                Gson gson = new Gson();
                String json = mPrefs.getString(Constant.KEY_USER_MODEL, "");
                if (!json.isEmpty()) {
                    userModel = gson.fromJson(json, UserModel.class);
                }
//            MyObject obj = gson.fromJson(json, MyObject.class);
            }
        } catch (Exception e) {
            userModel = new UserModel();
        }
        app_bar_layout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        lyt_profile = (LinearLayout) view.findViewById(R.id.lyt_profile);
        if (mMotelModel.getReason().isEmpty()) {
            mTvError.setVisibility(View.GONE);
        } else {
            mTvError.setVisibility(View.VISIBLE);
            mTvError.setText("(*"+mMotelModel.getReason()+")");
        }
        mIvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogReport dialogReport = new DialogReport(getActivity(), new DialogReport.OnClickDangKy() {
                    @Override
                    public void onClick(String codeReason) {
                        DialogUtils.showProgressDialog(getActivity());
                        NetworkController.reportMotel(DeviceUtils.getAndroidID(), codeReason, mMotelModel.getId(), new CommonCallBack<SimpleResult>(getActivity()) {
                            @Override
                            public void onCallSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                                DialogUtils.dismissProgressDialog();
                                if (response.body().getErrorCode() == 0 ) {
                                    Toast.makeText(getActivity(),"Báo cáo của bạn đã được hệ thống ghi nhận. Cảm ơn sự đóng góp của bạn",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCallFailure(Call<SimpleResult> call) {
                                DialogUtils.dismissProgressDialog();
                                Toast.makeText(getActivity(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialogReport.show();

            }
        });
        // set data to view
//        Tools.displayImageRound(getActivity(), (ImageView) view.findViewById(R.id.image), R.drawable.photo_female_1);

//        Tools.displayImageRound(getActivity(),mIvAvatar,R.drawable.photo_male_6);
        ((TextView) view.findViewById(R.id.name)).setText(mMotelModel.getEmailPost());
        ((TextView) view.findViewById(R.id.name_toolbar)).setText(mMotelModel.getPrice());
        ((View) view.findViewById(R.id.lyt_spacer)).setMinimumHeight(Tools.getScreenHeight() / 2);
        if (canEdit) {
            mIvSaved.setVisibility(View.GONE);
            mIvPhone.setVisibility(View.GONE);
            mIvEdit.setVisibility(View.VISIBLE);
            mIvRemove.setVisibility(View.VISIBLE);
            mIvReport.setVisibility(View.GONE);
        } else {
            mIvSaved.setVisibility(View.VISIBLE);
            mIvPhone.setVisibility(View.VISIBLE);
            mIvReport.setVisibility(View.VISIBLE);
            mIvEdit.setVisibility(View.GONE);
            mIvRemove.setVisibility(View.GONE);
        }
        setUpUIWithMotel(mMotelModel);
        setOnClick();


        hideView(app_bar_layout);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, getActionBarSize());
                    hideView(lyt_profile);
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                    showView(lyt_profile, getActionBarSize());
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((ImageButton) view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }

    private void showMap() {
        if (showStaticMap) {
            mIvStaticMap.setVisibility(View.VISIBLE);
            String link = String.format(getString(R.string.link_static_map), String.valueOf(mMotelModel.getLat()), String.valueOf(mMotelModel.getLng()), getString(R.string.maps_api_key));
            Tools.loadImage(getContext(),link,mIvStaticMap, R.drawable.default_image_thumbnail,R.drawable.default_image_thumbnail,false);
        } else {
            mIvStaticMap.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mFirebaseStorage = FirebaseStorage.getInstance();
    }



    private void setUpUIWithMotel(MotelModel mMotelModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTvDescription.setText(Html.fromHtml(mMotelModel.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mTvDescription.setText(Html.fromHtml(mMotelModel.getDescription()));
        }
        if (mMotelModel.isSave()) {
            mIvSaved.setImageResource(R.drawable.ic_bookmark_saved);
        } else {
            mIvSaved.setImageResource(R.drawable.ic_bookmark);
        }
        mTvTitle.setText(mMotelModel.getTitle());
        mTvAddress.setText(mMotelModel.getAddress());
        mTvSpace.setText(mMotelModel.getSpace());
        mTvPhone.setText(mMotelModel.getPhone());
        mTvTimePost.setText(Tools.howLongFrom(mMotelModel.getTimePost()));
        mTvPrice1.setText(mMotelModel.getPrice());
        if (mMotelModel.isSave()) {
            if (!StringUtils.isEmpty(mMotelModel.getNote())) {
                mLlNote.setVisibility(View.VISIBLE);
                mEtNote.setText(mMotelModel.getNote());
                mLLAddNote.setVisibility(View.GONE);
            } else {
                mLlNote.setVisibility(View.GONE);
                mLLAddNote.setVisibility(View.VISIBLE);
            }
        } else {
            mLlNote.setVisibility(View.GONE);
            mLLAddNote.setVisibility(View.GONE);
        }
        if (mMotelModel.getSourceInternet().equals("1") && !mMotelModel.getNameSource().isEmpty()) {
            mTvSourceInternet.setVisibility(View.VISIBLE);
            mTvSourceInternet.setText(String.format(getString(R.string.string_source_internet), mMotelModel.getNameSource()));
        } else {
            mTvSourceInternet.setVisibility(View.GONE);
        }
        if (!mMotelModel.getKhepKin().isEmpty()) {
            mLlKhepKin.setVisibility(View.VISIBLE);
            mTvKhepKin.setText(mMotelModel.getKhepKin());
        } else {
            mLlKhepKin.setVisibility(View.GONE);
        }
        if (!mMotelModel.getChungChu().isEmpty()) {
            mLlChungChu.setVisibility(View.VISIBLE);
            mTvChungChu.setText(mMotelModel.getChungChu());
        } else {
            mLlChungChu.setVisibility(View.GONE);
        }
        if (!mMotelModel.getChoDeXe().isEmpty()) {
            mLlChoDeXe.setVisibility(View.VISIBLE);
            mTvChoDeXe.setText(mMotelModel.getChoDeXe());
        } else {
            mLlChoDeXe.setVisibility(View.GONE);
        }
        if (!mMotelModel.getChoPhoiQuanAo().isEmpty()) {
            mLlChoPhoiQuanAo.setVisibility(View.VISIBLE);
            mTvChoPhoiQuanAo.setText(mMotelModel.getChoPhoiQuanAo());
        } else {
            mLlChoPhoiQuanAo.setVisibility(View.GONE);
        }
        initSliderImage();
        showMap();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(EventBusUpdateListMotelPost event) {
        if (event!=null && event.getMotelModel()!=null) {
            mMotelModel = event.getMotelModel();
            setUpUIWithMotel(event.getMotelModel());
        }
    }

    private void setOnClick() {
        mIvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogConfrim dialogConfrim = new DialogConfrim(getActivity(), "Xác nhận", "Bạn có chắc chắn muốn xoá thông tin về nhà trọ đã đăng này?",
                        "Đồng ý", "Huỷ bỏ", new DialogConfrim.OnClickDialogTTC() {
                    @Override
                    public void onButonYesClick() {
                        DialogUtils.showProgressDialog(getActivity());
                        NetworkController.removeMotelPost(userModel.getEmail(),mMotelModel.getId(), new CommonCallBack<SimpleResult>(getActivity(),false) {

                            @Override
                            public void onCallSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                                if (getActivity() == null)
                                    return;
                                DialogUtils.dismissProgressDialog();
                                Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                if (response.body().getErrorCode() == 0 ) {
                                    //remove success;
                                    for (String link : mMotelModel.getListPicture().split(" ")) {
                                        link = link.replaceAll(" ","%20");
                                        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(link);
                                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully
                                                Log.e("khoado", "onSuccess: deleted file");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                                Log.e("khoado", "onFailure: did not delete file");
                                            }
                                        });
                                    }
                                    EventBus.getDefault().post(new EventBusUpdateListMotelPost());
                                    dismiss();
                                }
                            }

                            @Override
                            public void onCallFailure(Call<SimpleResult> call) {
                                if (getActivity() == null)
                                    return;
                                DialogUtils.dismissProgressDialog();
                                Toast.makeText(getActivity(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onButtonNoClick() {

                    }

                    @Override
                    public void onButtonCloseClick() {

                    }
                });
                dialogConfrim.show();
            }
        });
        mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostMotelFragmentDialog dialog = new PostMotelFragmentDialog();
                dialog.setEdit(true,mMotelModel.getTitle(),mMotelModel.getPrice().replaceAll("\\D",""),mMotelModel.getSpace().replaceAll("\\D",""),mMotelModel.getPhone(),
                                mMotelModel.getAddress(),mMotelModel.getDescription(),mMotelModel.getChungChu(),mMotelModel.getChoDeXe(),
                                mMotelModel.getKhepKin(),mMotelModel.getChoPhoiQuanAo(),mMotelModel.getListPicture(),mMotelModel.getId());
                dialog.show(getFragmentManager(),dialog.getTag());
            }
        });
        mIvSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMotelModel.isSave()) {
                    DialogConfrim dialogConfrim = new DialogConfrim(getActivity(), "Xác nhận", "Bạn có chắc chắn muốn xóa lưu trữ về thông tin nhà trọ này?", "Đồng ý", "Hủy bỏ", new DialogConfrim.OnClickDialogTTC() {
                        @Override
                        public void onButonYesClick() {
                            mMotelModel.setSave(false);
                            deleteMotelModel();
                        }

                        @Override
                        public void onButtonNoClick() {

                        }

                        @Override
                        public void onButtonCloseClick() {

                        }
                    });
                    dialogConfrim.show();
                } else {
                    mMotelModel.setSave(true);
                    DialogAddNote dialog = new DialogAddNote(getActivity(), new DialogAddNote.OnFilterListener() {
                        @Override
                        public void onClickApply(String note) {
                            mMotelModel.setNote(note);
                            if (note.isEmpty()) {
                                mLlNote.setVisibility(View.GONE);
                                mLLAddNote.setVisibility(View.VISIBLE);
                            } else {
                                mLlNote.setVisibility(View.VISIBLE);
                                mEtNote.setText(mMotelModel.getNote());
                                mLLAddNote.setVisibility(View.GONE);
                                //save to database
                            }
                            upgradeMotel();
                        }
                    });
                    dialog.show();
                    saveMotelModel();
                }
                if (mMotelModel.isSave()) {
                    mIvSaved.setImageResource(R.drawable.ic_bookmark_saved);
                } else {
                    mIvSaved.setImageResource(R.drawable.ic_bookmark);
                }
                if (mMotelModel.isSave()) {
                    if (!StringUtils.isEmpty(mMotelModel.getNote())) {
                        mLlNote.setVisibility(View.VISIBLE);
                        mEtNote.setText(mMotelModel.getNote());
                        mLLAddNote.setVisibility(View.GONE);
                    } else {
                        mLlNote.setVisibility(View.GONE);
                        mLLAddNote.setVisibility(View.VISIBLE);
                    }
                } else {
                    mLlNote.setVisibility(View.GONE);
                    mLLAddNote.setVisibility(View.GONE);
                }
            }
        });
        mIvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mMotelModel.getPhone()));
                startActivity(intent);
            }
        });
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = mMotelModel.toString() +"\n--Ứng dụng Tìm nhà trọ--";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tin tức từ ứng dụng Tìm nhà trọ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing With"));
            }
        });
        mTvAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddNote dialog = new DialogAddNote(getActivity(), new DialogAddNote.OnFilterListener() {
                    @Override
                    public void onClickApply(String note) {
                        mMotelModel.setNote(note);
                        if (note.isEmpty()) {
                            mLlNote.setVisibility(View.GONE);
                            mLLAddNote.setVisibility(View.VISIBLE);
                        } else {
                            mLlNote.setVisibility(View.VISIBLE);
                            mEtNote.setText(mMotelModel.getNote());
                            mLLAddNote.setVisibility(View.GONE);
                            //save to database
                        }
                        upgradeMotel();
                    }
                });
                dialog.show();
            }
        });
        mEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtNote.setEnabled(true);
                mEtNote.requestFocus();
                mEtNote.setBackgroundResource(R.drawable.bg_border_gray);
                llEditNote.setVisibility(View.VISIBLE);
            }
        });
        mEtNote.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT ||
                        (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {

                    return true;
                }
                return false;
            }
        });
        mIvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtNote.setEnabled(false);
                mEtNote.setBackground(null);
                llEditNote.setVisibility(View.GONE);
                mMotelModel.setNote(mEtNote.getText().toString());
                if (mEtNote.getText().toString().isEmpty()) {
                    mLlNote.setVisibility(View.GONE);
                    mLLAddNote.setVisibility(View.VISIBLE);
                } else {
                    mLlNote.setVisibility(View.VISIBLE);
                    mEtNote.setText(mMotelModel.getNote());
                    mLLAddNote.setVisibility(View.GONE);
                }
                upgradeMotel();
                //save motel to database.
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtNote.setEnabled(false);
                mEtNote.setBackground(null);
                llEditNote.setVisibility(View.GONE);
                mEtNote.setText(mMotelModel.getNote());
                mLlNote.setVisibility(View.VISIBLE);
                mLLAddNote.setVisibility(View.GONE);
            }
        });
        mIvDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "http://maps.google.com/maps?q=loc:" + mMotelModel.getLat() + "," +  mMotelModel.getLng();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
    }

    private void saveMotelModel() {
        MotelDAO motelDAO = AppDatabase.getInstance(getContext()).getMotelDAO();
        motelDAO.insert(mMotelModel);
        EventBus.getDefault().post(new EventBusMotelModelSave(mMotelModel));
    }

    private void deleteMotelModel() {
        MotelDAO motelDAO = AppDatabase.getInstance(getContext()).getMotelDAO();
        motelDAO.delete(mMotelModel);
        EventBus.getDefault().post(new EventBusMotelModelSave(mMotelModel));
//        if (callBackSave!=null) {
//            callBackSave.onSave(false);
//        }
    }
    private void upgradeMotel() {
        MotelDAO motelDAO = AppDatabase.getInstance(getContext()).getMotelDAO();
        motelDAO.update(mMotelModel);
        EventBus.getDefault().post(new EventBusMotelModelSave(mMotelModel));
    }

    private void initSliderImage() {
        List<String> items = new ArrayList<>();
        String[] listPicture = mMotelModel.getListPicture().split(" ");
        if (listPicture.length > 0) {
            items.addAll(Arrays.asList(listPicture));
        } else {
            items.add("error");
        }
        adapterImageSlider = new AdapterImageSlider(getActivity(), items);
        adapterImageSlider.setOnItemClickListener(new AdapterImageSlider.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ImageFullSreenFragmentDialog dialog = new ImageFullSreenFragmentDialog(mMotelModel,position);
                dialog.show(getChildFragmentManager(),dialog.getTag());
            }
        });

        mViewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        mViewPager.setCurrentItem(0);
        addBottomDots(mLayoutDots, adapterImageSlider.getCount(), 0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(mLayoutDots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        startAutoSlider(adapterImageSlider.getCount());
    }
    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }

//    public interface CallBackSave {
//        void onSave(boolean isSave);
//
//    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
