package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String toRoman(int number) {
        if (number == 0) {
            return "0";
        }

        StringBuilder roman = new StringBuilder();

        while (number > 0) {
            if (number >= 1000) {
                roman.append("M");
                number -= 1000;
            } else if (number >= 900) {
                roman.append("CM");
                number -= 900;
            } else if (number >= 500) {
                roman.append("D");
                number -= 500;
            } else if (number >= 400) {
                roman.append("CD");
                number -= 400;
            } else if (number >= 100) {
                roman.append("C");
                number -= 100;
            } else if (number >= 90) {
                roman.append("XC");
                number -= 90;
            } else if (number >= 50) {
                roman.append("L");
                number -= 50;
            } else if (number >= 40) {
                roman.append("XL");
                number -= 40;
            } else if (number >= 10) {
                roman.append("X");
                number -= 10;
            } else if (number >= 9) {
                roman.append("IX");
                number -= 9;
            } else if (number >= 5) {
                roman.append("V");
                number -= 5;
            } else if (number >= 4) {
                roman.append("IV");
                number -= 4;
            } else {
                roman.append("I");
                number -= 1;
            }
        }

        return roman.toString();
    }

    public static String color(String str) {
        Matcher matcher = Pattern.compile("#([A-Fa-f\b]{6})").matcher(str);

        StringBuilder buffer = new StringBuilder(str.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x" + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1) + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3) + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    public static List<String> color(List<String> list) {
        list.replaceAll(StringUtil::color);
        return list;
    }

    public static String removeColors(String str) {
        String tmp = ChatColor.stripColor(str);
        return tmp == null ? "" : tmp;
    }
}
