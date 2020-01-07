package com.ehualu.calabashandroid;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-29 21:19:59
 * <p>
 * describe：排序测试：中文>字母>数字>特殊符号
 */
public class SortPinYinTest {
    public static List<String> chineseAndEnglishSort(String[] arrays) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            String str = arrays[i];
            String alphabet = str.substring(0, 1);
            /*判断首字符是否为中文，如果是中文便将首字符拼音的首字母和&符号加在字符串前面*/
            if (alphabet.matches("[\\u4e00-\\u9fa5]+")) {
                str = getAlphabet(str) + "&" + str;
                arrays[i] = str;
            }
        }
        /*设置排序语言环境*/
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(arrays, com);

        /*遍历数组，去除标识符&及首字母*/
        for (int i = 0; i < arrays.length; i++) {
            String str = arrays[i];
            if (str.contains("&") && str.indexOf("&") == 1) {
                arrays[i] = str.split("&")[1];
            }
            list.add(arrays[i]);
        }
        return list;
    }

    public static String getAlphabet(String str) {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String pinyin = null;
        try {
            pinyin = (String) PinyinHelper.toHanyuPinyinStringArray(str.charAt(0), defaultFormat)[0];
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return pinyin.substring(0, 1);
    }

//    public static void sortList(List<RemoteFile> files) {
//
//        //        // 将list集合分成只包含以汉字开头元素的集合和不包含以汉字开头元素的集合
//        //        List<String> chineseCharacters = new ArrayList<>();
//        //        List<String> notChineseCharacters = new ArrayList<>();
//        //        for (String str : list) {
//        //            if (!TextUtils.isEmpty(str) && String.valueOf(str.charAt(0)).matches("[\u4e00-\u9fa5]")) {
//        //                // 如果开头为汉字，则加入汉字列表中
//        //                chineseCharacters.add(str);
//        //            } else {
//        //                notChineseCharacters.add(str);
//        //            }
//        //        }
//        //
//        //        //中文>字母>数字>特殊符号
//        //        Comparator<Object> collator = Collator.getInstance(java.util.Locale.CHINA);
//        //        Pinyin4jUtil pinyin4jUtil = new Pinyin4jUtil();
//        //
//        //        //以汉字开头的，先将汉字转成拼音，然后进行排序
//        //        chineseCharacters.sort((o1, o2) -> ((Collator) collator).compare(pinyin4jUtil.getStringPinYin(o1),
//        //                pinyin4jUtil.getStringPinYin(o2)));
//        //        //对非汉字开头内容进行排序
//        //        notChineseCharacters.sort((o2, o1) -> ((Collator) collator).compare(o1, o2));
//        //        chineseCharacters.addAll(notChineseCharacters);
//        //        MyLog.d(chineseCharacters.toString());
//
//        // 将list集合分成只包含以汉字开头元素的集合和不包含以汉字开头元素的集合
//        List<RemoteFile> all = new ArrayList<>();
//
//        List<RemoteFile> filechineseCharacters = new ArrayList<>();
//        List<RemoteFile> filenotChineseCharacters = new ArrayList<>();
//
//        List<RemoteFile> folderchineseCharacters = new ArrayList<>();
//        List<RemoteFile> foldernotChineseCharacters = new ArrayList<>();
//        List<RemoteFile> folder = new ArrayList<>();
//
//
//        for (RemoteFile remoteFile : files) {
//            if (remoteFile.getCategory().equals("1")) {
//                if (!TextUtils.isEmpty(remoteFile.getFileName()) && String.valueOf(remoteFile.getFileName().charAt(0)).matches("[\u4e00-\u9fa5]")) {
//                    // 如果开头为汉字，则加入汉字列表中
//                    filechineseCharacters.add(remoteFile);
//                } else {
//                    filenotChineseCharacters.add(remoteFile);
//                }
//            } else {
//                if (remoteFile.getFileName().equals("葫芦备份")) {
//                    folder.add(remoteFile);
//                } else if (remoteFile.getFileName().equals("备份恢复")) {
//                    folder.add(remoteFile);
//                } else if (remoteFile.getFileName().equals("收到文件")) {
//                    folder.add(remoteFile);
//                } else if (remoteFile.getFileName().equals("我的收藏")) {
//                    folder.add(remoteFile);
//                } else if (remoteFile.getFileName().equals("我的相册")) {
//                    folder.add(remoteFile);
//                } else {
//                    if (!TextUtils.isEmpty(remoteFile.getFileName()) && String.valueOf(remoteFile.getFileName().charAt(0)).matches("[\u4e00-\u9fa5]")) {
//                        // 如果开头为汉字，则加入汉字列表中
//                        folderchineseCharacters.add(remoteFile);
//                    } else {
//                        foldernotChineseCharacters.add(remoteFile);
//                    }
//                }
//            }
//        }
//
//        //中文>字母>数字>特殊符号
//        Comparator<Object> collator = Collator.getInstance(java.util.Locale.CHINA);
//        Pinyin4jUtil pinyin4jUtil = new Pinyin4jUtil();
//
//        //以汉字开头的，先将汉字转成拼音，然后进行排序
//        filechineseCharacters.sort((o1, o2) -> ((Collator) collator).compare(pinyin4jUtil.getStringPinYin(o1.getFileName()),
//                pinyin4jUtil.getStringPinYin(o2.getFileName())));
//        //对非汉字开头内容进行排序
//        filenotChineseCharacters.sort((o2, o1) -> ((Collator) collator).compare(o1.getFileName(), o2.getFileName()));
//        filechineseCharacters.addAll(filenotChineseCharacters);
//
//
//        //以汉字开头的，先将汉字转成拼音，然后进行排序
//        folderchineseCharacters.sort((o1, o2) -> ((Collator) collator).compare(pinyin4jUtil.getStringPinYin(o1.getFileName()),
//                pinyin4jUtil.getStringPinYin(o2.getFileName())));
//        //对非汉字开头内容进行排序
//        foldernotChineseCharacters.sort((o2, o1) -> ((Collator) collator).compare(o1.getFileName(), o2.getFileName()));
//        folderchineseCharacters.addAll(foldernotChineseCharacters);
//
//        all.addAll(folder);
//        all.addAll(folderchineseCharacters);
//        all.addAll(filechineseCharacters);
//        for (int i = 0; i < all.size(); i++) {
//            MyLog.d(all.get(i).getFileName() + "====" + all.get(i).getCategory());
//        }
//    }

}
