package com.quyuanjin.im2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.ac.AddFriendAc;
import com.quyuanjin.im2.adapter.ContactAdapter;
import com.quyuanjin.im2.adapter.ContractAdapter;
import com.quyuanjin.im2.adapter.contact.bean.Contact;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.greendao.pojo.Friends;
import com.quyuanjin.im2.greendao.pojo.UnReadAddFriendsMsg;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.pojo.AddFriend;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriend;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendTwo;
import com.quyuanjin.im2.ui.widget.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ContractFragment extends Fragment  {
    private ContractAdapter arrayAdapter;
    private ContactAdapter mAdapter;
    private TextView mFooterView;
    private ListView mListView;
    private List<String> mList = new ArrayList();
    private ArrayList<Contact> datas = new ArrayList<>();
    private LinearLayout addFriends;
    View view;
    private TextView unreadMsg;
    private SideBar mSideBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (null != view) {

            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {

            view = inflater.inflate(R.layout.contract_fragment, container, false);
            mListView = view.findViewById(R.id.school_friend_member);
            final SideBar mSideBar = view.findViewById(R.id.school_friend_sidrbar);
            unreadMsg = view.findViewById(R.id.unreadFriendRequestCountTextView);
             TextView mDialog = view.findViewById(R.id.school_friend_dialog);
            EditText mSearchInput = view.findViewById(R.id.school_friend_member_search_input);

            //新朋友按钮
            addFriends = view.findViewById(R.id.addNewFriend);
            addFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ContractFragment.this.getContext(), AddFriendAc.class);

                    startActivity(intent);


                    //   mSearchInput.addTextChangedListener(this);
                }
            });

              mSideBar.setTextView(mDialog);
            mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
                @Override
                public void onTouchingLetterChanged(String s) {
                    int position = 0;
                    // 该字母首次出现的位置
                    if (mAdapter != null) {
                        position = mAdapter.getPositionForSection(s.charAt(0));
                    }
                    if (position != -1) {
                        mListView.setSelection(position);
                    } else if (s.contains("#")) {
                        mListView.setSelection(0);
                    }
                }
            });
            mSearchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                /*    ArrayList<Contact> temp = new ArrayList<>(datas);
                    for (Contact data : datas) {
                        if (data.getName().contains(s) || data.getPinyin().contains(s)) {
                        } else {
                            temp.remove(data);
                        }
                    }
                    if (mAdapter != null) {
                        mAdapter.refresh(temp);
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            // 给listView设置adapter
            mFooterView = (TextView) View.inflate(this.getContext(), R.layout.item_list_contact_count, null);
            mListView.addFooterView(mFooterView);


    /*     arrayAdapter=new ArrayAdapter<>
                (this.getContext(),R.layout.item_list_contact,R.id.contact_title,
                        new String[]{"item","1235","qumui","miss","mis"});*/

            //在contractFragment中存入
            //mlist存放uuid
            //TODO 查询pojo.Friends 中的数据


            List<Friends> friendsList = App.getDaoSession().getFriendsDao().queryBuilder().build().list();
            for (int i = 0; i < friendsList.size(); i++) {
                Friends friends2 = friendsList.get(i);
                //mList.add(friends2.getFriendUUId());
                Contact data = new Contact();
                data.setName(friends2.getName());
                data.setUrl("http://192.168.1.102:8080/tomcat.png");

                data.setUUID(friends2.getFriendUUId());
                data.setPinyin(friends2.getPinYin());
                datas.add(data);
            }

          //  mList.add("uuid");

            //todo 传入的list应该来自于sqlite，list为uuid

//            arrayAdapter = new ContractAdapter(this.getContext(), mList);


         //   for (int i = 0; i < 10; i++) {
            Contact data8 = new Contact();
            data8.setName("aaa");
            data8.setUrl("http://192.168.1.102:8080/tomcat.png");
            data8.setId(123);
            data8.setUUID("1234");
            data8.setPinyin("kefu");
            datas.add(data8);


       //     }
            mAdapter = new ContactAdapter(mListView, datas,this.getContext());

            mListView.setAdapter(mAdapter);
            //  mListView.setAdapter(arrayAdapter);
            //获取未添加好友请求数量
            getUnReadAddFriendMsgNumber();
        }
        return view;
    }

    private void getUnReadAddFriendMsgNumber() {
        List<UnReadAddFriendsMsg> unReadAddFriendsMsgList = App.getDaoSession().getUnReadAddFriendsMsgDao().queryBuilder().build().list();

        if (unReadAddFriendsMsgList != null) {
            if (unReadAddFriendsMsgList.size() > 0) {
                unreadMsg.setText(String.valueOf(unReadAddFriendsMsgList.size()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AddEvent(AddFriend addFriend) {
        ToastUtils.show(ContractFragment.this.getContext(), addFriend.getFromuuid());
        unreadMsg.setVisibility(View.VISIBLE);
        unreadMsg.setText("1");


        //greendao存储addfriends的信息
        UnReadAddFriendsMsg unReadAddFriendsMsg = new UnReadAddFriendsMsg();
        unReadAddFriendsMsg.setFriendUUId(addFriend.getFromuuid());
        App.getDaoSession().getUnReadAddFriendsMsgDao().insert(unReadAddFriendsMsg);

        ToastUtils.show(ContractFragment.this.getContext(), addFriend.getFromuuid());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//friend表好友加一，并通知list显示
    public void AddEvent2(SucceedAddFriend succeedAddFriend) {
        Friends friend = new Friends();
        friend.setFriendUUId(succeedAddFriend.getFriendUUID());
        App.getDaoSession().getFriendsDao().insert(friend);
       // mList.add(succeedAddFriend.getFriendUUID());
        Contact data8 = new Contact();
        data8.setName("aaa");
        data8.setUrl("http://192.168.1.102:8080/tomcat.png");
        data8.setId(123);
        data8.setUUID(succeedAddFriend.getFriendUUID());
        data8.setPinyin("xinzeng");
        datas.add(data8);
       mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//friend表好友加一，并通知list显示
    public void AddEvent02(SucceedAddFriendTwo succeedAddFriendTwo) {
        Friends friend = new Friends();
        friend.setFriendUUId(succeedAddFriendTwo.getFriendUUID());
        App.getDaoSession().getFriendsDao().insert(friend);
      //  mList.add(succeedAddFriendTwo.getFriendUUID());
        Contact data = new Contact();
        data.setName("新加好友名称");
        data.setUUID(succeedAddFriendTwo.getFriendUUID());
        datas.add(data);
       mAdapter.notifyDataSetChanged();
    }


}
