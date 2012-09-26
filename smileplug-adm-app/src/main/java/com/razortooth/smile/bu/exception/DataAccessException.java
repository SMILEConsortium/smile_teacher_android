package com.razortooth.smile.bu.exception;

public class DataAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Exception e) {
        super(e);
    }

    public DataAccessException(Error e) {
        super(e);
    }

}
