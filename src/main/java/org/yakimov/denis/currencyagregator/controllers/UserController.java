//package org.yakimov.denis.currencyagregator.controllers;
//
//import com.fasterxml.jackson.annotation.JsonView;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.yakimov.denis.currencyagregator.models.Group;
//import org.yakimov.denis.currencyagregator.models.User;
//import org.yakimov.denis.currencyagregator.services.UserService;
//import org.yakimov.denis.currencyagregator.support.JacksonMappingMarker;
//
//@RestController("/user")
//public class UserController {
//
//    @Autowired
//    UserService userService;
//
//
//    @JsonView(JacksonMappingMarker.Public.class)
//    @PostMapping(value="/create", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public ResponseEntity<User> createUser(User user){
//        String login = user.getLogin();
//        if (StringUtils.isEmpty(login)){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        User newOne = userService.create(user);
//
//        if (newOne!=null){
//            return new ResponseEntity<>(newOne, HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//    @JsonView(JacksonMappingMarker.Public.class)
//    @PostMapping(value="/update", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public ResponseEntity<User> updateUser(User user){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//
//        String targetLogin = user.getLogin();
//        if (StringUtils.isEmpty(targetLogin)){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        User currentUser = userService.getUserByLogin(currentPrincipalName);
//        User updated = null;
//        if (targetLogin.equals(currentPrincipalName) || currentUser.getGroup().equals(Group.ADMIN)){
//            updated = userService.updateUser(targetLogin, user);
//        }
//
//        if (updated!=null){
//            return new ResponseEntity<>(updated, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//}
