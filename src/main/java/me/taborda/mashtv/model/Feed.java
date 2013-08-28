package me.taborda.mashtv.model ;

import javax.persistence.Entity ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

@Entity
public class Feed extends AbstractEntity {

    private static final long serialVersionUID = 1L ;

    @NotNull
    @Size(min = 1)
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
