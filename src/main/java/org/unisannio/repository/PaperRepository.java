package org.unisannio.repository;

import org.unisannio.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    // Custom query methods can be defined here if needed
}
