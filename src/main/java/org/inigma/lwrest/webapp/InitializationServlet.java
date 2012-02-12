package org.inigma.lwrest.webapp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.inigma.lwrest.InjectionHolder;
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
            InjectionHolder.addInjectable(new MongoDataStore(mongoUri));
        }

        String msgCollection = config.getInitParameter("collection.message");
        if (msgCollection != null) {
            InjectionHolder.addInjectable(new MessageDaoTemplate(msgCollection));
        } else {
            InjectionHolder.addInjectable(new MessageDaoTemplate());
        }

        MongoDataStore dataStore = InjectionHolder.getInjectable(MongoDataStore.class);
        if (dataStore != null) {
            String cfgCollection = config.getInitParameter("collection.config");
            if (cfgCollection != null) {
                InjectionHolder.addInjectable(new MongoConfiguration(dataStore, cfgCollection));
            } else {
                InjectionHolder.addInjectable(new MongoConfiguration(dataStore));
            }
        } else {
            InjectionHolder.addInjectable(new InMemoryConfiguration());
        }
    }
}
