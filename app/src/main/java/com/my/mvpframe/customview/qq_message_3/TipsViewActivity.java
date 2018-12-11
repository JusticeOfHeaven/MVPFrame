package com.my.mvpframe.customview.qq_message_3;

import android.view.View;
import android.widget.TextView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;

import butterknife.BindView;

/**
 * Create by jzhan on 2018/12/10
 */
public class TipsViewActivity extends BaseActivity {
    @BindView(R.id.messageView)
    TextView messageView;
    @BindView(R.id.tipsView)
    TipsView tipsView;
    @Override
    protected int getLayoutId() {
        return R.layout.act_tips_view;
    }

    @Override
    protected void initView() {
        tipsView.attach(messageView, new TipsView.Listener() {
            @Override
            public void onStart() {
                messageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onCancel() {
                messageView.setVisibility(View.VISIBLE);
            }
        });
    }
}
