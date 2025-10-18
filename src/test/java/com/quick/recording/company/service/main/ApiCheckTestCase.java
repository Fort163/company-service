package com.quick.recording.company.service.main;

@FunctionalInterface
public interface ApiCheckTestCase<T> {

    void check(ResultRequest<T> result);

}
