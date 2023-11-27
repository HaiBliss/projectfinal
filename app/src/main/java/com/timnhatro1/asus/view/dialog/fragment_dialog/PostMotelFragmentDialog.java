package com.timnhatro1.asus.view.dialog.fragment_dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gemvietnam.base.log.Logger;
import com.gemvietnam.utils.RecyclerUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.model.UserModel;
import com.timnhatro1.asus.model.event_bus.EventBusChoosePicture;
import com.timnhatro1.asus.model.event_bus.EventBusUpdateListMotelPost;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.GifSizeFilter;
import com.timnhatro1.asus.utils.Glide4Engine;
import com.timnhatro1.asus.utils.StringUtils;
import com.timnhatro1.asus.view.activity.LocationSelectActivity;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.view.adapter.ChoosePictureRecyclerAdapter;
import com.timnhatro1.asus.view.dialog.bottom_sheet_dialog.PickPictureBottomSheetFragment;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PostMotelFragmentDialog extends DialogFragment {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_done)
    ImageView mIvDone;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_price)
    EditText mEtPrice;
    @BindView(R.id.et_space)
    EditText mEtSpace;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    @BindView(R.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R.id.rv_picture)
    RecyclerView mRvPicture;
    @BindView(R.id.infor_more)
    View mInforMore;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.ll_infor_more)
    View mLLInforMore;
    @BindView(R.id.spinner_chung_chu)
    Spinner mSpinnerChungChu;
    @BindView(R.id.spinner_cho_de_xe)
    Spinner mSpinnerChoDeXe;
    @BindView(R.id.spinner_khep_kin)
    Spinner mSpinnerKhepKin;
    @BindView(R.id.spinner_cho_phoi_quan_ao)
    Spinner mSpinnerChoPhoiQuanAo;
//    @BindView(R.id.spinner_gio_giac)
//    Spinner mSpinnerGioGiac;
    private boolean isShowInforMore = false;
    private ChoosePictureRecyclerAdapter adapter;
    private List<Bitmap> listBitmap;
    private String linkImage = "";
    public static final int REQUEST_CODE_CHOOSE = 101;
    public static final int CAMERA_REQUEST = 102;
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private UserModel userModel;
    private SharedPreferences mPrefs;
    private int sizeImage;
    private int currentImage;
    private boolean isFault;
    private String chungChu;
    private String choDeXe;
    private String khepKin;
    private String phoiQuanAo;
    private boolean isEdit = false;


    private String tieuDeEdit;
    private String giaPhongEdit;
    private String dienTichEdit;
    private String dienThoaiEdit;
    private String diaChiEdit;
    private String moTaEdit;
    private String chungChuEdit;
    private String choDeXeEdit;
    private String khepKinEdit;
    private String phoiQuanAoEdit;
    private String anhEdit;
    private String idEdit = "";
    private int sizeImageEdit;
    private int sizeImageCurrentEdit;
    private boolean isFaultEdit = false;
    private TextWatcher textWatcherSpace;
    private TextWatcher textWatcherPrice;
    private AutocompleteSupportFragment autocompleteFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mPrefs = getActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
    }

    public void setEdit(boolean edit, String tieuDeEdit,
             String giaPhongEdit,
             String dienTichEdit,
             String dienThoaiEdit,
             String diaChiEdit,
             String moTaEdit,
             String chungChuEdit,
             String choDeXeEdit,
             String khepKinEdit,
             String phoiQuanAoEdit,
             String anhEdit,
             String idEdit) {
        isEdit = edit;
        this.tieuDeEdit = tieuDeEdit;
        this.giaPhongEdit = giaPhongEdit;
        this.dienTichEdit = dienTichEdit;
        this.dienThoaiEdit = dienThoaiEdit;
        this.diaChiEdit = diaChiEdit;
        this.moTaEdit = moTaEdit;
        this.chungChuEdit = chungChuEdit;
        this.choDeXeEdit =choDeXeEdit;
        this.khepKinEdit = khepKinEdit;
        this.phoiQuanAoEdit =phoiQuanAoEdit;
        this.anhEdit = anhEdit;
        this.idEdit = idEdit;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (autocompleteFragment!=null) {
            getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveMotelModelChange(EventBusChoosePicture event){
        if (event.getBitmap()!=null) {
            listBitmap.add(event.getBitmap());
            adapter.notifyDataSetChanged();
        }
        if (event.getBitmapList()!=null) {
            listBitmap.addAll(event.getBitmapList());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return dialog;
    }
    @OnClick({R.id.iv_map})
    public void onClickMap() {
        Intent intent = new Intent(getActivity(), LocationSelectActivity.class);
        startActivityForResult(intent,LocationSelectActivity.RESULT_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationSelectActivity.RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                double lat = data.getDoubleExtra(LocationSelectActivity.LATITUDE,-1);
                double lng = data.getDoubleExtra(LocationSelectActivity.LONGITUDE,-1);
                if (lat == -1 || lng == -1) {
                    Toast.makeText(getContext(),"Lỗi không lấy được địa chỉ",Toast.LENGTH_SHORT).show();
                    return;
                }
                autocompleteFragment.setText(getCompleteAddressString(lat,lng));
            }
        }
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Toast.makeText(getContext(),"Lỗi không lấy được địa chỉ",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Lỗi không lấy được địa chỉ",Toast.LENGTH_SHORT).show();
        }
        return strAdd;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_post_motel,container,false);
        ButterKnife.bind(this,view);
        initSpinner();
        initView();
        initPlaceAutoComplete();
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

        }
        if (isEdit)
            initViewEdit();
        return view;
    }
    private void initPlaceAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.maps_api_key));
        }
