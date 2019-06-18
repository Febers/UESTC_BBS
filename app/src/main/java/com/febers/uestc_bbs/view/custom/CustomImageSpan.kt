package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class CustomImageSpan: ImageSpan {

    constructor(context: Context, b: Bitmap): super(context, b)

    constructor(d: Drawable): super(d)

}