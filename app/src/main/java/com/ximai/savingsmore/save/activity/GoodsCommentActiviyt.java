package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.cache.MyImageLoader;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.view.MyGridView;
import com.ximai.savingsmore.library.view.RoundImageView;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.Comment;
import com.ximai.savingsmore.save.modle.CommentList;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.view.GlideRoundTransform;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/1.
 */
//商品评论页面
public class GoodsCommentActiviyt extends BaseActivity implements View.OnClickListener {
    private String id;
    private List<Comment> commentList = new ArrayList<Comment>();
    private List<Images> imagesList = new ArrayList<Images>();
//    private ListAdapter listAdapter;
    private LinearLayout listView;
    private String score;
    private LinearLayout linearLayout;
    private TextView score1;
    private LinearLayout all_score;
    private boolean isCommnet;
    private RelativeLayout back;
    private RelativeLayout title＿right;
    private LinearLayout ll_defaultdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_comment_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * init-view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);

        back = (RelativeLayout) findViewById(R.id.back);
        title＿right = (RelativeLayout) findViewById(R.id.title＿right);

        listView = (LinearLayout) findViewById(R.id.listview);
        score1 = (TextView) findViewById(R.id.score);
        linearLayout = (LinearLayout) findViewById(R.id.start_comment);
        all_score = (LinearLayout) findViewById(R.id.all_score);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
    }

    /**
     * init-data
     */
    private void initData() {

        /**
         * 根据不同显示
         */
        if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")){
            title＿right.setVisibility(View.VISIBLE);
        }else{
            title＿right.setVisibility(View.GONE);
        }

        id = getIntent().getStringExtra("id");
        // listAdapter = new ListAdapter();
        // listView.setAdapter(listAdapter);
        score = getIntent().getStringExtra("score");
        if (getIntent().getStringExtra("isComment").equals("true")) {
            isCommnet = true;
        } else {
            isCommnet = false;
        }
        /**
         * 获取评论数据
         */
        getComment(id);
    }

    /**
     * init-event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        title＿right.setOnClickListener(this);
    }

    /**
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.title＿right://我要评价
                Intent intent = new Intent(GoodsCommentActiviyt.this, IssueCommentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComment(id);
    }

    /**
     * 获取评论数据
     * @param id
     */
    private void getComment(String id) {
        WebRequestHelper.json_post(GoodsCommentActiviyt.this, URLText.GOODS_COMMENT, RequestParamsPool.getGoodsComment(id), new MyAsyncHttpResponseHandler(GoodsCommentActiviyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                CommentList commentList1 = GsonUtils.fromJson(result, CommentList.class);
                commentList = commentList1.MainData;

                /**
                 * 总体
                 */
                if (commentList.size() > 0 && null != score) {
                    linearLayout.removeAllViews();
                    all_score.setVisibility(View.VISIBLE);
                    score1.setText(score + "分");
                    LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(50, 50);
                    layout.setMargins(5, 0, 5, 0);
                    if (score.length() > 1) {
                        int score1 = Integer.parseInt(score.substring(0, 1));
                        for (int i = 0; i < score1; i++) {
                            ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundResource(R.mipmap.comment_star);
                            linearLayout.addView(imageView);
                        }
                        ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                        imageView.setLayoutParams(layout);
                        imageView.setBackgroundResource(R.mipmap.start_half);
                        linearLayout.addView(imageView);

                        for (int i = 0; i < 5 - score1 - 1; i++) {
                            ImageView imageView1 = new ImageView(GoodsCommentActiviyt.this);
                            imageView1.setLayoutParams(layout);
                            imageView1.setBackgroundResource(R.mipmap.comment_start_gray);
                            linearLayout.addView(imageView1);
                        }
                    } else {
                        int score1 = Integer.parseInt(score);
                        for (int i = 0; i < score1; i++) {
                            ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundResource(R.mipmap.comment_star);
                            linearLayout.addView(imageView);
                        }

                        for (int i = 0; i < 5 - score1; i++) {
                            ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundResource(R.mipmap.comment_start_gray);
                            linearLayout.addView(imageView);
                        }
                    }
                }

                if (commentList.size() == 0){
                    ll_defaultdata.setVisibility(View.VISIBLE);//如果没有数据 - 显示缺省图
                }else{
                    ll_defaultdata.setVisibility(View.GONE);//如果没有数据 - 显示缺省图
                }
                addView();
                //listAdapter.notifyDataSetChanged();
            }
        });
    }

