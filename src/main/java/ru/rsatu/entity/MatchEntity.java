package ru.rsatu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_guest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TeamEntity teamGuest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_host_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TeamEntity teamHost;

    @ManyToOne(optional = false)
    @JoinColumn(name = "referee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RefereeEntity referee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CityEntity city;

    private Integer stageType;
    private Integer phaseType;
    private Integer guestCount;
    private Integer hostCount;
    private LocalDateTime dateTime;
}
