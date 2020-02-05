package com.xmple.securityapi.controllers;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRest {

    @GetMapping(value="/api/test/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(" hasRole('USER') or hasRole('ADMIN')  and hasAuthority('WRITE')")
    public String userAccess() { return ">>> User Contents!"; }

    @GetMapping(value="/api/test/pm",produces = MediaType.APPLICATION_JSON_VALUE )
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    public String projectManagementAccess() {
        return ">>> Board Management Project";
    }


    @GetMapping(value="/api/test/admin",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return ">>> Admin Contents";
    }
}