package com.fs.solr.exception;


public class ReIndexException extends Exception {

    public ReIndexException(String message, Exception exception) {
        super(message, exception);
    }
}
