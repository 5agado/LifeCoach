package model;

public class ResourceResponse {
	protected Status status;
	protected int resourceId;
	
    protected ResourceResponse() {}

    public static ResourceResponse status(Status status) {
    	ResourceResponse r = new ResourceResponse();
        r.setStatus(status);
        return r;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setResourceId(int id) {
        this.resourceId = id;
    }

    public static ResourceResponse ok() {
    	ResourceResponse s = status(Status.OK);
        return s;
    }
    
    public static ResourceResponse ok(int resourceId) {
    	ResourceResponse s = status(Status.OK);
    	s.setResourceId(resourceId);
        return s;
    }

    public static ResourceResponse serverError() {
    	ResourceResponse b = status(Status.INTERNAL_SERVER_ERROR);
        return b;
    }

    public static ResourceResponse noContent() {
    	ResourceResponse b = status(Status.NO_CONTENT);
        return b;
    }

    public static ResourceResponse notModified() {
    	ResourceResponse b = status(Status.NOT_MODIFIED);
        return b;
    }
    
    public static ResourceResponse notAcceptable() {
    	ResourceResponse b = status(Status.NOT_ACCEPTABLE);
        return b;
    }
    
    public enum Status {
        OK(200, "OK"),
       
        CREATED(201, "Created"),
        
        ACCEPTED(202, "Accepted"),
        
        NO_CONTENT(204, "No Content"),
        
        MOVED_PERMANENTLY(301, "Moved Permanently"),
       
        SEE_OTHER(303, "See Other"),
        
        NOT_MODIFIED(304, "Not Modified"),
        
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        
        BAD_REQUEST(400, "Bad Request"),
        
        UNAUTHORIZED(401, "Unauthorized"),
        
        FORBIDDEN(403, "Forbidden"),
       
        NOT_FOUND(404, "Not Found"),
       
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        
        CONFLICT(409, "Conflict"),
        
        GONE(410, "Gone"),
        
        PRECONDITION_FAILED(412, "Precondition Failed"),
        
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        
        SERVICE_UNAVAILABLE(503, "Service Unavailable");
        
        private final int code;
        private final String reason;

        Status(final int statusCode, final String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
        }
        
        public int getStatusCode() {
            return code;
        }
        
        public String getReasonPhrase() {
            return toString();
        }
        
        @Override
        public String toString() {
            return reason;
        }
        
        public static Status fromStatusCode(final int statusCode) {
            for (Status s : Status.values()) {
                if (s.code == statusCode) {
                    return s;
                }
            }
            return null;
        }
    }
}

