package com.edunext.exam_service.Repository;

import com.edunext.exam_service.Model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
   /* List<Certificate> findByExam_IdExam(int idExam);*/
   @Query("SELECT c.title, COUNT(c.id) as count FROM Certificate c GROUP BY c.title ORDER BY count DESC")
   List<Object[]> findTop10Certificates(Pageable pageable);
}

