package com.wd.daquan.common.utils;

import com.wd.daquan.model.bean.Friend;
import java.util.Comparator;


/**
 * @author
 */
public class PinyinComparator implements Comparator<Friend> {

    public static PinyinComparator instance = null;

    public static PinyinComparator getInstance() {
        if (instance == null) {
            instance = new PinyinComparator();
        }
        return instance;
    }

    public int compare(Friend o1, Friend o2) {
        if (o1.letters.equals("@")
                || o2.letters.equals("#")) {
            return -1;
        } else if (o1.letters.equals("#")
                || o2.letters.equals("@")) {
            return 1;
        } else {
            return o1.letters.compareTo(o2.letters);
        }
    }

}
