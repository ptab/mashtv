package me.taborda.mashtv.repository ;

import org.springframework.data.jpa.repository.JpaRepository ;

import me.taborda.mashtv.model.Show ;

public interface ShowRepository extends JpaRepository<Show, Long> {

    Show findByTitleIgnoreCase(String title) ;
}
