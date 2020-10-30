package server.model;

import lombok.Builder;
import lombok.Data;
import server.enums.StatusCode;

import java.util.Map;

@Builder
@Data
public class RequestHandler {
    private StatusCode status = StatusCode.OK;
    private HttpRequest requestContext;
    private Map<Integer, String> messages;
    private String newMessage;

    public HttpResponse handleRequest() {

        String message = null;
        String[] pathParts = this.requestContext.getPath().split("/");

        if(this.requestContext.getPath().matches("/messages/([0-9]*)(/$)") ) {

            for (int i = 0; i < this.messages.size(); i++) { //check if key exists
                if(this.messages.get(Integer.parseInt(pathParts[1])) != null) {
                    setStatus(StatusCode.NOCONTENT);
                    message = "Message does not exist or was not found";
                }
            }

            if(getStatus() != StatusCode.NOCONTENT) { //exists
                message = crudMessage(Integer.parseInt(pathParts[1]));
            }

        } else if(this.requestContext.getPath().matches("/messages(/$)")) {
            message = HandleMessages();
        } else {
            setStatus(StatusCode.BADREQUEST);
            message = "Usage: URL/messages OR URL/messages/{number}";
        }

       return new HttpResponse(this.requestContext.getVersion(), message, this.status);
    }

    private String HandleMessages() {
        switch (this.requestContext.getMethod()) {
            case GET:
                return getAllMessages() == null ? "No messages" : getAllMessages();
            case POST:
                return addNewMessage(this.requestContext.getBody());
            default:
                setStatus(StatusCode.BADREQUEST);
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE";
        }
    }

    private String getAllMessages() {
        StringBuffer st = null;
        for (Map.Entry<Integer, String> entry : this.messages.entrySet()){
            st.append(entry.getValue());
            st.append(", ");
        }
        return st.toString();
    }

    private String addNewMessage(String body) {
        setStatus(StatusCode.CREATED);
        this.messages.put(this.messages.size(), body);
        return "New Message was Created";
    }

    private String crudMessage(Integer messageKey) { //message does exist
        switch (this.requestContext.getMethod()) {
            case GET:
                return this.messages.get(messageKey);
            case PUT:
                return "Message Successfully Modified";
            case DELETE:
                this.messages.remove(messageKey);
                return "Message Successfully Deleted";
            default:
                setStatus(StatusCode.BADREQUEST);
                return "Bad Request: Only Methods Accepted: GET, PUT, DELETE";
        }
    }
}
