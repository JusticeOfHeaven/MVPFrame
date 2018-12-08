package com.my.mvpframe.customview.rollingtext;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;

import butterknife.BindView;

/**
 * Create by jzhan on 2018/12/3
 */
public class RollingTextAct extends BaseActivity {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.rollingText)
    RollingText rollingText;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView1)
    TextView textView1;
    @Override
    protected int getLayoutId() {
        return R.layout.act_rolling_text;
    }

    @Override
    protected void initView() {
        textView.setOnClickListener(view -> {
            String str = editText.getText().toString();
            if (TextUtils.isEmpty(str)) {
                str = "4321";
            }
            rollingText.setRollText(str);
            rollingText.start();
        });
    }
}
