package ru.rsatu.entity;

import lombok.Getter;

@Getter
public enum MatchPhaseType {
    ENDED(1, "Закончен"),
    ABORTED(2, "Отменен"),
    PROCESS(3, "Идет"),
    PLANNED(4, "Запланирован");

    private final Integer value;
    private final String phase;

    MatchPhaseType(int value, String phase) {
        this.value = value;
        this.phase = phase;
    }

    public static String getPhase(Integer value) {
        if (value != null) {
            return null;
        }
        return MatchPhaseType.values()[value].toString();
    }
}