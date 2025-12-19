package com.yiava.exception;

/**
 * Exception thrown when a document is corrupted or cannot be read
 */
public class CorruptedDocumentException extends DocumentProcessingException {

    public CorruptedDocumentException(String message) {
        super(message);
    }

    public CorruptedDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
