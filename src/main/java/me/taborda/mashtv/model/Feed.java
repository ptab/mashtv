package me.taborda.mashtv.model ;

import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

@Entity
public class Feed extends AbstractEntity {

    private static final long serialVersionUID = 1L ;

    @Column(unique = true)
    @NotNull
    @Size(min = 1)
    private String url ;

    protected Feed() {
        // hibernate && JPA
    }

    public Feed(final String url) {
        this.url = url ;
    }

    public String getUrl() {
        return url ;
    }

    @Override
    public int hashCode() {
        return url.hashCode() ;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true ;
        }
        if (!(obj instanceof Feed)) {
            return false ;
        }

        Feed other = (Feed) obj ;
        return url.equals(other.getUrl()) ;
    }

    @Override
    public String toString() {
        return url ;
    }

}
