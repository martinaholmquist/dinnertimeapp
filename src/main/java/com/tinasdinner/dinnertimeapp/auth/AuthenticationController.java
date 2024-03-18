package com.tinasdinner.dinnertimeapp.auth;

import com.tinasdinner.dinnertimeapp.records.AuthenticationReq;
import com.tinasdinner.dinnertimeapp.records.RegisterReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {


    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterReq request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationReq request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


}
