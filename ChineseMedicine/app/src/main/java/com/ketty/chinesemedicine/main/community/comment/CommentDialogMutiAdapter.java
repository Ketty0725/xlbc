package com.ketty.chinesemedicine.main.community.comment;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.main.community.comment.bean.CommentEntity;
import com.ketty.chinesemedicine.main.community.comment.bean.CommentMoreBean;
import com.ketty.chinesemedicine.main.community.comment.bean.Comment;
import com.ketty.chinesemedicine.main.community.comment.bean.Reply;
import com.ketty.chinesemedicine.main.community.comment.widget.TextClickSpans;
import com.ketty.chinesemedicine.main.community.comment.widget.TextMovementMethods;
import com.ketty.chinesemedicine.util.TimeUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CommentDialogMutiAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public CommentDialogMutiAdapter(List list) {
        super(list);
        addItemType(CommentEntity.TYPE_COMMENT_PARENT, R.layout.item_comment_new);
        addItemType(CommentEntity.TYPE_COMMENT_CHILD, R.layout.item_comment_child_new);
        addItemType(CommentEntity.TYPE_COMMENT_MORE, R.layout.item_comment_new_more);
        addItemType(CommentEntity.TYPE_COMMENT_EMPTY, R.layout.item_comment_empty);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case CommentEntity.TYPE_COMMENT_PARENT:
                bindCommentParent(helper, (Comment) item);
                break;
            case CommentEntity.TYPE_COMMENT_CHILD:
                bindCommentChild(helper, (Reply) item);
                break;
            case CommentEntity.TYPE_COMMENT_MORE:
                bindCommonMore(helper, (CommentMoreBean) item);
                break;
            case CommentEntity.TYPE_COMMENT_EMPTY:
                bindEmpty(helper, item);
                break;
        }
    }

    private void bindCommentParent(BaseViewHolder helper, Comment item) {
        LinearLayout mLlLike = helper.getView(R.id.ll_like);
        RelativeLayout mRlGroup = helper.getView(R.id.rl_group);
        RoundedImageView mIvHeader = helper.getView(R.id.iv_header);
        ImageView mIvLike = helper.getView(R.id.iv_like);
        TextView mTvLikeCount = helper.getView(R.id.tv_like_count);
        TextView mTvContent = helper.getView(R.id.tv_content);
        TextView mTvIpAddress = helper.getView(R.id.tv_ip_address);

        mLlLike.setOnClickListener(null);
        mLlLike.setTag(item.getItemType());
        helper.addOnClickListener(R.id.ll_like);

        mRlGroup.setTag(item.getItemType());
        helper.addOnClickListener(R.id.rl_group);
        helper.addOnLongClickListener(R.id.rl_group);
        mIvLike.setImageResource(item.getIsLike() == 0 ? R.drawable.ic_love_heart_normal : R.drawable.ic_love_heart_selected);
        mTvLikeCount.setText(item.getLikeCount() + "");
        mTvLikeCount.setVisibility(item.getLikeCount() <= 0 ? View.GONE : View.VISIBLE);

        String time = TimeUtils.getRecentTimeSpanByNow(item.getCreateTime());
        helper.setText(R.id.tv_time, time);
        helper.setText(R.id.tv_user_name, TextUtils.isEmpty(item.getUserName()) ? " " : item.getUserName());

        String contents = TextUtils.isEmpty(item.getContent()) ? " " : item.getContent();
        mTvContent.setText(contents);

        Glide.with(mContext).load(item.getHeadImg()).into(mIvHeader);

        String ip = item.getIpAddress();
        if (ip.contains("-")) {
            String[] ipAddress = ip.split("-");
            if ("中国".equals(ipAddress[0]) && !"".equals(ipAddress[1])) {
                ip = ipAddress[1];
            } else {
                ip = ipAddress[0];
            }
        }
        mTvIpAddress.setText(ip);
    }

    private void bindCommentChild(final BaseViewHolder helper, Reply item) {
        LinearLayout mLlLike = helper.getView(R.id.ll_like);
        RelativeLayout mRlGroup = helper.getView(R.id.rl_group);
        RoundedImageView mIvHeader = helper.getView(R.id.iv_header);
        ImageView mIvLike = helper.getView(R.id.iv_like);
        TextView mTvLikeCount = helper.getView(R.id.tv_like_count);
        TextView mTvContent = helper.getView(R.id.tv_content);
        TextView mTvIpAddress = helper.getView(R.id.tv_ip_address);

        mLlLike.setOnClickListener(null);
        mLlLike.setTag(item.getItemType());
        helper.addOnClickListener(R.id.ll_like);

        mRlGroup.setTag(item.getItemType());
        helper.addOnClickListener(R.id.rl_group);
        helper.addOnLongClickListener(R.id.rl_group);
        Glide.with(mContext).load(item.getHeadImg()).into(mIvHeader);
        mIvLike.setImageResource(item.getIsLike() == 0 ? R.drawable.ic_love_heart_normal : R.drawable.ic_love_heart_selected);
        mTvLikeCount.setText(item.getLikeCount() + "");
        mTvLikeCount.setVisibility(item.getLikeCount() <= 0 ? View.GONE : View.VISIBLE);

        final TextMovementMethods movementMethods = new TextMovementMethods();
        if (item.getReplyUserId() == 0) {
            mTvContent.setText(item.getContent());
            mTvContent.setMovementMethod(null);
        } else {
            SpannableString stringBuilder = makeReplyCommentSpan(item.getReplyUserName(), item.getReplyUserId(), item.getContent());
            mTvContent.setHighlightColor(Color.parseColor("#00000000"));
            mTvContent.setText(stringBuilder);
            mTvContent.setMovementMethod(movementMethods);
        }
        mTvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movementMethods.isSpanClick()) return;
                helper.itemView.performClick();
            }
        });

        String time = TimeUtils.getRecentTimeSpanByNow(item.getCreateTime());
        helper.setText(R.id.tv_time, time);
        helper.setText(R.id.tv_user_name, TextUtils.isEmpty(item.getUserName()) ? " " : item.getUserName());

        String ip = item.getIpAddress();
        if (ip.contains("-")) {
            String[] ipAddress = ip.split("-");
            if ("中国".equals(ipAddress[0]) && !"".equals(ipAddress[1])) {
                ip = ipAddress[1];
            } else {
                ip = ipAddress[0];
            }
        }
        mTvIpAddress.setText(ip);
    }

    private void bindCommonMore(BaseViewHolder helper, CommentMoreBean item) {
        LinearLayout mLlGroup = helper.getView(R.id.ll_group);
        mLlGroup.setTag(item.getItemType());
        helper.addOnClickListener(R.id.ll_group);
    }

    private void bindEmpty(BaseViewHolder helper, MultiItemEntity item) {

    }

    public SpannableString makeReplyCommentSpan(final String atSomeone, final long id, String commentContent) {
        String richText = String.format("回复 %s : %s", atSomeone, commentContent);
        SpannableString builder = new SpannableString(richText);
        if (!TextUtils.isEmpty(atSomeone)) {
            int childStart = 2;
            int childEnd = childStart + atSomeone.length() + 1;
            builder.setSpan(new TextClickSpans() {

                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext, atSomeone + " id: " + id, Toast.LENGTH_LONG).show();
                }
            }, childStart, childEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}
