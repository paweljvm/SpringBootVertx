package pl.paweljvm.sbv.example;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.paweljvm.sbv.controller.Controller;
import pl.paweljvm.sbv.controller.Request;
import pl.paweljvm.sbv.controller.ResponseType;

import java.util.Collection;

@Component
public class ExampleController implements Controller {

    @Autowired
    private PojoService pojoService;

    @Request(value = "/getAll",method= HttpMethod.GET,blocking = true,responseType = ResponseType.JSON)
    public Collection<Pojo> getAll() {
        return pojoService.getAll();
    }
    @Request(value="/getById")
    public Pojo getById(RoutingContext context) {
        final String id =getIdFromContext(context);
        return pojoService.getById(id);
    }
    @Request(value="/create",method= HttpMethod.POST,blocking = true)
    public String create(RoutingContext context) {
        final JsonObject body = context.getBodyAsJson();
        return pojoService.create(new Pojo(body.getString("prop1"),body.getBoolean("prop2")));
    }
    @Request(value="/deleteById",method= HttpMethod.DELETE)
    public void delete(RoutingContext context) {
        pojoService.deleteById(getIdFromContext(context));
    }
    private String getIdFromContext(RoutingContext context) {
        return context.queryParam("id").get(0);
    }
}
