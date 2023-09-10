package com.ketty.chinesemedicine.adapter;

import static com.ketty.chinesemedicine.main.home.TextSign.FJend;
import static com.ketty.chinesemedicine.main.home.TextSign.FJstart;

import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketty.chinesemedicine.entity.Typhoidtheorychineseherbcontent;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.List;

public class TyphoidTheoryChineseHerbAdapter extends RecyclerView.Adapter<TyphoidTheoryChineseHerbAdapter.ViewHolder> {
    private List<Typhoidtheorychineseherbcontent> mList;
    private Integer type;

    public TyphoidTheoryChineseHerbAdapter(List<Typhoidtheorychineseherbcontent> mList, Integer type) {
        this.mList = mList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(14);
        textView.setTextColor(0xFF333333);
        textView.setLineSpacing(0,1.2f);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(textView,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mList != null && type != null) {
            if (type != -1) {
                final String text = mList.get(position).getContent();
                if (text.contains(FJstart) && text.contains(FJend)) {
                    String subText = text.substring(text.indexOf(FJstart)+FJstart.length(),text.indexOf(FJend));
                    String showText = text.replace(FJstart,"").replace(FJend,"");
                    List<TextColorSizeHelper.SpanInfo> list = new ArrayList<TextColorSizeHelper.SpanInfo>();
                    list.add(
                            new TextColorSizeHelper.SpanInfo(
                                    subText,
                                    -1,
                                    Color.parseColor("#5ebdbf"),
                                    new ClickableSpan() {
                                        @Override
                                        public void onClick(@NonNull View widget) {
                                            Layout layout = holder.textView.getLayout();
                                            Rect bound = new Rect();
                                            int line = layout.getLineForOffset(showText.indexOf(subText));
                                            layout.getLineBounds(line, bound);
                                            int xAxisLeft = (int) layout.getPrimaryHorizontal(showText.indexOf(subText));
                                            int yAxisTop = bound.top;
                                            mOnItemClickListener.onItemClick(widget,subText,xAxisLeft,yAxisTop);
                                        }
                                    }, true,
                                    true
                            )
                    );
                    holder.textView.setHighlightColor(Color.parseColor("#00000000"));
                    holder.textView.setPadding(0,
                            DisplayUtil.dip2px(holder.itemView.getContext(),10),0,0);
                    holder.textView.setText(TextColorSizeHelper.getTextSpan(holder.itemView.getContext(), showText, list));
                    holder.textView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    holder.textView.setPadding(0,
                            DisplayUtil.dip2px(holder.itemView.getContext(),10),0,0);
                    holder.textView.setText(text);
                }
            } else {
                holder.textView.setPadding(0,DisplayUtil.dip2px(holder.itemView.getContext(),15),0,0);
                holder.textView.setText(mList.get(position).getContent().replace("\\n", "\n"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull TextView view, OnItemClickListener listener) {
            super(view);
            this.mOnItemClickListener = listener;
            this.textView = view;

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, String title, int xAxisLeft, int yAxisTop);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
