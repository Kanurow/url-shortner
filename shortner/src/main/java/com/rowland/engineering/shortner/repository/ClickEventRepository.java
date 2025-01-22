package com.rowland.engineering.shortner.repository;

import com.rowland.engineering.shortner.entity.ClickEvent;
import com.rowland.engineering.shortner.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    List<ClickEvent>
    findByUrlMappingAndClickDateBetween(UrlMapping urlMapping,
                                                         LocalDateTime startDate, LocalDateTime endDate);

    List<ClickEvent>
    findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappings,
                                        LocalDateTime startDate, LocalDateTime endDate);

}
