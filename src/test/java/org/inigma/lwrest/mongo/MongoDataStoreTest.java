package org.inigma.lwrest.mongo;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.ReadPreference;

public class MongoDataStoreTest {
    private static final Random RAND = new SecureRandom();

    private static MongoDataStore mds;

    @BeforeClass
    public static void initialization() throws Exception {
        mds = new MongoDataStore("mongodb://192.168.1.100/junit");
    }

    private TestTemplate template;

    @Test
    public void getCollection() {
        DBCollection collection = template.getCollection(false);
        assertEquals("simple", collection.getName());
        assertEquals(ReadPreference.PRIMARY, collection.getReadPreference());

        collection = template.getCollection(true);
        assertEquals(ReadPreference.SECONDARY, collection.getReadPreference());

        collection = template.getCollection();
        assertEquals(mds.getDb().getReadPreference(), collection.getReadPreference());
    }

    @Test
    public void getDb() {
        DB db = template.getCollection().getDB();
        assertEquals("junit", db.getName());
    }

    @Test
    public void readWriteData() {
        Date date = new Date();
        SimpleBean bean = new SimpleBean();
        bean.setAlive(true);
        bean.setBirthdate(date);
        bean.setName("Special Name");
        bean.setRating(RAND.nextFloat());
        bean.setWeight(RAND.nextInt(500));
        bean.setId(template.generateId());

        assertNull(template.findById(bean.getId()));
        template.save(bean);
        assertNotNull(template.findById(bean.getId()));
    }

    @Before
    public void setup() {
        template = new TestTemplate();
    }
}
