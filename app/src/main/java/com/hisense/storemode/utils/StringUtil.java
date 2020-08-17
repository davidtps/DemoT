package com.hisense.storemode.utils;

import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 主要封装了一些常用的字符串操作方法
 */

public final class StringUtil {
    private StringUtil() {
        throw new RuntimeException("…（⊙＿⊙；）…");
    }

    /**
     * 去除数字最后的0
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 返回字符串长度
     *
     * @param str
     * @return
     */
    public static int strLength(String str) {
        int len = 0;
        if (str != null) {
            return str.length();
        }
        return len;
    }

    /**
     * 判断字符串是否为空或长度为0
     *
     * @param str 需要判断的字符串
     * @return 为空则返回true
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.trim().length() == 0 || str.toUpperCase().equals("NULL");
    }

    /**
     * 半角转换为全角
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 判断字符串不为null或""
     *
     * @param str 需要判断的字符串
     * @return 不为空则返回true
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空或长度为0或由空白字符组成
     *
     * @param str 需要判断的字符串
     * @return 如果为空白字符串则返回true
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    /**
     * 判断字符串是否非空白
     *
     * @param str 需要判断的字符串
     * @return 如果不为空白字符串则返回true
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 去掉字符串左右两边的空格
     *
     * @param str
     * @return 如果字符串为null， 怎返回null
     * 否则返回去两端空白后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 将null转成空字符串""
     *
     * @param str
     * @return 如果字符串为null， 将字符串转成""
     * 不为null， 则返回员字符串
     */
    public static String nullToBlank(String str) {
        return str == null ? "" : str;
    }

    /**
     * 判断两个字符串内容是否相同
     *
     * @param actualStr
     * @param expectedStr
     * @return 如果actualStr和expectedStr相同则返回true
     */
    public static boolean equals(String actualStr, String expectedStr) {
        return actualStr == null ? expectedStr == null : actualStr
                .equals(expectedStr);
    }

    /**
     * 判断两个字符串内容是否相同(不区分大小写)
     *
     * @param actualStr
     * @param expectedStr
     * @return 如果actualStr和expectedStr相同则返回true
     */
    public static boolean equalsIgnoreCase(String actualStr, String expectedStr) {
        return actualStr == null ? expectedStr == null : actualStr
                .equalsIgnoreCase(expectedStr);
    }

    /**
     * 字符串中是否包含某字符
     *
     * @param str
     * @param searchChar
     * @return 如果字符串为空， 返回false
     * 如果字符串中包含searchChar字符返回true， 否则返回false
     */
    public static boolean contains(String str, char searchChar) {
        if (isEmpty(str))
            return false;
        else
            return str.indexOf(searchChar) >= 0;
    }

    /**
     * 字符串中是否包含某字符
     *
     * @param str
     * @param searchStr
     * @return 如果str或则searchStr为空， 返回false
     * 如果str字符串中包含searchChar字符返回true， 否则返回false
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;
        else
            return str.indexOf(searchStr) >= 0;
    }

    /**
     * 字符串中是否包含某字符(不区分大小写)
     *
     * @param str
     * @param searchStr
     * @return 如果str或则searchStr为空， 返回false
     * 如果str字符串中包含searchChar字符返回true， 否则返回false
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++)
            if (str.regionMatches(true, i, searchStr, 0, len))
                return true;

        return false;
    }

    /**
     * 从字符串左边开始截取指定长度的字符串
     *
     * @param str
     * @param len 指定长度
     * @return 如果字符串为空，则返回null
     * 如果len小于0，则返回""
     * 如果字符串的长度小于等于len，返回原字符串
     * 否则返回str.substring(0, len)
     */
    public static String subLeft(String str, int len) {
        if (str == null)
            return null;
        if (len < 0)
            return "";
        if (str.length() <= len)
            return str;
        else
            return str.substring(0, len);
    }

    /**
     * 从字符串右边开始截取指定长度的字符串
     *
     * @param str
     * @param len 指定长度
     *            如果字符串为空，则返回null
     *            如果len小于0，则返回""
     *            如果字符串的长度小于等于len，返回原字符串
     *            否则返回str.substring(str.length() - len)
     */
    public static String subRight(String str, int len) {
        if (str == null)
            return null;
        if (len < 0)
            return "";
        if (str.length() <= len)
            return str;
        else
            return str.substring(str.length() - len);
    }

    /**
     * 从指定位置开始截取指定长度的字符串
     *
     * @param str 目标字符串
     * @param pos 位置索引
     * @param len 要截取的长度
     * @return 如果字符串为空，则返回null
     * 如果pos小于0，则将pos置为0
     * 如果pos大于字符串的长度，则返回""
     * 如果len小于0，则返回""
     * 如果字符串的长度小于等于len，返回原字符串
     * 否则返回str.substring(str.length() - len)
     */
    public static String subMid(String str, int pos, int len) {
        if (str == null)
            return null;
        if (len < 0 || pos > str.length())
            return "";
        if (pos < 0)
            pos = 0;
        if (str.length() <= pos + len)
            return str.substring(pos);
        else
            return str.substring(pos, pos + len);
    }

