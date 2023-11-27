package com.timnhatro1.asus.view.dialog.fragment_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.MyApplication;
import com.timnhatro1.asus.view.activity.LocationSelectActivity;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.model.LocationModel;
import com.timnhatro1.asus.utils.DummyData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class SearchMotelFragmentDialog extends DialogFragment {
    @BindView(R.id.spinner_ban_kinh)
    Spinner mSpinnerBanKinh;
    @BindView(R.id.rb_search_near_me)
    RadioButton mRbSearchNearMe;
    @BindView(R.id.rb_search_direct)
    RadioButton mRbSearchDirect;
    @BindView(R.id.rb_search_khu_vuc)
    RadioButton mRbSearchKhuVuc;
    @BindView(R.id.ll_ban_kinh)
    View mViewBanKinh;
    @BindView(R.id.ll_direct)
    View mViewQuanHuyen;
//    @BindView(R.id.rb_filter_thap_cao)
//    RadioButton mRbFilterThapCao;
//    @BindView(R.id.rb_filter_cao_thap)
//    RadioButton mRbFilterCaoThap;
    @BindView(R.id.spinner_tinh_thanh_pho)
    Spinner mSpinnerTinhThanhPho;
    @BindView(R.id.spinner_quan_huyen)
    Spinner mSpinnerQuanHuyen;
    @BindView(R.id.spinner_chon_muc_gia)
    Spinner mSpinnerChonMucGia;
    @BindView(R.id.spinner_dien_tich)
    Spinner mSpinnerDienTich;
    @BindView(R.id.spinner_thoi_gian)
    Spinner mSpinnerThoiGian;
    @BindView(R.id.ll_search_map)
    View mViewSearchMap;
    public static final int CHOOSE_NEAR_ME = 1;
    public static final int CHOOSE_KHU_VUC = 2;
    public static final int CHOOSE_QUAN_HUYEN = 3;
    private int optionSearch;
//    private boolean isChooseThapCao;
    private OnDismiss onDismiss;
    private LocationModel locationModel;
    private List<LocationModel.ListProvince> provinceChoose;
    private int indexQuanHuyen;
    private AutocompleteSupportFragment autocompleteFragment;
    private LatLng latLngSearch;
    private float radius;
    private String minPrice, maxPrice, minSpace, maxSpace, time;
    public static final int PLACE_PICKER_REQUEST = 1;
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
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    public void setOnDismiss(OnDismiss onDismiss) {
        this.onDismiss = onDismiss;
    }

    private void initView() {
        initSpinner();
        initRadioButton();
        initPlaceAutoComplete();
    }

    private void initPlaceAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.maps_api_key));
        }
//        PlacesClient placesClient = Places.createClient(getActivity());
        autocompleteFragment = (AutocompleteSupportFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("VN");
        autocompleteFragment.setHint("Nhập vị trí tìm kiếm");
// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                Log.e("khoado",place.getAddress() + " " + place.getLatLng());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autocompleteFragment.setText(place.getAddress());
                    }
                },100);
                latLngSearch = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("khoado",status.getStatusMessage());
            }
        });
    }

    private void initRadioButton() {
        mRbSearchNearMe.setChecked(true);
        mRbSearchDirect.setChecked(false);
        mRbSearchKhuVuc.setChecked(false);
        mViewBanKinh.setVisibility(View.VISIBLE);
        mViewQuanHuyen.setVisibility(View.GONE);
        mViewSearchMap.setVisibility(View.GONE);
        optionSearch = CHOOSE_NEAR_ME;
        mRbSearchNearMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mViewBanKinh.setVisibility(View.VISIBLE);
                    mViewQuanHuyen.setVisibility(View.GONE);
                    mViewSearchMap.setVisibility(View.GONE);
                    optionSearch = CHOOSE_NEAR_ME;
                }
                clearSpinner();
            }
        });
        mRbSearchDirect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mViewBanKinh.setVisibility(View.GONE);
                    mViewQuanHuyen.setVisibility(View.VISIBLE);
                    mViewSearchMap.setVisibility(View.GONE);
                    optionSearch = CHOOSE_QUAN_HUYEN;
                }
                clearSpinner();
            }
        });
        mRbSearchKhuVuc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mViewBanKinh.setVisibility(View.VISIBLE);
                    mViewQuanHuyen.setVisibility(View.GONE);
                    mViewSearchMap.setVisibility(View.VISIBLE);
                    optionSearch = CHOOSE_KHU_VUC;
                }
                clearSpinner();
            }
        });
