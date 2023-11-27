package com.timnhatro1.asus.view.fragment.function_map;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.base.BaseFragment;
import com.timnhatro1.asus.view.dialog.DialogConfrim;
import com.timnhatro1.asus.view.dialog.bottom_sheet_dialog.MotelDetailBottomSheetFragment;
import com.timnhatro1.asus.view.dialog.DialogAddNote;
import com.timnhatro1.asus.view.dialog.DialogReport;
import com.timnhatro1.asus.model.event_bus.EventBusMotelModelSave;
import com.timnhatro1.asus.interactor.model.motel.MotelDAO;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.Tools;
import com.github.florent37.viewtooltip.ViewTooltip;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.github.florent37.viewtooltip.ViewTooltip.ALIGN.CENTER;
import static com.github.florent37.viewtooltip.ViewTooltip.Position.TOP;

public class NItemMotelFragment extends BaseFragment{
    @BindView(R.id.ll_infor)
    View mViewInfor;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.image)
    ImageView mImageAvatar;
    @BindView(R.id.name)
    TextView mTvNameUserPost;
    @BindView(R.id.tv_time_post)
    TextView mTvTimePost;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_space)
    TextView mTvSpace;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.iv_saved)
    ImageView mIvSaved;
    private MotelModel data;
    @BindView(R.id.iv_phone)
    ImageView mIvPhone;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.iv_direction)
    ImageView mIvDirection;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.iv_report)
    ImageView mIvReport;
    @Override
    public int getLayoutId() {
        return R.layout.item_result_viewpaper;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        mTvTitle.setText(data.getTitle());
//        Tools.displayImageRound(getActivity(),mImageAvatar,R.drawable.avatar_profile);
        mTvTimePost.setText(Tools.howLongFrom(data.getTimePost()));
        mTvAddress.setText(data.getAddress());
        mTvSpace.setText(data.getSpace());
        mTvPhone.setText(data.getPhone());
        mTvPrice.setText(data.getPrice());
        mTvNameUserPost.setText(data.getEmailPost());
        if (data.isSave()) {
            mIvSaved.setImageResource(R.drawable.ic_bookmark_saved);
        } else {
            mIvSaved.setImageResource(R.drawable.ic_bookmark);
        }
        if (data.getReason().isEmpty()) {
            mIvError.setVisibility(View.GONE);
        } else {
            mIvError.setVisibility(View.VISIBLE);
        }
        mIvSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.isSave()) {
                    DialogConfrim dialogConfrim = new DialogConfrim(getActivity(), "Xác nhận", "Bạn có chắc chắn muốn xóa lưu trữ về thông tin nhà trọ này?", "Đồng ý", "Hủy bỏ", new DialogConfrim.OnClickDialogTTC() {
                        @Override
                        public void onButonYesClick() {
                            data.setSave(false);
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
                    data.setSave(true);
                    DialogAddNote dialog = new DialogAddNote(NItemMotelFragment.this.getActivity(), new DialogAddNote.OnFilterListener() {
                        @Override
                        public void onClickApply(String note) {
                            data.setNote(note);
                            upgradeMotel();
                        }
                    });
                    dialog.show();
                    saveMotelModel();
                }
                if (data.isSave()) {
                    mIvSaved.setImageResource(R.drawable.ic_bookmark_saved);
                } else {
                    mIvSaved.setImageResource(R.drawable.ic_bookmark);
                }
            }
        });
        mIvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getPhone()));
                startActivity(intent);
            }
        });
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = data.toString() +"\n--Ứng dụng Tìm nhà trọ--";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tin tức từ ứng dụng Tìm nhà trọ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing With"));
            }
        });
        mIvDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "http://maps.google.com/maps?q=loc:" + data.getLat() + "," +  data.getLng();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
        mIvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewTooltip
                        .on(getActivity(), mIvError)

                        .autoHide(true, 2000)
                        .clickToHide(false)

                        .align(CENTER)

                        .position(TOP)

                        .text(data.getReason())

                        .textColor(Color.WHITE)
                        .color(Color.BLUE)

                        .corner(10)

                        .arrowWidth(15)
                        .arrowHeight(15)

                        .distanceWithView(0)

                        //listeners
                        .onDisplay(new ViewTooltip.ListenerDisplay() {
                            @Override
                            public void onDisplay(View view) {

                            }
                        })
                        .onHide(new ViewTooltip.ListenerHide() {
                            @Override
                            public void onHide(View view) {

                            }
                        })
                        .show();
            }
        });
        mIvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogReport dialogReport = new DialogReport(getActivity(), new DialogReport.OnClickDangKy() {
                    @Override
                    public void onClick(String codeReason) {
                        DialogUtils.showProgressDialog(getActivity());
                        NetworkController.reportMotel(DeviceUtils.getAndroidID(), codeReason, data.getId(), new CommonCallBack<SimpleResult>(getActivity()) {
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
    }

    private void saveMotelModel() {
        MotelDAO motelDAO = AppDatabase.getInstance(getContext()).getMotelDAO();
        motelDAO.insert(data);
        EventBus.getDefault().post(new EventBusMotelModelSave(data));
    }
    private void upgradeMotel() {
        MotelDAO motelDAO = AppDatabase.getInstance(getContext()).getMotelDAO();
        motelDAO.update(data);
        EventBus.getDefault().post(new EventBusMotelModelSave(data));
    }

    private void deleteMotelModel() {
        MotelDAO motelDAO = AppDatabase.getInstance(getContext()).getMotelDAO();
        motelDAO.delete(data);
        EventBus.getDefault().post(new EventBusMotelModelSave(data));
    }

    @OnClick({R.id.ll_infor})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_infor:
                MotelDetailBottomSheetFragment fragment = new MotelDetailBottomSheetFragment();
                fragment.setmMotelModel(data);
                fragment.show(getActivity().getSupportFragmentManager(), fragment.getTag());
                break;

        }
    }

    public void setData(MotelModel data) {
        this.data = data;
    }
}