//    /**
//     * 适配器
//     */
//    private class ListAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return commentList.size();
//        }
//        @Override
//        public Object getItem(int position) {
//            return commentList.get(position);
//        }
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final ListViewHodel listViewHodel;
//            if (convertView == null) {
//                listViewHodel = new ListViewHodel();
//                convertView = LayoutInflater.from(GoodsCommentActiviyt.this).inflate(R.layout.business_comment_item, null);
//                listViewHodel.head_image = (RoundImageView) convertView.findViewById(R.id.head_iamge);
//                listViewHodel.name = (TextView) convertView.findViewById(R.id.name);
//                listViewHodel.comment_score = (LinearLayout) convertView.findViewById(R.id.comment_star);
//                listViewHodel.date = (TextView) convertView.findViewById(R.id.date);
//                listViewHodel.content = (TextView) convertView.findViewById(R.id.content);
//                listViewHodel.gridView = (MyGridView) convertView.findViewById(R.id.gridview);
//                listViewHodel.huifu = (TextView) convertView.findViewById(R.id.huifu);
//                listViewHodel.neirong = (EditText) convertView.findViewById(R.id.message_content);
//                listViewHodel.submit = (TextView) convertView.findViewById(R.id.submit);
//                listViewHodel.message_item = (LinearLayout) convertView.findViewById(R.id.message_item);
//                convertView.setTag(listViewHodel);
//            } else {
//                listViewHodel = (ListViewHodel) convertView.getTag();
//            }
//            /**
//             * 是否编辑
//             */
//            if (commentList.get(position).IsEdit) {
//                listViewHodel.message_item.setVisibility(View.VISIBLE);
//            } else {
//                listViewHodel.message_item.setVisibility(View.GONE);
//            }
//
//            if (null != commentList.get(position).ReplyContent && !TextUtils.isEmpty(commentList.get(position).ReplyContent)) {
//                listViewHodel.huifu.setVisibility(View.GONE);
//                listViewHodel.submit.setVisibility(View.GONE);
//            }
//
//            listViewHodel.huifu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listViewHodel.message_item.getVisibility() == View.GONE) {
//                        commentList.get(position).IsEdit = true;
//                        listAdapter.notifyDataSetChanged();
//                    } else {
//                        commentList.get(position).IsEdit = false;
//                        listAdapter.notifyDataSetChanged();
//                    }
//                }
//            });
////            MyImageLoader.displayDefaultImage(URLText.img_url + commentList.get(position).User.PhotoPath, listViewHodel.head_image);
//            Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + commentList.get(position).User.PhotoPath).into(listViewHodel.head_image);
//            listViewHodel.name.setText(commentList.get(position).User.ShowName);
//            listViewHodel.date.setText(commentList.get(position).CreateDateName);
//            listViewHodel.content.setText(commentList.get(position).Content);
//            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(50, 50);
//            layout.setMargins(5, 0, 5, 0);
//            listViewHodel.comment_score.removeAllViews();
//            if (commentList.get(position).ProductScore.length() > 1) {
//                int score1 = Integer.parseInt(commentList.get(position).ProductScore.substring(0, 1));
//                for (int i = 0; i < score1; i++) {
//                    ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
//                    imageView.setLayoutParams(layout);
//                    imageView.setBackgroundResource(R.mipmap.comment_star);
//                    listViewHodel.comment_score.addView(imageView);
//                }
//                ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
//                imageView.setLayoutParams(layout);
//                imageView.setBackgroundResource(R.mipmap.start_half);
//                listViewHodel.comment_score.addView(imageView);
//
//                for (int i = 0; i < 5 - score1 - 1; i++) {
//                    ImageView imageView1 = new ImageView(GoodsCommentActiviyt.this);
//                    imageView1.setLayoutParams(layout);
//                    imageView1.setBackgroundResource(R.mipmap.comment_start_gray);
//                    listViewHodel.comment_score.addView(imageView1);
//                }
//            } else {
//                int score1 = Integer.parseInt(commentList.get(position).ProductScore);
//                for (int i = 0; i < score1; i++) {
//                    ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
//                    imageView.setLayoutParams(layout);
//                    imageView.setBackgroundResource(R.mipmap.comment_star);
//                    listViewHodel.comment_score.addView(imageView);
//                }
//                for (int i = 0; i < 5 - score1; i++) {
//                    ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
//                    imageView.setLayoutParams(layout);
//                    imageView.setBackgroundResource(R.mipmap.comment_start_gray);
//                    listViewHodel.comment_score.addView(imageView);
//                }
//            }
////            imagesList = commentList.get(position).Images;------------------------
////            listViewHodel.gridView.setAdapter(new GridViewAdapter());
//            return convertView;
//        }
//    }

