package com.deleap.repository;

import com.deleap.domain.AcademyCourseRelation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AcademyCourseRelation entity.
 */
@Repository
public interface AcademyCourseRelationRepository extends JpaRepository<AcademyCourseRelation, Long> {
    default Optional<AcademyCourseRelation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AcademyCourseRelation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AcademyCourseRelation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct academyCourseRelation from AcademyCourseRelation academyCourseRelation left join fetch academyCourseRelation.adademy left join fetch academyCourseRelation.course",
        countQuery = "select count(distinct academyCourseRelation) from AcademyCourseRelation academyCourseRelation"
    )
    Page<AcademyCourseRelation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct academyCourseRelation from AcademyCourseRelation academyCourseRelation left join fetch academyCourseRelation.adademy left join fetch academyCourseRelation.course"
    )
    List<AcademyCourseRelation> findAllWithToOneRelationships();

    @Query(
        "select academyCourseRelation from AcademyCourseRelation academyCourseRelation left join fetch academyCourseRelation.adademy left join fetch academyCourseRelation.course where academyCourseRelation.id =:id"
    )
    Optional<AcademyCourseRelation> findOneWithToOneRelationships(@Param("id") Long id);
}
