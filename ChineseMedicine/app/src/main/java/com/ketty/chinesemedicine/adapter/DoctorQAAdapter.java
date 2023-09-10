package com.ketty.chinesemedicine.adapter;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.Productdoctorqa;

import java.util.ArrayList;
import java.util.List;

public class DoctorQAAdapter extends RecyclerView.Adapter<DoctorQAAdapter.ViewHolder> {
    private List<Productdoctorqa> list = new ArrayList<>();
    private String expand = "[展开]";
    private String collapse = "[收起]";

    public DoctorQAAdapter(List<Productdoctorqa> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DoctorQAAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_doctor_qaactivity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorQAAdapter.ViewHolder holder, int position) {
        if (list.size() > 0) {
            holder.mTvAsk.setText(list.get(position).getAsk());
            String text = list.get(position).getAnswer().replace("\\n", "\n");
            holder.mTvAnswer.setText(text);
            holder.mTvAnswer.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            holder.mTvAnswer.getViewTreeObserver().removeOnPreDrawListener(this);
                            expandText(holder.mTvAnswer,text);
                            return false;
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAsk, mTvAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAsk = itemView.findViewById(R.id.tv_ask);
            mTvAnswer = itemView.findViewById(R.id.tv_answer);
        }
    }

    private void expandText(TextView textView, String str) {
        CharSequence text = str;

        int width = textView.getWidth();
        TextPaint paint = textView.getPaint();
        Layout layout = textView.getLayout();
        int line = layout.getLineCount();
        if (line > 2) {
            int start = layout.getLineStart(1);
            int end = layout.getLineVisibleEnd(1);
            CharSequence lastLine = text.subSequence(start, end);

            float expandWidth = paint.measureText(expand);
            float remain = width - expandWidth;

            CharSequence ellipsize =
                    TextUtils.ellipsize(lastLine,
                            paint,
                            remain,
                            TextUtils.TruncateAt.END);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    collapseText(textView,str);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(Color.parseColor("#00CBAB"));
                    ds.setUnderlineText(false);
                }
            };

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(text.subSequence(0, start));
            ssb.append(ellipsize);
            ssb.append(expand);
            ssb.setSpan(new ForegroundColorSpan(0xFF7369F8),
                    ssb.length() - expand.length(), ssb.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.setSpan(clickableSpan,
                    ssb.length() - expand.length(), ssb.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(ssb);
        }
    }

    private void collapseText(TextView textView, String str) {
        // 默认此时文本肯定超过行数了，直接在最后拼接文本
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                expandText(textView, str);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#00CBAB"));
                ds.setUnderlineText(false);
            }
        };

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        ssb.append(collapse);
        ssb.setSpan(new ForegroundColorSpan(0xFF7369F8),
                ssb.length() - collapse.length(), ssb.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.setSpan(clickableSpan,
                ssb.length() - collapse.length(), ssb.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);
    }
}
