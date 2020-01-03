package com.quyuanjin.im2.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quyuanjin.im2.R;

public class MeFragment extends Fragment {
    View view;
   @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       if (null!=view){

           ViewGroup parent= (ViewGroup) view.getParent();
           if (null!=parent){
               parent.removeView(view);
           }
       }else {
           view = inflater.inflate(R.layout.me_ac, container, false);




       }
        return view;
    }
}
