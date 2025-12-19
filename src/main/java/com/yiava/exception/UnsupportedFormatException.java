package com.yiava.exception;

/**
 * Exception thrown when an unsupported file format is uploaded
 */
public class UnsupportedFormatException extends DocumentProcessingException {

    public UnsupportedFormatException(String message) {
        super(message);
    }

    public UnsupportedFormatException(String format, String supportedFormats) {
        super(String.format("Unsupported file format: %s. Supported formats: %s", format, supportedFormats));
    }
}
