package com.project.AlgoLMS.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AlgoLMS.dto.UserDto;
import com.project.AlgoLMS.model.User;
import com.project.AlgoLMS.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return convertToDto(user);
    }

    public boolean registerUser(String email) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }
        String token = UUID.randomUUID().toString();
        
        User user = new User();
        user.setEmail(email);
        user.setConfirmationToken(token);
        userRepository.save(user);

        sendConfirmationEmail(user);

        return true;
    }

    private void sendConfirmationEmail(User user) {
        String confirmationLink = "http://localhost:8080/confirm?token=" + user.getConfirmationToken();
        String emailContent = "Pozdrav, " + user.getFullName() + "!\n"
                            + "Molimo kliknite na poveznicu ispod kako biste potvrdili svoju e-mail adresu:\n"
                            + confirmationLink;

        emailService.sendMessage(user.getEmail(), "Potvrdite svoju e-mail adresu | AlgoLMS", emailContent);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setRole(user.getRole());
        return userDto;
    }
}