package com.my.mvpframe.customview.qq_message_4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.mvpframe.R;


/**
 * Created by bingosoft on 15/6/18.
 */
public class ContactFragment extends Fragment {
    private String mTitle = "TabFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.list_item,container,false);
    }

    public String getTitle(){
        return mTitle;
    }
}
