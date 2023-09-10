package com.ketty.chinesemedicine.main.community.comment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.ActionSheetDialog;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.DialogCommentViewBinding;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.main.community.comment.bean.Comment;
import com.ketty.chinesemedicine.main.community.comment.bean.CommentEntity;
import com.ketty.chinesemedicine.main.community.comment.bean.CommentMoreBean;
import com.ketty.chinesemedicine.main.community.comment.bean.CommentPagerEnable;
import com.ketty.chinesemedicine.main.community.comment.bean.Reply;
import com.ketty.chinesemedicine.main.community.comment.listener.SoftKeyBoardListener;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.RecyclerViewUtil;
import com.ketty.chinesemedicine.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommentBottomDialog extends BottomSheetDialogFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    private DialogCommentViewBinding bind;
    private MMKVUtil mmkvUtil;
    private long noteId;
    private String uId;
    private String uName;
    private String uHeadicon;
    private String ip;
    private StateListener mStateListener;
    private List<MultiItemEntity> data = new ArrayList<>();
    private List<Comment> datas = new ArrayList<>();
    private CommentInputDialog commentInputDialog;
    private float slideOffset = 0;
    private CommentDialogMutiAdapter mAdapter;
    private int offsetY;
    private int positionCount = 0;
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;
    private CommentPagerEnable pagerEnable;
    private Comment comment;
    private long totalSize = 0;

    public CommentBottomDialog() {
    }

    public static CommentBottomDialog getInstance() {
        return new CommentBottomDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(this.getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.0F);

        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            //获取根部局的LayoutParams对象
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getPeekHeight();
            //修改弹窗的最大高度，不允许上滑（默认可以上滑）
            bottomSheet.setLayoutParams(layoutParams);

            final BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(getPeekHeight());
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            //dialog滑动监听
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                        if (slideOffset <= -0.28) {
                            //当向下滑动时 值为负数
                            mStateListener.close();
                        }
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    CommentBottomDialog.this.slideOffset = slideOffset;//记录滑动值
                }
            });
        }

        initView();
        initData();
    }

    protected int getPeekHeight() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogCommentViewBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    private void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);
        Bundle bundle = getArguments();
        noteId = bundle.getLong("noteId");
    }

    private void initRefresh() {
        datas.clear();
        initData();
        dataSort(0);
        mAdapter.setNewData(data);
    }

    private void dataSort(int position) {
        if (datas.isEmpty()) {
            data.clear();
            data.add(new MultiItemEntity() {
                @Override
                public int getItemType() {
                    return CommentEntity.TYPE_COMMENT_EMPTY;
                }
            });
            return;
        }

        if (position <= 0) data.clear();
        int posCount = data.size();
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            if (i < position) continue;

            //一级评论
            Comment comment = datas.get(i);
            if (comment == null) continue;
            comment.setPosition(i);
            posCount += 2;
            List<Reply> replies = comment.getReplies();
            if (replies == null || replies.isEmpty()) {
                comment.setPositionCount(posCount);
                data.add(comment);
                continue;
            }
            int beanSize = replies.size();
            posCount += beanSize;
            comment.setPositionCount(posCount);
            data.add(comment);

            //二级评论
            for (int j = 0; j < beanSize; j++) {
                Reply reply = replies.get(j);
                reply.setChildPosition(j);
                reply.setPosition(i);
                reply.setPositionCount(posCount);
                data.add(reply);
            }

            //展示更多的item
            CommentMoreBean moreBean = new CommentMoreBean();
            moreBean.setPosition(i);
            moreBean.setPositionCount(posCount);
            moreBean.setTotalCount(comment.getTotalDataSize());
            if (comment.getTotalDataSize() > 2) {
                data.add(moreBean);
            }

        }
    }

    private void initData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("noteId", noteId);
        map.put("currentPage", 1);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/comment/get", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        pagerEnable = JsonHelper.parserJson2Object(
                                data, CommentPagerEnable.class);
                        datas = pagerEnable.getComments();
                        initRecyclerView();

                        totalSize = pagerEnable.getTotalAllSize();
                        bind.tvCommentNumber.setText("共"+totalSize+"条评论");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        bind.progressBar.setVisibility(View.GONE);
                    }
                });

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("uId", App.getMmkvUtil().getString("user","uId",null));

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/getUserInfo", map2)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        User user = JsonHelper.parserJson2Object(
                                data, User.class);
                        uName = user.getuName();
                        uHeadicon = user.getuHeadicon();
                        ip = user.getIpAddress();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView() {
        dataSort(0);

        mRecyclerViewUtil = new RecyclerViewUtil();
        bind.ivClose.setOnClickListener(v -> mStateListener.close());
        bind.slEditor.setOnClickListener(v -> {
            //添加二级评论
            initCommentInputDialog(null, null, -1, "");
        });

        //adapter
        mAdapter = new CommentDialogMutiAdapter(data);
        bind.recyclerView.setHasFixedSize(true);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        closeDefaultAnimator(bind.recyclerView);
        mAdapter.setOnLoadMoreListener(this, bind.recyclerView);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        initListener();

        mAdapter.notifyDataSetChanged();
        slideOffset = 0;
    }

    private void initListener() {
        // 点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view1, int position) {
                switch ((int) view1.getTag()) {
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论
                            Comment comment = (Comment) mAdapter.getData().get(position);
                            initCommentInputDialog((View) view1.getParent(), mAdapter.getData().get(position), position, comment.getUserName());
                        } else if (view1.getId() == R.id.ll_like) {
                            //一级评论点赞
                            Comment comment = (Comment) mAdapter.getData().get(position);

                            Long likeCount = comment.getLikeCount() + (comment.getIsLike() == 0 ? 1 : -1);
                            int isLike = comment.getIsLike() == 0 ? 1 : 0;

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("userId", uId);
                            map.put("beLikedId", comment.getId());

                            RetrofitManager
                                    .getInstance()
                                    .getApiService()
                                    .postMethod("/comment/updateLikedState", map)
                                    .compose(RxHelper.observableIO2Main(getContext()))
                                    .subscribe(new BaseObserver() {
                                        @Override
                                        public void onSuccess(Map<String, Object> response) {
                                            comment.setLikeCount(likeCount);
                                            comment.setIsLike(isLike);

                                            datas.set(comment.getPosition(), comment);
                                            dataSort(0);
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String errorMsg) {

                                        }
                                    });

                        }
                        break;
                    case CommentEntity.TYPE_COMMENT_CHILD:
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论（回复）
                            Reply reply = (Reply) mAdapter.getData().get(position);
                            initCommentInputDialog(view1, mAdapter.getData().get(position), position, reply.getUserName());
                        } else if (view1.getId() == R.id.ll_like) {
                            //二级评论点赞
                            Reply reply = (Reply) mAdapter.getData().get(position);
                            Long likeCount = reply.getLikeCount() + (reply.getIsLike() == 0 ? 1 : -1);
                            int isLike = reply.getIsLike() == 0 ? 1 : 0;

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("userId", uId);
                            map.put("beLikedId", reply.getId());

                            RetrofitManager
                                    .getInstance()
                                    .getApiService()
                                    .postMethod("/reply/updateLikedState", map)
                                    .compose(RxHelper.observableIO2Main(getContext()))
                                    .subscribe(new BaseObserver() {
                                        @Override
                                        public void onSuccess(Map<String, Object> response) {
                                            reply.setLikeCount(likeCount);
                                            reply.setIsLike(isLike);

                                            List<Reply> replies = datas.get((int) reply.getPosition()).getReplies();
                                            replies.set(reply.getChildPosition(), reply);
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String errorMsg) {

                                        }
                                    });

                        }

                        break;
                    case CommentEntity.TYPE_COMMENT_MORE:
                        CommentMoreBean moreBean = (CommentMoreBean) mAdapter.getData().get(position);

                        TextView textView = view1.findViewById(R.id.tv_more);
                        ImageView imageView = view1.findViewById(R.id.iv_more);
                        long id = datas.get((int) moreBean.getPosition()).getId();
                        long currentPage = datas.get((int) moreBean.getPosition()).getNextPage();

                        if (currentPage == 0) {
                            currentPage++;
                            datas.get((int) moreBean.getPosition()).getReplies().clear();
                        }

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("fatherId", id);
                        map.put("currentPage", currentPage);

                        RetrofitManager
                                .getInstance()
                                .getApiService()
                                .postMethod("/reply/get", map)
                                .compose(RxHelper.observableIO2Main(getContext()))
                                .subscribe(new BaseObserver() {
                                    @Override
                                    public void onSuccess(Map<String, Object> response) {
                                        String data = JsonHelper.parserObject2Json(response.get("result"));
                                        comment = JsonHelper.parserJson2Object(
                                                data, Comment.class);
                                        datas.get((int) moreBean.getPosition()).setNextPage(
                                                comment.getNextPage());
                                        datas.get((int) moreBean.getPosition()).getReplies()
                                                .removeAll(comment.getReplies());
                                        datas.get((int) moreBean.getPosition()).getReplies()
                                                .addAll(comment.getReplies());

                                        dataSort(0);
                                        mAdapter.notifyDataSetChanged();

                                        if (comment.getNextPage() == 0) {
                                            textView.setText("收起更多回复");
                                            imageView.setScaleY(-1);
                                        } else {
                                            textView.setText("展开更多回复");
                                            imageView.setScaleY(1);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable e, String errorMsg) {

                                    }
                                });

                        break;
                    case CommentEntity.TYPE_COMMENT_EMPTY:
                        initRefresh();
                        break;

                }

            }
        });
        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                switch ((int) view.getTag()) {
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        Comment comment = (Comment) mAdapter.getData().get(position);
                        if (uId.equals(String.valueOf(comment.getUserId()))) {
                            new ActionSheetDialog(getContext())
                                    .builder()
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(true)
                                    .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                            new ActionSheetDialog.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(int which) {
                                                    new CommonAlertDialog(getContext()).builder()
                                                            .setTitle("确认删除这条评论？")
                                                            .setNegativeButton("确认", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    HashMap<String, Object> map = new HashMap<>();
                                                                    map.put("id", comment.getId());
                                                                    map.put("userId", uId);

                                                                    RetrofitManager
                                                                            .getInstance()
                                                                            .getApiService()
                                                                            .postMethod("/comment/deleteById", map)
                                                                            .compose(RxHelper.observableIO2Main(getContext()))
                                                                            .subscribe(new BaseObserver() {
                                                                                @Override
                                                                                public void onSuccess(Map<String, Object> response) {
                                                                                    datas.remove(comment.getPosition());
                                                                                    dataSort(0);
                                                                                    mAdapter.notifyDataSetChanged();
                                                                                    T.showShort("删除成功");
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Throwable e, String errorMsg) {

                                                                                }
                                                                            });
                                                                }
                                                            }).setPositiveButton("再想想", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                }
                                                            }).setCancelable(false)
                                                            .show();
                                                }
                                            }).show();
                        } else {
                            new ActionSheetDialog(getContext())
                                    .builder()
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(true)
                                    .addSheetItem("回复", null,
                                            new ActionSheetDialog.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(int which) {
                                                    initCommentInputDialog((View) view.getParent(), mAdapter.getData().get(position), position, comment.getUserName());
                                                }
                                            }).show();
                        }
                        break;
                    case CommentEntity.TYPE_COMMENT_CHILD:
                        Reply reply = (Reply) mAdapter.getData().get(position);
                        if (uId.equals(String.valueOf(reply.getUserId()))) {
                            new ActionSheetDialog(getContext())
                                    .builder()
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(true)
                                    .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                            new ActionSheetDialog.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(int which) {
                                                    new CommonAlertDialog(getContext()).builder()
                                                            .setTitle("确认删除这条评论？")
                                                            .setNegativeButton("确认", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    HashMap<String, Object> map = new HashMap<>();
                                                                    map.put("id", reply.getId());
                                                                    map.put("userId", uId);

                                                                    RetrofitManager
                                                                            .getInstance()
                                                                            .getApiService()
                                                                            .postMethod("/reply/deleteById", map)
                                                                            .compose(RxHelper.observableIO2Main(getContext()))
                                                                            .subscribe(new BaseObserver() {
                                                                                @Override
                                                                                public void onSuccess(Map<String, Object> response) {
                                                                                    datas.get((int) reply.getPosition()).getReplies()
                                                                                            .remove(reply.getChildPosition());
                                                                                    dataSort(0);
                                                                                    mAdapter.notifyDataSetChanged();
                                                                                    T.showShort("删除成功");
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Throwable e, String errorMsg) {

                                                                                }
                                                                            });
                                                                }
                                                            }).setPositiveButton("再想想", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                }
                                                            }).setCancelable(false)
                                                            .show();
                                                }
                                            }).show();
                        } else {
                            new ActionSheetDialog(getContext())
                                    .builder()
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(true)
                                    .addSheetItem("回复", null,
                                            new ActionSheetDialog.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(int which) {
                                                    initCommentInputDialog(view, mAdapter.getData().get(position), position, reply.getUserName());
                                                }
                                            }).show();
                        }
                        break;
                }
                return false;
            }
        });
        //滚动事件
        if (mRecyclerViewUtil != null) mRecyclerViewUtil.initScrollListener(bind.recyclerView);

        mKeyBoardListener = new SoftKeyBoardListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
    }

    private void initCommentInputDialog(View view, final MultiItemEntity item, final int position, String replyName) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (commentInputDialog == null) {
            commentInputDialog = new CommentInputDialog();
            if (!"".equals(replyName)) {
                Bundle bundle = new Bundle();
                bundle.putString("editor", "回复 @"+replyName);
                commentInputDialog.setArguments(bundle);
            }
            commentInputDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
            commentInputDialog.setStateListener(new CommentInputDialog.StateListener() {
                @Override
                public void onTextSend(String msg) {
                    addComment(item, position, msg);
                }

                @Override
                public void dismiss() {
                    scrollLocation(-offsetY);
                }

            });
        }
        showInputTextMsgDialog();
    }

    //添加评论
    private void addComment(MultiItemEntity item, final int position, String msg) {
        if (position >= 0) {
            //添加二级评论
            int pos = 0;
            String replyUserName = "";
            long replyUserId = 0;
            long fatherId = 0;
            if (item instanceof Comment) {
                Comment comment = (Comment) item;
                positionCount = (int) (comment.getPositionCount() + 1);
                pos = (int) comment.getPosition();
                fatherId = comment.getId();
            } else if (item instanceof Reply) {
                Reply reply = (Reply) item;
                positionCount = (int) (reply.getPositionCount() + 1);
                pos = (int) reply.getPosition();
                replyUserName = reply.getUserName();
                replyUserId = reply.getUserId();
                fatherId = reply.getFatherId();
            }

            Reply reply = new Reply();
            reply.setUserId(Long.parseLong(uId));
            reply.setUserName(uName);
            reply.setHeadImg(uHeadicon);
            reply.setReplyUserId(replyUserId);
            reply.setReplyUserName(replyUserName);
            reply.setContent(msg);
            reply.setCreateTime(System.currentTimeMillis());
            String newIp = ip;
            if (newIp.contains("-")) {
                String[] ipAddress = newIp.split("-");
                if ("中国".equals(ipAddress[0]) && !"".equals(ipAddress[1])) {
                    newIp = ipAddress[1];
                } else {
                    newIp = ipAddress[0];
                }
            }
            reply.setIpAddress(newIp);
            reply.setLikeCount(0);
            reply.setFatherId(fatherId);

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("userId", String.valueOf(reply.getUserId()))
                    .addFormDataPart("replyUserId", String.valueOf(reply.getReplyUserId()))
                    .addFormDataPart("content", reply.getContent())
                    .addFormDataPart("createTime", String.valueOf(reply.getCreateTime()))
                    .addFormDataPart("ipAddress", ip)
                    .addFormDataPart("likeCount", String.valueOf(reply.getLikeCount()))
                    .addFormDataPart("fatherId", String.valueOf(reply.getFatherId()))
                    .build();

            int finalPos = pos;
            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/reply/add", body)
                    .compose(RxHelper.observableIO2Main(getContext()))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            String result = JsonHelper.parserObject2Json(response.get("result"));
                            Long id = JsonHelper.parserJson2Object(result,Long.class);
                            reply.setId(id);
                            reply.setPosition(positionCount);

                            datas.get(finalPos).getReplies().add(reply);
                            dataSort(0);
                            mAdapter.notifyDataSetChanged();
                            bind.recyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((LinearLayoutManager) bind.recyclerView.getLayoutManager())
                                            .scrollToPositionWithOffset(positionCount >= data.size() - 1 ? data.size() - 1
                                                    : positionCount, positionCount >= data.size() - 1 ? Integer.MIN_VALUE : bind.recyclerView.getHeight());
                                }
                            }, 100);

                            totalSize++;
                            bind.tvCommentNumber.setText("共"+totalSize+"条评论");
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

                        }
                    });

        } else {
            //添加一级评论
            Comment comment = new Comment();
            comment.setUserId(Long.parseLong(uId));
            comment.setUserName(uName);
            comment.setHeadImg(uHeadicon);
            comment.setNoteId(noteId);
            comment.setContent(msg);
            comment.setCreateTime(System.currentTimeMillis());
            String newIp = ip;
            if (newIp.contains("-")) {
                String[] ipAddress = newIp.split("-");
                if ("中国".equals(ipAddress[0]) && !"".equals(ipAddress[1])) {
                    newIp = ipAddress[1];
                } else {
                    newIp = ipAddress[0];
                }
            }
            comment.setIpAddress(newIp);
            comment.setLikeCount(0);
            comment.setReplies(new ArrayList<Reply>());

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("userId", String.valueOf(comment.getUserId()))
                    .addFormDataPart("noteId", String.valueOf(comment.getNoteId()))
                    .addFormDataPart("content", comment.getContent())
                    .addFormDataPart("createTime", String.valueOf(comment.getCreateTime()))
                    .addFormDataPart("ipAddress", ip)
                    .addFormDataPart("likeCount", String.valueOf(comment.getLikeCount()))
                    .build();

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/comment/add", body)
                    .compose(RxHelper.observableIO2Main(getContext()))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            String result = JsonHelper.parserObject2Json(response.get("result"));
                            Long id = JsonHelper.parserJson2Object(result,Long.class);
                            comment.setId(id);

                            pagerEnable.setNextPage(1L);
                            datas.add(0, comment);
                            dataSort(0);
                            mAdapter.notifyDataSetChanged();
                            bind.recyclerView.scrollToPosition(0);

                            totalSize++;
                            bind.tvCommentNumber.setText("共"+totalSize+"条评论");
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

                        }
                    });
        }
    }

    private void dismissInputDialog() {
        if (commentInputDialog != null) {
            if (commentInputDialog.isVisible()) commentInputDialog.dismiss();
            commentInputDialog = null;
        }
    }

    private void showInputTextMsgDialog() {
        commentInputDialog.show(getChildFragmentManager(),"CommentInputDialog");
    }

    // item滑动
    public void scrollLocation(int offsetY) {
        try {
            bind.recyclerView.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator(RecyclerView mRvCustomer) {
        if (null == mRvCustomer) return;
        mRvCustomer.getItemAnimator().setAddDuration(0);
        mRvCustomer.getItemAnimator().setChangeDuration(0);
        mRvCustomer.getItemAnimator().setMoveDuration(0);
        mRvCustomer.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRvCustomer.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewUtil != null){
            mRecyclerViewUtil.destroy();
            mRecyclerViewUtil = null;
        }
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
        mAdapter = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLoadMoreRequested() {
        if (pagerEnable.getNextPage() == 0) {
            mAdapter.loadMoreEnd(false);
            return;
        } else if (pagerEnable.getNextPage() == 1) {
            datas.clear();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("noteId", noteId);
        map.put("currentPage", pagerEnable.getNextPage());

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/comment/get", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        pagerEnable = JsonHelper.parserJson2Object(data, CommentPagerEnable.class);
                        datas.addAll(pagerEnable.getComments());
                        dataSort(0);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.loadMoreComplete();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    public interface StateListener {
        void close();
    }

    public void setStateListener(StateListener listener) {
        this.mStateListener = listener;
    }

}
