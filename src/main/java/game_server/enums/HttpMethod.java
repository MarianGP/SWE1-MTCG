package game_server.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    NOTSUPPORTED
    ;

//    public static List<HttpMethod> listOfMethods = List.of(values()); //immutable collection

    public static List<HttpMethod> listOfMethods =
      new ArrayList<>(Arrays.asList(HttpMethod.values()));

}
