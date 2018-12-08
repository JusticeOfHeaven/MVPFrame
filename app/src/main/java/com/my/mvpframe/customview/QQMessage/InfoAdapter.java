package com.my.mvpframe.customview.QQMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mvpframe.R;


/**
 * Created by yinwei on 2015-12-01.
 */
public class InfoAdapter extends BaseAdapter {

    private Context context;

    private TipsView tipsView;

    private MsgXlListView listView;

    public InfoAdapter(Context context) {
        super();
        this.context=context;
    }

    public void setTipsView(TipsView tipsView) {
        this.tipsView = tipsView;
    }

    public void setListView(MsgXlListView listView) {
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return 15;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.infoitem, null);//别吐槽效率啊，自己做个viewholder
            holder.count=(TextView)convertView.findViewById(R.id.infoCount);
            convertView.setTag(holder);
        }else {


            holder=(ViewHolder)convertView.getTag();

        }

         //小红点关键代码
        if(tipsView!=null){

            tipsView.attach(holder.count, new TipsView.Listener() {
                @Override
                public void onStart() {
                    holder.count.setVisibility(View.INVISIBLE);
                    listView.requestDisallowInterceptTouchEvent(true);
                }

                @Override
                public void onComplete() {



                }

                @Override
                public void onCancel() {

                    holder.count.setVisibility(View.VISIBLE);
                }
            });


        }

        return convertView;
    }


    static class  ViewHolder{
        LinearLayout item;
        ImageView infoImage;
        TextView infoTitle,infoDetails,infoTime,putTop,delete,count;

    }
}
