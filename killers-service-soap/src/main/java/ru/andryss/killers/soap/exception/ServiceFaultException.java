package ru.andryss.killers.soap.exception;

import lombok.Getter;

@Getter
public class ServiceFaultException extends RuntimeException {

    private final ServiceFault serviceFault;

    public ServiceFaultException(String message, ServiceFault serviceFault) {
        super(message);
        this.serviceFault = serviceFault;
    }
}
