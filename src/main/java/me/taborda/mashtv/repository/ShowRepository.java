package me.taborda.mashtv.repository ;

import java.util.Optional;

import me.taborda.mashtv.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {

    Optional<Show> findByTitleIgnoreCase(String title) ;
}
