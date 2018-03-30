package pl.paweljvm.sbv.controller;

import com.google.gson.Gson;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Aspect
@Component
public class ControllerCreator {

    private final Router router;
    private final Map<ResponseType,Function<Object,String>> mappersMap;
    private final Gson gson;

    private final Collection<Controller> controllers;

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerCreator.class);



    @Autowired
    public ControllerCreator(Router router, Collection<Controller> controllers ) {
        this.router = router;
        this.router.route().handler(BodyHandler.create());
        this.controllers = controllers;
        gson = new Gson();

        mappersMap = new HashMap<>();
        mappersMap.put(ResponseType.STRING,Object::toString);
        mappersMap.put(ResponseType.JSON,gson::toJson);

        LOGGER.info("Registering request handlers...");
        registerRequestHandlers();
    }

    private void registerRequestHandlers() {
        controllers.forEach(this::registerRequestHandlers);
    }


    private void registerRequestHandlers(Controller controller) {
        final Class<?> controllerType = controller.getClass();
        final Method[] methods = controllerType.getMethods();
        Stream.of(methods).forEach(method -> {
            final Request request = method.getAnnotation(Request.class);
            if(request != null) {
                final String path = request.value();
                final Route route =  router.route(request.method(),path);
                LOGGER.info("Registering listener for {} ",path);
                final Consumer<Handler<RoutingContext>> handlerType = request.blocking() ?
                        route::blockingHandler :
                        route::handler;
                handlerType.accept(routingContext ->   handleRequest(controller, method, request, routingContext));
            }
        });
    }

    private void handleRequest(Controller controller ,Method method, Request request, RoutingContext routingContext) {
        final HttpServerResponse response = routingContext.response();
        try {
            final Function<Object,String> mapper = mappersMap.get(request.responseType());
            final Object[] args = prepareMethodArgs(method,routingContext);
            final Object value =  method.invoke(controller,args);
            final String endValue  =  Optional.ofNullable(value).map(mapper).orElse("");
            response.end(endValue);
        } catch(Throwable throwable) {
            LOGGER.error("Error during handing request ", throwable);
            response.setStatusCode(500).setStatusMessage(throwable.getMessage());
        }
    }

    private Object[] prepareMethodArgs(Method method, RoutingContext routingContext) {
        final Parameter[] args = method.getParameters();
        final Object[] result = new Object[args.length];
        final Class<?>[] parameterTypes =  method.getParameterTypes();
        for(int i =0,n=parameterTypes.length;i<n;i++) {
            final Class<?> parameterType = parameterTypes[i];
            if(RoutingContext.class.equals(parameterType)) {
                result[i] = routingContext;
            } else {
                result[i] = null;
            }
        }
        return result;
    }


}
