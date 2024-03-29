package org.inigma.lwrest.message;

import java.util.Collection;

import org.inigma.lwrest.mongo.DBObjectWrapper;
import org.inigma.lwrest.mongo.MongoDaoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class MessageDaoTemplate extends MongoDaoTemplate<Message> {
    public MessageDaoTemplate() {
        this("message");
    }

    public MessageDaoTemplate(String collection) {
        super(collection);
    }

    public Message delete(String code, String locale) {
        return convert(getCollection(false).findAndRemove(createId(code, locale)));
    }

    public Collection<Message> find() {
        return super.find();
    }

    public Message findById(String code, String locale) {
        DBObject id = createId(code, locale);
        return convert(getCollection(true).findOne(id));
    }

    public void save(Message message) {
        DBObject query = new BasicDBObject("_id", createId(message.getCode(), message.getLocale()));
        DBObject data = new BasicDBObject("value", message.getValue());
        DBObject dataset = new BasicDBObject("$set", data);
        getCollection(false).update(query, dataset, true, false, WriteConcern.SAFE);
    }

    @Override
    protected Message convert(DBObjectWrapper data) {
        Message message = new Message();
        DBObjectWrapper document = data.getDocument("_id");
        message.setCode(document.getString("code"));
        message.setLocale(document.getString("locale"));
        message.setValue(data.getString("value"));
        return message;
    }

    private DBObject createId(String code, String locale) {
        DBObject id = new BasicDBObject("code", code);
        id.put("locale", locale);
        return id;
    }
}
