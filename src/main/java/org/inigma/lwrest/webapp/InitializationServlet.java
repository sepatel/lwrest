package org.inigma.lwrest.webapp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.inigma.lwrest.SingletonHolder;
import org.inigma.lwrest.config.InMemoryConfiguration;
import org.inigma.lwrest.config.MongoConfiguration;
import org.inigma.lwrest.message.MessageDaoTemplate;
import org.inigma.lwrest.mongo.MongoDataStore;

public class InitializationServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String mongoUri = config.getInitParameter("mongo.uri");
        if (mongoUri != null) {
            SingletonHolder.setDataStore(new MongoDataStore(mongoUri));
        }

        String msgCollection = config.getInitParameter("collection.message");
        if (msgCollection != null) {
            SingletonHolder.setMessageTemplate(new MessageDaoTemplate(msgCollection));
        } else {
            SingletonHolder.setMessageTemplate(new MessageDaoTemplate());
        }

        MongoDataStore dataStore = SingletonHolder.getDataStore();
        if (dataStore != null) {
            String cfgCollection = config.getInitParameter("collection.config");
            if (cfgCollection != null) {
                SingletonHolder.setConfiguration(new MongoConfiguration(dataStore, cfgCollection));
            } else {
                SingletonHolder.setConfiguration(new MongoConfiguration(dataStore));
            }
        } else {
            SingletonHolder.setConfiguration(new InMemoryConfiguration());
        }
    }
}
