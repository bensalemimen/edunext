package com.edunext.exam_service.Service;

import com.edunext.exam_service.Model.Exam;
import com.edunext.exam_service.Model.Question;
import com.edunext.exam_service.Repository.ExamRepository;
import com.edunext.exam_service.Repository.QuestionRepository;
import com.edunext.exam_service.dto.ExamSubmissionDTO;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Optional<Exam> getExamById(int id) {
        return examRepository.findById(id);
    }

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }


    public Exam updateExam(int id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));

        exam.setExamTitle(examDetails.getExamTitle());
        exam.setExamDescription(examDetails.getExamDescription());
        exam.setExamDuration(examDetails.getExamDuration());
        exam.setTotalMarks(examDetails.getTotalMarks());
        exam.setPassingScore(examDetails.getPassingScore());
        exam.setScheduledDate(examDetails.getScheduledDate());

        return examRepository.save(exam);
    }


    public void deleteExam(int id) {
        examRepository.deleteById(id);
    }
    public Exam addQuestionToExam(int idExam, Question question) {
        Exam exam = examRepository.findById(idExam).orElseThrow(() -> new RuntimeException("Exam not found"));
        question.setExam(exam);
        exam.getQuestions().add(question);
        return examRepository.save(exam);
    }

    public Exam getExamWithQuestions(int idExam) {
        return examRepository.findById(idExam).orElseThrow(() -> new RuntimeException("Exam not found"));
    }
    public Question updateQuestion(Long questionId, Question questionDetails) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setQuestionText(questionDetails.getQuestionText());
        question.setAnswerOptions(questionDetails.getAnswerOptions());

        return questionRepository.save(question);
    }
    public List<Question> getQuestionsForExam(int examId) {
        return questionRepository.findByExam_IdExam(examId);
    }

    public int correctExam(ExamSubmissionDTO submission) {
        Exam exam = examRepository.findById(submission.examId)
                .orElseThrow(() -> new RuntimeException("Examen non trouvé"));


        List<Question> questions = questionRepository.findByExam_IdExam(submission.examId);
        int score = 0;
        int totalMarksPerQuestion = exam.getTotalMarks() / questions.size();

        for (Question question : questions) {
            String userAnswer = submission.answers.get(question.getId());
            if (question.getAnswerOptions().equalsIgnoreCase(userAnswer)) {
                score += totalMarksPerQuestion;
            }
        }
        return score;
    }
    public void deactivateExpiredExams() {
        List<Exam> allExams = examRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Exam exam : allExams) {
            if (exam.getExpirationDate() != null && exam.getExpirationDate().isBefore(today)) {
                exam.setActive(false);
                examRepository.save(exam);
            }
        }
    }

    public int submitExam(int examId, Map<Long, String> userAnswers) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Examen introuvable"));

        int score = 0;

        for (Question question : exam.getQuestions()) {
            String correctAnswer = question.getCorrectAnswer();
            String userAnswer = userAnswers.get(question.getId());

            // Normaliser la réponse de l'utilisateur : extraire la première lettre
            if (userAnswer != null && userAnswer.contains(")")) {
                userAnswer = userAnswer.split("\\)")[0].trim();  // Extrait "C" de "C) 32 bits"
            }

            // Debugging pour voir les réponses après normalisation
            System.out.println("Question ID: " + question.getId() + ", Réponse correcte: " + correctAnswer + ", Réponse de l'utilisateur: " + userAnswer);

            if (correctAnswer != null && correctAnswer.equals(userAnswer)) {
                score++;
            }
        }

        System.out.println("Score final : " + score);
        return score;
    }
    public List<Exam> searchExamsByTitle(String title) {
        return examRepository.findByExamTitleContainingIgnoreCase(title);
    }
    public ByteArrayResource generateExamPdf(int examId) {
        try {
            Exam exam = getExamWithQuestions(examId);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Titre
            document.add(new Paragraph("EXAMEN")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" "));

            // Infos générales
            document.add(new Paragraph("Titre : " + exam.getExamTitle()).setBold());
            document.add(new Paragraph("Description : " + exam.getExamDescription()));
            document.add(new Paragraph("Durée : " + exam.getExamDuration() + " minutes"));
            document.add(new Paragraph("Date prévue : " + exam.getScheduledDate()));
            document.add(new Paragraph("Total de points : " + exam.getTotalMarks()));
            document.add(new Paragraph("Score minimum : " + exam.getPassingScore()));
            document.add(new Paragraph(" "));

            // Questions
            int i = 1;
            for (Question q : exam.getQuestions()) {
                document.add(new Paragraph(i++ + ". " + q.getQuestionText()).setBold());
                document.add(new Paragraph("   Choix : " + q.getAnswerOptions()));
                document.add(new Paragraph(" "));
            }

            document.close();
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
    // Méthode pour compter les examens dont la date d'expiration est avant ou après une date donnée
    public long countExamsByDate(LocalDate date) {
        // Trouver tous les examens dont la date d'expiration est avant ou égale à la date spécifiée
        List<Exam> exams = examRepository.findByExpirationDateBefore(date);

        // Retourner le nombre d'examens
        return exams.size();
    }



}
