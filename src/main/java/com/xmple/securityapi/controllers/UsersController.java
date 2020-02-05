package com.xmple.securityapi.controllers;

import com.xmple.securityapi.entities.User;
import com.xmple.securityapi.exceptions.ResourceNotFoundException;
import com.xmple.securityapi.services.IUserService;
import com.xmple.securityapi.utils.PatchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonPatch;


@RestController
@RequestMapping("/api/users")
public class UsersController {


    @Autowired
    IUserService userService;

    @Autowired
    PatchHelper patchHelper;

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> updateContact(@PathVariable Integer id,
                                              @RequestBody JsonPatch patchDocument) throws Exception {

        // Find the model that will be patched

        User user = userService.findById(id).orElseThrow(ResourceNotFoundException::new);

        // Apply the patch
        User userPatched = patchHelper.patch(patchDocument, user, User.class);

        // Persist the changes
        userService.save(userPatched);

        // Return 204 to indicate the request has succeeded


        return ResponseEntity.noContent().build();
    }




}
