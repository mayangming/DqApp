package com.wd.daquan.mine.scan;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.PhotoAlbumListUtil;
import com.wd.daquan.mine.adapter.PhotoAlbumListAdapter;
import com.wd.daquan.mine.bean.PhotoAlbumEntity;
import com.da.library.widget.AnimUtils;
import com.da.library.widget.CommTitle;

import java.util.List;

public class QCPhotoAlbumListActivity extends DqBaseActivity {

    public CommTitle mCommTitle;
    private List<PhotoAlbumEntity> photoAlbumlList;
    private RecyclerView mRecyclerView;

    @Override
    protected Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.photo_album_list_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.photoAlbumListCommtitle);
        mRecyclerView = findViewById(R.id.photoAlbumListRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommTitle.setTitle(getString(R.string.album_list));
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        PhotoAlbumListUtil util = new PhotoAlbumListUtil(this);
        photoAlbumlList = util.LocalImgFileList();
        PhotoAlbumListAdapter adapter = new PhotoAlbumListAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.update(photoAlbumlList);
        adapter.setPhotoAlbumListListener(new PhotoAlbumListAdapter.OnPhotoAlbumListListener() {
            @Override
            public void onLayout(PhotoAlbumEntity data, int position) {
                Intent intent = new Intent(QCPhotoAlbumListActivity.this, QCPhotoAlbumDetailsActivity.class);
                intent.putExtra(KeyValue.PHOTO_ALBUM_LIST_DATA, photoAlbumlList.get(position));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        AnimUtils.exitAnimForActivity(this);
    }
}
