package com.ximai.savingsmore.save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.FileSystem.FileSystem;
import com.ximai.savingsmore.library.constants.AppConstants;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.library.toolbox.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/18.
 */
public class GuidePageActivity extends Activity implements View.OnClickListener {
    private ViewGroup viewGroup;
    private int[] imgae = AppConstants.GUIDE_PAGE;
    private List<View> list;
    private ViewPager pager;
    private ImageView[] dotImageView;
    private TextView kaishitiyan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidepage);
        initViewPager();
        initDot();
        saveImages();
        pager.setAdapter(new MyAdapater(list));
        pager.setOnPageChangeListener(new MyPageChangeListener());
        kaishitiyan = (TextView) findViewById(R.id.cuxiao_style);
        kaishitiyan.setOnClickListener(this);


    }


    /**
     * 初始化viewpager
     */
    public void initViewPager() {
        pager = (ViewPager) findViewById(R.id.vp);
        list = new ArrayList<>();
        for (int i = 0; i < imgae.length; i++) {
//            if(i==imgae.length-1) {
//                View view = LayoutInflater.from(GuidePageActivity.this).inflate(R.layout.guide_begin_page, null);
//                view.setBackgroundResource(imgae[i]);
//                Button btn= (Button) view.findViewById(R.id.start_login);
//                btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        gotoTarget();
//                    }
//                });
//                list.add(view);
//            }else {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgae[i]);
            list.add(imageView);
            //  }


        }
    }

    /**
     * 初始化小点
     */
    public void initDot() {
        dotImageView = new ImageView[list.size()];
        viewGroup = (ViewGroup) findViewById(R.id.viewgroup);
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < list.size(); i++) {
            margin.setMargins((int) ScreenUtils.dpToPx(GuidePageActivity.this, 5.0f), 0, 0, 0);
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(margin);
            dotImageView[i] = imageView;
            if (i == 0) {
                dotImageView[i].setBackgroundResource(AppConstants.GUIDE_PAGE_POINT[0]);

            } else {
                dotImageView[i].setBackgroundResource(AppConstants.GUIDE_PAGE_POINT[1]);
            }
            viewGroup.addView(dotImageView[i]);
        }
    }


    public void gotoTarget() {


        //保存打开引导页标记
        PreferencesUtils.putBoolean(GuidePageActivity.this, SplashActivity.NOT_FIRST_OPEN, true);

        Intent it = new Intent(GuidePageActivity.this, NoLoginMainactivity.class);
        startActivity(it);

        finish();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cuxiao_style) {
            if (kaishitiyan.getVisibility() == View.VISIBLE) {
                gotoTarget();
            }
        }
    }


    //viewpage适配器
    public class MyAdapater extends PagerAdapter {
        public List<View> list;

        public MyAdapater(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }

    //页面滑动监听
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < list.size(); i++) {
                if (position == i) {
                    dotImageView[i].setBackgroundResource(AppConstants.GUIDE_PAGE_POINT[0]);
                } else {
                    dotImageView[i].setBackgroundResource(AppConstants.GUIDE_PAGE_POINT[1]);
                }
                if (position == 3) {
                    kaishitiyan.setVisibility(View.VISIBLE);
                    // gotoTarget();
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    private void saveImages() {

        String path = FileSystem.getCachesDir(GuidePageActivity.this, true).getAbsolutePath();//获取手机本地存//储路径，这个位置可以根据需要自己改。

        InputStream in = getResources().openRawResource(R.raw.ximaiicon);//读取程序中的图片

        File file = new File(path + File.separator + "icon.jpg");//创建文件
        if (!file.exists()) {
            try {//如果文件不存在就创建文件，写入图片
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                int read = in.read();
                while (read != -1) {
                    fo.write(read);
                    read = in.read();
                }
                //关闭流
                fo.flush();
                fo.close();
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
