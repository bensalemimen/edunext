package com.edunext.exam_service.Repository;

import com.edunext.exam_service.Model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findByExamTitleContainingIgnoreCase(String examTitle);

    // Trouver les examens dont la date d'expiration est avant la date donnée
    List<Exam> findByExpirationDateBefore(LocalDate date);
}
