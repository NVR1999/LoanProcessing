package com.example.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.DTO.UserDTO;
import com.example.entity.User;
import com.example.exceptionHandler.CustomException;
import com.example.exceptionHandler.PasswordMissmatchException;
import com.example.exceptionHandler.UserNotFound;
import com.example.service.UserService;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping(path="/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO user, BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
//	        List<String> errors = bindingResult.getAllErrors().stream()
//	                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//	                .collect(Collectors.toList());
//	        return ResponseEntity.badRequest().body(errors);
	    	return new ResponseEntity<Object>("Invalid input",HttpStatus.CONFLICT);
	    }

	    try {
	        User u1 = userService.createUser(user);
	        return new ResponseEntity<>(u1, HttpStatus.CREATED);
	    } catch (CustomException e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@DeleteMapping
	public ResponseEntity<Object> deleteUser(@RequestParam long id) throws UserNotFound{
			userService.deleteUser(id);
			return new ResponseEntity<>( "User Delete Successfully",HttpStatus.OK);
	}

	
	@PostMapping("/login")
    public ResponseEntity<User> loginUser(@Valid @RequestParam String email, @RequestParam String password) throws UserNotFound,PasswordMissmatchException {
		
        User user = userService.loginUser(email.toLowerCase(), password);
        // You can return a token or any other response based on successful login
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO user) {
	    try {
	        User updatedUser = userService.updateUser(id, user);
	        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	    } catch (UserNotFound e) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
	
}

