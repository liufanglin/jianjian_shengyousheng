package com.ximai.savingsmore.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;




/**
 * fragmentmy自定义组件
 * Created by C.ym on 2016/4/25.
 */
public class ItemWithoutIcon extends LinearLayout {

    public TextView tvContent;
    public TextView tvTitle;
    public ImageView ivIcon, ivArrow;

    public ItemWithoutIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemWithoutIcon);

        tvTitle.setText(typedArray.getString(R.styleable.ItemWithoutIcon_head));
        tvContent.setText(typedArray.getString(R.styleable.ItemWithoutIcon_content));

        typedArray.recycle();
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_item_without_icon, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        ivArrow = (ImageView) findViewById(R.id.ivArrow);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
    }
}
