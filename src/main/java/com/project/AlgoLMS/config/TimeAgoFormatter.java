package com.project.AlgoLMS.config;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class TimeAgoFormatter {

    public String formatTimeAgo(Timestamp createdAt) {
        LocalDateTime createdAtLocalDateTime = createdAt.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAtLocalDateTime, now);
        Period period = Period.between(createdAtLocalDateTime.toLocalDate(), now.toLocalDate());

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        int months = period.getMonths();
        int years = period.getYears();

        if (seconds < 60) {
            return "prije " + seconds + " s";
        } else if (minutes < 60) {
            return "prije " + minutes + " min";
        } else if (hours < 24) {
            return "prije " + hours + " h";
        } else if (days < 30) {
            return "prije " + days + " d";
        } else if (months < 12) {
            return "prije " + months + " mj";
        } else {
            return "prije " + years + " g";
        }
    }
}