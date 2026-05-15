package ru.rsatu.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity teamGuest;

    @OneToMany
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity teamHost;

    @ManyToOne
    @JoinColumn(name = "referee_id", nullable = false)
    private RefereeEntity referee;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;
}
