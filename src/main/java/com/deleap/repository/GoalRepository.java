package com.deleap.repository;

import com.deleap.domain.Goal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Goal entity.
 */
@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    default Optional<Goal> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Goal> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Goal> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct goal from Goal goal left join fetch goal.course",
        countQuery = "select count(distinct goal) from Goal goal"
    )
    Page<Goal> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct goal from Goal goal left join fetch goal.course")
    List<Goal> findAllWithToOneRelationships();

    @Query("select goal from Goal goal left join fetch goal.course where goal.id =:id")
    Optional<Goal> findOneWithToOneRelationships(@Param("id") Long id);
}
