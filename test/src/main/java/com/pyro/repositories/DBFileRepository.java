package com.pyro.repositories;

import com.pyro.entities.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, Long> {
    DBFile findByFileName(String filename);
}