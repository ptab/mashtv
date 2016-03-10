package me.taborda.mashtv.integration;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import me.taborda.mashtv.AbstractIntegrationTest;
import me.taborda.mashtv.enricher.trakt.TestTraktShow;
import me.taborda.mashtv.enricher.trakt.TraktClient;
import me.taborda.mashtv.exception.NonUniqueException;
import me.taborda.mashtv.model.Show;
import me.taborda.mashtv.repository.ShowRepository;
import me.taborda.mashtv.service.ShowService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShowServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ShowService victim;

    @Autowired
    private ShowRepository repository;

    @Autowired
    private TraktClient traktClient;

    @After
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void addShowWithEnrichment() {
        String title = "a sHoW tiTle";
        String expectedTitle = "A Show Title";
        when(traktClient.findShow(title)).thenReturn(Collections.singletonList(new TestTraktShow().withTitle(expectedTitle)));

        Show show = victim.add(title);
        assertEquals(expectedTitle, show.getTitle());
    }

    @Test(expected = NonUniqueException.class)
    public void addShouldFailWhenTitleExists() {
        victim.add("a show title");
        victim.add("a sHoW tiTle");
    }

    @Test
    public void findByTitle() {
        Show show = victim.add("A Show Title");
        assertEquals(show, victim.find("a sHoW tiTle").get());
    }

    @Test
    public void findById() {
        Show show = victim.add("A Show Title");
        assertEquals(show, victim.find(show.getId()));
    }

    @Test
    public void findAll() {
        Show show = victim.add("A Show Title");
        Show differentShow = victim.add("a different show");
        assertThat(victim.findAll(), contains(show, differentShow));
    }

    @Test
    public void delete() {
        Show show = victim.add("A Show Title");
        Show differentShow = victim.add("a different show");
        victim.delete(show);
        assertThat(victim.findAll(), contains(differentShow));
    }
}