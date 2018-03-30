package pl.paweljvm.sbv.config;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"pl.paweljvm.sbv"})
public class AppConfig {

    @Bean
    public Router router() {
        final Vertx vertx =  Vertx.vertx();
        final Router router = Router.router(vertx);
        final HttpServer server = vertx.createHttpServer();
        server.requestHandler(router::accept).listen(2222);
        return router;
    }


}
