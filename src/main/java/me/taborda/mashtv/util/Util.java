package me.taborda.mashtv.util ;

import org.apache.commons.lang3.text.WordUtils ;

public final class Util {

    private Util() {
        // do not instantiate
    }

    public static String fixTitle(final String str) {
        String title = str.replace('.', ' ') ;
        title = WordUtils.capitalizeFully(title) ;
        return title.trim() ;
    }

    public static String decimal(final int i) {
        if (i < 10) {
            return "0" + i ;
        }
        return Integer.toString(i) ;
    }

}
