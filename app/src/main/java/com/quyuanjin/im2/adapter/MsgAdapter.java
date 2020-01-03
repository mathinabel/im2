package com.quyuanjin.im2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quyuanjin.im2.R;

import com.quyuanjin.im2.ac.ChatAc;
import com.quyuanjin.im2.test.QmuiTest;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.myViewHodler> {

    private Context context;
    private List<String> mArrayList;
    private List<String> muuidList;

    public MsgAdapter(Context context, List<String> list, List<String> uuidlist) {
        this.context = context;
        this.mArrayList = list;
        this.muuidList = uuidlist;
    }


    @NonNull
    @Override
    public myViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //创建自定义布局
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_test, viewGroup, false);
        return new myViewHodler(view);

    }



    @Override
    public void onBindViewHolder(@NonNull myViewHodler myViewHodler, int i) {

        //根据点击位置绑定数据
        final String   uuid = muuidList.get(i);
        String   s = mArrayList.get(i);

        myViewHodler.myNameTextView.setText(uuid);
        myViewHodler.mMsg.setText(s);
        myViewHodler.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatAc.class);
                intent.putExtra("receiveUUID", uuid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    /**
     * 用于初始化控件viewHolder
     */
    class myViewHodler extends RecyclerView.ViewHolder {


        //    @BindView(R.id.nameTextView)
        private TextView myNameTextView;
        private LinearLayout mLinearLayout;
        private TextView mMsg;

        public myViewHodler(@NonNull View itemView) {
            super(itemView);
            myNameTextView = itemView.findViewById(R.id.nameTextView);
            mLinearLayout = itemView.findViewById(R.id.item_click);
            mMsg = itemView.findViewById(R.id.contentTextView);

        }
    }
}
