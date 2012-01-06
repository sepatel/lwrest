package org.inigma.shared.webapp;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

/**
 * Base controller providing access to a common set of functionality.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public abstract class BaseController extends HttpServlet {
    protected final Logger logger = Logger.getLogger(getClass().getName());
//    private static MessageDaoTemplate messageTemplate = new MessageDaoTemplate();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

    protected void response(HttpServletResponse response, Object data) {
        // protected void response(Writer w, Object data) {
        response.setContentType("application/json");
//        List<ObjectError> errors = getErrors().getAllErrors();
        try {
            JSONWriter writer = new JSONWriter(response.getWriter()).object();
//            if (hasNoErrors()) {
                writer.key("data");
                if (data == null) {
                    writer.value(null);
                } else if (data instanceof String || data instanceof Number || data instanceof Boolean
                        || data instanceof Map<?, ?>) {
                    writer.value(data);
                } else if (data instanceof Collection<?> || data instanceof Array) {
                    writer.value(new JSONArray(data));
                } else {
                    writer.value(new JSONObject(data));
                }
//            }
                writer.key("success").value(true);
//            writer.key("success").value(hasNoErrors());
//            writer.key("errors").array();
//            for (ObjectError error : errors) {
//                writer.object();
//                writer.key("code").value(error.getCode());
//                writer.key("message")
//                        .value(messageSource.getMessage(error.getCode(), error.getArguments(),
//                                error.getDefaultMessage(), null));
//                if (error instanceof FieldError) {
//                    writer.key("field").value(((FieldError) error).getField());
//                }
//                writer.endObject();
//            }
//            writer.endArray();
            writer.endObject();
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Unable to generate response", e);
            throw new RuntimeException("Error responding with errors", e);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to generate response", e);
            throw new RuntimeException("Error responding with errors", e);
        }
    }
}
