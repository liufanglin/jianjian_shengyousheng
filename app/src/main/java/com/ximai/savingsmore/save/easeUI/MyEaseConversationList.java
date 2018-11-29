package com.ximai.savingsmore.save.easeUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.easeui.adapter.EaseConversationAdapater;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.modle.IMUser;
import com.ximai.savingsmore.save.modle.IMUserList;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by caojian on 16/12/18.
 */
public class MyEaseConversationList extends ListView {
    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;
    private List<IMUserList> imUserLists = new ArrayList<IMUserList>();
    private List<IMUser> imUsers = new ArrayList<IMUser>();


    protected final int MSG_REFRESH_ADAPTER_DATA = 0;

    protected Context context;
    protected MyEaseConversationAdapater adapter;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();


    public MyEaseConversationList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyEaseConversationList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, com.easemob.easeui.R.styleable.EaseConversationList);
        primaryColor = ta.getColor(com.easemob.easeui.R.styleable.EaseConversationList_cvsListPrimaryTextColor, com.easemob.easeui.R.color.list_itease_primary_color);
        secondaryColor = ta.getColor(com.easemob.easeui.R.styleable.EaseConversationList_cvsListSecondaryTextColor, com.easemob.easeui.R.color.list_itease_secondary_color);
        timeColor = ta.getColor(com.easemob.easeui.R.styleable.EaseConversationList_cvsListTimeTextColor, com.easemob.easeui.R.color.list_itease_secondary_color);
        primarySize = ta.getDimensionPixelSize(com.easemob.easeui.R.styleable.EaseConversationList_cvsListPrimaryTextSize, 0);
        secondarySize = ta.getDimensionPixelSize(com.easemob.easeui.R.styleable.EaseConversationList_cvsListSecondaryTextSize, 0);
        timeSize = ta.getDimension(com.easemob.easeui.R.styleable.EaseConversationList_cvsListTimeTextSize, 0);

        ta.recycle();

    }

    public void init(List<EMConversation> conversationList, String result) {
        Map<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    getUserByIM(conversation.getUserName());
                }
            }
        }
        this.conversationList = conversationList;
        adapter = new MyEaseConversationAdapater(context, 0, conversationList);
        adapter.setPrimaryColor(primaryColor);
        adapter.setPrimarySize(primarySize);
        adapter.setSecondaryColor(secondaryColor);
        adapter.setSecondarySize(secondarySize);
        adapter.setTimeColor(timeColor);
        adapter.setTimeSize(timeSize);
        setAdapter(adapter);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_ADAPTER_DATA:
                    if (adapter != null) {
                        conversationList.clear();
                        conversationList.addAll(loadConversationsWithRecentChat());
                        adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 获取所有会话
     *
     * @param context
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param usernames
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    public EMConversation getItem(int position) {
        return (EMConversation) adapter.getItem(position);
    }

    public void refresh() {
        conversationList = loadConversationsWithRecentChat();

        handler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
    }

    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }

    private void getUserByIM(String userName) {
        WebRequestHelper.json_post(context, URLText.USERBYIM, RequestParamsPool.getUserByIM(userName), new MyAsyncHttpResponseHandler(context) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                IMUserList imUserList = GsonUtils.fromJson(result, IMUserList.class);
                if (imUserList.IsSuccess.equals("true") && imUserList.MainData.size() > 0 ) {
                    imUserLists.add(imUserList);
                }
                if (imUserLists.size() == EMChatManager.getInstance().getAllConversations().size()) {
                    adapter.setImUsers(imUserLists);
                    refresh();
                }
            }
        });
    }

}
