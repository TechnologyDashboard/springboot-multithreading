package com.techboard.springbootmultithreading.executor.api.service;

import com.techboard.springbootmultithreading.executor.api.entity.User;
import com.techboard.springbootmultithreading.executor.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    Object target;
    Logger logger= LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();
        List<User> users=parseCSVFile(file);
        logger.info("saving list of users of size {}",users.size()," " + Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long endTime = System.currentTimeMillis();
        logger.info("total time taken {}",(endTime-startTime));
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> fetchAllUsers(){
        logger.info("fetch allUsers by Thread " + Thread.currentThread().getName());
        List<User> users= userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try{
           final BufferedReader br=new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line;
                while((line=br.readLine()) != null){
                    final String[] data=line.split(",");
                    final User user=new User();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user);

                }
                return users;

        }catch(final Exception ex){
            logger.info("Failed to parse CSV file --- {}",ex);
        throw new Exception("Failed to parse CSV file {}",ex);

        }
    }
}
