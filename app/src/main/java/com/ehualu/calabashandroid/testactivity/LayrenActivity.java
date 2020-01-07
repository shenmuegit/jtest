package com.ehualu.calabashandroid.testactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.requestBean.RequestDeleteFileBean;
import com.ehualu.calabashandroid.responseBean.BaseResponseBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by GaoTing on 2019/12/25.
 * <p>
 * Explain:接口测试Activity
 */
public class LayrenActivity extends BaseActivity {
    @BindView(R.id.lv_test_layren)
    ListView listView;
    @BindView(R.id.tv_test_layren)
    TextView textView;

    List<Layren> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layren_test);
        ButterKnife.bind(this);
        initView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        initData();
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
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
                TextView textView = new TextView(LayrenActivity.this);
                Layren layren = list.get(position);
                textView.setText(layren.name);
                textView.setTextSize(20);
                if (layren.success) {
                    textView.setTextColor(0xff00ff00);
                }
                textView.setPadding(10, 15, 10, 15);
                return textView;
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Layren layren = list.get(position);
            layren.observable.subscribe((Consumer<BaseResponseBean>) bean -> textView.setText(layren.name + "\n" + bean.toString())
                    , throwable -> textView.setText(layren.name + "\n" + throwable.toString()));
        });

    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new Layren("文件永存", ApiRetrofit.getInstance().postImmortal("1240151-04", "1"), false));
        list.add(new Layren("文件夹永存", ApiRetrofit.getInstance().postImmortal("3", "2"), false));
        list.add(new Layren("文件收藏", ApiRetrofit.getInstance().postCollection("1240151-04", "1"), true));
        list.add(new Layren("文件取消收藏", ApiRetrofit.getInstance().postCollection("1240151-04", "2"), true));
//        list.add(new Layren("新建文件夹", ApiRetrofit.getInstance().postFolder(getFolderName(), "3"), false));
//        list.add(new Layren("文件移动", ApiRetrofit.getInstance().postRegion("1240151-04", "3", "1"), false));
//        list.add(new Layren("文件夹移动", ApiRetrofit.getInstance().postRegion("1240151-04", "3", "2"), false));
//        list.add(new Layren("文件删除", ApiRetrofit.getInstance().postDelete(new RequestDeleteFileBean("1240151-08", "1")), true));
    }

    private String getFolderName() {
        String name = "";
        String hexDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 4; i++) {
            int x = new Random().nextInt(hexDigits.length());
            name += hexDigits.substring(x, x + 1);
        }
        return name;
    }
}
