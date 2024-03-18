package com.tinasdinner.dinnertimeapp.familyInformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository extends JpaRepository<FamilyInfo, Integer> {
}
