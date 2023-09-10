package com.ketty.chinesemedicine.main.home;

import static com.ketty.chinesemedicine.main.home.TextSign.ZYend;
import static com.ketty.chinesemedicine.main.home.TextSign.ZYstart;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.DialogBubbleBinding;
import com.ketty.chinesemedicine.entity.Chineseherb;
import com.ketty.chinesemedicine.entity.ChineseherbAndImages;
import com.ketty.chinesemedicine.entity.Prescription;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BubbleDialog extends DialogFragment {
    private DialogBubbleBinding bind;
    private int[] mLocation;
    private int textWidth, xAxisLeft, yAxisTop;
    private StateListener mStateListener;
    private int type;

    public BubbleDialog(int[] mLocation, int textWidth, int xAxisLeft, int yAxisTop) {
        this.mLocation = mLocation;
        this.textWidth = textWidth;
        this.xAxisLeft = xAxisLeft;
        this.yAxisTop = yAxisTop;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DialogBubbleBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        bind.tvContent.setVisibility(View.INVISIBLE);

        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        type = bundle.getInt("type");

        bind.tvTitle.setText(title);

        if (type == CHINESEHERB) {
            bind.tvSearch.setText("查中药");
            bind.llRecord.setVisibility(View.GONE);
            bind.view.setVisibility(View.GONE);
            initRequest(title, "/chineseherb/getByName",0);
        } else if (type == PRESCRIPTION) {
            bind.tvSearch.setText("查方剂");
            initRequest(title, "/prescription/getByName",0);
            initRequest(title, "/typhoidtheoryprescriptionworkcited/countByName",1);
        }

    }

    private void initRequest(String title, String url, int order) {
        if (type == PRESCRIPTION) {
            if (order == 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", title);
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod(url, map)
                        .compose(RxHelper.observableIO2Main(getContext()))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                String data = JsonHelper.parserObject2Json(response.get("result"));
                                bind.tvContent.setVisibility(View.VISIBLE);
                                Prescription prescription = JsonHelper.parserJson2Object(data,Prescription.class);
                                bind.tvContent.setText(prescription.getCompose().replace(ZYstart,"").replace(ZYend,""));
                                bind.llContent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mStateListener.onFirstItemClick(title,type);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {
                                bind.tvContent.setVisibility(View.INVISIBLE);
                            }
                        });
            } else {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", title);
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod(url, map)
                        .compose(RxHelper.observableIO2Main(getContext()))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                String data = JsonHelper.parserObject2Json(response.get("result"));
                                Long l = JsonHelper.parserJson2Object(data,Long.class);
                                bind.tvRecord.setText("含“" + title + "”条文共" + l + "条");
                                bind.llRecord.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mStateListener.onSecondItemClick(title);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {
                                bind.tvRecord.setText("含“" + title + "”条文共0条");
                            }
                        });
            }
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", title);
            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod(url, map)
                    .compose(RxHelper.observableIO2Main(getContext()))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            String data = JsonHelper.parserObject2Json(response.get("result"));
                            bind.tvContent.setVisibility(View.VISIBLE);
                            Chineseherb chineseherb = JsonHelper.parserJson2Object(data, ChineseherbAndImages.class).getChineseherb();

                            String text = chineseherb.getMedicinalName()+" ["+chineseherb.getPinYin()+"]";
                            List<TextColorSizeHelper.SpanInfo> list = new ArrayList<TextColorSizeHelper.SpanInfo>();
                            list.add(
                                    new TextColorSizeHelper.SpanInfo(
                                            text.substring(0,text.indexOf("[")),
                                            DisplayUtil.dip2px(getContext(),16),
                                            Color.parseColor("#5ebdbf"),
                                            false
                                    )
                            );
                            list.add(
                                    new TextColorSizeHelper.SpanInfo(
                                            text.substring(text.indexOf("[")),
                                            DisplayUtil.dip2px(getContext(),12),
                                            Color.parseColor("#B6B6B6"),
                                            false
                                    )
                            );

                            bind.tvTitle.setText(TextColorSizeHelper.getTextSpan(getContext(), text, list));
                            bind.tvContent.setText(chineseherb.getSexualTaste()+"\n"+chineseherb.getEfficacy());
                            bind.tvContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mStateListener.onFirstItemClick(title,type);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {
                            bind.tvContent.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        bind.shadowLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                bind.shadowLayout.removeOnLayoutChangeListener(this);
                int height = bind.shadowLayout.getHeight();
                WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.TOP | Gravity.START;
                params.dimAmount = 0.0F;
                params.y = mLocation[1] - height + yAxisTop;
                params.windowAnimations = R.style.DialogScaleInOut;
                getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
                bind.ivTriangle.setTranslationX(mLocation[0]+(textWidth/2)+xAxisLeft-DisplayUtil.dip2px(getDialog().getContext(),32));
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        super.onStart();
    }

    public interface StateListener {
        void onFirstItemClick(String name, int type);
        void onSecondItemClick(String name);
    }

    public void setStateListener(StateListener listener) {
        this.mStateListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}
