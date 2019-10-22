package com.febers.uestc_bbs.lib.baseAdapter.interfaces;

import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnMultiItemClickListeners<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position, int viewType);
}
