package com.example.demo;

import com.example.demo.src.data.entity.User;
import com.example.demo.src.data.entity.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    UserRepository userRepository;

    @Test
    void findAllTest() {
        if(userRepository!=null){
            List<User> userList = userRepository.findAll();
            for(User u : userList) System.out.println("[FindAll]: " + u.getUserId() + " | " +u.getNickname());
        }else {
            System.out.println("no data");
        }
    }
}
