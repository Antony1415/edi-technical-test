package edi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edi.dto.ResponseDto;
import edi.dto.UserDataRequestDto;
import edi.model.User;
import edi.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseDto<User> sayHello(@PathVariable("userId") String userId) {
        ResponseDto<User> response = new ResponseDto<>();
        if (userId.equals("all")) {
            List<User> users = userService.findAllUser();
            response.setPayload(users);
            response.setStatus(HttpStatus.OK);
            return response;
        }

        try {
            User user = userService.findOneUser(Integer.parseInt(userId))
                    .orElseThrow(() -> new NullPointerException());
            List<User> users = new ArrayList<>();
            users.add(user);

            response.setPayload(users);
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception err) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Something Went WRONG!");
            return response;
        }
    }

    @PostMapping
    public ResponseDto<User> post(@RequestBody UserDataRequestDto request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());

        ResponseDto<User> response = new ResponseDto<>();
        response.setStatus(HttpStatus.CREATED);
        response.getPayload().add(userService.saveUser(user));
        return response;
    }

    @PutMapping("/{userId}")
    public ResponseDto<User> put(@PathVariable("userId") String userId, @RequestBody UserDataRequestDto request) {
        ResponseDto<User> response = new ResponseDto<>();
        try {
            User user = userService.findOneUser(Integer.parseInt(userId)).orElseThrow(() -> new NullPointerException());
            user.setFullName(request.getFullName());
            user.setPassword(request.getPassword());
            user.setStatus(request.getStatus());
            user.setUsername(request.getUsername());

            response.setStatus(HttpStatus.OK);
            response.getPayload().add(userService.saveUser(user));
            return response;
        } catch (Exception err) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(String.format("User with ID - %s is not Found!", userId));
            return response;
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseDto<User> delete(@PathVariable("userId") String userId) {
        ResponseDto<User> response = new ResponseDto<>();
        try {
            User user = userService.findOneUser(Integer.parseInt(userId)).orElseThrow(() -> new NullPointerException());
            userService.deleteUser(user);

            response.setStatus(HttpStatus.OK);
            response.setMessage(String.format("User with ID - %s is successfull Deleted!", userId));
            return response;
        } catch (Exception err) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(String.format("User with ID - %s is not Found!", userId));
            return response;
        }
    }
}
