package server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NOCONTENT(204, "No Content"),
    BADREQUEST(400, "Bad Request"),
    NOTFOUND(404, "Not Found"),
    INTERNALERROR(500, "Internal Server Error"),
    VERSIONNOTSUPPORTED(505, "Version Not Supported")
    ;

    private Integer code;
    private String status;

}
