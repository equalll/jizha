package com.example.sony.jizha.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by sony on 15-9-12.
 */
public class Pinyin4j {

    /**
     * 将汉字转换为全拼
     *
     * @param src         你的需要转换的汉字
     * @param isUPPERCASE 是否转换为大写的拼音； true:转换为大写；false：转换为小写
     * @return res 转换后的结果
     */
    public static String getPingYin(String src, boolean isUPPERCASE) {

        char[] charArray = null;

        charArray = src.toCharArray();

        String[] strArray = new String[charArray.length];

        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        if (isUPPERCASE) {
            // 大写设置
            hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        } else {
            // 小写设置
            hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        }

        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        String res = "";
        int t0 = charArray.length;

        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    strArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], hanyuPinyinOutputFormat);
                    res += strArray[0];
                } else {
                    res += Character.toString(charArray[i]);
                }
            }
            return res;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return res;
    }

    /**
     * 返回中文的首字母
     *
     * @param str 你需要转换的汉字
     * @return res 转换后的结果
     */
    public static String getPinYinHeadChar(String str) {

        String res = "";
        for (int j = 0; j < str.length(); j++) {

            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);

            if (pinyinArray != null) {
                res += pinyinArray[0].charAt(0);
            } else {
                res += word;
            }
        }
        return res;
    }
}
