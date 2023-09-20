package com.itheima.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;

@SpringBootTest
class ReggieApplicationTests {

    @Test
    void contextLoads() {
String version= SpringVersion.getVersion();
String version1= SpringBootVersion.getVersion();
        System.out.println(version);
        System.out.println(version1);
    }

}
