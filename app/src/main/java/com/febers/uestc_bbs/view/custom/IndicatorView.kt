package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.utils.colorAccent

/**
 * 自定义的指示器
 *
 *          .,:,,,                                        .::,,,::.
 *        .::::,,;;,                                  .,;;:,,....:i:
 *        :i,.::::,;i:.      ....,,:::::::::,....   .;i:,.  ......;i.
 *        :;..:::;::::i;,,:::;:,,,,,,,,,,..,.,,:::iri:. .,:irsr:,.;i.
 *        ;;..,::::;;;;ri,,,.                    ..,,:;s1s1ssrr;,.;r,
 *        :;. ,::;ii;:,     . ...................     .;iirri;;;,,;i,
 *        ,i. .;ri:.   ... ............................  .,,:;:,,,;i:
 *        :s,.;r:... ....................................... .::;::s;
 *        ,1r::. .............,,,.,,:,,........................,;iir;
 *        ,s;...........     ..::.,;:,,.          ...............,;1s
 *       :i,..,.              .,:,,::,.          .......... .......;1,
 *      ir,....:rrssr;:,       ,,.,::.     .r5S9989398G95hr;. ....,.:s,
 *     ;r,..,s9855513XHAG3i   .,,,,,,,.  ,S931,.,,.;s;s&BHHA8s.,..,..:r:
 *    :r;..rGGh,  :SAG;;G@BS:.,,,,,,,,,.r83:      hHH1sXMBHHHM3..,,,,.ir.
 *   ,si,.1GS,   sBMAAX&MBMB5,,,,,,:,,.:&8       3@HXHBMBHBBH#X,.,,,,,,rr
 *   ;1:,,SH:   .A@&&B#&8H#BS,,,,,,,,,.,5XS,     3@MHABM&59M#As..,,,,:,is,
 *  .rr,,,;9&1   hBHHBB&8AMGr,,,,,,,,,,,:h&&9s;   r9&BMHBHMB9:  . .,,,,;ri.
 *  :1:....:5&XSi;r8BMBHHA9r:,......,,,,:ii19GG88899XHHH&GSr.      ...,:rs.
 *  ;s.     .:sS8G8GG889hi.        ....,,:;:,.:irssrriii:,.        ...,,i1,
 *  ;1,         ..,....,,isssi;,        .,,.                      ....,.i1,
 *  ;h:               i9HHBMBBHAX9:         .                     ...,,,rs,
 *  ,1i..            :A#MBBBBMHB##s                             ....,,,;si.
 *  .r1,..        ,..;3BMBBBHBB#Bh.     ..                    ....,,,,,i1;
 *   :h;..       .,..;,1XBMMMMBXs,.,, .. :: ,.               ....,,,,,,ss.
 *    ih: ..    .;;;, ;;:s58A3i,..    ,. ,.:,,.             ...,,,,,:,s1,
 *    .s1,....   .,;sh,  ,iSAXs;.    ,.  ,,.i85            ...,,,,,,:i1;
 *     .rh: ...     rXG9XBBM#M#MHAX3hss13&&HHXr         .....,,,,,,,ih;
 *      .s5: .....    i598X&&A&AAAAAA&XG851r:       ........,,,,:,,sh;
 *      . ihr, ...  .         ..                    ........,,,,,;11:.
 *         ,s1i. ...  ..,,,..,,,.,,.,,.,..       ........,,.,,.;s5i.
 *          .:s1r,......................       ..............;shs,
 *          . .:shr:.  ....                 ..............,ishs.
 *              .,issr;,... ...........................,is1s;.
 *                 .,is1si;:,....................,:;ir1sr;,
 *                    ..:isssssrrii;::::::;;iirsssssr;:..
 *                         .,::iiirsssssssssrri;;:.
*/

class IndicatorView : View {

    private var indicationSize = -1
    private var curIndex = -1
    private lateinit var mPaint: Paint

    private var radius = 0
    private var bgColor = Color.GRAY
    private var selectedColor = Color.BLUE

    private val bgView = RectF(0f,0f,0f,0f)
    private val selected = RectF(0f,0f,0f,0f)

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mPaint = Paint()
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView)
            radius = ta.getDimensionPixelOffset(R.styleable.IndicatorView_radius, 0)
            bgColor = ta.getColor(R.styleable.IndicatorView_background_color, Color.GRAY)
            ta.recycle()
            //selectedColor = ta.getColor(R.styleable.IndicatorView_selected_color, Color.BLUE)
            selectedColor = colorAccent()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        //画圆角矩形
        mPaint.style = Paint.Style.FILL//充满
        mPaint.color = bgColor
        mPaint.isAntiAlias = true// 设置画笔的锯齿效果
        bgView.set(0f,0f,width.toFloat(), height.toFloat()) // 设置个新的长方形
        canvas.drawRoundRect(bgView, radius.toFloat(), radius.toFloat(), mPaint)//第二个参数是x半径，第三个参数是y半径
        if (indicationSize == -1) {
            return
        }
        if (curIndex == -1) {
            return
        }
        val itemW = width / indicationSize
        val index = curIndex

        mPaint.color = selectedColor
        val left = index * itemW
        selected.set(left.toFloat(), 0f, (left + itemW).toFloat(), height.toFloat())
        canvas.drawRoundRect(selected, radius.toFloat(), radius.toFloat(), mPaint)//第二个参数是x半径，第三个参数是y半径
    }

    fun setMaxSize(size: Int) {
        indicationSize = size
    }

    fun setCurIndex(index: Int) {
        curIndex = index
        postInvalidate()
    }
}