//    /**
//     * 图片展示
//     */
//    private class GridViewAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return GridviewList.size();
//        }
//        @Override
//        public Object getItem(int position) {
//            return GridviewList.get(position);
//        }
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            GridViewViewHodel gridViewViewHodel;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(GoodsCommentActiviyt.this).inflate(R.layout.commen_gridview_item, null);
//                gridViewViewHodel = new GridViewViewHodel();
//                gridViewViewHodel.imageView = (ImageView) convertView.findViewById(R.id.iamge);
//                convertView.setTag(gridViewViewHodel);
//            }
//            gridViewViewHodel = (GridViewViewHodel) convertView.getTag();
////            MyImageLoader.displayDefaultImage(URLText.img_url + imagesList.get(position).ImagePath, gridViewViewHodel.imageView);
//            Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + GridviewList.get(position).ImagePath)
//                    .transform(new CenterCrop(GoodsCommentActiviyt.this), new GlideRoundTransform(GoodsCommentActiviyt.this,5))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .crossFade().into(gridViewViewHodel.imageView);
//            return convertView;
//        }
//    }

//    class ListViewHodel {
//        RoundImageView head_image;
//        TextView name;
//        LinearLayout comment_score;
//        TextView date;
//        TextView content;
//        MyGridView gridView;
//        TextView huifu;
//        EditText neirong;
//        TextView submit;
//        LinearLayout message_item;
//    }
//
//    class GridViewViewHodel {
//        ImageView imageView;
//    }

    private void replyComment(String Id, String Content) {
        WebRequestHelper.json_post(GoodsCommentActiviyt.this, URLText.replyComment, RequestParamsPool.replyComment(Id, Content), new MyAsyncHttpResponseHandler(GoodsCommentActiviyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    Toast.makeText(GoodsCommentActiviyt.this, object.optString("Message"), Toast.LENGTH_SHORT).show();
                    if (object.optString("IsSuccess").equals("true")) {
                        getComment(id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addView() {
        listView.removeAllViews();

        RequestOptions options = new RequestOptions()
                .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(GoodsCommentActiviyt.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

        for (int position = 0; position < commentList.size(); position++) {
            View convertView = LayoutInflater.from(GoodsCommentActiviyt.this).inflate(R.layout.business_comment_item, null);
            final RoundImageView head_image;
            final TextView name;
            final LinearLayout comment_score;
            final TextView date;
            final TextView content;
//            MyGridView gridView;
            TextView huifu;
            final EditText neirong;
            TextView submit;
            final LinearLayout message_item;

            LinearLayout ll_iv;
            ImageView iv1;
            ImageView iv2;
            ImageView iv3;


            head_image = (RoundImageView) convertView.findViewById(R.id.head_iamge);
            name = (TextView) convertView.findViewById(R.id.name);
            comment_score = (LinearLayout) convertView.findViewById(R.id.comment_star);
            date = (TextView) convertView.findViewById(R.id.date);
            content = (TextView) convertView.findViewById(R.id.content);
//            gridView = (MyGridView) convertView.findViewById(R.id.gridview);
            huifu = (TextView) convertView.findViewById(R.id.huifu);
            neirong = (EditText) convertView.findViewById(R.id.message_content);
            submit = (TextView) convertView.findViewById(R.id.submit);
            message_item = (LinearLayout) convertView.findViewById(R.id.message_item);

            ll_iv = (LinearLayout) convertView.findViewById(R.id.ll_iv);
            iv1 = (ImageView) convertView.findViewById(R.id.iv1);
            iv2 = (ImageView) convertView.findViewById(R.id.iv2);
            iv3 = (ImageView) convertView.findViewById(R.id.iv3);

            imagesList = commentList.get(position).Images;
            if (imagesList.size() == 0){
                ll_iv.setVisibility(View.GONE);
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
            } else if (imagesList.size() == 1){
                ll_iv.setVisibility(View.VISIBLE);
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.INVISIBLE);
                iv3.setVisibility(View.INVISIBLE);

                Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + imagesList.get(0).ImagePath)
                        .apply(options).into(iv1);
            }else if (imagesList.size() == 2){
                ll_iv.setVisibility(View.VISIBLE);

                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + imagesList.get(0).ImagePath)
                        .apply(options).into(iv1);
                Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + imagesList.get(1).ImagePath)
                        .apply(options).into(iv2);
            }else if (imagesList.size() == 3){
                ll_iv.setVisibility(View.VISIBLE);
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);

                Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + imagesList.get(0).ImagePath)
                        .apply(options).into(iv1);
                Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + imagesList.get(1).ImagePath)
                        .apply(options).into(iv2);
                Glide.with(GoodsCommentActiviyt.this).load(URLText.img_url + imagesList.get(2).ImagePath)
                        .apply(options).into(iv3);
            }


            if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2") || !isCommnet) {
                huifu.setVisibility(View.GONE);
            } else {
                huifu.setVisibility(View.VISIBLE);
            }
