package me.taborda.mashtv ;

public class NonUniqueException extends RuntimeException {

    private static final long serialVersionUID = -60441491295589110L ;

    public NonUniqueException(final String text) {
        super(text + " already exists, you might want to chose a different one.") ;
    }
}
