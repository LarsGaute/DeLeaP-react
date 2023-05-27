package com.deleap.repository;

import com.deleap.domain.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select course from Course course where course.user.login = ?#{principal.username}")
    List<Course> findByUserIsCurrentUser();

    default Optional<Course> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Course> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Course> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct course from Course course left join fetch course.user",
        countQuery = "select count(distinct course) from Course course"
    )
    Page<Course> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct course from Course course left join fetch course.user")
    List<Course> findAllWithToOneRelationships();

    @Query("select course from Course course left join fetch course.user where course.id =:id")
    Optional<Course> findOneWithToOneRelationships(@Param("id") Long id);
}
