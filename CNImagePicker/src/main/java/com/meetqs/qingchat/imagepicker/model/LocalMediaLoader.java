package com.meetqs.qingchat.imagepicker.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.meetqs.qingchat.imagepicker.R;
import com.meetqs.qingchat.imagepicker.config.PictureConfig;
import com.meetqs.qingchat.imagepicker.config.PictureMimeType;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.entity.LocalMediaFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * author：luck
 * project：LocalMediaLoader
 * package：com.luck.picture.ui
 * email：893855882@qq.com
 * data：16/12/31
 * modify 2018/6/29 方志，仿微信页面，加入所有视频文件夹
 */

public class LocalMediaLoader {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
    private static final String DURATION = "duration";
    private static final String NOT_GIF = "!='image/gif'";
    private static final int AUDIO_DURATION = 500;// 过滤掉小于500毫秒的录音
    private int type = PictureConfig.TYPE_IMAGE;
    private FragmentActivity activity;
    private boolean isGif;
    private long videoMaxS = 0;
    private long videoMinS = 0;
    private List<LocalMediaFolder> imageFolders = new ArrayList<>();

    // 媒体文件数据库字段
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            MediaStore.MediaColumns.SIZE,
            DURATION};

//    String[] projection = new String[]{"_data", "date_added"};

    // 图片
    private static final String SELECTION = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static final String SELECTION_NOT_GIF = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF;

    // 查询条件(音视频)
    private static String getSelectionArgsForSingleMediaCondition(String time_condition) {
        return MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + time_condition;
    }

    // 全部模式下条件
    private static String getSelectionArgsForAllMediaCondition(String time_condition, boolean isGif) {
        return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + (isGif ? "" : " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                + " OR "
                + (MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + time_condition) + ")"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    }

    // 获取图片or视频
    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };

    /**
     * 获取指定类型的文件
     *
     * @param mediaType 类型
     * @return String[]
     */
    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }

    public LocalMediaLoader(FragmentActivity activity, int type, boolean isGif, long videoMaxS, long videoMinS) {
        this.activity = activity;
        this.type = type;
        this.isGif = isGif;
        this.videoMaxS = videoMaxS;
        this.videoMinS = videoMinS;

    }

    public void loadAllMedia(final LocalMediaLoadListener imageLoadListener) {
        activity.getSupportLoaderManager().initLoader(type, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @NonNull
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        CursorLoader cursorLoader = null;
                        switch (id) {
                            //只获取图片和视频
                            case PictureConfig.TYPE_ALL:
                                String all_condition = getSelectionArgsForAllMediaCondition(getDurationCondition(0, 0), isGif);
                                cursorLoader = new CursorLoader(
                                        activity, QUERY_URI,
                                        PROJECTION, all_condition,
                                        SELECTION_ALL_ARGS, ORDER_BY);
                                break;
                            case PictureConfig.TYPE_IMAGE:
                                // 只获取图片
                                String[] MEDIA_TYPE_IMAGE = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
                                cursorLoader = new CursorLoader(
                                        activity, QUERY_URI,
                                        PROJECTION, isGif ? SELECTION : SELECTION_NOT_GIF, MEDIA_TYPE_IMAGE
                                        , ORDER_BY);
                                break;
                            case PictureConfig.TYPE_VIDEO:
                                // 只获取视频
                                String video_condition = getSelectionArgsForSingleMediaCondition(getDurationCondition(0, 0));
                                String[] MEDIA_TYPE_VIDEO = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
                                cursorLoader = new CursorLoader(
                                        activity, QUERY_URI, PROJECTION, video_condition, MEDIA_TYPE_VIDEO
                                        , ORDER_BY);
                                break;
                            case PictureConfig.TYPE_AUDIO:
                                String audio_condition = getSelectionArgsForSingleMediaCondition(getDurationCondition(0, AUDIO_DURATION));
                                String[] MEDIA_TYPE_AUDIO = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO);
                                cursorLoader = new CursorLoader(
                                        activity, QUERY_URI, PROJECTION, audio_condition, MEDIA_TYPE_AUDIO
                                        , ORDER_BY);
                                break;
                        }
                        return cursorLoader;
                    }

                    @Override
                    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                        LocalMediaFolder allImageFolder = new LocalMediaFolder();
                        List<LocalMedia> latelyImages = new ArrayList<>();
                        List<LocalMedia> allVideos = new ArrayList<>();
                        if (null == data) {
                            // 如果没有相册
                            if (null != imageLoadListener) {
                                imageLoadListener.loadComplete(imageFolders);
                            }
                            return;
                        }
                        try {
                            if (data.isClosed()) {
                                return;
                            }
                            int count = data.getCount();
                            if (count > 0) {
                                data.moveToFirst();
                                do {
                                    String path = "";
                                    String pictureType = "";
                                    int width = 0;
                                    int height = 0;
                                    long fileSize = 0;
                                    int duration = 0;
                                    try {
                                        path = data.getString(data.getColumnIndexOrThrow(PROJECTION[1]));
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        ToastManage.s(activity, "路径型为空..." + e.getMessage());
                                    }

                                    try {
                                        pictureType = data.getString(data.getColumnIndexOrThrow(PROJECTION[2]));
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        ToastManage.s(activity, "图片类型为空..." + e.getMessage());
                                    }

                                    try {
                                        width = data.getInt(data.getColumnIndexOrThrow(PROJECTION[3]));
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        ToastManage.s(activity, "宽类型为空..." + e.getMessage());
                                    }

                                    try {
                                        height = data.getInt(data.getColumnIndexOrThrow(PROJECTION[4]));
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        ToastManage.s(activity, "高类型为空..." + e.getMessage());
                                    }

                                    try {
                                        fileSize = data.getInt(data.getColumnIndexOrThrow(PROJECTION[5]));
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        ToastManage.s(activity, "文件大小类型为空..." + e.getMessage());
                                    }

                                    if (!TextUtils.isEmpty(pictureType) && pictureType.startsWith("video")) {
                                        try {
                                            duration = data.getInt(data.getColumnIndexOrThrow(PROJECTION[6]));
                                        } catch (Exception e) {
                                            e.printStackTrace();
//                                            ToastManage.s(activity, "时长类型为空..." + e.getMessage());
                                        }
                                    }

                                    LocalMedia image = new LocalMedia
                                            (path, duration, type, pictureType, width, height, fileSize);

                                    //添加视频文件
                                    if (!TextUtils.isEmpty(pictureType) && pictureType.startsWith("video")) {
                                        allVideos.add(image);
                                    }

                                    LocalMediaFolder folder = getImageFolder(path);
                                    List<LocalMedia> images = folder.getImages();
                                    images.add(image);
                                    folder.setImageNum(folder.getImageNum() + 1);
                                    latelyImages.add(image);
                                } while (data.moveToNext());

                                if (latelyImages.size() > 0) {
                                    sortFolder(imageFolders);
                                    imageFolders.add(0, allImageFolder);
                                    allImageFolder.setFirstImagePath(latelyImages.get(0).getPath());
                                    String title = type == PictureMimeType.ofAudio() ?
                                            activity.getString(R.string.picture_all_audio)
                                            : activity.getString(R.string.picture_camera_roll);
                                    allImageFolder.setName(title);
                                    allImageFolder.setImageNum(latelyImages.size());
                                    allImageFolder.setImages(latelyImages);
                                }

                                if (allVideos.size() > 0) {
                                    //设置视频数据
                                    LocalMediaFolder videoFolder = new LocalMediaFolder();
                                    videoFolder.setName(activity.getString(R.string.all_video));
                                    String path = allVideos.get(0).getPath();
                                    videoFolder.setFirstImagePath(path);
                                    videoFolder.setImageNum(allVideos.size());
                                    videoFolder.setImages(allVideos);
                                    imageFolders.add(1, videoFolder);
                                }

                                if (null != imageLoadListener) {
                                    imageLoadListener.loadComplete(imageFolders);
                                }
                            } else {
                                // 如果没有相册
                                if (null != imageLoadListener) {
                                    imageLoadListener.loadComplete(imageFolders);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            ToastManage.s(activity, "Error..." + e.getMessage());
                            if (null != imageLoadListener) {
                                imageLoadListener.onError(e);
                            }
                        } finally {
                            try {
                                if (data != null) {
                                    data.close();
                                }
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }

                    @Override
                    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    }
                });
    }

    /**
     * 文件夹数量进行排序
     *
     * @param imageFolders 文件夹
     */
    private void sortFolder(List<LocalMediaFolder> imageFolders) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, new Comparator<LocalMediaFolder>() {
            @Override
            public int compare(LocalMediaFolder lhs, LocalMediaFolder rhs) {
                if (lhs.getImages() == null || rhs.getImages() == null) {
                    return 0;
                }
                int lsize = lhs.getImageNum();
                int rsize = rhs.getImageNum();
                return lsize == rsize ? 0 : (lsize < rsize ? 1 : -1);
            }
        });
    }

    /**
     * 创建相应文件夹
     *
     * @param path 路径
     * @return LocalMediaFolder
     */
    private LocalMediaFolder getImageFolder(String path) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();
        for (LocalMediaFolder folder : imageFolders) {
            // 同一个文件夹下，返回自己，否则创建新文件夹
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(path);
        imageFolders.add(newFolder);
        return newFolder;
    }

    /**
     * 获取视频(最长或最小时间)
     *
     * @param exMaxLimit 最大时长
     * @param exMinLimit 最小时长
     * @return String
     */
    private String getDurationCondition(long exMaxLimit, long exMinLimit) {
        long maxS = videoMaxS == 0 ? Long.MAX_VALUE : videoMaxS;
        if (exMaxLimit != 0) maxS = Math.min(maxS, exMaxLimit);

        return String.format(Locale.CHINA, "%d <%s duration and duration <= %d",
                Math.max(exMinLimit, videoMinS),
                Math.max(exMinLimit, videoMinS) == 0 ? "" : "=",
                maxS);
    }


    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> folders);
        void onError(Exception exception);
    }
}
