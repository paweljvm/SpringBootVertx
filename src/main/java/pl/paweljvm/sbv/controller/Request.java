package pl.paweljvm.sbv.controller;

import io.vertx.core.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Request {
    String value();
    ResponseType responseType() default ResponseType.JSON;
    HttpMethod method() default HttpMethod.GET;
    boolean blocking() default false;
}
