package com.ecowatch.repository;

import com.ecowatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User WHERE u.reportsSubmitted >= :minReports")
    List<User> findTopReporters(@Param("minReports") int minReports);

    @Query("SELECT DISTINCT r.user FROM PollutionReport r WHERE r.reportedAt >= CURRENT_DATE - :days")
    List<User> findActiveUsers(@Param("minReports") int minReports);

    @Query("SELECT u FROM User u WHERE SIZE(u.badges) >= :badgeCount")
    List<User> findUsersByBadgeCount(@Param("badgeCount") int badgeCount);
}