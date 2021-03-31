package com.neurondigital.selfcare.service;

public interface  AsyncResponse<T> {
    void processFinish(T output);
}