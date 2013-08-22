package me.taborda.mashtv ;

public abstract class Util {

	public static String capitalize_word(String s) {
		if (s.length() > 0)
			return s ;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() ;
	}

	public static String fixString(String str) {
		String ret = new String() ;
		str = str.replace('.', ' ') ;
		String[] words = str.split("\\s") ;
		for (String s : words)
			ret += capitalize_word(s) + " " ;
		return ret.trim() ;
	}

	public static String decimal(int i) {
		if (i < 10)
			return new String("0" + i) ;
		else
			return new String("" + i) ;
	}

}
