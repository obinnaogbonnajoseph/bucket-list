package com.obinna.bucketlist.serviceImpl;

import com.obinna.bucketlist.dto.LoginRequestDto;
import com.obinna.bucketlist.model.User;
import com.obinna.bucketlist.repository.UserRepository;
import com.obinna.bucketlist.security.JwtTokenProvider;
import com.obinna.bucketlist.service.UserService;
import com.obinna.bucketlist.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String signIn(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException("User with username: '" + username + "' does not exist", HttpStatus.NOT_FOUND))
                    .getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String signUp(User user) {
        if (!userRepository.findByUsername(user.getUsername()).isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            if(user.getId() >= 0) {
                return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            } else throw new CustomException("User could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public User currentUser(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)))
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }
}
