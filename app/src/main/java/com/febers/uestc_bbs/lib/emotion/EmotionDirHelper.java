package com.febers.uestc_bbs.lib.emotion;

import com.febers.uestc_bbs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 该助手类用来管理表情包的目录
 * 目录位于assert文件夹中，
 * 另外提供封面的图片资源
 */
public class EmotionDirHelper {

    public static List<String> getEmotionFileDir() {
        List<String> fileDirs = new ArrayList<>();
        fileDirs.add("second");
        fileDirs.add("lv");
        fileDirs.add("tu");
        fileDirs.add("yang");
        return fileDirs;
    }

    public static List<String> getEmotionConfigFile() {
        List<String> configDirs = new ArrayList<>();
        configDirs.add("alu.xml");
        configDirs.add("lv.xml");
        configDirs.add("tu.xml");
        configDirs.add("yang.xml");
        return configDirs;
    }

    public static int getTabIcon(int position) {
        if (position == 0) {
            return R.drawable.ic_forum_gray_airplane;
        }
        if (position == 1) {
            return R.drawable.ic_emotion_gray;
        }
        if (position == 2) {
            return R.drawable.ic_forum_gray_animal;
        }
        if (position == 3) {
            return R.drawable.ic_forum_gray_camera;
        }
        return R.drawable.ic_emotion_gray;
    }
}
