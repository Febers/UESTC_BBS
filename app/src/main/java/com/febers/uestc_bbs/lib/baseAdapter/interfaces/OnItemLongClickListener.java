package com.febers.uestc_bbs.lib.baseAdapter.interfaces;

import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder;

/**
 * Author: SheHuan
 * Time: 2019/6/13 16:48
 */
public interface OnItemLongClickListener<T> {
    void onItemLongClick(ViewHolder viewHolder, T data, int position);
}
