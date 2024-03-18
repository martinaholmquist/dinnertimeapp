package com.tinasdinner.dinnertimeapp.familyInformation;

import com.tinasdinner.dinnertimeapp.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "familyinfo")
public class FamilyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String information;
    private int familymembers;
    private String preferences;
    /*@ElementCollection
    private List<String> preferences;*/




    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Override
    public String toString() {
        return "FamilyInfo{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", information='" + information + '\'' +
                ", familymembers=" + familymembers +
                ", preferences='" + preferences + '\'' +
                // Lägg till andra fält här om det behövs
                '}';
    }
}
