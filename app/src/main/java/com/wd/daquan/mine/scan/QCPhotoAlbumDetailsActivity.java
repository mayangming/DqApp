package com.wd.daquan.mine.scan;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.scancode.QRCodeUtils;
import com.wd.daquan.model.log.DqToast;
import com.meetqs.qingchat.imagepicker.decoration.GridSpacingItemDecoration;
import com.meetqs.qingchat.imagepicker.tools.ScreenUtils;
import com.wd.daquan.mine.adapter.PhotoAlbumDetailsAdapter;
import com.wd.daquan.mine.bean.PhotoAlbumEntity;
import com.da.library.tools.Utils;
import com.da.library.widget.AnimUtils;
import com.da.library.widget.CommTitle;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.ArrayList;
import java.util.List;

public class QCPhotoAlbumDetailsActivity extends DqBaseActivity {

	private List<String> data = new ArrayList<>();
	private PhotoAlbumDetailsAdapter mAdapter;
	private RecyclerView mRecyclerView;
	public CommTitle mCommTitle;

	@Override
	protected Presenter.IPresenter createPresenter() {
		return null;
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.photo_album_details_activity);
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initView() {
		mCommTitle = findViewById(R.id.photoAlbumDetailsCommtitle);
		mRecyclerView = findViewById(R.id.photoAlbumDetailsRecyclerview);
		mCommTitle.setTitle(getString(R.string.album_list));

		mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
				ScreenUtils.dip2px(this, 2), false));
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
	}

	@Override
	protected void initListener() {
		mCommTitle.getLeftIv().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void initData() {
		PhotoAlbumEntity photoAlbumEntity = getIntent().getParcelableExtra(KeyValue.PHOTO_ALBUM_LIST_DATA);
		assert photoAlbumEntity != null;
		for (int i = photoAlbumEntity.filecontent.size()-1; i>=0; i--){
			data.add(photoAlbumEntity.filecontent.get(i));
		}
		PhotoAlbumDetailsAdapter adapter = new PhotoAlbumDetailsAdapter();
		mRecyclerView.setAdapter(adapter);
		adapter.update(data);
		adapter.setPhotoAlbumDetailsListener(new PhotoAlbumDetailsAdapter.OnPhotoAlbumDetailsListener() {
			@Override
			public void onLayout(String imgPath, int position) {
				if(Utils.isFastDoubleClick(500)){
					return;
				}
				String filePath = data.get(position);
				//CNLog.w("xxxx", "识别图片地址: " + filapath);
				QRCodeUtils.recognitionQrcode(filePath, new QRCodeUtils.AnalyzeCallback() {
					@Override
					public void onSuccess(Bitmap mBitmap, String result) {
						//CNLog.w("xxxx", "识别图片成功");
						//EBApplication.getInstance().deleteAllActivity(EBApplication.activityImg);
						MsgMgr.getInstance().sendMsg(MsgType.MT_QR_CODE, result);
						finish();
					}

					@Override
					public void onFailed() {
						//CNLog.w("xxxx", "识别图片失败");
						DqToast.showShort(getString(R.string.unidentification_pic));
					}
				});
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		AnimUtils.exitAnimForActivity(this);
	}
}
