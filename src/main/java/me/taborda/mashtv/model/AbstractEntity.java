package me.taborda.mashtv.model ;

import java.io.Serializable ;

import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.MappedSuperclass ;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L ;

    @Id
    @GeneratedValue
    private Integer id ;

    public Integer getId() {
        return id ;
    }

    @SuppressWarnings("unused")
    private void setId(final Integer id) {
        this.id = id ;
    }
}
