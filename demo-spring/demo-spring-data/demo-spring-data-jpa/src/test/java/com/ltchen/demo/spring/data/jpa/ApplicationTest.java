package com.ltchen.demo.spring.data.jpa;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: 01139983
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationTest {

    @Autowired
    StringEncryptor encryptor;

    /**
     * https://github.com/ulisesbocchio/jasypt-spring-boot
     * https://www.jianshu.com/p/b047ed4a8dfa
     */
    @Test
    public void encrypt() {
        String username = encryptor.encrypt("admin");
        System.out.println(encryptor.decrypt("MkNDMTM3NzY4NjA0RDhEQUM0MzNGNzc3NDJDODBDRTY="));
        System.out.println("username: " + username);
        String password = encryptor.encrypt("admin");
        System.out.println("password: " + password);
        Assert.assertTrue(username.length() > 0);
        Assert.assertTrue(password.length() > 0);
    }
}
