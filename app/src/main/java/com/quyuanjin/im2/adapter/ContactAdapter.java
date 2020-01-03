/*
 * Copyright (c) 2015 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quyuanjin.im2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.quyuanjin.im2.R;
import com.quyuanjin.im2.ac.ContractDetailAc;
import com.quyuanjin.im2.adapter.contact.bean.Contact;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 9/16/15.
 */
public class ContactAdapter extends KJAdapter<Contact> implements SectionIndexer {

    private KJBitmap kjb = new KJBitmap();
    private ArrayList<Contact> datas;
private Context mContext;
    public ContactAdapter(AbsListView view, ArrayList<Contact> mDatas, Context context) {
        super(view, mDatas, R.layout.item_list_contact);
        datas = mDatas;
        mContext=context;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
    }

    @Override
    public void convert(AdapterHolder helper, Contact item, boolean isScrolling) {
    }

    @Override
    public void convert(final AdapterHolder holder, Contact item, boolean isScrolling, final int position) {

        holder.setText(R.id.contact_title, item.getName());

        LinearLayout linearLayout= holder.getView(R.id.contact_content);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContractDetailAc.class);
                //todo 将对应的uuid也传到下个ac
                intent.putExtra("receiveUUID",datas.get(position).getUUID());
                mContext.startActivity(intent);
            }
        });
        ImageView headImg = holder.getView(R.id.contact_head);
        if (isScrolling) {
            kjb.displayCacheOrDefult(headImg, item.getUrl(), R.drawable.default_head_rect);
        } else {
            kjb.displayWithLoadBitmap(headImg, item.getUrl(), R.drawable.default_head_rect);
        }

        TextView tvLetter = holder.getView(R.id.contact_catalog);
        TextView tvLine = holder.getView(R.id.contact_line);

        //如果是第0个那么一定显示#号
        if (position == 0) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText("#");
            tvLine.setVisibility(View.VISIBLE);
        } else {

            //如果和上一个item的首字母不同，则认为是新分类的开始
            Contact prevData = datas.get(position - 1);
            if (item.getFirstChar() != prevData.getFirstChar()) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText("" + item.getFirstChar());
                tvLine.setVisibility(View.VISIBLE);
            } else {
                tvLetter.setVisibility(View.GONE);
                tvLine.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        Contact item = datas.get(position);
        return item.getFirstChar();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = datas.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
