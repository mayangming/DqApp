package com.wd.daquan.chat.emotion;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Media;

import androidx.collection.ArrayMap;
import androidx.core.content.FileProvider;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.log.DqToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PicSelectorActivity extends Activity {
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private GridView mGridView;
    private ImageButton mBtnBack;
    private PicTypeBtn mPicType;
    private View mCatalogView;
    private ListView mCatalogListView;
    private List<PicItem> mAllItemList;
    private Map<String, List<PicItem>> mItemMap;
    private List<String> mCatalogList;
    private String mCurrentCatalog = "";
    private Uri mTakePictureUri;
    private int perWidth;
    private int perHeight;

    public PicSelectorActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pic_selector_activity);
        this.mGridView = (GridView)this.findViewById(R.id.cnPicGridList);
        this.mBtnBack = (ImageButton)this.findViewById(R.id.cnPicBack);
        this.mBtnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PicSelectorActivity.this.finish();
            }
        });
        this.mPicType = (PicTypeBtn)this.findViewById(R.id.cnPicPicType);
        this.mPicType.init(this);
        this.mPicType.setEnabled(false);
        this.mCatalogView = this.findViewById(R.id.cnPicCatalogWindow);
        this.mCatalogListView = (ListView)this.findViewById(R.id.cnPicCatalogListview);
        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
        if(!DqUtils.checkPermissions(this, permissions)) {
//            PermissionCheckUtil.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            this.initView();
        }
    }

    private void initView() {
        updatePictureItems();
        mGridView.setAdapter(new GridViewAdapter());
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    PicItemHolder.itemList = new ArrayList();
                    if(mCurrentCatalog.isEmpty()) {
                        PicItemHolder.itemList.addAll(mAllItemList);
                    } else {
                        PicItemHolder.itemList.addAll((Collection)mItemMap.get(mCurrentCatalog));
                        Iterator var6 = mItemMap.keySet().iterator();

                        label32:
                        while(true) {
                            String key;
                            do {
                                if(!var6.hasNext()) {
                                    break label32;
                                }

                                key = (String)var6.next();
                            } while(key.equals(PicSelectorActivity.this.mCurrentCatalog));

                        }
                    }
                }
            }
        });
        this.mPicType.setEnabled(true);
        this.mPicType.setTextColor(this.getResources().getColor(R.color.rc_picsel_toolbar_send_text_normal));
        this.mPicType.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mCatalogView.setVisibility(View.VISIBLE);
            }
        });
        this.mCatalogListView.setAdapter(new CatalogAdapter());
        this.mCatalogListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catalog;
                if(position == 0) {
                    catalog = "";
                } else {
                    catalog = (String)mCatalogList.get(position - 1);
                }

                if(catalog.equals(mCurrentCatalog)) {
                    mCatalogView.setVisibility(View.GONE);
                } else {
                    mCurrentCatalog = catalog;
                    TextView textView = (TextView)view.findViewById(R.id.cnPicCatalogName);
                    mPicType.setText(textView.getText().toString());
                    mCatalogView.setVisibility(View.GONE);
                    ((CatalogAdapter)mCatalogListView.getAdapter()).notifyDataSetChanged();
                    ((GridViewAdapter)mGridView.getAdapter()).notifyDataSetChanged();
                }
            }
        });
        this.perWidth = (((WindowManager)this.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth() / 3;
        this.perHeight = (((WindowManager)this.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getHeight() / 5;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if(VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") ==
                    PackageManager.PERMISSION_GRANTED) {
                this.initView();
            } else {
                DqToast.showShort("您需要在设置中打开权限");
                this.finish();
            }
        }

        if(resultCode == REQUEST_CAMERA) {
            this.setResult(-1, data);
            this.finish();
        } else {
            switch(requestCode) {
                case REQUEST_CAMERA:
                    if(this.mTakePictureUri != null) {
                        PicItemHolder.itemList = new ArrayList();
                        PicItem item = new PicItem();
                        item.uri = this.mTakePictureUri.getPath();
                        PicItemHolder.itemList.add(item);
                        MediaScannerConnection.scanFile(this, new String[]{this.mTakePictureUri.getPath()}, (String[])null, new OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                updatePictureItems();
                            }
                        });
                    }
            }

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 4 && this.mCatalogView != null && this.mCatalogView.getVisibility() == View.VISIBLE) {
            this.mCatalogView.setVisibility(View.GONE);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Deprecated
    protected void requestCamera() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(!path.exists()) {
            path.mkdirs();
        }

        String name = System.currentTimeMillis() + ".jpg";
        File file = new File(path, name);
        this.mTakePictureUri = Uri.fromFile(file);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DIRECT_BOOT_AWARE);
        Uri uri = null;

        try {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".FileProvider", file);
        } catch (Exception var10) {
            var10.printStackTrace();
            throw new RuntimeException("Please check IMKit Manifest FileProvider config.");
        }

        Iterator var7 = resInfoList.iterator();

        while(var7.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo)var7.next();
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.putExtra("output", uri);
        this.startActivityForResult(intent, REQUEST_CAMERA);
    }
    //初始化所有图片
    private void updatePictureItems() {
        String[] projection = new String[]{"_data", "date_added"};
        String orderBy = "datetaken DESC";
        Cursor cursor = this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, (String)null, (String[])null, orderBy);
        this.mAllItemList = new ArrayList();
        this.mCatalogList = new ArrayList();
        this.mItemMap = new ArrayMap();
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    PicItem item = new PicItem();
                    item.uri = cursor.getString(0);
                    if(item.uri != null) {
                        File file = new File(item.uri);
                        if(file.exists() && file.length() != 0L) {
                            this.mAllItemList.add(item);
                            int last = item.uri.lastIndexOf("/");
                            if(last != -1) {
                                String catalog;
                                if(last == 0) {
                                    catalog = "/";
                                } else {
                                    int secondLast = item.uri.lastIndexOf("/", last - 1);
                                    catalog = item.uri.substring(secondLast + 1, last);
                                }

                                if(this.mItemMap.containsKey(catalog)) {
                                    ((List)this.mItemMap.get(catalog)).add(item);
                                } else {
                                    ArrayList<PicItem> itemList = new ArrayList();
                                    itemList.add(item);
                                    this.mItemMap.put(catalog, itemList);
                                    this.mCatalogList.add(catalog);
                                }
                            }
                        }
                    }
                } while(cursor.moveToNext());
            }

            cursor.close();
        }

    }
    //根据标签取图片
    private PicItem getItemAt(String catalog, int index) {
        if(!this.mItemMap.containsKey(catalog)) {
            return null;
        } else {
            int sum = 0;

            for(Iterator var4 = ((List)this.mItemMap.get(catalog)).iterator(); var4.hasNext(); ++sum) {
                PicItem item = (PicItem)var4.next();
                if(sum == index) {
                    return item;
                }
            }

            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == 0) {
                    if(permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE")) {
                        this.initView();
                    } else if(permissions[0].equals("android.permission.CAMERA")) {
                        this.requestCamera();
                    }
                } else if(permissions[0].equals("android.permission.CAMERA")) {
                    DqToast.showShort("您需要在设置中打开权限");
                } else {
                    DqToast.showShort("您需要在设置中打开权限");
                    this.finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
    @Override
    protected void onDestroy() {
        PicItemHolder.itemList = null;
        super.onDestroy();
    }

    public static class PicItemHolder {
        public static ArrayList<PicItem> itemList;

        public PicItemHolder() {
        }
    }

    public static class PicTypeBtn extends LinearLayout {
        TextView mText;

        public PicTypeBtn(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void init(Activity root) {
            this.mText = (TextView)root.findViewById(R.id.type_text);
        }

        public void setText(String text) {
            this.mText.setText(text);
        }

        public void setTextColor(int color) {
            this.mText.setTextColor(color);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if(this.isEnabled()) {
                switch(event.getAction()) {
                    case 0:
                        this.mText.setVisibility(INVISIBLE);
                        break;
                    case 1:
                        this.mText.setVisibility(VISIBLE);
                }
            }

            return super.onTouchEvent(event);
        }
    }

    public static class PicItem implements Parcelable {
        String uri;
        public static final Creator<PicItem> CREATOR = new Creator<PicItem>() {
            public PicItem createFromParcel(Parcel source) {
                return new PicItem(source);
            }

            public PicItem[] newArray(int size) {
                return new PicItem[size];
            }
        };

        public int describeContents() {
            return 0;
        }

        public PicItem() {
        }

        public PicItem(Parcel in) {
            this.uri = in.readString();
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.uri);
//            ParcelUtils.writeToParcel(dest, this.uri);
        }
    }
    //标签
    private class CatalogAdapter extends BaseAdapter {
        private LayoutInflater mInflater = getLayoutInflater();

        public CatalogAdapter() {
        }
        @Override
        public int getCount() {
            return mItemMap.size() + 1;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return (long)position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            CatalogAdapter.ViewHolder holder;
            if(convertView == null) {
                view = this.mInflater.inflate(R.layout.pic_selector_catalog_listview, parent, false);
                holder = new CatalogAdapter.ViewHolder();
                holder.image = (ImageView)view.findViewById(R.id.cnPicCatalogImage);
                holder.name = (TextView)view.findViewById(R.id.cnPicCatalogName);
                holder.number = (TextView)view.findViewById(R.id.cnPicCatalogNumber);
                view.setTag(holder);
            } else {
                holder = (CatalogAdapter.ViewHolder)convertView.getTag();
            }

            String path;
            if(holder.image.getTag() != null) {
                path = (String)holder.image.getTag();
                AlbumBitmapCacheHelper.getInstance().removePathFromShowlist(path);
            }

            int num = 0;
            String name;
            Bitmap bitmap;
            BitmapDrawable bd;
            if(position == 0) {
                if(PicSelectorActivity.this.mItemMap.size() == 0) {
                    holder.image.setImageResource(R.mipmap.pic_empty_pic);
                } else {
                    path = ((PicItem)((List)mItemMap.get(mCatalogList.get(0))).get(0)).uri;
                    AlbumBitmapCacheHelper.getInstance().addPathToShowlist(path);
                    holder.image.setTag(path);
                    bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(path, perWidth, perHeight, new AlbumBitmapCacheHelper.ILoadImageCallback() {
                        public void onLoadImageCallBack(Bitmap bitmap, String path1, Object... objects) {
                            if(bitmap != null) {
                                BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                                View v = mGridView.findViewWithTag(path1);
                                if(v != null) {
                                    v.setBackgroundDrawable(bd);
                                    CatalogAdapter.this.notifyDataSetChanged();
                                }

                            }
                        }
                    }, new Object[]{Integer.valueOf(position)});
                    if(bitmap != null) {
                        bd = new BitmapDrawable(getResources(), bitmap);
                        holder.image.setBackgroundDrawable(bd);
                    } else {
                        holder.image.setBackgroundResource(R.mipmap.grid_image_default);
                    }
                }

                name = getResources().getString(R.string.allpics);
                holder.number.setVisibility(View.GONE);
            } else {
                path = ((PicItem)((List)mItemMap.get(mCatalogList.get(position - 1))).get(0)).uri;
                name = (String)mCatalogList.get(position - 1);
                num = ((List)mItemMap.get(mCatalogList.get(position - 1))).size();
                holder.number.setVisibility(View.VISIBLE);
                AlbumBitmapCacheHelper.getInstance().addPathToShowlist(path);
                holder.image.setTag(path);
                bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(path, perWidth, perHeight, new AlbumBitmapCacheHelper.ILoadImageCallback() {
                    public void onLoadImageCallBack(Bitmap bitmap, String path1, Object... objects) {
                        if(bitmap != null) {
                            BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                            View v = mGridView.findViewWithTag(path1);
                            if(v != null) {
                                v.setBackgroundDrawable(bd);
                                CatalogAdapter.this.notifyDataSetChanged();
                            }

                        }
                    }
                }, new Object[]{Integer.valueOf(position)});
                if(bitmap != null) {
                    bd = new BitmapDrawable(getResources(), bitmap);
                    holder.image.setBackgroundDrawable(bd);
                } else {
                    holder.image.setBackgroundResource(R.mipmap.grid_image_default);
                }
            }

            holder.name.setText(name);
            holder.number.setText(String.format(getResources().getString(R.string.pic_catalog_number), new Object[]{Integer.valueOf(num)}));
            return view;
        }

        private class ViewHolder {
            ImageView image;
            TextView name;
            TextView number;
        }
    }
    //显示图片的item
    private class GridViewAdapter extends BaseAdapter {
        private LayoutInflater mInflater = getLayoutInflater();

        public GridViewAdapter() {
        }
        @Override
        public int getCount() {
            int sum = 0;
            String key;
            if(mCurrentCatalog.isEmpty()) {
                for(Iterator var2 = mItemMap.keySet().iterator(); var2.hasNext(); sum += ((List)mItemMap.get(key)).size()) {
                    key = (String)var2.next();
                }
            } else {
                sum += ((List)mItemMap.get(mCurrentCatalog)).size();
            }

            return sum;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final PicItem item;
            if(mCurrentCatalog.isEmpty()) {
                item = (PicItem)mAllItemList.get(position);
            } else {
                item = getItemAt(mCurrentCatalog, position);
            }

            View view = convertView;
            final GridViewAdapter.ViewHolder holder;
            if(convertView != null && convertView.getTag() != null) {
                holder = (GridViewAdapter.ViewHolder)convertView.getTag();
            } else {
                view = this.mInflater.inflate(R.layout.pic_selector_grid_item, parent, false);
                holder = new GridViewAdapter.ViewHolder();
                holder.image = (ImageView)view.findViewById(R.id.picSelectorItemImage);
                view.setTag(holder);
            }

            String path;
            if(holder.image.getTag() != null) {
                path = (String)holder.image.getTag();
                AlbumBitmapCacheHelper.getInstance().removePathFromShowlist(path);
            }
            path = item.uri;
            AlbumBitmapCacheHelper.getInstance().addPathToShowlist(path);
            holder.image.setTag(path);
            Bitmap bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(path, perWidth, perHeight,
                    new AlbumBitmapCacheHelper.ILoadImageCallback() {
                public void onLoadImageCallBack(Bitmap bitmap, String path1, Object... objects) {
                    if(bitmap != null) {
                        BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                        View v = mGridView.findViewWithTag(path1);
                        if(v != null) {
                            v.setBackgroundDrawable(bd);
                        }

                    }
                }
            }, new Object[]{Integer.valueOf(position)});
            if(bitmap != null) {
                BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                holder.image.setBackgroundDrawable(bd);
            } else {
                holder.image.setBackgroundResource(R.mipmap.grid_image_default);
            }
            holder.image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("localImgPath", item.uri);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            return view;
        }

        private class ViewHolder {
            ImageView image;
        }
    }
}
