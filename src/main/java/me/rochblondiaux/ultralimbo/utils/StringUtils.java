package me.rochblondiaux.ultralimbo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String maximizeLength(String msg, int maxLength) {
        if (msg.length() > maxLength)
            return msg.substring(0, maxLength);
        return msg;
    }


}
