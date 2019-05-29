package com.factly.dega.web.rest.util;

public final class CommonUtil {

    private static final String NUMBERS_AND_CHARS_REGEX = "[-;./?:@=&<>#%{}|\\^~`“$]";
    private static final String EMPTY_STRING = "";
    private static final String EXTRA_WHITE_SPACE_REGEX = "\\s+";
    private static final String HYPHEN = "-";

    private CommonUtil(){
    }

    public static String removeSpecialCharsFromString(String string){
        if(string != null){
            return string.replaceAll(NUMBERS_AND_CHARS_REGEX , EMPTY_STRING).trim().replaceAll(EXTRA_WHITE_SPACE_REGEX, HYPHEN).toLowerCase();
        }
        return null;
    }
}
