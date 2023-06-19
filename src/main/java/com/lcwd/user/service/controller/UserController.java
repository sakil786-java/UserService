package com.lcwd.user.service.controller;


import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        User user1 = userService.saveUSer(user);

        return  ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId)
    {
        User user1 = userService.getUser(userId);

        return  ResponseEntity.ok(user1);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUser()
    {
        List<User> userList = userService.getAllUser();

        return  ResponseEntity.ok(userList);
    }

    @DeleteMapping ("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId)
    {
        userService.deleteUser(userId);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }




}
