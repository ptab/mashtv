package me.taborda.mashtv.repository ;

import org.springframework.data.jpa.repository.JpaRepository ;

import me.taborda.mashtv.model.Episode ;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

}
