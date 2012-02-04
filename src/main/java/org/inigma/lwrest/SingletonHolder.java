package org.inigma.lwrest;

import org.inigma.lwrest.config.Configuration;
import org.inigma.lwrest.message.MessageDaoTemplate;
import org.inigma.lwrest.mongo.MongoDataStore;

public abstract class SingletonHolder {
    private static MongoDataStore dataStore;
    private static MessageDaoTemplate messageTemplate;
    private static Configuration configuration;

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static MongoDataStore getDataStore() {
        return dataStore;
    }

    public static MessageDaoTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public static void setConfiguration(Configuration config) {
        SingletonHolder.configuration = config;
    }

    public static void setDataStore(MongoDataStore mds) {
        SingletonHolder.dataStore = mds;
    }

    public static void setMessageTemplate(MessageDaoTemplate template) {
        SingletonHolder.messageTemplate = template;
    }
}
