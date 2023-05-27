package com.deleap.repository;

import com.deleap.domain.CourseGoalRelations;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseGoalRelations entity.
 */
@Repository
public interface CourseGoalRelationsRepository extends JpaRepository<CourseGoalRelations, Long> {
    default Optional<CourseGoalRelations> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CourseGoalRelations> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CourseGoalRelations> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct courseGoalRelations from CourseGoalRelations courseGoalRelations left join fetch courseGoalRelations.course left join fetch courseGoalRelations.goal left join fetch courseGoalRelations.parent",
        countQuery = "select count(distinct courseGoalRelations) from CourseGoalRelations courseGoalRelations"
    )
    Page<CourseGoalRelations> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct courseGoalRelations from CourseGoalRelations courseGoalRelations left join fetch courseGoalRelations.course left join fetch courseGoalRelations.goal left join fetch courseGoalRelations.parent"
    )
    List<CourseGoalRelations> findAllWithToOneRelationships();

    @Query(
        "select courseGoalRelations from CourseGoalRelations courseGoalRelations left join fetch courseGoalRelations.course left join fetch courseGoalRelations.goal left join fetch courseGoalRelations.parent where courseGoalRelations.id =:id"
    )
    Optional<CourseGoalRelations> findOneWithToOneRelationships(@Param("id") Long id);
}
