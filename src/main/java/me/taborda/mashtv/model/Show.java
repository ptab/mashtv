package me.taborda.mashtv.model ;

import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

@Entity
public class Show extends AbstractEntity implements Comparable<Show> {

    private static final long serialVersionUID = 1L ;

    @Column(unique = true)
    @NotNull
    @Size(min = 1)
    private String title ;

    public Show() {
    }

    public Show(final String title) {
        setTitle(title) ;
    }

    public String getTitle() {
        return title ;
    }

    public void setTitle(final String title) {
        this.title = title ;
    }

    @Override
    public int compareTo(final Show o) {
        return getTitle().compareTo(o.getTitle()) ;
    }

    @Override
    public String toString() {
        return getTitle() ;
    }
}