//        mRbFilterThapCao.setChecked(true);
//        mRbFilterCaoThap.setChecked(false);
//        isChooseThapCao = true;
//        mRbFilterThapCao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isChooseThapCao = b;
//            }
//        });
    }

    private void initSpinner() {
        List<String> listOptionsSpinner = new ArrayList<>();
        listOptionsSpinner.add("---");
        listOptionsSpinner.add("0.5km");
        listOptionsSpinner.add("1km");
        listOptionsSpinner.add("2km");
        listOptionsSpinner.add("4km");
        listOptionsSpinner.add("8km");
        listOptionsSpinner.add("10km");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerBanKinh.setAdapter(adapter);
        mSpinnerBanKinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerBanKinh.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                try {
//                    radius = Float.valueOf(mSpinnerBanKinh.getSelectedItem().toString().replaceAll("km",""));
//                } catch (Exception e) {
//                    radius = Constant.DEFAULT_RADIUS;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        locationModel = DummyData.initDummyData(getContext());
        List<String> listOptionsSpinnerTinhThanhPho = new ArrayList<>();
        listOptionsSpinnerTinhThanhPho.add("---");
        for (LocationModel.Data tinhThanhPho : locationModel.getData()) {
            listOptionsSpinnerTinhThanhPho.add(tinhThanhPho.getNameCity());
        }
        ArrayAdapter<String> adapterTinhThanhPho = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinnerTinhThanhPho);
        adapterTinhThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerTinhThanhPho.setAdapter(adapterTinhThanhPho);
        mSpinnerTinhThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupSpinnerQuanHuyen(mSpinnerTinhThanhPho.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        List<String> listOptionsSpinnerMucGia = new ArrayList<>();
        listOptionsSpinnerMucGia.add("Tất cả");
        listOptionsSpinnerMucGia.add("500.000 - 1.000.000");
        listOptionsSpinnerMucGia.add("1.000.000 - 1.500.000");
        listOptionsSpinnerMucGia.add("1.500.000 - 2.500.000");
        listOptionsSpinnerMucGia.add(">3.000.000");
        ArrayAdapter<String> adapterMucGia = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinnerMucGia);
        adapterMucGia.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerChonMucGia.setAdapter(adapterMucGia);
        mSpinnerChonMucGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerChonMucGia.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                if (mSpinnerChonMucGia.getSelectedItem().toString().equals("Tất cả")) {
//                    minPrice = "all";
//                    maxPrice = "all";
//                } else {
//                    String[] price = mSpinnerChonMucGia.getSelectedItem().toString().split("-");
//                    if (price.length > 1) {
//                        minPrice = price[0].replaceAll("\\D", "");
//                        maxPrice = price[1].replaceAll("\\D", "");
//                    } else {
//                        minPrice = price[0].replaceAll("\\D", "");
//                        maxPrice = "";
//                    }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        List<String> listOptionsSpinnerDienTich = new ArrayList<>();
        listOptionsSpinnerDienTich.add("Tất cả");
        listOptionsSpinnerDienTich.add("<20 m²");
        listOptionsSpinnerDienTich.add("20 m² - 30 m²");
        listOptionsSpinnerDienTich.add("30 m² - 50m²");
        listOptionsSpinnerDienTich.add(">50m²");
        ArrayAdapter<String> adapterDienTich = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinnerDienTich);
        adapterDienTich.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerDienTich.setAdapter(adapterDienTich);
        mSpinnerDienTich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerDienTich.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        List<String> listOptionsSpinnerThoiGian = new ArrayList<>();
        listOptionsSpinnerThoiGian.add("Tất cả");
        listOptionsSpinnerThoiGian.add("< 1 ngày trước");
        listOptionsSpinnerThoiGian.add("< 4 ngày trước");
        listOptionsSpinnerThoiGian.add("< 7 ngày trước");
        listOptionsSpinnerThoiGian.add("< 30 ngày trước");
        ArrayAdapter<String> adapterThoiGian = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinnerThoiGian);
        adapterThoiGian.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerThoiGian.setAdapter(adapterThoiGian);
        mSpinnerThoiGian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), mSpinnerThoiGian.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        List<String> listOptionsSpinnerQuanHuyen = new ArrayList<>();
