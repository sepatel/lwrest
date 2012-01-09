package org.inigma.lwrest.webapp;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
public abstract class AbstractRestController extends HttpServlet {
    private class RestMapping {
        public Method method;
        public String pattern;

        public RestMapping(Method m, String p) {
            this.method = m;
            this.pattern = p;
        }
    }

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private Set<RestMapping> getMappings = new HashSet<RestMapping>();
    private Set<RestMapping> postMappings = new HashSet<RestMapping>();
    private Set<RestMapping> putMappings = new HashSet<RestMapping>();
    private Set<RestMapping> deleteMappings = new HashSet<RestMapping>();

    private ThreadLocal<HttpServletRequest> request;

    @Override
    public void init() throws ServletException {
        super.init();
        this.request = new ThreadLocal<HttpServletRequest>();
        for (Method method : getClass().getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (GET.class.isInstance(annotation)) {
                    GET g = (GET) annotation;
                    for (String value : g.value()) {
                        getMappings.add(new RestMapping(method, value));
                    }
                } else if (POST.class.isInstance(annotation)) {
                    POST g = (POST) annotation;
                    for (String value : g.value()) {
                        postMappings.add(new RestMapping(method, value));
                    }
                } else if (PUT.class.isInstance(annotation)) {
                    PUT g = (PUT) annotation;
                    for (String value : g.value()) {
                        putMappings.add(new RestMapping(method, value));
                    }
                } else if (DELETE.class.isInstance(annotation)) {
                    DELETE g = (DELETE) annotation;
                    for (String value : g.value()) {
                        deleteMappings.add(new RestMapping(method, value));
                    }
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processMapping(req, resp, deleteMappings);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processMapping(req, resp, getMappings);
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
        processMapping(req, resp, postMappings);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processMapping(req, resp, putMappings);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

    protected void response(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        // List<ObjectError> errors = getErrors().getAllErrors();
        try {
            Object data = request.getAttribute("_data");
            Exception exception = (Exception) request.getAttribute("_exception");
            boolean success = exception == null;
            JSONWriter writer = new JSONWriter(response.getWriter()).object();
            // if (hasNoErrors()) {
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
            // }
            writer.key("success").value(success);
            if (exception != null) {
                writer.key("exception").value(exception.getMessage());
            }
            // writer.key("success").value(hasNoErrors());
            // writer.key("errors").array();
            // for (ObjectError error : errors) {
            // writer.object();
            // writer.key("code").value(error.getCode());
            // writer.key("message")
            // .value(messageSource.getMessage(error.getCode(), error.getArguments(),
            // error.getDefaultMessage(), null));
            // if (error instanceof FieldError) {
            // writer.key("field").value(((FieldError) error).getField());
            // }
            // writer.endObject();
            // }
            // writer.endArray();
            writer.endObject();
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Unable to generate response", e);
            throw new RuntimeException("Error responding with errors", e);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to generate response", e);
            throw new RuntimeException("Error responding with errors", e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.request.set(req);
        try {
            super.service(req, resp);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled Exception", e);
            req.setAttribute("_exception", e);
        }
        response(req, resp);
        this.request.remove();
    }

    private void processMapping(HttpServletRequest req, HttpServletResponse resp, Set<RestMapping> mappings) {
        boolean handled = false;
        for (RestMapping mapping : mappings) {
            PathParameters pp = new PathParameters();
            if (mapping.pattern.equals(req.getPathInfo())) {
                try {
                    handled = true;
                    Class<?>[] parameterTypes = mapping.method.getParameterTypes();
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (parameterTypes[i].isAssignableFrom(PathParameters.class)) {
                            parameters[i] = pp;
                        } else if (parameterTypes[i].isAssignableFrom(HttpServletRequest.class)) {
                            parameters[i] = req;
                        } else if (parameterTypes[i].isAssignableFrom(HttpServletResponse.class)) {
                            parameters[i] = resp;
                        } else {
                            logger.log(Level.WARNING, "Parameter type: " + parameterTypes[i].getClass()
                                    + " is not handled!");
                        }
                    }

                    mapping.method.invoke(this, parameters);
                } catch (Exception e) {
                    req.setAttribute("_exception", e);
                    logger.log(Level.SEVERE, "Something bad happened", e);
                }
            }
        }
        if (!handled) {
            req.setAttribute("_exception", new IllegalAccessException("Invalid mapping: " + req.getMethod() + " " + req.getPathInfo()));
        }
    }
}
