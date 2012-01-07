package org.inigma.lwrest.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteConcern;

public class TestTemplate extends MongoDaoTemplate<SimpleBean> {
    public TestTemplate() {
        super("simple");
    }

    protected SimpleBean convert(DBObjectWrapper data) {
        SimpleBean sb = new SimpleBean();
        sb.setId(data.getString("_id"));
        sb.setAlive(data.getBoolean("alive", false));
        sb.setBirthdate(data.getDate("age"));
        sb.setName(data.getString("name"));
        sb.setRating(data.getFloat("rating"));
        sb.setWeight(data.getInteger("weight"));
        return sb;
    }

    public void save(SimpleBean bean) {
        BasicDBObject data = new BasicDBObject("_id", bean.getId());
        data.put("age", bean.getBirthdate());
        data.put("name", bean.getName());
        data.put("rating", bean.getRating());
        data.put("weight", bean.getWeight());
        getCollection(false).save(data, WriteConcern.SAFE);
    }
}
