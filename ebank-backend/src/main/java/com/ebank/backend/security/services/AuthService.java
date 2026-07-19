package com.ebank.backend.security.services;

import com.ebank.backend.security.dtos.ChangePasswordDTO;
import com.ebank.backend.security.dtos.LoginRequestDTO;
import com.ebank.backend.security.dtos.LoginResponseDTO;
import com.ebank.backend.security.dtos.RegisterRequestDTO;
import com.ebank.backend.security.entities.AppRole;
import com.ebank.backend.security.entities.AppUser;
import com.ebank.backend.security.jwt.JwtUtils;
import com.ebank.backend.security.repositories.RoleRepository;
import com.ebank.backend.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AppUser register(RegisterRequestDTO request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already taken: " + request.getUsername());
        });

        AppRole userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(AppRole.builder().roleName("ROLE_USER").build()));

        Set<AppRole> roles = new HashSet<>();
        roles.add(userRole);

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        String token = jwtUtils.generateToken(user.getUsername(), user.getAuthorities());

        return new LoginResponseDTO(user.getUsername(), token, roles);
    }

    public void changePassword(String username, ChangePasswordDTO request) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
