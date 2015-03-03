package me.taborda.mashtv.service ;

import static org.hamcrest.Matchers.contains ;
import static org.junit.Assert.assertEquals ;
import static org.junit.Assert.assertThat ;

import org.junit.After ;
import org.junit.Test ;
import org.springframework.beans.factory.annotation.Autowired ;

import me.taborda.mashtv.AbstractIntegrationTest ;
import me.taborda.mashtv.NonUniqueException ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.ShowRepository ;

public class ShowServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ShowService victim ;

    @Autowired
    private ShowRepository repository ;

    @After
    public void cleanup() {
        repository.deleteAll() ;
    }

    @Test
    public void add() {
        Show show = victim.add("a sHoW tiTle") ;
        assertEquals("A Show Title", show.getTitle()) ;
    }

    @Test(expected = NonUniqueException.class)
    public void addShouldFailWhenTitleExists() {
        victim.add("a show title") ;
        victim.add("a sHoW tiTle") ;
    }

    @Test
    public void findByTitle() {
        Show show = victim.add("A Show Title") ;
        assertEquals(show, victim.find("a sHoW tiTle")) ;
    }

    @Test
    public void findById() {
        Show show = victim.add("A Show Title") ;
        assertEquals(show, victim.find(show.getId())) ;
    }

    @Test
    public void findAll() {
        Show show = victim.add("A Show Title") ;
        Show differentShow = victim.add("a different show") ;
        assertThat(victim.findAll(), contains(show, differentShow)) ;
    }

    @Test
    public void delete() {
        Show show = victim.add("A Show Title") ;
        Show differentShow = victim.add("a different show") ;
        victim.delete(show) ;
        assertThat(victim.findAll(), contains(differentShow)) ;
    }
}