package com.ojt.controller;

import com.ojt.dto.BatchDTO;
import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.entity.Course;
import com.ojt.entity.OJT;
import com.ojt.service.BatchService;
import com.ojt.service.CVService;
import com.ojt.service.CourseService;
import com.ojt.service.OJTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private OJTService ojtService;

    @Autowired
    private CVService cvService;

@GetMapping
public String viewAll(@RequestParam(name = "batchId", required = false) Long batchId, Model model, @ModelAttribute("message") String message) {
        List<Batch> allBatches = batchService.getAllBatches();
    List<Batch> batches;

    if (batchId != null) {
        batches = allBatches.stream()
                .filter(b -> b.getId().equals(batchId))
                .toList();
    } else {
        batches = allBatches;
    }


    Map<Long, Long> studentCounts = new HashMap<>();
    for (Batch batch : batches) {
        studentCounts.put(batch.getId(), ojtService.countOjtAllStudent(batch.getId()));
    }
    Map<Long, Long> courseCounts = new HashMap<>();
    for (Batch batch : batches) {
        long courseCount = courseService.getCoursesByBatchId(batch.getId()).size();
        courseCounts.put(batch.getId(), courseCount);
    }

    model.addAttribute("message", message);
    model.addAttribute("allBatches", allBatches);
    model.addAttribute("batches", batches);
    model.addAttribute("studentCounts", studentCounts);
    model.addAttribute("courseCounts", courseCounts);
    model.addAttribute("selectedBatchId", batchId);
    model.addAttribute("activePage", "batch");

    return "admin/batch/batch-management";
}



    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("batch", new BatchDTO());
        model.addAttribute("activePage", "batch");
        return "admin/batch/batch-create";

    }


    @PostMapping("/create")
    public String create(@ModelAttribute("batch") @Valid BatchDTO batchDto,
                         BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/batch/batch-create";
        }

        batchService.saveBatch(batchDto);
        redirectAttributes.addFlashAttribute("message", "Batch successfully created!");
        return "redirect:/admin/batch";
    }



    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model,@ModelAttribute("message") String message) {
        Batch batch = batchService.getBatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch Id: " + id));

        BatchDTO dto = new BatchDTO();
        dto.setId(batch.getId());
        dto.setName(batch.getName());
        dto.setStartDate(batch.getStartDate());
        dto.setEndDate(batch.getEndDate());

        List<OJT> enrolledOjts = ojtService.getOjtByBatchIdAndStatusTypeAndStatus(id);
        model.addAttribute("message", message);
        model.addAttribute("batches", batch);
        model.addAttribute("batch", dto);
        model.addAttribute("enrolledOjts", enrolledOjts);
        model.addAttribute("activePage", "batch");
        return "admin/batch/batch-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("batch") @Valid BatchDTO batchDto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            List<OJT> enrolledOjts = ojtService.getOjtByBatchIdAndStatusTypeAndStatus(id);

            model.addAttribute("enrolledOjts", enrolledOjts);
            return "admin/batch/batch-edit";
        }

        batchService.updateBatch(id, batchDto);
        redirectAttributes.addFlashAttribute("message", "Batch Edit successfully!");
        return "redirect:/admin/batch";
    }
    @GetMapping("/assign-course/{batchId}")
    public String assignCourseForm(@PathVariable("batchId") Long batchId, Model model,@ModelAttribute("message") String message) {
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch Id: " + batchId));

        List<Course> assignedCourses = courseService.getCoursesByBatchId(batchId);
        List<Course> availableCourses = courseService.getCoursesWithoutBatch(batchId);
        long studentCount = ojtService.countOjtAllStudent(batchId);

        model.addAttribute("message", message);
        model.addAttribute("batch", batch);
        model.addAttribute("assignedCourses", assignedCourses);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("activePage", "batch");

        return "admin/batch/assign-course-batch";
    }


    @PostMapping("/assign-course/{batchId}")
    public String assignCoursesToBatch(@PathVariable("batchId") Long batchId,
                                       @RequestParam List<Long> courseIds,
                                       RedirectAttributes redirectAttributes) {
        courseService.assignCoursesToBatch(courseIds, batchId);
        redirectAttributes.addFlashAttribute("message", "Course successfully assigned!");

        return "redirect:/admin/batch";
    }

    @PostMapping("/unassign-course/{batchId}")
    public String unassignCoursesFromBatch(@PathVariable("batchId") Long batchId,
                                           @RequestParam List<Long> courseIds,
                                           RedirectAttributes redirectAttributes) {
        courseService.unassignCoursesFromBatch(courseIds, batchId);
        redirectAttributes.addFlashAttribute("message", "Course successfully unassigned!");
        return "redirect:/admin/batch";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {

        Batch batch = batchService.getBatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch Id: " + id));
        List<OJT> enrolledOjts = ojtService.getOjtByBatchIdAndStatusTypeAndStatus(id);

        long studentCount = ojtService.countOjtActiveStudent(batch.getId());
        long withDrawCount = ojtService.countWithDrawStudentCount(batch.getId());
        long ojtPass = ojtService.countOJTPassed(batch.getId());
        long ojtFailed = ojtService.countOJTFailed(batch.getId());

        model.addAttribute("batch", batch);
        model.addAttribute("enrolledOjts", enrolledOjts);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("withDrawCount", withDrawCount);
        model.addAttribute("ojtPass", ojtPass);
        model.addAttribute("ojtFailed", ojtFailed);
        model.addAttribute("activePage", "batch");
        return "admin/batch/batch-details";
    }
 
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        batchService.deleteBatch(id);
        redirectAttributes.addFlashAttribute("message", "Batch successfully deleted!");
        return "redirect:/admin/batch";
    }
}

