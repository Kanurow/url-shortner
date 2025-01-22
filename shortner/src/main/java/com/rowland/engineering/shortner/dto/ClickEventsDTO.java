package com.rowland.engineering.shortner.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClickEventsDTO {
    private LocalDate clickDate;
    private long count;
}
