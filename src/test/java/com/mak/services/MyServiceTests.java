package com.mak.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MyService.class })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyServiceTests {

    // due to : @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    private final MyService myService;

    // without @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    // @Autowired
    // private MyService myService;

    @Test
    void Should_get_Message() {
        log.info(myService.doSomething());
    }

}
