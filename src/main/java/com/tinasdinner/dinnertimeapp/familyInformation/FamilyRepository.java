package com.tinasdinner.dinnertimeapp.familyInformation;

import com.tinasdinner.dinnertimeapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository  extends JpaRepository<User, Integer> {
    @Query("SELECT f.preferences FROM FamilyInfo f")
    List<String> findAllPreferences();

    @Query("SELECT u.city FROM FamilyInfo u")
    List<String> findAllCities();
}
