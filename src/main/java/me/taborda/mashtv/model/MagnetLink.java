package me.taborda.mashtv.model ;

import javax.persistence.Entity ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

@Entity
public class MagnetLink extends AbstractEntity implements Comparable<MagnetLink> {

    private static final long serialVersionUID = 1L ;

    @NotNull
    @Size(min = 1)
    private String url ;

    @NotNull
    @Size(min = 1)
    private String filename ;

    private boolean hd ;

    protected MagnetLink() {
        // hibernate && JPA
    }

    public MagnetLink(final String url, final String filename, final boolean hd) {
        this.url = url ;
        this.filename = filename ;
        this.hd = hd ;
    }

    public String getUrl() {
        return url ;
    }

    public String getFilename() {
        return filename ;
    }

    public boolean isHd() {
        return hd ;
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

        if (!(obj instanceof MagnetLink)) {
            return false ;
        }
        MagnetLink other = (MagnetLink) obj ;
        return url.equals(other.getUrl()) ;
    }

    @Override
    public int compareTo(final MagnetLink o) {
        if (isHd() && !o.isHd()) {
            return 1 ;
        }
        if (!isHd() && o.isHd()) {
            return -1 ;
        }
        return url.compareTo(o.getUrl()) ;
    }

}
