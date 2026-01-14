package com.ecowatch.repository;

import com.ecowatch.model.PollutionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PollutionReportRepository extends JpaRepository<PollutionReport, Long>{

    List<PollutionReport> findByUserId(Long userId);

    List<PollutionReport> findByPollutionType(PollutionReport.PollutionType pollutionType);

    List<PollutionReport> findBySeverity(PollutionReport.Severity severity);

    @Query("SELECT r FROM PollutionReport r WHERE " +
            "(:type IS NULL OR r.pollutionType = :type) AND " +
            "(:severity IS NULL OR r.severity = :severity) AND " +
            "(:verified IS NULL OR r.verified = :verified) AND " +
            "ORDER BY r.reportedAt DESC"
    )
    List<PollutionReport> findFilteredReports(
        @Param("type") PollutionReport.PollutionType type,
        @Param("severity") PollutionReport.severity severity,
        @Param("verified") Boolean verified
    );

    @Query(value = "SELECT * FROM pollution_reports " + 
            "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " + 
            "cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * " + "sin(radians(latitude))))) < :radius",
            nativeQuery = true)
    List<PollutionReport> findReportsNearLocation(
        @Param("lat") double latitude,
        @Param("lng") double longitude,
        @Param("radius") double radiusInKm
    )

    @Query("SELECT r FROM PollutionReport r WHERE r.reportedAt BETWEEN :startDate AND :endDate")
    List<PollutionReport> findReportByDateRange(
        @Param("startDate") LocalDateTime startDate;
        @Param("endDate") LocalDateTime endDate;
    )

    List<PollutionReport> findByOrderByUpvotesDesc();

    List<PollutionReport> findByVerifiedFalse();

    @Query("SELECT COUNT(r) FROM PollutionReport r WHERE r.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT r.pollutionType, COUNT(r) FROM PollutionReport r " +
            "WHERE r.reportedAt >=  :startDate GROUP BY r.pollutionType"
    )
    List<Object[]> countByTypeSinceDate(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT r FROM PollutionReport r " +
            "WHERE r.reportedAt >= :weekAgo " +
            "ORDER BY r.upvotes DESC, r.reportedAt DESC"
    )
    List<PollutionReport> findTrendingReports(@Param("weekAgo") LocalDateTime weekAgo);
}