package com.quyuanjin.im2.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.ac.ChatAc;
import com.quyuanjin.im2.ac.ContractDetailAc;

import java.util.List;

public class ContractAdapter extends BaseAdapter {

        private Context mContext;
        private List mList;


    public ContractAdapter(Context mContext, List mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view1 = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_list_contact, viewGroup, false);

        TextView textView = view1.findViewById(R.id.contact_title);
        LinearLayout linearLayout = view1.findViewById(R.id.contact_content);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContractDetailAc.class);
                //todo 将对应的uuid也传到下个ac
                intent.putExtra("receiveUUID",mList.get(i).toString());
                mContext.startActivity(intent);
            }
        });
        textView.setText(mList.get(i).toString());
        //  textView.setText("123");

        return view1;
    }
}
