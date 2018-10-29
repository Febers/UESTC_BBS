package com.febers.uestc_bbs.module.setting

import android.content.Context
import com.scwang.smartrefresh.header.*
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader

const val HEAD_POSITION = "header_position"
const val REFRESH_HEADER_CODE = "header_code"

const val HEAD_CLASSIC = 0
const val HEAD_BEZIER_CIRCLE = 1
const val HEAD_DELIVERY = 2
const val HEAD_MATERIAL = 3
const val HEAD_TAURUS = 4
const val HEAD_WATER_DROP =5
const val HEAD_BEZIER_RADAR = 6

const val HEAD_COUNT = 7

class RefreshViewHelper(val context: Context) {

    private val headers: Map<Int, RefreshHeader> =
            mapOf(  HEAD_CLASSIC       to ClassicsHeader(context),
                    HEAD_DELIVERY      to DeliveryHeader(context),
                    HEAD_MATERIAL      to MaterialHeader(context),
                    HEAD_TAURUS        to TaurusHeader(context),
                    HEAD_BEZIER_CIRCLE to BezierCircleHeader(context),
                    HEAD_WATER_DROP    to WaterDropHeader(context),
                    HEAD_BEZIER_RADAR  to BezierRadarHeader(context))

    fun getHeader(position: Int): RefreshHeader {
        return when(position) {
            HEAD_CLASSIC -> ClassicsHeader(context)
            HEAD_DELIVERY ->  DeliveryHeader(context)
            HEAD_MATERIAL ->  MaterialHeader(context)
            HEAD_TAURUS ->  TaurusHeader(context)
            HEAD_BEZIER_CIRCLE -> BezierCircleHeader(context)
            HEAD_WATER_DROP -> WaterDropHeader(context)
            HEAD_BEZIER_RADAR -> BezierRadarHeader(context)
            else -> ClassicsHeader(context)
        }
    }
}