//        listOptionsSpinnerQuanHuyen.add("---");
//        ArrayAdapter<String> adapterQuanHuyen = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinnerQuanHuyen);
//        adapterQuanHuyen.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        mSpinnerQuanHuyen.setAdapter(adapterQuanHuyen);
//        mSpinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), provinceChoose.get(i).getCode(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private void setupSpinnerQuanHuyen(String tinhThanhPho) {
        List<String> listOptionsSpinnerQuanHuyen = new ArrayList<>();
        for (LocationModel.Data data : locationModel.getData()) {
            if (data.getNameCity().equals(tinhThanhPho)) {
                for (LocationModel.ListProvince str : data.getListProvince()) {
                    listOptionsSpinnerQuanHuyen.add(str.getNameProvince());
                }
                provinceChoose = data.getListProvince();
                break;
            }
        }
        if (listOptionsSpinnerQuanHuyen.size() <1)
            listOptionsSpinnerQuanHuyen.add("---");
        ArrayAdapter<String> adapterQuanHuyen = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listOptionsSpinnerQuanHuyen);
        adapterQuanHuyen.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerQuanHuyen.setAdapter(adapterQuanHuyen);
        mSpinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), provinceChoose.get(i).getCode(), Toast.LENGTH_SHORT).show();
                indexQuanHuyen = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @OnClick({R.id.iv_back,R.id.tv_tim_kiem,R.id.tv_xoa_bo_loc})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.tv_tim_kiem:
                if (validate()) {
                    //
                    try {
                        radius = Float.valueOf(mSpinnerBanKinh.getSelectedItem().toString().replaceAll("km",""));
                    } catch (Exception e) {
                        radius = Constant.DEFAULT_RADIUS;
                    }
                    if (mSpinnerChonMucGia.getSelectedItem().toString().equals("Tất cả")) {
                        minPrice = "all";
                        maxPrice = "all";
                    } else {
                        String[] price = mSpinnerChonMucGia.getSelectedItem().toString().split("-");
                        if (price.length > 1) {
                            minPrice = price[0].replaceAll("\\D", "");
                            maxPrice = price[1].replaceAll("\\D", "");
                        } else {
                            minPrice = price[0].replaceAll("\\D", "");
                            maxPrice = "";
                        }
                    }
                    if (mSpinnerDienTich.getSelectedItem().toString().equals("Tất cả")) {
                        minSpace = "all";
                        maxSpace = "all";
                    } else {
                        String[] price = mSpinnerDienTich.getSelectedItem().toString().replaceAll("m²","").split("-");
                        if (price.length > 1) {
                            minSpace = price[0].replaceAll("\\D", "");
                            maxSpace = price[1].replaceAll("\\D", "");
                        } else {
                            if (price[0].contains("<")) {
                                maxSpace = price[0].replaceAll("\\D", "");
                                minSpace = "";
                            } else {
                                minSpace = price[0].replaceAll("\\D", "");
                                maxSpace = "";
                            }
                        }
                    }
                    if (mSpinnerThoiGian.getSelectedItem().toString().equals("Tất cả")) {
                        time = "all";
                    } else {
                        time = mSpinnerThoiGian.getSelectedItem().toString().replaceAll("\\D", "");
                        long timeFomat = Long.valueOf(time);
                        timeFomat = timeFomat* 86400000;
                        timeFomat = System.currentTimeMillis() - timeFomat;
                        time = String.valueOf(timeFomat);
                    }


                    if (onDismiss!=null) {
                        if (optionSearch == CHOOSE_NEAR_ME) {
                            onDismiss.onSearchNearMe(radius,minPrice,maxPrice,minSpace,maxSpace,time);
                        } else if (optionSearch == CHOOSE_QUAN_HUYEN) {
                            onDismiss.onSearchQuanHuyen(provinceChoose.get(indexQuanHuyen).getLat()
                                    ,provinceChoose.get(indexQuanHuyen).getLong(),minPrice,maxPrice,minSpace,maxSpace,time,provinceChoose.get(indexQuanHuyen).getCode());
                        } else  {
                            onDismiss.onSearchArea(latLngSearch.latitude,latLngSearch.longitude,radius,minPrice,maxPrice,minSpace,maxSpace,time);
                        }
                    }
                    dismiss();
                } else {

                }
                break;
            case R.id.tv_xoa_bo_loc:
                clearSpinner();
                break;
        }
    }

    private void clearSpinner() {
        mSpinnerDienTich.setSelection(0);
        mSpinnerChonMucGia.setSelection(0);
        mSpinnerTinhThanhPho.setSelection(0);
        mSpinnerBanKinh.setSelection(0);
        mSpinnerThoiGian.setSelection(0);
        autocompleteFragment.a.setText("");
    }

    private boolean validate() {
        if (optionSearch == CHOOSE_NEAR_ME) {
            if ( mSpinnerBanKinh.getSelectedItem().toString().equals("---")) {
                Toast.makeText(getContext(),"Cần chọn bán kính tìm kiếm",Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (optionSearch == CHOOSE_QUAN_HUYEN) {
            if ( mSpinnerTinhThanhPho.getSelectedItem().toString().equals("---")) {
                Toast.makeText(getContext(),"Cần chọn Tỉnh/TP tìm kiếm",Toast.LENGTH_SHORT).show();
                return false;
            }
            if ( mSpinnerQuanHuyen.getSelectedItem().toString().equals("---")) {
                Toast.makeText(getContext(),"Cần chọn Quận/Huyện tìm kiếm",Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if ( mSpinnerBanKinh.getSelectedItem().toString().equals("---")) {
                Toast.makeText(getContext(),"Cần chọn bán kính tìm kiếm",Toast.LENGTH_SHORT).show();
                return false;
            }
            if (autocompleteFragment.a.getText().toString().isEmpty()) {
                Toast.makeText(getContext(),"Cần chọn vị trí tìm kiếm",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
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
                latLngSearch = new LatLng(lat,lng);
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
    public interface OnDismiss {
        void onSearchNearMe(float radius,String minPrice,String maxPrice,String minSpace,String maxSpace,String time);
        void onSearchArea(double lat,double log,float radius,String minPrice,String maxPrice,String minSpace,String maxSpace,String time);
        void onSearchQuanHuyen(double lat,double log,String minPrice,String maxPrice,String minSpace,String maxSpace,String time,String codeQuanHuyen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autocompleteFragment!=null) {
            getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
        }
    }
}
