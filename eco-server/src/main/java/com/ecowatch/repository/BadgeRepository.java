package com.ecowatch.repository;

import com.ecowatch.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long>{
    Optional<Badge> findByName(String name);

    List<Badge> findByType(Badge.BadgeType type);

    List<Badge> fndByRequiredReportsLessThanEqual(Integer reportCount);

    @Query("SELECT b FROM Badge b WHERE b.criteria LIKE %:criteria%")
    List<Badge> findByCriteriaContaining(@Param("criteria") String criteria);

    @Query("SELECT b FROM Badge b WHERE b NOT IN " + 
            "(SELECT ub FROM User u JOIN u.badges ub WHERE u.id = :userId)"
    )
    List<Badge> findUnearnedBadgesByUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Badges b WHERE b.requiredReports <= :userReportCount " +
           "AND b NOT IN (SELECT ub FROM User u JOIN u.badges ub WHERE u.id = :userId)"
    )

    List<Badge> findEligibleBadges(@Param("userId") Long userId,
                                    @Param("userReportCount") Integer userReportCount);

    @Query("SELECT u FROM User u JOIN u.badges WHERE b.id = badgeId")
    List<Object[]> findUsersWithBadge(@Param("badgeId") Long badgeId);
}