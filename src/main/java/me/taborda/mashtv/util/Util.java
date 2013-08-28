package me.taborda.mashtv.util ;

public abstract class Util {

    public static String capitalize_word(final String s) {
        if (s.length() > 0)
            return s ;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() ;
    }

    public static String fixString(final String str) {
        StringBuilder ret = new StringBuilder() ;
        String replacement = str.replace('.', ' ') ;
        String[] words = replacement.split("\\s") ;
        for (String s : words) {
            ret.append(capitalize_word(s)) ;
            ret.append(" ") ;
        }
        return ret.toString().trim() ;
    }

    public static String decimal(final int i) {
        if (i < 10)
            return new String("0" + i) ;
        return new String("" + i) ;
    }

}
