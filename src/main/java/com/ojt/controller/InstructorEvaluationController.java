package com.ojt.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ojt.entity.*;
import com.ojt.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import com.ojt.repository.NotificationRepository;

@Controller
@RequestMapping("instructor/evaluation")
public class InstructorEvaluationController {
    private final EvaluationService evaluationService;
    private final OJTService ojtService;
    private final CourseService courseService;
    private final InstructorService instructorService;

    // Notification
    private final NotificationEmitterService notificationEmitterService;
    private final NotificationRepository notificationRepository;

    public InstructorEvaluationController(EvaluationService evaluationService, OJTService ojtService, CourseService courseService, InstructorService instructorService, NotificationEmitterService notificationEmitterService, NotificationRepository notificationRepository) {
        this.evaluationService = evaluationService;
        this.ojtService = ojtService;
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.notificationEmitterService = notificationEmitterService;
        this.notificationRepository = notificationRepository;
    }

    // List with search + pagination
    @GetMapping
    public String listEvaluations(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "7") int size,
                                  @RequestParam(required = false) String query,
                                  Model model,
                                  @ModelAttribute("successMessage") String successMessage,
                                  @ModelAttribute("errorMessage") String errorMessage) {

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size);
        Page<Evaluation> evaluationPage = (query != null && !query.isBlank())
                ? evaluationService.searchEvalutionsByCvName(query, pageable)
                : evaluationService.findAllEvaluationPaginated(pageable);

        model.addAttribute("evaluationList", evaluationPage.getContent());
        model.addAttribute("pageNumber", page);
        model.addAttribute("totalPages", evaluationPage.getTotalPages());
        model.addAttribute("query", query);
        model.addAttribute("totalElements", evaluationPage.getTotalElements());
        model.addAttribute("size", size);

        if (successMessage != null && !successMessage.isBlank()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isBlank()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "instructor/evaluation";
    }

    @GetMapping("/create")
    public String showCreateEvaluationForm(Model model, Principal principal) {
        //String loginInput = principal.getName(); // can be email or staffId

        Instructor instructor = instructorService.findByEmailOrStaffId("nyomonnaingwin@gmail.com");
        if (instructor == null) {
            // handle error: no instructor found
            throw new RuntimeException("Instructor not found");
        }

        List<Course> taughtCourses = instructor.getCourses();
        model.addAttribute("evaluation", new Evaluation());
        model.addAttribute("studentList", ojtService.getOJTByStatus());
        model.addAttribute("courseList", taughtCourses);

        return "instructor/evaluation-create";
    }

    @PostMapping("/save")
    public String saveEvaluation(@Valid @ModelAttribute("evaluation") Evaluation evaluation,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Principal principal) {
        //String loginInput = principal.getName(); // can be email or staffId
//        Instructor instructor = instructorService.findByEmailOrStaffId(principal.getName());
        Instructor instructor = instructorService.findByEmailOrStaffId("nyomonnaingwin@gmail.com");
        Long instructorId = instructor.getId();

        Long ojtId = Optional.ofNullable(evaluation.getOjt()).map(OJT::getId).orElse(null);
        Long courseId = Optional.ofNullable(evaluation.getCourse()).map(Course::getId).orElse(null);

        boolean isEdit = evaluation.getId() != null;

        if (result.hasErrors()) {
            if (!isEdit) {
                model.addAttribute("studentList", ojtService.getOJTByStatus());
                model.addAttribute("courseList", instructor.getCourses());
            }

            return isEdit ? "instructor/evaluation-edit" : "instructor/evaluation-create";
        }

        if (ojtId == null || courseId == null) {
            model.addAttribute("errorMessage", "Student and Course selection is required.");
            if (!isEdit) {
                model.addAttribute("studentList", ojtService.getOJTByStatus());
                model.addAttribute("courseList", instructor.getCourses());
            }
            return (evaluation.getId() != null) ? "instructor/evaluation-edit" : "instructor/evaluation-create";
        }

        try {
            evaluationService.createUpdateEvaluation(evaluation, ojtId, courseId, instructorId);

            // for notification
            Instructor inst = instructorService.getInstructorById(instructorId);
            Course course = courseService.getCourseById(courseId);
            OJT ojt = ojtService.getOJTById(ojtId);

            // sending notification to UI(Thant Sin Win)
            Notification notification = new Notification();

            notification.setMessage(inst.getName() + " has been evaluated mark set on course " + course.getName() + " student " + ojt.getCv().getName());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRole("admin");
            notificationRepository.save(notification);

            notificationEmitterService.broadcastToAdmin(notification);

            redirectAttributes.addFlashAttribute("successMessage", "Evaluation saved successfully!");
            return "redirect:/instructor/evaluation";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving evaluation: " + e.getMessage());
            return (evaluation.getId() != null) ? "redirect:/instructor/evaluation/edit/" + evaluation.getId() : "redirect:/instructor/evaluation/create";
        }
    }



    @GetMapping("/edit/{id}")
    public String showEditEvaluationForm(@PathVariable Long id,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        Optional<Evaluation> evaluationOptional = evaluationService.findEvaluationById(id);
        if (evaluationOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Evaluation not found.");
            return "redirect:/instructor/evaluation";
        }

        Evaluation evaluation = evaluationOptional.get();
        if (evaluation.getOjt() == null) evaluation.setOjt(new OJT());
        if (evaluation.getCourse() == null) evaluation.setCourse(new Course());

        model.addAttribute("evaluation", evaluation);
        model.addAttribute("studentList", ojtService.getAllOJT());
        model.addAttribute("courseList", courseService.getAllCourses());
        model.addAttribute("pageTitle", "Edit Evaluation");

        return "instructor/evaluation-edit";
    }

    @GetMapping("/view/{id}")
    public String viewEvaluation(@PathVariable Long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Optional<Evaluation> evaluationOptional = evaluationService.findEvaluationById(id);
        if (evaluationOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Evaluation not found.");
            return "redirect:/instructor/evaluation";
        }

        model.addAttribute("evaluation", evaluationOptional.get());
        return "instructor/evaluation-detail";
    }

    // Extra optional views
    @GetMapping("/instructor-dashboard")
    public String showDashboard() {
        return "instructor/instructor-dashboard";
    }

    @GetMapping("/timetable")
    public String showTimetable() {
        return "instructor/timetable";
    }


    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

}

