package server.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import server.enums.HttpMethod;
import server.enums.StatusCode;

import java.util.Map;

@Builder
@Getter
@Setter
public class RequestHandler {
    private StatusCode status;
    private HttpRequest requestContext;
    private Map<Integer, String> messages;
    private HttpResponse response;
    private String objectName; // user, message, package

    public HttpResponse handleRequest() {
        String message;
        StringBuffer st = new StringBuffer();
        String[] pathParts = this.requestContext.getPath().split("/");

        for (int i = 0; i < pathParts[1].length() - 1; i++) {
            st.append(pathParts[1].charAt(i));
        }

        objectName = st.toString();

        //Http Method - Check
        if(this.requestContext.getMethod() == HttpMethod.NOTSUPPORTED) {
            this.status = StatusCode.BADREQUEST;
            message = "Method not supported";
        } else {
            //Http Version - Check
            if(this.requestContext.getVersion().equals("HTTP/1.1")) {

                //URL Path "/messages/<Number>"
                if(this.requestContext.getPath().matches("(/messages/)([0-9]+)(/?)")) {
                    message = checkIfMessageExists(this.messages.size(), Integer.parseInt(pathParts[2], 10));

                    if(getStatus() != StatusCode.NOCONTENT) { //exists
                        message = crudMessage(Integer.parseInt(pathParts[2], 10));
                    }

                //URL Path "/messages/"
                } else if(this.requestContext.getPath().matches("(/messages)(/?)")) {
                    message = HandleMessages();
                //URL Path "/another/Path"
                } else {
                    setStatus(StatusCode.BADREQUEST);
                    message = "Usage: URL/messages OR URL/messages/{number}";
                }

            } else {
                setStatus(StatusCode.VERSIONNOTSUPPORTED);
                message = "Method not supported";
            }
        }

        return HttpResponse.builder()
                .version(this.requestContext.getVersion())
                .response(message)
                .status(this.status)
                .build();
    }

    private String checkIfMessageExists(int size, Integer messageKey) {
        boolean exists = false;

        if(this.requestContext.getMethod() != HttpMethod.POST) {
            if(size > 0) {
                for (int i = 0; i < this.messages.size(); i++) { //check if key exists
                    if(this.messages.get(messageKey) != null) {
                        exists = true;
                    }
                    if(!exists) {
                        setStatus(StatusCode.NOCONTENT);
                        return this.objectName + " does not exist or was not found";
                    }
                }
            } else {
                setStatus(StatusCode.NOCONTENT);
                return "The " + this.objectName + " list is empty";
            }
            return this.objectName + " exit(s)";
        } else {
            setStatus(StatusCode.BADREQUEST);
            return "Usage: To add new " + this.objectName + " use POST Method and URL: /messages";
        }

    }

    private String HandleMessages() {
        switch (this.requestContext.getMethod()) {
            case GET:
                return getAllMessages();
            case POST:
                return addNewMessage(this.requestContext.getBody());
            default:
                setStatus(StatusCode.BADREQUEST);
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE";
        }
    }

    private String getAllMessages() {
        StringBuffer st =new StringBuffer();
        int count = 0;
        for (Map.Entry<Integer, String> entry : this.messages.entrySet()){
            st.append(entry.getKey());
            st.append(")  ");
            st.append(entry.getValue());
            st.append("\r\n");
            count++;
        }
        if(count == 0) {
            return "The " + this.objectName + " list is empty";
        } else {
            return st.toString();
        }
    }

    private String addNewMessage(String body) {
        try {
            if(!body.isEmpty()) {
                this.messages.put(this.messages.size()+1, body);
                setStatus(StatusCode.CREATED);
                return "New " + this.objectName + " was created";
            } else {
                setStatus(StatusCode.BADREQUEST);
                return "Couldn't create new "+ this.objectName +". Body was empty.";
            }

        } catch (Exception e){
            setStatus(StatusCode.INTERNALERROR);
            return "Internal Error";
        }
    }

    private String crudMessage(Integer messageKey) { //message does exist
        switch (this.requestContext.getMethod()) {
            case GET:
                return this.messages.get(messageKey);
            case PUT:
                this.messages.put(messageKey,this.requestContext.getBody());
                return "The " + this.objectName + " was modified";
            case DELETE:
                this.messages.remove(messageKey);
                return "The " + this.objectName + " was deleted";
            default:
                setStatus(StatusCode.BADREQUEST);
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE, when indicating a " + this.objectName + " number/index";
        }
    }
}
