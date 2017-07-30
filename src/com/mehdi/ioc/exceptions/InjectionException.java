package com.mehdi.ioc.exceptions;

/**
 * Costume exception thrown in case the container cannot inject a given bean
 *
 * @author Mehdi Maick
 */
public class InjectionException extends RuntimeException {

    public InjectionException() {
        super();
    }

    public InjectionException(String message) {
        super(message);
    }

    public String getMessage() {
        return "Could Not Inject the given class";
    }
}
