package com.ximai.savingsmore.save.view.imagepicker.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;


/**
 * Created by lihao
 * Date: 6/12.
 * Desc:
 */
public class LoadingDialog extends Dialog {
    AnimationDrawable anim;
    View view;
    TextView loadingText;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
        loadingText = (TextView) view.findViewById(R.id.loadingText);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_loading);
        anim = (AnimationDrawable) ContextCompat.getDrawable(context,
                R.drawable.loading_drawable);
        imageView.setImageDrawable(anim);
        setContentView(view);
    }

    public void SetLoadingText(int textRes){
        if(loadingText !=null){
            loadingText.setText(textRes);
        }
    }

    public void startAnimation() {
        if (!anim.isRunning()) {
            anim.start();
        }

    }

    public void stopAnimation() {
        if (anim.isRunning()) {
            anim.stop();
        }
    }
}
