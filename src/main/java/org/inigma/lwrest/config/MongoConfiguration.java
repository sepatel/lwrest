package org.inigma.lwrest.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.inigma.lwrest.mongo.MongoDataStore;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Dynamically loads and reloads configuration settings from a collection. The data model is presumed to be in the
 * following syntax.
 * 
 * <pre>
 * {_id: string, value: object}
 * </pre>
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class MongoConfiguration extends AbstractConfiguration {
    private static final String KEY = "_id";
    private static final String VALUE = "value";
    private static final Timer TIMER = new Timer(true);
    private static MongoConfiguration singleton;

    public static synchronized MongoConfiguration getSingleton() {
        if (singleton == null) {
            new MongoConfiguration();
        }
        return singleton;
    }

    private final MongoDataStore ds;
    private final String collection;
    private TimerTask reloadTask;

    public MongoConfiguration() {
        this(MongoDataStore.getSingleton(), "config");
    }

    public MongoConfiguration(MongoDataStore ds) {
        this(ds, "config");
    }

    public MongoConfiguration(MongoDataStore ds, String collection) {
        if (singleton == null) {
            singleton = this;
        }
        this.ds = ds;
        this.collection = collection;

        reload();
    }

    public MongoConfiguration(String collection) {
        this(MongoDataStore.getSingleton(), collection);
    }

    /**
	 * @see org.inigma.lwrest.config.Configuration#remove(java.lang.String)
	 */
    @Override
	public Object remove(String key) {
        ds.getCollection(collection).remove(new BasicDBObject("_id", key));
        return super.remove(key);
    }

    @Override
    protected void setValue(String key, Object value) {
        DBObject query = new BasicDBObject(KEY, key);
        DBObject data = new BasicDBObject(KEY, key);
        data.put(VALUE, value);
        ds.getCollection(collection).update(query, data, true, false);
    }

    public void setPollingFrequency(long pollingFrequency) {
        if (reloadTask != null) {
            reloadTask.cancel();
        }
        if (pollingFrequency > 0) {
            reloadTask = new TimerTask() {
                @Override
                public void run() {
                    reload();
                }
            };
            TIMER.schedule(reloadTask, pollingFrequency, pollingFrequency);
        }
    }

    private void reload() {
    	Map<String, Object> newconfigs = new HashMap<String, Object>();
        DBCursor allConfigs = ds.getCollection(collection, true).find();
        for (DBObject o : allConfigs) {
        	newconfigs.put((String) o.get(KEY), o.get(VALUE));
        }
        super.reload(newconfigs);
    }

    @Override
    protected Object getValue(String key) {
        DBObject query = new BasicDBObject(KEY, key);
        DBObject dbObject = ds.getCollection(collection, true).findOne(query);
        if (dbObject == null) {
            throw new IllegalStateException("Configuration " + key + " not found!");
        }
        return dbObject.get(VALUE);
    }


}
