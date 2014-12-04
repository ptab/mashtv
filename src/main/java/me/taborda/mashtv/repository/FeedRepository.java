package me.taborda.mashtv.repository ;

import org.springframework.data.jpa.repository.JpaRepository ;

import me.taborda.mashtv.model.Feed ;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Feed findByUrlIgnoreCase(String url) ;
}
