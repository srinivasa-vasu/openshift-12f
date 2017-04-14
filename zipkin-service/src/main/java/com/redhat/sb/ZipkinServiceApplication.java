package com.redhat.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinStreamServer
public class ZipkinServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServiceApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/healthz")
    public String health() {
        return "I'm OK";
    }
}
