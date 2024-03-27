package com.tinasdinner.dinnertimeapp.controllers;



import com.tinasdinner.dinnertimeapp.familyInformation.FamilyInfo;
import com.tinasdinner.dinnertimeapp.models.User;
import com.tinasdinner.dinnertimeapp.records.AllUserInformationRecord;
import com.tinasdinner.dinnertimeapp.records.ChangePasswordReq;
import com.tinasdinner.dinnertimeapp.services.LogoutService;

import com.tinasdinner.dinnertimeapp.records.UserViewRecord;

import com.tinasdinner.dinnertimeapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
//@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequiredArgsConstructor

public class UserController {
    private final UserService service;
    private final LogoutService logoutService;

    @PatchMapping ("/changepassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordReq request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currentuser")  //FUNKAR
    public ResponseEntity<UserViewRecord> CurrentUser(Principal connectedUser) {
        UserViewRecord currentUser = service.findConnectedUser(connectedUser);
        System.out.println("Här kommer en användares förnamn:" + currentUser.username());
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> Profile (Principal connectedUser)  throws IOException {
        User currentUser = service.UserInformation2(connectedUser);
        return ResponseEntity.ok(currentUser);

    }
    @GetMapping("/alluserinfo")
    //@PreAuthorize("hasAnyRole('User')")
    public ResponseEntity<List<AllUserInformationRecord>> AllUserInformationRecordOne() {
        List<AllUserInformationRecord> userRecords = service.AllUserInformationRecord();
        if (userRecords.isEmpty()) {
            System.out.println("Här kommer userRecords:" + userRecords);
            return new ResponseEntity<>(userRecords, HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(userRecords, HttpStatus.OK);
        }
    }


    @GetMapping("/allusers")
    public ResponseEntity<List<AllUserInformationRecord>> AllUserInformationRecord(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "preferences", required = false) String preferences
    ) throws IOException {
        System.out.println("City: " + city);
        System.out.println("Preferences: " + preferences);
        List<AllUserInformationRecord> resultOfAllList = service.AllUserInformationRecord();

        if (city != null || preferences != null) {
            resultOfAllList = resultOfAllList.stream()
                    .filter(user ->
                            (city == null || (user.city() != null && user.city().equalsIgnoreCase(city))) &&
                                    (preferences == null || (user.preferences() != null && user.preferences().equalsIgnoreCase(preferences)))
                    )
                    .collect(Collectors.toList());
        }

        if (!resultOfAllList.isEmpty()) {
            return ResponseEntity.ok(resultOfAllList);
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/cities")
    public ResponseEntity<List<String>> listAllCities() throws IOException {
        List<String> cities = service.getCities();

        // Filtrera unika städer
        List<String> uniqueCities = cities.stream()
                .filter(city -> city != null)  // Filtrera bort null-städer
                .distinct()
                .collect(Collectors.toList());

        if (!uniqueCities.isEmpty()) {
            return ResponseEntity.ok(uniqueCities);
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/preferences")
    public ResponseEntity<List<String>> listAllPreferences() throws IOException {
        List<String> preferences = service.getPreferences();

        // Filtrera unika users som test bara, här ska vara matpreferenser sen....
        List<String> uniquePreferences = preferences.stream()
                .distinct()
                .collect(Collectors.toList());

        if (!uniquePreferences.isEmpty()) {
            return ResponseEntity.ok(uniquePreferences);
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping("/logout")  //funkar
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/deactivateaccountwithlogout")
    public ResponseEntity<String> deactivateAccountWithLogOut(Principal connectedUser, HttpServletRequest request, HttpServletResponse response) {
        service.deactivateAccountWithLogOut(connectedUser, request, response);
        return ResponseEntity.ok("Deactivateaccount successfully");
    }

    @PostMapping("/adduserinfo")
    public ResponseEntity<Void> AddUserInfo(@RequestBody FamilyInfo request, Principal connectedUser) {
        service.addUserInfo(request, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
        //FamilyInfo familyInfo = service.addUserInfo(request, connectedUser);
        //System.out.println("Här kommer en användares info:" + familyInfo);
        //return new ResponseEntity<>(familyInfo, HttpStatus.OK);
    }



}
