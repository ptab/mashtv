package me.taborda.mashtv.repository ;

import java.util.Optional;

import me.taborda.mashtv.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Optional<Feed> findByUrlIgnoreCase(String url) ;
}
