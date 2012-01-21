package org.inigma.lwrest.webapp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestMappingTest {
    @Test
    public void noParameter() {
        RequestMapping mapping = new RequestMapping(null, "/test");
        assertNull(mapping.matches("/girl"));
        assertNotNull(mapping.matches("/test"));
        assertNull(mapping.matches("/tester"));
    }

    @Test
    public void oneParameter() {
        RequestMapping mapping = new RequestMapping(null, "/test/{name}");
        assertNull(mapping.matches("/test"));
        assertNull(mapping.matches("/test/john/doe"));
        PathParameters parameters = mapping.matches("/test/frank");
        assertNotNull(parameters);
        assertEquals("frank", parameters.getString("name"));
    }
    
    @Test
    public void twoParameters() {
        RequestMapping mapping = new RequestMapping(null, "/test/{name}/{id}");
        assertNull(mapping.matches("/test"));
        assertNull(mapping.matches("/test/john"));
        PathParameters parameters = mapping.matches("/test/john/doe");
        assertNotNull(parameters);
        assertEquals("john", parameters.getString("name"));
        assertEquals("doe", parameters.getString("id"));
    }
}
