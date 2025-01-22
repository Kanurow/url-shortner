package com.rowland.engineering.shortner.repository;

import com.rowland.engineering.shortner.entity.UrlMapping;
import com.rowland.engineering.shortner.entity.User;
import com.rowland.engineering.shortner.projections.UrlMappingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

//    boolean existsByShortUrl(String shortUrl);

    boolean existsByShortUrlAndUserId(String shortUrl, long user);

//    @Query("SELECT u FROM UrlMapping u WHERE u.user.id = :userId AND u.isDeleted = false")
//    List<UrlMapping> findActiveUrlsByUser(@Param("userId") long userId);

    @Query("SELECT u FROM UrlMapping u WHERE u.userId = :userId AND u.isDeleted = false")
    List<UrlMapping> findActiveUrlsByUser(@Param("userId") long userId);

    @Query("SELECT u.shortUrl AS shortUrl, u.originalUrl AS originalUrl, u.clickCount AS clickCount FROM UrlMapping u WHERE u.userId = :userId AND u.isDeleted = false")
    List<UrlMappingProjection> findActiveUrlsByUserProjection(@Param("userId") Long userId);

    @Query("SELECT u.shortUrl as shortUrl, u.originalUrl as originalUrl, u.clickCount as clickCount FROM UrlMapping u WHERE u.userId = :userId ")
    Page<UrlMappingProjection> getPagedUserUrls(@Param("userId") Long userId, PageRequest pageRequest);

    @Query("SELECT u FROM UrlMapping u WHERE u.userId =:userId AND u.shortUrl = :shortUrl")
    Optional<UrlMapping> findByShortUrlAndUserId(@Param("shortUrl") String shortUrl, @Param("userId") long userId);
}
