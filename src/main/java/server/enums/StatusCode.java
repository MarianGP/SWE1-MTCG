package server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {
    OK("OK", 200),
    CREATED("Created", 201),
    NOCONTENT("No Content", 204),
    BADREQUEST("Bad Request", 400),
    NOTFOUND("Not Found", 404),
    INTERNALERROR("Internal Server Error", 500),
    VERSIONNOTSUPPORTED("Version Not Supported", 505)
    ;

    private String status;
    private Integer code;

}
