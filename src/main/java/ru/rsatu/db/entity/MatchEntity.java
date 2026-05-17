package ru.rsatu.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_guest_id", nullable = false)
    private TeamEntity teamGuest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_host_id", nullable = false)
    private TeamEntity teamHost;

    @ManyToOne(optional = false)
    @JoinColumn(name = "referee_id", nullable = false)
    private RefereeEntity referee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;
}