//        PlacesClient placesClient = Places.createClient(getActivity());
        autocompleteFragment = (AutocompleteSupportFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment1);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("VN");
        autocompleteFragment.setHint("Nhập địa chỉ");
// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autocompleteFragment.setText(place.getAddress());
                    }
                },100);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("khoado",status.getStatusMessage());
            }
        });
    }
    private void initViewEdit() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtils.showProgressDialog(getActivity());
            }
        },100);
        mEtTitle.setText(tieuDeEdit);
        mEtPrice.setText(StringUtils.formatPriceNumber(giaPhongEdit));
        mEtSpace.setText(StringUtils.formatPriceNumber(dienTichEdit));
        mEtPhone.setText(dienThoaiEdit);
//        mEtAddress.setText(diaChiEdit);
        autocompleteFragment.a.setText(diaChiEdit);
        mEtDescription.setText(moTaEdit);
        if (chungChuEdit.isEmpty()) {
            mSpinnerChungChu.setSelection(0);
        } else if (chungChuEdit.equals("Có")) {
            mSpinnerChungChu.setSelection(1);
        } else {
            mSpinnerChungChu.setSelection(2);
        }
        if (choDeXeEdit.isEmpty()) {
            mSpinnerChoDeXe.setSelection(0);
        } else if (choDeXeEdit.equals("Có")) {
            mSpinnerChoDeXe.setSelection(1);
        } else {
            mSpinnerChoDeXe.setSelection(2);
        }
        if (khepKinEdit.isEmpty()) {
            mSpinnerKhepKin.setSelection(0);
        } else if (khepKinEdit.equals("Có")) {
            mSpinnerKhepKin.setSelection(1);
        } else {
            mSpinnerKhepKin.setSelection(2);
        }
        if (phoiQuanAoEdit.isEmpty()) {
            mSpinnerChoPhoiQuanAo.setSelection(0);
        } else if (phoiQuanAoEdit.equals("Có")) {
            mSpinnerChoPhoiQuanAo.setSelection(1);
        } else {
            mSpinnerChoPhoiQuanAo.setSelection(2);
        }
        String[] linkImage = anhEdit.split(" ");
        sizeImageEdit = linkImage.length;
        sizeImageCurrentEdit = 1;
        for (String link : linkImage) {
            link = link.replaceAll(" ","%20");
            StorageReference httpsReference = mStorage.getReferenceFromUrl(link);
            httpsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Use the bytes to display the image
                    if (isFaultEdit)
                        return;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    listBitmap.add(bitmap);
                    adapter.notifyDataSetChanged();
                    if (sizeImageCurrentEdit == sizeImageEdit) {
                        DialogUtils.dismissProgressDialog();
                    } else {
                        sizeImageCurrentEdit++;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    if (isFaultEdit)
                        return;
                    DialogUtils.dismissProgressDialog();
                    isFaultEdit = true;
                }
            });
            if (isFaultEdit)
                break;
        }
        if (isShowInforMore) {
            isShowInforMore = false;
        } else {
            isShowInforMore = true;
        }
        showInforMore(isShowInforMore);

    }

    private void initView() {
        mIvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickPictureBottomSheetFragment dialog = new PickPictureBottomSheetFragment(new PickPictureBottomSheetFragment.OnClickChoose() {
                    @Override
                    public void onClickedGallery() {
                        RxPermissions rxPermissions = new RxPermissions(getActivity());
                        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if (aBoolean) {
                                            pickPictureFromGallery();
//                            changeNumberLayout.setVisibility(View.VISIBLE);
                                        } else {
                                            //loadWithPermissionContact();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e("ErrorPermission", throwable.toString());
                                    }
                                });

                    }

                    @Override
                    public void onClickedCamera() {
                        RxPermissions rxPermissions = new RxPermissions(getActivity());
                        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if (aBoolean) {
                                            pickPictureFromCamera();
//                            changeNumberLayout.setVisibility(View.VISIBLE);
                                        } else {
                                            //loadWithPermissionContact();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e("ErrorPermission", throwable.toString());
                                    }
                                });
                    }
                });
                dialog.show(getFragmentManager(),dialog.getTag());
            }
        });
        RecyclerUtils.setupHorizontalRecyclerView(getActivity(),mRvPicture);
        listBitmap = new ArrayList<>();
        adapter = new ChoosePictureRecyclerAdapter(getActivity(),listBitmap);
        mRvPicture.setAdapter(adapter);
        textWatcherSpace = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEtSpace.removeTextChangedListener(textWatcherSpace);
                if (!StringUtils.isEmpty(mEtSpace.getText())) {
                    try {
                        mEtSpace.setText(StringUtils.formatPriceNumber(mEtSpace.getText().toString().replaceAll("\\.", "")));
                    } catch (Exception ex) {
                        Logger.w(ex);
                    }
                }
                mEtSpace.addTextChangedListener(textWatcherSpace);
                mEtSpace.setSelection(mEtSpace.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mEtSpace.addTextChangedListener(textWatcherSpace);
        textWatcherPrice = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEtPrice.removeTextChangedListener(textWatcherPrice);
                if (!StringUtils.isEmpty(mEtPrice.getText())) {
                    try {
                        mEtPrice.setText(StringUtils.formatPriceNumber(mEtPrice.getText().toString().replaceAll("\\.", "")));
                    } catch (Exception ex) {
                        Logger.w(ex);
                    }
                }
                mEtPrice.addTextChangedListener(textWatcherPrice);
                mEtPrice.setSelection(mEtPrice.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mEtPrice.addTextChangedListener(textWatcherPrice);
    }

    private void pickPictureFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    public void animationRoundImage(ImageView imageView) {
        float deg = (imageView.getRotation() == 180f) ? 0F : 180f;
        imageView.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
    }
    private void pickPictureFromGallery() {
        Matisse.from(getActivity())
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .maxSelectable(9)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())    // for glide-V4
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(
                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("onSelected", "onSelected: pathList=" + pathList);

                    }
                })
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void initSpinner() {
        List<String> listOptionsSpinner = new ArrayList<>();
        listOptionsSpinner.add("---");
        listOptionsSpinner.add("Có");
        listOptionsSpinner.add("Không");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerChungChu.setAdapter(adapter);
        mSpinnerChungChu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerChungChu.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinnerChoDeXe.setAdapter(adapter);
        mSpinnerChoDeXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerChoDeXe.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinnerKhepKin.setAdapter(adapter);
        mSpinnerKhepKin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerKhepKin.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinnerChoPhoiQuanAo.setAdapter(adapter);
        mSpinnerChoPhoiQuanAo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerChoPhoiQuanAo.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        List<String> listOptionsGioGiac = new ArrayList<>();
//        listOptionsGioGiac.add("---");
//        listOptionsGioGiac.add("Thoải mái");
//        listOptionsGioGiac.add("Trước 23h");
//        listOptionsGioGiac.add("Khác");
//        ArrayAdapter<String> adapterGioGiac = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsGioGiac);
//        adapterGioGiac.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        mSpinnerGioGiac.setAdapter(adapterGioGiac);
//        mSpinnerGioGiac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerGioGiac.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


    }

    @OnClick({R.id.iv_back,R.id.iv_done,R.id.infor_more})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.iv_done:
                Toast.makeText(getActivity(),"upload",Toast.LENGTH_SHORT).show();
                if (validate()) {
                    DialogUtils.showProgressDialog(getActivity());
                    sizeImage = listBitmap.size();
                    currentImage = 1;
                    isFault = false;
                    if (mSpinnerChungChu.getSelectedItem().toString().equals("---")) {
                        chungChu = "";
                    } else {
                        chungChu = mSpinnerChungChu.getSelectedItem().toString();
                    }
                    if (mSpinnerChoDeXe.getSelectedItem().toString().equals("---")) {
                        choDeXe = "";
                    } else {
                        choDeXe = mSpinnerChoDeXe.getSelectedItem().toString();
                    }
                    if (mSpinnerChoPhoiQuanAo.getSelectedItem().toString().equals("---")) {
                        phoiQuanAo = "";
                    } else {
                        phoiQuanAo = mSpinnerChoPhoiQuanAo.getSelectedItem().toString();
                    }
                    if (mSpinnerKhepKin.getSelectedItem().toString().equals("---")) {
                        khepKin = "";
                    } else {
                        khepKin = mSpinnerKhepKin.getSelectedItem().toString();
                    }

                    for (int i = 0 ;i < listBitmap.size() ; i++) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        listBitmap.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        if (userModel!=null) {
                            StorageReference mountainsRef = mStorageRef.child(userModel.getEmail()+"/" + System.currentTimeMillis() +".jpg");
                            UploadTask uploadTask = mountainsRef.putBytes(data);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (isFault)
                                        return;
                                    if (task.isSuccessful()) {
                                        Task<Uri> downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                linkImage = linkImage + uri.toString() + " ";
                                                if (currentImage == sizeImage) {
                                                    NetworkController.postMotel(idEdit,userModel.getEmail(),mEtTitle.getText().toString(),mEtPrice.getText().toString().replaceAll("\\D",""),
                                                            mEtSpace.getText().toString().replaceAll("\\D",""),mEtPhone.getText().toString(),
                                                            autocompleteFragment.a.getText().toString().trim(),mEtDescription.getText().toString()
                                                            ,linkImage.trim(),chungChu,choDeXe,phoiQuanAo,khepKin, new CommonCallBack<MotelResult>(getActivity(),false) {
                                                                @Override
                                                                public void onCallSuccess(Call<MotelResult> call, Response<MotelResult> response) {
                                                                    if (getActivity()==null)
                                                                        return;
                                                                    DialogUtils.dismissProgressDialog();
                                                                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                                                    if (response.body().getErrorCode() == 0 ) {
                                                                        if (anhEdit!=null) {
                                                                            for (String link : anhEdit.split(" ")) {
                                                                                link = link.replaceAll(" ","%20");
                                                                                StorageReference photoRef = mStorage.getReferenceFromUrl(link);
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
                                                                        }
                                                                        EventBusUpdateListMotelPost eventBus = new EventBusUpdateListMotelPost();
                                                                        eventBus.setMotelModel(response.body().getData().get(0));
                                                                        EventBus.getDefault().post(eventBus);
                                                                        dismiss();
                                                                    } else {
                                                                        System.out.println("lỗi firebase: " + response.body().getErrorCode());

                                                                    }

                                                                    //cap nhat view here;
                                                                }

                                                                @Override
                                                                public void onCallFailure(Call<MotelResult> call) {
                                                                    if (getActivity()==null)
                                                                        return;
                                                                    DialogUtils.dismissProgressDialog();
                                                                    Toast.makeText(getActivity(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                } else {
                                                    currentImage++;
                                                }
                                            }

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                isFault = true;
                                                DialogUtils.dismissProgressDialog();
                                                Toast.makeText(getActivity(),"Lỗi tải ảnh, thử lại sau!",Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                                    } else {
                                        isFault = true;
                                        DialogUtils.dismissProgressDialog();
                                        Toast.makeText(getActivity(),"Lỗi tải ảnh, thử lại sau!",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    isFault = true;
                                    DialogUtils.dismissProgressDialog();
                                    Toast.makeText(getActivity(),"Lỗi tải ảnh, thử lại sau!",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(),"Lỗi hệ thống, thử lại sau!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (isFault)
                            break;


                    }
                }
                break;
            case R.id.infor_more:
                if (isShowInforMore) {
                    isShowInforMore = false;
                } else {
                    isShowInforMore = true;
                }
                showInforMore(isShowInforMore);
                break;
        }
    }

    private boolean validate() {
        if (mEtTitle.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nhập tiêu đề",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEtPrice.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nhập giá phòng",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEtSpace.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nhập diện tích phòng",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEtPhone.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nhập số điện thoại liên hệ",Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (mEtAddress.getText().toString().isEmpty()) {
//            Toast.makeText(getActivity(),"Nhập địa chỉ phòng",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (autocompleteFragment.a.getText().toString().isEmpty()) {
            Toast.makeText(getContext(),"Nhập địa chỉ phòng",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEtDescription.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nhập mô tả phòng",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (listBitmap.size() < 3) {
            Toast.makeText(getActivity(),"Cần chọn tối thiểu 3 ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showInforMore(boolean isShowInforMore) {
        if (isShowInforMore) {
            mIvMore.setImageResource(R.drawable.ic_keyboard_arrow_up_grey_500_24dp);
//            animationRoundImage(mIvMore);
            mLLInforMore.setVisibility(View.VISIBLE);
        } else{
            mIvMore.setImageResource(R.drawable.ic_keyboard_arrow_down_grey_500_24dp);
//            animationRoundImage(mIvMore);
            mLLInforMore.setVisibility(View.GONE);
        }
    }
}
