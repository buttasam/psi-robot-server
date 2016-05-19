package cvut.fit.test;

import cvut.fit.server.Pair;
import cvut.fit.server.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {

    private Util util;

    @Before
    public void setUp() {
        util = new Util();
    }


    @Test
    public void parseCoordinatesTest() {
        Assert.assertEquals(new Pair(-3, -1), util.parseCoordinates("OK -3 -1"));
        assertEquals(new Pair(0, 0), util.parseCoordinates("OK 0 0"));
        assertEquals(new Pair(0, 0), util.parseCoordinates("OK 0 0"));
        assertEquals(new Pair(8, 0), util.parseCoordinates("OK 8 0"));
    }

    @Test
    public void validateClientInputTest() {
        assertTrue(util.validateClientInput("OK 1 2"));
        assertTrue(util.validateClientInput("OK 1 -2"));
        assertTrue(util.validateClientInput("OK 0 0"));
        assertFalse(util.validateClientInput("OK 1 0.1"));
        assertTrue(util.validateClientInput("RECHARGING"));
        assertTrue(util.validateClientInput("FULL POWER"));
    }

}
