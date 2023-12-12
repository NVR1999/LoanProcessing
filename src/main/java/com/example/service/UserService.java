package com.example.service;


//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.UserDTO;
import com.example.entity.User;
import com.example.exceptionHandler.CustomException;
import com.example.exceptionHandler.PasswordMissmatchException;
import com.example.exceptionHandler.UserNotFound;
import com.example.repository.UserRepository;

import jakarta.validation.Valid;



@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public User createUser(@Valid  UserDTO user) throws CustomException {
      // Validate if username is unique
		
      if (userRepo.existsByEmail(user.getEmail())) {
          throw new CustomException("Username already exists");
      }
		
      User u1 = new User();
      u1.setName(user.getName());
      u1.setPassword(user.getPassword());
      u1.setEmail(user.getEmail().toLowerCase());

      return userRepo.save(u1);
  }
	
	public void deleteUser(long id) throws UserNotFound{
		
		User user = userRepo.findById(id).orElseThrow(()->new UserNotFound("Not Found"));
		userRepo.deleteById(id);
//		Optional<User> op = userRepo.findById(id);
//		if(op.isPresent()) {
//			userRepo.deleteById(id);
//		}
//		else {
//			throw new UserNotFound("Not Found");
//		}
		
	}
	
	public User loginUser(@Valid String email, String password) throws UserNotFound,PasswordMissmatchException {
      User user = userRepo.findByEmail(email.toLowerCase())
              .orElseThrow(() -> new UserNotFound("User not found"));

      if (!user.getPassword().equals(password)) {
          throw new PasswordMissmatchException("Invalid password");
      }

      return user;
  }
	

	public User updateUser(Long id, UserDTO user) throws UserNotFound {
	    User existingUser = userRepo.findById(id)
	            .orElseThrow(() -> new UserNotFound("User not found"));

	    existingUser.setName(user.getName());
	    existingUser.setEmail(user.getEmail());
	    existingUser.setPassword(user.getPassword());

	    return userRepo.save(existingUser);
	}
	
	

}
