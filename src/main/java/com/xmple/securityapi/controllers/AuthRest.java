package com.xmple.securityapi.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.xmple.securityapi.dtos.JwtResponse;
import com.xmple.securityapi.dtos.Login;
import com.xmple.securityapi.dtos.Refresh;
import com.xmple.securityapi.dtos.SignUp;
import com.xmple.securityapi.entities.*;
import com.xmple.securityapi.repositories.IPermissionRepository;
import com.xmple.securityapi.repositories.IRoleRepository;
import com.xmple.securityapi.repositories.IUserRepository;
import com.xmple.securityapi.security.jwt.JwtProvider;
import com.xmple.securityapi.services.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthRest {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    IPermissionRepository permissionRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    IRefreshTokenService refreshTokenService;


    @PostMapping(value="/signin",consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody Login loginRequest) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername()).get();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Auth-Token", jwt);
        responseHeaders.add("Refresh-Token",jwtProvider.generateRefreshToken(user,jwt,refreshTokenService) );


        //return new ResponseEntity<>("authenticated",responseHeaders, HttpStatus.OK);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("authenticated");
    }


    @PostMapping(value="/refresh",consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> refreshUser(@Valid @RequestBody Refresh refresh) throws Exception {

        RefreshToken refreshToken = refreshTokenService.findById(refresh.getRefreshToken()).get();
        String tokenId =jwtProvider.getJwtTokenIdForRefresh(refresh.getToken());

        if(refreshToken.getToken().equals(tokenId) ){

            User user = refreshToken.getUser();

            String jwt = jwtProvider.generateJwtTokenByRefresh(user);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Auth-Token", jwt);
            responseHeaders.add("Refresh-Token",jwtProvider.generateRefreshToken(user,jwt,refreshTokenService) );

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body("refreshed");
        } else{
            return ResponseEntity.badRequest().body("Error");
        }

    }



    @PostMapping(value="/signup",consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUp signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));



       //Roles
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "pm":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);


        //Permissions
        Set<String> strPermissions = signUpRequest.getPermission();
        Set<Permission> permissions = new HashSet<>();

        if(strPermissions != null) {
            strPermissions.forEach(permission -> {
                switch (permission) {
                    case "WRITE":
                        Permission writePermission = permissionRepository.findByName(PermissionName.WRITE)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Permission not find."));
                        permissions.add(writePermission);
                        break;
                }
            });

            user.setPermissions(permissions);
        }

        userRepository.save(user);









        return ResponseEntity.ok().body("User registered successfully!");
    }
}