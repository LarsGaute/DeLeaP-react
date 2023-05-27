package com.deleap.repository;

import com.deleap.domain.Curriculum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Curriculum entity.
 */
@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
    default Optional<Curriculum> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Curriculum> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Curriculum> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct curriculum from Curriculum curriculum left join fetch curriculum.goal",
        countQuery = "select count(distinct curriculum) from Curriculum curriculum"
    )
    Page<Curriculum> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct curriculum from Curriculum curriculum left join fetch curriculum.goal")
    List<Curriculum> findAllWithToOneRelationships();

    @Query("select curriculum from Curriculum curriculum left join fetch curriculum.goal where curriculum.id =:id")
    Optional<Curriculum> findOneWithToOneRelationships(@Param("id") Long id);
}
