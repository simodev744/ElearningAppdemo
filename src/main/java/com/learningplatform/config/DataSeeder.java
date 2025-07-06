package com.learningplatform.config;

import com.learningplatform.entity.*;
import com.learningplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CourseRepository courseRepository;
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final ProjectRepository projectRepository;
    private final CertificateRepository certificateRepository;
    private final PasswordEncoder passwordEncoder;

    private static final List<String> COURSE_CATEGORIES = Arrays.asList(
            "Programming", "Mathematics", "Science", "Business", "Art",
            "Language", "History", "Music", "Health", "Engineering"
    );

    private static final List<String> ARTICLE_CATEGORIES = Arrays.asList(
            "Technology", "Education", "Career", "Productivity", "Learning Tips",
            "Industry News", "Study Techniques", "Online Learning", "Programming", "Data Science"
    );

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        // Create admin user
        User admin = User.builder()
                .nom("Admin User")
                .email("admin@learning.com")
                .motDePasse(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .actif(true)
                .build();
        userRepository.save(admin);

        // Create 20 formateurs
        List<User> formateurs = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> User.builder()
                        .nom("Formateur " + i)
                        .email("formateur" + i + "@learning.com")
                        .motDePasse(passwordEncoder.encode("password"))
                        .role(Role.FORMATEUR)
                        .actif(true)
                        .build())
                .collect(Collectors.toList());
        userRepository.saveAll(formateurs);

        // Create 100 students
        List<User> students = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> User.builder()
                        .nom("Student " + i)
                        .email("student" + i + "@learning.com")
                        .motDePasse(passwordEncoder.encode("password"))
                        .role(Role.ETUDIANT)
                        .actif(true)
                        .build())
                .collect(Collectors.toList());
        userRepository.saveAll(students);

        // Combine all users
        List<User> allUsers = new ArrayList<>();
        allUsers.add(admin);
        allUsers.addAll(formateurs);
        allUsers.addAll(students);

        // Create 50 courses
        List<Course> courses = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Course course = Course.builder()
                    .titre("Course " + i + ": " + getRandomCourseTitle())
                    .categorie(getRandomCategory(COURSE_CATEGORIES))
                    .statut(i % 5 == 0 ? CourseStatus.EN_ATTENTE : (i % 7 == 0 ? CourseStatus.REJETE : CourseStatus.VALIDE))
                    .estPayant(i % 3 == 0)
                    .formateur(getRandomUser(formateurs))
                    .build();
            courses.add(course);
        }
        courseRepository.saveAll(courses);

        // Create 100 articles
        List<Article> articles = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Article article = Article.builder()
                    .titre("Article " + i + ": " + getRandomArticleTitle())
                    .contenu(generateArticleContent(i))
                    .categorie(getRandomCategory(ARTICLE_CATEGORIES))
                    .author(getRandomUser(formateurs))
                    .build();
            articles.add(article);
        }
        articleRepository.saveAll(articles);

        // Create 300 comments on articles
        List<Comment> comments = new ArrayList<>();
        for (int i = 1; i <= 300; i++) {
            Comment comment = Comment.builder()
                    .contenu("This is comment #" + i + " on this article. " + getRandomCommentText())
                    .article(getRandomArticle(articles))
                    .author(getRandomUser(allUsers))
                    .build();
            comments.add(comment);
        }
        commentRepository.saveAll(comments);

        // Create 50 quizzes (1 per course)
        List<Quiz> quizzes = new ArrayList<>();
        for (Course course : courses) {
            Quiz quiz = Quiz.builder()
                    .titre("Quiz for " + course.getTitre())
                    .course(course)
                    .build();
            quizzes.add(quiz);
        }
        quizRepository.saveAll(quizzes);

        // Create 500 quiz results (students taking quizzes)
        List<QuizResult> quizResults = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Quiz quiz = getRandomQuiz(quizzes);
            int totalQuestions = 10; // Assuming 10 questions per quiz
            int score = ThreadLocalRandom.current().nextInt(0, totalQuestions + 1);

            QuizResult result = QuizResult.builder()
                    .user(getRandomUser(students))
                    .quiz(quiz)
                    .score(score)
                    .totalQuestions(totalQuestions)
                    .build();
            quizResults.add(result);
        }
        quizResultRepository.saveAll(quizResults);

        // Create 200 projects
        List<Project> projects = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            ProjectStatus status = getRandomProjectStatus();
            User correctedBy = status == ProjectStatus.CORRIGE ? getRandomUser(formateurs) : null;
            LocalDateTime correctedAt = status == ProjectStatus.CORRIGE ? LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 30)) : null;
            Integer note = status == ProjectStatus.CORRIGE ? ThreadLocalRandom.current().nextInt(50, 101) : null;
            String commentaire = status == ProjectStatus.CORRIGE ? "Good work on project " + i + ". " + getRandomFeedback() : null;

            Project project = Project.builder()
                    .course(getRandomCourse(courses))
                    .student(getRandomUser(students))
                    .description("Description for project " + i + ". " + getRandomProjectDescription())
                    .fileUrl("https://example.com/projects/file" + i + ".zip")
                    .projectLink("https://github.com/student/project" + i)
                    .status(status)
                    .note(note)
                    .commentaire(commentaire)
                    .correctedBy(correctedBy)
                    .correctedAt(correctedAt)
                    .build();
            projects.add(project);
        }
        projectRepository.saveAll(projects);

        // Create 100 certificates
        List<Certificate> certificates = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Course course = getRandomCourse(courses);
            User student = getRandomUser(students);

            Certificate certificate = Certificate.builder()
                    .student(student)
                    .course(course)
                    .certificateNumber("CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                    .pdfPath("/certificates/" + student.getId() + "/" + course.getId() + ".pdf")
                    .build();
            certificates.add(certificate);
        }
        certificateRepository.saveAll(certificates);
    }

    // Helper methods remain the same as previous version
    private String getRandomCourseTitle() {
        String[] prefixes = {"Introduction to", "Advanced", "Fundamentals of", "Mastering", "Complete Guide to"};
        String[] subjects = {"Java Programming", "Python", "Web Development", "Data Science", "Machine Learning",
                "Algorithms", "Database Design", "Cloud Computing", "Cybersecurity", "Mobile App Development"};
        return prefixes[ThreadLocalRandom.current().nextInt(prefixes.length)] + " " +
                subjects[ThreadLocalRandom.current().nextInt(subjects.length)];
    }

    private String getRandomArticleTitle() {
        String[] prefixes = {"How to", "The Future of", "5 Tips for", "Understanding", "Beginner's Guide to"};
        String[] subjects = {"Online Learning", "Programming", "Study Techniques", "Career Growth",
                "Productivity", "Time Management", "Coding Interviews", "Remote Work"};
        return prefixes[ThreadLocalRandom.current().nextInt(prefixes.length)] + " " +
                subjects[ThreadLocalRandom.current().nextInt(subjects.length)];
    }

    private String generateArticleContent(int index) {
        String[] paragraphs = {
                "This is the first paragraph of article " + index + ". It introduces the main topic.",
                "The second paragraph goes into more detail about the subject matter.",
                "Here we discuss some important considerations and best practices.",
                "This section provides examples and practical applications.",
                "Finally, we conclude with a summary and additional resources."
        };
        return String.join("\n\n", paragraphs);
    }

    private String getRandomCommentText() {
        String[] comments = {
                "Great article! Very informative.",
                "I found this really helpful for my studies.",
                "Could you provide more examples on this topic?",
                "I disagree with some points but overall good content.",
                "Thanks for sharing this valuable information.",
                "This helped me understand the concept much better.",
                "Looking forward to more articles like this!"
        };
        return comments[ThreadLocalRandom.current().nextInt(comments.length)];
    }

    private String getRandomProjectDescription() {
        String[] descriptions = {
                "This project implements the core concepts learned in class.",
                "A practical application of the course material.",
                "An innovative approach to solving a common problem.",
                "Demonstrates understanding through a real-world example.",
                "Includes additional features beyond the basic requirements."
        };
        return descriptions[ThreadLocalRandom.current().nextInt(descriptions.length)];
    }

    private String getRandomFeedback() {
        String[] feedbacks = {
                "Could improve documentation.",
                "Excellent implementation of concepts.",
                "Needs more test cases.",
                "Very creative solution!",
                "Good structure and organization.",
                "Some edge cases not handled.",
                "Perfect score! Great job."
        };
        return feedbacks[ThreadLocalRandom.current().nextInt(feedbacks.length)];
    }

    private String getRandomCategory(List<String> categories) {
        return categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
    }

    private ProjectStatus getRandomProjectStatus() {
        int random = ThreadLocalRandom.current().nextInt(0, 3);
        return ProjectStatus.values()[random];
    }

    private <T> T getRandomUser(List<T> users) {
        return users.get(ThreadLocalRandom.current().nextInt(users.size()));
    }

    private Course getRandomCourse(List<Course> courses) {
        return courses.get(ThreadLocalRandom.current().nextInt(courses.size()));
    }

    private Article getRandomArticle(List<Article> articles) {
        return articles.get(ThreadLocalRandom.current().nextInt(articles.size()));
    }

    private Quiz getRandomQuiz(List<Quiz> quizzes) {
        return quizzes.get(ThreadLocalRandom.current().nextInt(quizzes.size()));
    }
}