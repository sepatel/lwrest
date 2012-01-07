package org.inigma.lwrest.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.ReadPreference;
import com.mongodb.gridfs.GridFS;

/**
 * A spring friendly wrapper for accessing a specific database instance.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class MongoDataStore {
    private static MongoDataStore singleton;

    public static MongoDataStore getSingleton() {
        return singleton;
    }

    private DB db;
    private Mongo mongo;

    public MongoDataStore(String uri) throws Exception {
        MongoURI mongoUri = new MongoURI(uri);
        this.mongo = new Mongo(mongoUri);
        this.db = mongo.getDB(mongoUri.getDatabase());
        if (singleton == null) {
            singleton = this;
        }
    }

    public void endSession() {
        this.db.requestDone();
    }

    public DBCollection getCollection(String name) {
        DBCollection collection = db.getCollection(name);
        collection.setReadPreference(db.getReadPreference());
        return collection;
    }

    public DBCollection getCollection(String name, boolean slave) {
        DBCollection collection = db.getCollection(name);
        if (slave) {
            collection.setReadPreference(ReadPreference.SECONDARY);
        } else {
            collection.setReadPreference(ReadPreference.PRIMARY);
        }
        return collection;
    }

    public DB getDb() {
        return db;
    }

    public GridFS getGridFS() {
        return new GridFS(db); // default is 'fs'
    }

    public GridFS getGridFS(String directory) {
        return new GridFS(db, directory);
    }

    public Mongo getMongo() {
        return mongo;
    }

    public void startSession() {
        this.db.requestStart();
    }

    @Override
    protected void finalize() throws Throwable {
        this.mongo.close();
        super.finalize();
    }
}
