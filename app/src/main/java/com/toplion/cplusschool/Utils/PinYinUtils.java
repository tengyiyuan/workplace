package com.toplion.cplusschool.Utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtils {
    public static String getPinYin(String src) {

        char[] t1 = null;

        t1 = src.toCharArray();

        // System.out.println(t1.length);

        String[] t2 = new String[t1.length];

        // System.out.println(t2.length);

        // 设置汉字拼音输出的格式

        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        t3.setVCharType(HanyuPinyinVCharType.WITH_V);

        String t4 = "";

        int t0 = t1.length;

        try {

            for (int i = 0; i < t0; i++) {

                // 判断是否为汉字字符

                // System.out.println(t1[i]);

                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中

                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后

                } else {

                    // 如果不是汉字字符，直接取出字符并连接到字符串t4后

                    t4 += Character.toString(t1[i]);

                }

            }

        } catch (BadHanyuPinyinOutputFormatCombination e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return t4;

    }

    public static String getPinYinHeadChar(String str) {

        String convert = "";

        for (int j = 0; j < str.length(); j++) {

            char word = str.charAt(j);

            // 提取汉字的首字母

            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);

            if (pinyinArray != null) {

                convert += pinyinArray[0].charAt(0);

            } else {

                convert += word;

            }

        }

        return convert;

    }

    public static String getCnASCII(String cnStr) {

        StringBuffer strBuf = new StringBuffer();

        // 将字符串转换成字节序列

        byte[] bGBK = cnStr.getBytes();

        for (int i = 0; i < bGBK.length; i++) {

            // System.out.println(Integer.toHexString(bGBK[i] & 0xff));

            // 将每个字符转换成ASCII码

            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));

        }

        return strBuf.toString();

    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (char curchar : input) {
                if (java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                } else
                    output += java.lang.Character.toString(curchar);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char curchar : arr) {
            if (curchar > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(curchar);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

}
