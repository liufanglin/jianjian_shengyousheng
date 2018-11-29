package com.ximai.savingsmore.save.activity;

import android.os.Bundle;

import com.easemob.easeui.EaseConstant;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.easeUI.MyEaseChatFragment;
import com.ximai.savingsmore.save.modle.IMUserList;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/16.
 */
public class ChatActivity extends BaseActivity {
    private String UserName;
    private String name;
    List<String> listName=new ArrayList<>();
    private String names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        setLeftBackMenuVisibility(ChatActivity.this, "");
        UserName = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        //聊天过长处理
        if (UserName.length() > 8){
            String substring = UserName.substring(0, 8);
            name = substring+"...";
            listName.add(name);
        }else{
            listName.add(UserName);
        }
        getUserByIM(UserName);
    }

    private void getUserByIM(String userName) {
        WebRequestHelper.json_post(ChatActivity.this, URLText.USERBYIM, RequestParamsPool.getUserByIM(userName), new MyAsyncHttpResponseHandler(ChatActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result=new String(responseBody);
                IMUserList imUserList= GsonUtils.fromJson(result,IMUserList.class);
                if(imUserList.IsSuccess.equals("true")&&imUserList.MainData.size()>0){
//                    setCenterTitle(imUserList.MainData.get(0).ShowName);

                    //聊天过长处理
                    if (imUserList.MainData.get(0).ShowName.length() > 8){
                        String substring = imUserList.MainData.get(0).ShowName.substring(0, 8);
                        names = substring+"...";
                        setCenterTitle(names);
                    }else{
                        setCenterTitle(imUserList.MainData.get(0).ShowName);
                    }

                    MyEaseChatFragment myEaseChatFragment=new MyEaseChatFragment();
                    Bundle args = new Bundle();
                    args.putString(EaseConstant.EXTRA_USER_ID, UserName);
                    args.putString("url",imUserList.MainData.get(0).PhotoPath);
                    myEaseChatFragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().add(R.id.message, myEaseChatFragment).commit();
                }
            }
        });
    }
}