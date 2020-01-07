package com.ehualu.calabashandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ehualu.calabashandroid.api.ApiRetrofit;
import com.ehualu.calabashandroid.db.entity.TestFile;
import com.ehualu.calabashandroid.db.manager.EntityManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private TextView queryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        queryResult = findViewById(R.id.queryResult);


        ApiRetrofit.getInstance().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfo -> {
                    Toast.makeText(TestActivity.this, userInfo.getOcs().getData().getEmail(), Toast.LENGTH_LONG).show();
                }, this::onError);
    }

    private void onError(Throwable throwable) {
        Log.e("请求失败", throwable.getMessage());
    }

    public void addData(View view) {
        TestFile testFile = new TestFile();
        testFile.setPath("/iLake/333/suhao/a.txt");
        testFile.setSize(1024);
        EntityManager.getInstance().getTestFileDao().insert(testFile);
    }

    public void queryData(View view) {
        List<TestFile> list = EntityManager.getInstance().getTestFileDao().loadAll();
        if (list.size() > 0) {
            queryResult.setText(list.get(0).getPath());
        }
    }
}
