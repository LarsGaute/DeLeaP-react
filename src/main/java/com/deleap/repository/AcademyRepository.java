package com.deleap.repository;

import com.deleap.domain.Academy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Academy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {}