//            if (commentList.get(position).IsEdit) {
//                message_item.setVisibility(View.VISIBLE);
//            } else {
//                message_item.setVisibility(View.GONE);
//            }
            String s = commentList.get(position).ReplyContent;
            LogUtils.instance.d("回复的内容=" + s);
            if (null != commentList.get(position).ReplyContent && !TextUtils.isEmpty(commentList.get(position).ReplyContent)) {
                message_item.setVisibility(View.VISIBLE);
                neirong.setText(commentList.get(position).ReplyContent);
                neirong.setEnabled(false);
                huifu.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
            }
            //回复点击事件
            huifu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (message_item.getVisibility() == View.GONE) {
                        message_item.setVisibility(View.VISIBLE);
                        neirong.requestFocus();
                    } else {
                        message_item.setVisibility(View.GONE);
                    }
                }
            });
            final int finalPosition = position;
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != neirong.getText().toString() && !TextUtils.isEmpty(neirong.getText().toString())) {
                        replyComment(commentList.get(finalPosition).Id, neirong.getText().toString());
                    } else {
                        Toast.makeText(GoodsCommentActiviyt.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            MyImageLoader.displayDefaultImage(URLText.img_url + commentList.get(position).User.PhotoPath, head_image);
            Glide.with(this).load(URLText.img_url + commentList.get(position).User.PhotoPath).into(head_image);
            name.setText(commentList.get(position).User.ShowName);
            date.setText(commentList.get(position).CreateDateName);
            content.setText(commentList.get(position).Content);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                    50, 50);
            layout.setMargins(5, 0, 5, 0);
            comment_score.removeAllViews();
            if (commentList.get(position).ProductScore.length() > 1) {
                int score1 = Integer.parseInt(commentList.get(position).ProductScore.substring(0, 1));
                for (int i = 0; i < score1; i++) {
                    ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                    imageView.setLayoutParams(layout);
                    imageView.setBackgroundResource(R.mipmap.comment_star);
                    comment_score.addView(imageView);
                }
                ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                imageView.setLayoutParams(layout);
                imageView.setBackgroundResource(R.mipmap.start_half);
                comment_score.addView(imageView);

                for (int i = 0; i < 5 - score1 - 1; i++) {
                    ImageView imageView1 = new ImageView(GoodsCommentActiviyt.this);
                    imageView1.setLayoutParams(layout);
                    imageView1.setBackgroundResource(R.mipmap.comment_start_gray);
                    comment_score.addView(imageView1);
                }
            } else {
                int score1 = Integer.parseInt(commentList.get(position).ProductScore);
                for (int i = 0; i < score1; i++) {
                    ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                    imageView.setLayoutParams(layout);
                    imageView.setBackgroundResource(R.mipmap.comment_star);
                    comment_score.addView(imageView);
                }
                for (int i = 0; i < 5 - score1; i++) {
                    ImageView imageView = new ImageView(GoodsCommentActiviyt.this);
                    imageView.setLayoutParams(layout);
                    imageView.setBackgroundResource(R.mipmap.comment_start_gray);
                    comment_score.addView(imageView);
                }
            }

            listView.addView(convertView);
        }
    }
}
