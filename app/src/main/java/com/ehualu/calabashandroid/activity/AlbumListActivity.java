package com.ehualu.calabashandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.AlbumAdapter;
import com.ehualu.calabashandroid.app.MyApp;
import com.ehualu.calabashandroid.base.BaseActivity;
import com.ehualu.calabashandroid.model.Album;
import com.ehualu.calabashandroid.utils.LocalImageHelper;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 照片
 */
public class AlbumListActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.recyAlbum)
    RecyclerView recyAlbum;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.tvSelectAllOrNot)
    TextView tvSelectAllOrNot;
    private LocalImageHelper localImageHelper;

    private AlbumAdapter adapter;

    private String dirID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        ButterKnife.bind(this);

        llBack.setOnClickListener(this);

        MyApp.needDestoryActs.add(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("相册");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        dirID = getIntent().getStringExtra("dirID");

        localImageHelper = new LocalImageHelper();
        localImageHelper.initImage(this, new DataCallBack() {
            @Override
            public void onReadFinished(List<Album> albums) {
                adapter = new AlbumAdapter(AlbumListActivity.this, albums);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyAlbum.setLayoutManager(new GridLayoutManager(AlbumListActivity.this, 2));
                        DividerItemDecoration decoration = new DividerItemDecoration(AlbumListActivity.this, DividerItemDecoration.VERTICAL);
                        decoration.setDrawable(getResources().getDrawable(R.drawable.vertical_line));
                        recyAlbum.addItemDecoration(decoration);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(AlbumListActivity.this, AlbumActivity.class);
                                intent.putExtra("buck_id", albums.get(position).getId());
                                intent.putExtra("dirID", dirID);
                                startActivity(intent);
                            }
                        });
                        recyAlbum.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public interface DataCallBack {
        void onReadFinished(List<Album> albums);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
        }
    }
}
