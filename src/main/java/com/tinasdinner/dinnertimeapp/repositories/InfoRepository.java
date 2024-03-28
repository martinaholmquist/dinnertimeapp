package com.tinasdinner.dinnertimeapp.repositories;

import com.tinasdinner.dinnertimeapp.models.FamilyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InfoRepository extends JpaRepository<FamilyInfo, Integer> {


    //kollar om ddetta funkar
    @Query("SELECT f.preferences FROM FamilyInfo f")
    List<String> findAllPreferences();

    @Query("SELECT u.city FROM FamilyInfo u")
    List<String> findAllCities();
}
