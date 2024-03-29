package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;
@EnableScheduling
@SpringBootApplication
public class DemoApplication {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("now time : "+ new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
    }

}
