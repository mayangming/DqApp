package com.wd.daquan.mine.bean;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class PhotoAlbumEntity implements Parcelable {
	public String filename;
	public List<String> filecontent = new ArrayList();
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(filename);
		dest.writeList(filecontent);
	}
	
	public static final Creator<PhotoAlbumEntity> CREATOR=new Creator<PhotoAlbumEntity>() {
		
		@Override
		public PhotoAlbumEntity[] newArray(int size) {
			return null;
		}
		
		@Override
		public PhotoAlbumEntity createFromParcel(Parcel source) {
			PhotoAlbumEntity ft=new PhotoAlbumEntity();
			ft.filename= source.readString();
			ft.filecontent= source.readArrayList(PhotoAlbumEntity.class.getClassLoader());
			
			return ft;
		}
		
		
	};
}
