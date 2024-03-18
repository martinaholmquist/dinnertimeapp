package com.tinasdinner.dinnertimeapp.controllers;

import com.tinasdinner.dinnertimeapp.records.AllUserInformationRecord;
import com.tinasdinner.dinnertimeapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;



@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService service;




    @GetMapping("/alluserinfo")
  //  @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<AllUserInformationRecord>> AllUserInformationRecord() {
        List<AllUserInformationRecord> userRecords = service.AllUserInformationRecord();
        if (userRecords.isEmpty()) {
            System.out.println("Här kommer userRecords:" + userRecords);
            return new ResponseEntity<>(userRecords, HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(userRecords, HttpStatus.OK);
        }
    }


    @DeleteMapping("/deleteuser/{id}")
   // @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable int id, Principal connectedUser) {
        service.deleteUser(id, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

