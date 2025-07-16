package com.ojt.controller;

import com.ojt.dto.CourseDTO;
import com.ojt.entity.Courses;
import com.ojt.entity.Instructor; // Ensure this is imported if not already
import com.ojt.entity.Batch;     // Ensure this is imported if not already
import com.ojt.service.CourseService;
import com.ojt.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet; // Make sure HashSet is imported if you initialize sets
//Nyi Min Htet Lwin
@Controller
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final InstructorService instructorService;

    @GetMapping("")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("activePage", "batch");
        model.addAttribute("activePage", "course");
        return "admin/course/courses";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("courseDTO", new CourseDTO());
        model.addAttribute("activePage", "course");        // You might want to add all instructors/batches here for the create form
        // model.addAttribute("allInstructors", instructorService.getAllInstructors());
        // model.addAttribute("allBatches", batchService.getAllBatches());
        return "admin/course/course-create";
    }

    @PostMapping("/create")
    public String handleCreate(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Re-add necessary attributes if the form relies on them for rendering dropdowns/checkboxes
            // model.addAttribute("allInstructors", instructorService.getAllInstructors());
            // model.addAttribute("allBatches", batchService.getAllBatches());
            return "admin/course/course-create";
        }

        try {
            courseService.createCourse(courseDTO);
            redirectAttributes.addFlashAttribute("message", "Course created successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/admin/course";
        } catch (RuntimeException e) {
            result.rejectValue("name", "course.name.exists", "Course name is already exists.");
            // Re-add necessary attributes on error
            // model.addAttribute("allInstructors", instructorService.getAllInstructors());
            // model.addAttribute("allBatches", batchService.getAllBatches());
            return "admin/course/course-create";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        Courses course = courseService.getCourseById(id);
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());

        // FIX HERE: Collect into a Set instead of a List
        courseDTO.setInstructorIds(course.getInstructors().stream()
                .map(Instructor::getId)
                .collect(Collectors.toList())); // Changed to List

        courseDTO.setBatchIds(course.getBatches().stream()
                .map(Batch::getId)
                .collect(Collectors.toList())); // Changed to List

        model.addAttribute("courseDTO", courseDTO);
        model.addAttribute("activePage", "batch");
        // If your edit form also needs all instructors/batches for dropdowns/checkboxes, add them:
        // model.addAttribute("allInstructors", instructorService.getAllInstructors());
        // model.addAttribute("allBatches", batchService.getAllBatches());
        return "admin/course/course-edit";
    }

    @PostMapping("/edit")
    public String handleEdit(@RequestParam Long id,
                             @Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Re-add necessary attributes if the form relies on them
            // model.addAttribute("allInstructors", instructorService.getAllInstructors());
            // model.addAttribute("allBatches", batchService.getAllBatches());
            return "admin/course/course-edit";
        }

        try {
            courseService.updateCourse(id, courseDTO);
            redirectAttributes.addFlashAttribute("message", "Course updated successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/admin/course";
        } catch (RuntimeException e) {
            result.rejectValue("name", "course.name.exists", "Course name is already exists.");
            // Re-add necessary attributes on error
            // model.addAttribute("allInstructors", instructorService.getAllInstructors());
            // model.addAttribute("allBatches", batchService.getAllBatches());
            return "admin/course/course-edit";
        }
    }

    @PostMapping("/delete")
    public String handleDelete(@RequestParam Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting course: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/admin/course";
    }

    @GetMapping("/assignInstructors")
    public String showAssignInstructorsPage(@RequestParam("courseId") Long courseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Courses course = courseService.getCourseById(courseId);
            List<Instructor> allInstructors = instructorService.getAllInstructors();

            Set<Long> assignedInstructorIds = course.getInstructors().stream()
                    .map(Instructor::getId)
                    .collect(Collectors.toSet()); // This already uses toSet()

            model.addAttribute("course", course);
            model.addAttribute("allInstructors", allInstructors);
            model.addAttribute("assignedInstructorIds", assignedInstructorIds);
            model.addAttribute("activePage", "course");
            model.addAttribute("activePage", "course");
            return "admin/course/assign-instructors";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/admin/course";
        }
    }

    @PostMapping("/assignInstructors")
    public String handleAssignInstructors(@RequestParam("courseId") Long courseId,
                                          @RequestParam(value = "instructorIds", required = false) List<Long> instructorIds, // This expects a List<Long> from form
                                          RedirectAttributes redirectAttributes) {
        try {
            courseService.assignInstructorsToCourse(courseId, instructorIds);
            redirectAttributes.addFlashAttribute("message", "Instructors assigned successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Error assigning instructors: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/admin/course";
    }

    @GetMapping("/{courseId}/instructors")
    public String showAssignedInstructors(@PathVariable Long courseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Courses course = courseService.getCourseById(courseId);
            List<Instructor> assignedInstructors = course.getInstructors();

            model.addAttribute("course", course);
            model.addAttribute("assignInstructors", assignedInstructors);
            model.addAttribute("activePage", "course");
            model.addAttribute("activePage", "course");

            return "admin/course/view-assign-instructors";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", "Course not found: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/admin/course";
        }
    }

}