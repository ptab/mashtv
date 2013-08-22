package me.taborda.mashtv.model ;

import javax.persistence.Entity ;

@Entity
public class Feed extends AbstractEntity {

    private static final long serialVersionUID = 1L ;

    private String url ;

    public Feed() {
    }

    public Feed(final String url) {
        setUrl(url) ;
    }

    public String getUrl() {
        return url ;
    }

    public void setUrl(final String url) {
        this.url = url ;
    }

}
