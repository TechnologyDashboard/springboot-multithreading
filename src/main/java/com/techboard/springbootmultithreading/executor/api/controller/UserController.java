package com.techboard.springbootmultithreading.executor.api.controller;

import com.techboard.springbootmultithreading.executor.api.entity.User;
import com.techboard.springbootmultithreading.executor.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/users",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for(MultipartFile file:files){
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/fetchUsers" ,produces = "application/json")
    public CompletableFuture<ResponseEntity> fetchUsers() throws Exception {

          return  userService.fetchAllUsers().thenApply(ResponseEntity::ok);

    }

    @GetMapping(value = "/getUsersbyThread" ,produces = "application/json")
    public ResponseEntity getUsers() throws Exception {

        CompletableFuture<List<User>> users1=  userService.fetchAllUsers();
        CompletableFuture<List<User>> users2=  userService.fetchAllUsers();
        CompletableFuture<List<User>> users3=  userService.fetchAllUsers();

        CompletableFuture.allOf(users1,users2,users3).join();
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
