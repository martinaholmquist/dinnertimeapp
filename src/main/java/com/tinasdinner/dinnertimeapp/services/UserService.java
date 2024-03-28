package com.tinasdinner.dinnertimeapp.services;


import com.tinasdinner.dinnertimeapp.models.FamilyInfo;

import com.tinasdinner.dinnertimeapp.repositories.InfoRepository;
import com.tinasdinner.dinnertimeapp.records.AllUserInformationRecord;
import com.tinasdinner.dinnertimeapp.records.ChangePasswordReq;
import com.tinasdinner.dinnertimeapp.records.UserViewRecord;
import com.tinasdinner.dinnertimeapp.repositories.UserRepository;
import com.tinasdinner.dinnertimeapp.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final LogoutService logoutService;

    //private final FamilyRepository familyRepository;
    private final InfoRepository infoRepository;


    public Optional UserInformation (Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> userr = repository.findByEmail(ofConnectedUser.getEmail());
        return userr;
    }

    public User UserInformation2 (Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var user = repository.findByEmail(ofConnectedUser.getEmail())
                .orElseThrow();  //handle correct exception and handle if exceptions
        return User.builder().build();
    }


    public void changePassword(ChangePasswordReq request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }


    public UserViewRecord findConnectedUser(Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> optionalUser = repository.findByEmail(ofConnectedUser.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

                    // Get authorities from SecurityContextHolder
                    List<String> authorities = SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());

                    return new UserViewRecord(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole(),
                            authorities
                    );
        } else {
            throw new RuntimeException("user not found i min metod....");
        }
    }


    public List<AllUserInformationRecord> AllUserInformationRecord() {
        List<User> users = repository.findAll();
        List<AllUserInformationRecord> userRecords = users.stream()
                .map(user -> {
                    FamilyInfo familyInfo = user.getFamilyinfo();
                    String city = familyInfo != null ? familyInfo.getCity() : null;
                    int familymembers = familyInfo != null ? familyInfo.getFamilymembers() : 0;
                    String information = familyInfo != null ? familyInfo.getInformation() : null;
                    String preferences = familyInfo != null ? familyInfo.getPreferences() : null;



                    return new AllUserInformationRecord(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            city,
                            familymembers,
                            information,
                            preferences

                    );
                })
                .collect(Collectors.toList());

        return userRecords;
    }

    public List<String> getCities() throws IOException {
       // List<String> cityList = familyRepository.findAllCities();
        List<String> cityList = infoRepository.findAllCities();

        return cityList;
    }

    public List<String> getPreferences() throws IOException {
       // List<String> preferenceList = familyRepository.findAllPreferences();
        List<String> preferenceList = infoRepository.findAllPreferences();


        return preferenceList;
    }

    public void addUserInfo(FamilyInfo request,     Principal connectedUser) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Optional<User> optionalUser = repository.findByEmail(ofConnectedUser.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Integer id = user.getId();
            String city = request.getCity();
            String info = request.getInformation();
            String pref = request.getPreferences();
            int menb = request.getFamilymembers();
            FamilyInfo infoFromUser = new FamilyInfo();
            infoFromUser.setUser(user);
            infoFromUser.setCity(city);
            infoFromUser.setFamilymembers(menb);
            infoFromUser.setInformation(info);
            infoFromUser.setPreferences(pref);

            infoRepository.save(infoFromUser);
            System.out.println("här skapas infoFromUser" + infoFromUser);

        } else {
            throw new RuntimeException("user not found i min metod....");
        }

    }


    public void deleteUser(int id, Principal connectedUser) {
            var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            repository.deleteById(id);
    }



    public void deactivateAccountWithLogOut(Principal connectedUser, HttpServletRequest request,
                                  HttpServletResponse response) {
        var ofConnectedUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        User userToDeactivate = repository.findByEmail(ofConnectedUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found in my method...."));

        userToDeactivate.setActive(false);
        repository.save(userToDeactivate);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutService.logout(request, response, authentication);
    }








}

