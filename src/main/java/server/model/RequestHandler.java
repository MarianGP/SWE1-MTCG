package server.model;

public class RequestHandler {

    public HttpResponse handleRequest(HttpRequest requestContext) {
        if(requestContext.getPath().matches("/messages/([0-9]*)(/$)")) {

            //requestContext.getPath().split("/")
        }

        if(requestContext.getPath().matches("/messages(/$)")) {
            HttpResponse reponse = new HttpResponse(requestContext, requestContext, status);
        }

//
    }
}
