package ru.rsatu.entity;

import lombok.Getter;

@Getter
public enum MatchStageType {
    GROUP_STAGE(1,"Групповой этап"),
    ROUND_OF_16(2,"1/8 финала"),
    QUARTER_FINAL(3,"1/4 финала"),
    SEMI_FINAL(4,"Полуфинал"),
    THIRD_PLACE(5,"Матч за 3-е место"),
    FINAL(6,"Финал");

    private final int value;
    private final String stage;

    MatchStageType(int value, String stage) {
        this.value = value;
        this.stage = stage;
    }

    public static String getStage(Integer value) {
        if (value != null) {
            return null;
        }
        return MatchStageType.values()[value].toString();
    }
}