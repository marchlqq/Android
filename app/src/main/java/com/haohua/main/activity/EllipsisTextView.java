package com.haohua.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oppo.im.R;
import com.oppo.im.app.IMApplication;
import com.oppo.im.frameworks.xlogger.XLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 增加省略号控件
 * <p>
 * eg：马豪华制作的省略号代码，包含后缀豪华
 * <p>设置后置：setEndStr("豪华");</p>
 * <p>展示成：马豪华制作{textView宽度}...豪华</p>
 * <p>
 * <p>注意，试用前，需要设置：setEndStr</p>
 */
@SuppressLint("AppCompatCustomView")
public class EllipsisTextView extends TextView {

    // 裁剪后半段String
    private String endStr;

    // 同样的str，只计算一次，之后不处理
    private String subStr;

    private String headStr = "";

    private boolean isHandlerData = false;

    public static final String ELLIPIS_STR = "...";

    public EllipsisTextView(Context context) {
        super(context);
    }

    public EllipsisTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsisTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
        subStr = "";
    }

    private SearchElement searchElement;

    public void setHandlerData(String data, SearchElement searchElement) {
        isHandlerData = true;
        this.searchElement = searchElement;
    }

    public void setHeadStr(String headStr) {
        this.headStr = headStr;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            subTextContext(this);
        }
    }

    private String highLightStr;

    private void subTextContext(TextView textView) {
        if (!isHandlerData) {
            return;
        }
        String originText = textView.getText().toString();

//        || originText.equals(subStr)
        if (originText == null || searchElement == null) {
            return;
        }

        int strLenth = originText.length();

        XLogger.d("im-search start index = " + searchElement.startIndex + ", end index = " + searchElement.endIndex);
        // 是否需要高亮
        if (searchElement.startIndex == -1 || searchElement.endIndex == -1 || searchElement.startIndex >= searchElement.endIndex || searchElement.endIndex > strLenth -1) {
            XLogger.e("im-search error 1");
            return;
        }

        //获取原文字长度
        float originTextWidth = textView.getPaint().measureText(originText);
        //获取控件长度
        float textViewWidth = textView.getMeasuredWidth();

        // 高亮字符串
        highLightStr = originText.substring(searchElement.startIndex, searchElement.endIndex);
        // 未获取到控件宽度，不进行计算
        if (textViewWidth < 10) {
            XLogger.e("im-search error 2");
            return;
        }

        String subShowText;
        //控件长度大于文字长度 直接显示
        if (textViewWidth >= originTextWidth) {
            XLogger.e("im-search origin text");
//            textView.setText(originText);
            subShowText = originText;
        } else {
            int lastIndexOfPoint = 0;
            lastIndexOfPoint = (int) ((originText.length() * textViewWidth / originTextWidth));

            if (lastIndexOfPoint > originText.length()) {
                lastIndexOfPoint = originText.length();
            }
            //获取指定省略位置
//            int lastIndexOfPoint = originText.lastIndexOf(endStr);
//            if (lastIndexOfPoint == -1) {
//                //找不到 直接显示
//                textView.setText(originText);
//            } else {
            //找到了 对字符串切分

            String showText = originText.substring(searchElement.startIndex, searchElement.endIndex);
            String otherSuffixText = ELLIPIS_STR + ELLIPIS_STR + headStr + ELLIPIS_STR;

            // 其他展示字符宽度
            float otherSuffixWidth = textView.getPaint().measureText(otherSuffixText);

            float showFixWidth = textView.getPaint().measureText(showText);

            //后缀太长 不处理
            if (otherSuffixWidth > textViewWidth) {
                textView.setText(originText);
                return;
            }

            int startSubI = searchElement.startIndex, endSubI = searchElement.endIndex;
            int startI = 0, endI = 0;
            //每减少前缀一个字符都去判断是否能塞满控件
            while (textViewWidth - showFixWidth > otherSuffixWidth) {
                if (startI <= endI) {
                    startI++;
                    if (startSubI == 0) {
                        if (endSubI == strLenth - 1) {
                            break;
                        }
                        endSubI++;
                    } else {
                        startSubI--;
                    }
                } else {
                    endI++;
                    if (endSubI == strLenth - 1) {
                        if (startSubI == 0) {
                            break;
                        }
                        startSubI--;
                    } else {
                        endSubI++;
                    }
                }

                if (startSubI < 0 || endSubI > strLenth - 1) {
                    // 计算有误，不处理
                    break;
                }
                showText = originText.substring(startSubI, endSubI);
                XLogger.e("im-search showText = " + showText);
                //关键
                showFixWidth = textView.getPaint().measureText(showText);
            }

            subStr = originText;

            StringBuilder sb = new StringBuilder();
            sb.append(headStr);
            if (startSubI == 0) {
                sb.append(ELLIPIS_STR);
            }
            sb.append(showText);
            if (endSubI == strLenth -1) {
                sb.append(ELLIPIS_STR);
            }

            subShowText = sb.toString();


//            //前缀
//            String prefixText = originText.substring(0, lastIndexOfPoint);
//
//            //后缀 添加省略符号 "..."+endStr
//            String suffixText = "...";// + endStr;
//
//            float prefixWidth = textView.getPaint().measureText(prefixText);
//            float suffixWidth = textView.getPaint().measureText(suffixText);
//
//            //后缀太长 不处理
//            if (suffixWidth > textViewWidth) {
//                textView.setText(originText);
//                return;
//            }
//
//            //每减少前缀一个字符都去判断是否能塞满控件
//            while (textViewWidth - prefixWidth < suffixWidth) {
//                prefixText = prefixText.substring(0, prefixText.length() - 1);
//                //关键
//                prefixWidth = textView.getPaint().measureText(prefixText);
//            }

//            subStr = originText;
//            //能塞满
//            textView.setText((prefixText + suffixText));
//            }
        }

        SpannableString ssLight = new SpannableString(subShowText);
        XLogger.d("im-search highLightStr = " + highLightStr);
        Pattern p = Pattern.compile(highLightStr);
        Matcher m = p.matcher(ssLight);
        while (m.find()) {
            int startIndex = m.start();
            int endIndex = m.end();
            ssLight.setSpan(new ForegroundColorSpan(ContextCompat.getColor(IMApplication.getContext(), R.color.materialcolorpicker__dribble)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //能塞满
        textView.setText(ssLight);
    }
}