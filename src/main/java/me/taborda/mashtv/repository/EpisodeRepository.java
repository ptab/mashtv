package me.taborda.mashtv.repository ;

import java.util.List ;

import org.springframework.data.domain.Sort ;
import org.springframework.data.jpa.repository.JpaRepository ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findFirst5ByShow(Show show, Sort sort) ;

    List<Episode> findByShow(Show show, Sort sort) ;

}
