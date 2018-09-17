package com.febers.uestc_bbs.utils

import com.febers.uestc_bbs.base.*

object BlockUtils {

    fun getBlockIdByPosition(group: Int, position: Int): String {
        var fid = "25"
        if (group == COMPUS_GROUP) {
            fid = when(position) {
                0 -> "174";1 -> "20";2 -> "219"
                3 -> "45";4 -> "309";5 -> "236"
                6 -> "225";7 -> "109";8 -> "382"
                9 -> "17";10 -> "237";11 -> "326"
                else -> fid
            }
        }
        if (group == TECH_GROUP) {
            fid = when(position) {
                0 -> "316";1 -> "258";2 -> "211"
                3 -> "272";4 -> "66";5 -> "108"
                6 -> "99";7 -> "70";8 -> "121"
                else -> fid
            }
        }
        if (group == PLAY_GROUP) {
            fid = when(position) {
                0 -> "25";1 -> "370";2 -> "244"
                3 -> "55";4 -> "115";5 -> "334"
                6 -> "74";7 -> "114";8 -> "118"
                9 -> "149";10 -> "140";11 -> "312"
                else -> fid
            }
        }
        if (group == MAKET_GRUOP) {
            fid = when(position) {
                0 -> "61";1 -> "255";2 -> "111"
                else -> fid
            }
        }
        if (group == MANAGE_GRUOP) {
            fid = when(position) {
                0 -> "2";1 -> "46"
                else -> fid
            }
        }
        return fid
    }
}