    /**
     * 删除指定字符串
     *
     * @param str    目标字符串
     * @param remove 需要删除的字符串
     * @return 如果目标字符串或者要删除的字符串为空， 则返回原字符串
     * 否则返回去除remove之后的字符串
     */
    public static String remove(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove))
            return str;
        else
            return replace(str, remove, "", -1);
    }

    /**
     * 替换字符串，只替换一次
     *
     * @param text         目标字符串
     * @param searchString 需要替换的字符串
     * @param replacement  替换的内容
     * @return 如果目标字符串或者需要替换的字符串为空，或者替换的内容为null， 就返回原目标字符串
     * 否则返回替换内容后的字符串
     */
    public static String replaceOnce(String text, String searchString,
                                     String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * 替换字符串(最多替换16处)
     *
     * @param text         目标字符串
     * @param searchString 需要替换的字符串
     * @param replacement  替换的内容
     * @return 如果目标字符串或者需要替换的字符串为空，或者替换的内容为null， 就返回原目标字符串
     * 否则返回替换内容后的字符串
     */
    public static String replace(String text, String searchString,
                                 String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * 替换字符串，可指定替换次数
     *
     * @param text         目标字符串
     * @param searchString 需要替换的字符串
     * @param replacement  替换的内容
     * @param max          要替换的次数
     * @return 如果目标字符串或者需要替换的字符串为空，或者替换的内容为null， 或者max为0， 就返回原目标字符串
     * 如果max小于0，则默认最多替换16处， 如果max大于64时，最多可替换64处
     * 否则返回替换内容后的字符串
     */
    public static String replace(String text, String searchString,
                                 String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null
                || max == 0)
            return text;
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1)
            return text;
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase >= 0 ? increase : 0;
        increase *= max >= 0 ? max <= 64 ? max : 64 : 16;
        StringBuffer buf = new StringBuffer(text.length() + increase);
        do {
            if (end == -1)
                break;
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0)
                break;
            end = text.indexOf(searchString, start);
        } while (true);
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 去除字符串尾部换行符(\r\n)
     *
     * @param str
     * @return 返回去除尾部换行符的字符串
     */
    public static String chomp(String str) {
        if (isEmpty(str))
            return str;
        if (str.length() == 1) {
            char ch = str.charAt(0);
            if (ch == '\r' || ch == '\n')
                return "";
            else
                return str;
        }
        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);
        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r')
                lastIdx--;
        } else if (last != '\r')
            lastIdx++;
        return str.substring(0, lastIdx);
    }

    /**
     * 去除字符串尾部指定的分隔符
     *
     * @param str
     * @param separator 需要去除的分割符
     * @return 返回去除尾部指定分隔符的字符串
     */
    public static String chomp(String str, String separator) {
        if (isEmpty(str) || separator == null)
            return str;
        if (str.endsWith(separator))
            return str.substring(0, str.length() - separator.length());
        else
            return str;
    }

    /**
     * 得到一个指定长度的字符串，长度不够在字符串右边补空格
     *
     * @param str
     * @param size
     * @return
     */
    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    /**
     * 得到一个指定长度的字符串，长度不够在字符串右边补充指定字符
     *
     * @param str
     * @param size
     * @param padChar
     * @return
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null)
            return null;
        int pads = size - str.length();
        if (pads <= 0)
            return str;
        if (pads > 8192)
            return rightPad(str, size, String.valueOf(padChar));
        else
            return str.concat(padding(pads, padChar));
    }

    /**
     * 得到一个指定长度的字符串，长度不够在字符串右边补充指定字符
     *
     * @param str
     * @param size
     * @param padStr
     * @return
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null)
            return null;
        if (isEmpty(padStr))
            padStr = " ";
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0)
            return str;
        if (padLen == 1 && pads <= 8192)
            return rightPad(str, size, padStr.charAt(0));
        if (pads == padLen)
            return str.concat(padStr);
        if (pads < padLen)
            return str.concat(padStr.substring(0, pads));
        char padding[] = new char[pads];
        char padChars[] = padStr.toCharArray();
        for (int i = 0; i < pads; i++)
            padding[i] = padChars[i % padLen];

        return str.concat(new String(padding));
    }

    /**
     * 得到一个指定长度的字符串，长度不够在字符串左边补空格
     *
     * @param str
     * @param size
     * @return
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * 得到一个指定长度的字符串，长度不够在字符串左边补充指定字符
     *
     * @param str
     * @param size
     * @param padChar
     * @return
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null)
            return null;
        int pads = size - str.length();
        if (pads <= 0)
            return str;
        if (pads > 8192)
            return leftPad(str, size, String.valueOf(padChar));
        else
            return padding(pads, padChar).concat(str);
    }

    /**
     * 得到一个指定长度的字符串，长度不够在字符串左边补充指定字符
     *
     * @param str
     * @param size   需要返回字符串的长度
     * @param padStr 要填充的内容，默认为空格" "
     * @return
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null)
            str = "";
        if (isEmpty(padStr))
            padStr = " ";
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0)
            return str;
        if (padLen == 1 && pads <= 8192)
            return leftPad(str, size, padStr.charAt(0));
        if (pads == padLen)
            return padStr.concat(str);
        if (pads < padLen)
            return padStr.substring(0, pads).concat(str);
        char padding[] = new char[pads];
        char padChars[] = padStr.toCharArray();
        for (int i = 0; i < pads; i++)
            padding[i] = padChars[i % padLen];

        return (new String(padding)).concat(str);
    }

    // 填充字符
    private static String padding(int repeat, char padChar)
            throws IndexOutOfBoundsException {
        if (repeat < 0)
            throw new IndexOutOfBoundsException(
                    "Cannot pad a negative amount: " + repeat);
        char buf[] = new char[repeat];
        for (int i = 0; i < buf.length; i++)
            buf[i] = padChar;

        return new String(buf);
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return 返回首字母大写的字符串
     */
    public static String capitalizeFirstLetter(String str) {
        if (isBlank(str)) {
            return str;
        }
        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * 将字符串转成utf-8编码
     *
     * @param str
     * @return 返回utf-8编码的字符串
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 字符串纯数字判断
     *
     * @param str
     * @return 纯数字组成返回true
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 字母判断
     */
    public static boolean isLetter(String str) {
        if (isEmpty(str))
            return false;
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        return pattern.matcher(str).matches();
    }

    public static boolean isLetterAndNumber(String str) {
        if (isEmpty(str))
            return false;
        Pattern pattern = Pattern.compile("[0-9A-Za-z]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是数字(小数or整数)
     *
     * @param str
     * @return 是浮点型则返回true
     */
    public static boolean isNumberDecimals(String str) {
        if (isEmpty(str))
            return false;
        Pattern pattern = Pattern.compile("^[0-9]+$|^[0-9]+\\.?[0-9]+$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断是否是由字母组成
     *
     * @param str
     * @return 由字母组成返回true
     */
    public static boolean isAlpha(String str) {
        if (str == null)
            return false;
        int sz = str.length();
        for (int i = 0; i < sz; i++)
            if (!Character.isLetter(str.charAt(i)))
                return false;

        return true;
    }

    /**
     * 判断是否由字母和数字组成
     *
     * @param str 为null返回false
     * @return 字符串含有数字or字符则返回true
     */
    public static boolean isAlphanumeric(String str) {
        if (str == null)
            return false;
        int sz = str.length();
        for (int i = 0; i < sz; i++)
            if (!Character.isLetterOrDigit(str.charAt(i)))
                return false;

        return true;
    }

    /**
     * 判断是否是中文字符
     *
     * @param c
     * @return 是中文字符则返回true
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串中是否含有中文
     *
     * @param str 为null时返回false
     * @return 含有中文字符返回true
     */
    public synchronized static boolean isChinese(String str) {
        if (str == null)
            return false;
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 反转字符串
     *
     * @param str
     * @return 如果str为""返回""， 为null返回null
     * 否则返回反转的字符串， 即"abc"返回"cba"
     */
    public static String reverse(String str) {
        if (isEmpty(str))
            return str;
        else
            return (new StringBuffer(str)).reverse().toString();
    }

    /**
     * 统计某个sub字符串出现的次数
     *
     * @param str 目标字符串，为空返回0
     * @param sub sub子串，为空返回0
     * @return sub字符串在目标字符串中出现的次数
     */
    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub))
            return 0;
        int count = 0;
        for (int idx = 0; (idx = str.indexOf(sub, idx)) != -1; idx += sub
                .length())
            count++;

        return count;
    }

    /**
     * 生成guid/uuid字符串
     *
     * @return
     */
    public static String guid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 改变字符串长度
     * <br>
     * 不足后面补空格， 超过截取
     *
     * @param s
     * @param length
     * @return
     */
    public static String plusString(String s, int length) {
        if (s == null)
            s = "";
        int primaryLength = s.getBytes().length;
        if (primaryLength >= length)
            return s.substring(0, length);
        int left = length - primaryLength;
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        for (int i = 0; i < left; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 获取textview的数字
     */
    public static int getTextNumInt(TextView tv) {
        String text = tv.getText().toString();
        if (isEmpty(text)) {
            text = "0";
        }
        int result = 0;
        try {
            result = Integer.parseInt(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 是否是合法的url链接
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (isEmpty(str)) {
            return false;
        }
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms|file|content|asset|res)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return str.matches(regex) || isRightUrl(str);
    }

    public static boolean isRightUrl(String str) {
        boolean result = false;
        if (str.startsWith("file")
                || str.startsWith("content")
                || str.startsWith("asset")
                || str.startsWith("res")) {
            result = true;
        }

        return result;
    }
}
