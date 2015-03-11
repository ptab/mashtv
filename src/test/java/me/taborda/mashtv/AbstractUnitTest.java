package me.taborda.mashtv ;

import org.junit.Before ;
import org.junit.runner.RunWith ;
import org.mockito.MockitoAnnotations ;
import org.mockito.runners.MockitoJUnitRunner ;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractUnitTest extends AbstractTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(getClass()) ;
    }
}
