package com.ehualu.calabashandroid.fragment;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehualu.calabashandroid.R;
import com.ehualu.calabashandroid.adapter.SelectDocumentAdapter;
import com.ehualu.calabashandroid.base.BaseFragment;
import com.ehualu.calabashandroid.model.Document;
import com.ehualu.calabashandroid.model.DocumentList;
import com.ehualu.calabashandroid.utils.LocalFileHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DocumentFragment extends BaseFragment {

    @BindView(R.id.recyDocumentList)
    RecyclerView recyDocumentList;

    private List<DocumentList> lists = new ArrayList<>();
    private SelectDocumentAdapter adapter;
    private LocalFileHelper fileHelper;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_document;
    }

    @Override
    protected void setUpView() {
        adapter = new SelectDocumentAdapter(baseActivity, lists);
        recyDocumentList.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyDocumentList.setAdapter(adapter);
        fileHelper = new LocalFileHelper();
        fileHelper.initFile(baseActivity, new DataCallBack() {
            @Override
            public void onReadFinished(List<DocumentList> documentLists) {
                lists.addAll(documentLists);
                adapter.notifyDataSetChanged();
                int size = 0;
                for (DocumentList dl : documentLists) {
                    size += dl.getDocuments().size();

                    for (Document d : dl.getDocuments()) {
                        Log.e("文档路径", d.getPath());
                    }
                }
                Log.e("总数量", size + "----");

            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onClick(View v) {

    }

    public interface DataCallBack {
        void onReadFinished(List<DocumentList> videos);
    }
}
