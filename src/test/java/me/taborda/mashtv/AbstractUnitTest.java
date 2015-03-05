package me.taborda.mashtv ;

import org.junit.Before ;
import org.mockito.MockitoAnnotations ;

public class AbstractUnitTest extends AbstractTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(getClass()) ;
    }
}
