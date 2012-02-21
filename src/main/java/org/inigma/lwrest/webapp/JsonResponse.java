package org.inigma.lwrest.webapp;

import org.json.JSONException;
import org.json.JSONWriter;

public interface JsonResponse extends ResponseTransformer {
    void toJson(JSONWriter writer) throws JSONException;
}
