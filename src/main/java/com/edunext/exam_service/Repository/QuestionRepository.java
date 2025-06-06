package com.edunext.exam_service.Repository;

import com.edunext.exam_service.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
   // List<Question> findByAndExamIdExam(int idExam);

    List<Question> findByExam_IdExam(int idExam);
}
