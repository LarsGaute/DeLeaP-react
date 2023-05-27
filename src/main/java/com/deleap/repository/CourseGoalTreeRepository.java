package com.deleap.repository;

import com.deleap.domain.CourseGoalTree;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseGoalTree entity.
 */
@Repository
public interface CourseGoalTreeRepository extends JpaRepository<CourseGoalTree, Long> {
    default Optional<CourseGoalTree> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CourseGoalTree> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CourseGoalTree> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct courseGoalTree from CourseGoalTree courseGoalTree left join fetch courseGoalTree.course left join fetch courseGoalTree.coursegoal",
        countQuery = "select count(distinct courseGoalTree) from CourseGoalTree courseGoalTree"
    )
    Page<CourseGoalTree> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct courseGoalTree from CourseGoalTree courseGoalTree left join fetch courseGoalTree.course left join fetch courseGoalTree.coursegoal"
    )
    List<CourseGoalTree> findAllWithToOneRelationships();

    @Query(
        "select courseGoalTree from CourseGoalTree courseGoalTree left join fetch courseGoalTree.course left join fetch courseGoalTree.coursegoal where courseGoalTree.id =:id"
    )
    Optional<CourseGoalTree> findOneWithToOneRelationships(@Param("id") Long id);
}
