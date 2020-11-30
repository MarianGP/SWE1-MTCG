package game_server.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    NOTSUPPORTED
    ;

    public static final List<HttpMethod> listOfMethods = List.of(values());
}
