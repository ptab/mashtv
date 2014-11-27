package me.taborda.mashtv.model ;

import java.io.Serializable ;

import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.MappedSuperclass ;
import javax.persistence.Version ;

import com.fasterxml.jackson.annotation.JsonIgnore ;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L ;

    @Id
    @GeneratedValue
    private Long id ;

    @JsonIgnore
    @Version
    private Long version ;

    public Long getId() {
        return id ;
    }

    public Long getVersion() {
        return version ;
    }
}
