package com.ojt.controller;

import com.ojt.dto.HolidayDto;
import com.ojt.entity.Holiday;
import com.ojt.repository.HolidayRepository;
import com.ojt.service.HolidayExcelServiceImplementation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/holiday")
public class HolidayController {

    private final HolidayRepository holidayRepository;
    private final HolidayExcelServiceImplementation excelService;

    public HolidayController(HolidayRepository holidayRepository, HolidayExcelServiceImplementation excelService) {
        this.holidayRepository = holidayRepository;
        this.excelService = excelService;
    }

    @GetMapping
    public String listHolidays(Model model) {
        model.addAttribute("holidays", holidayRepository.findAll());
        model.addAttribute("activePage", "holiday");
        return "admin/holiday/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("holiday", new HolidayDto());
        model.addAttribute("activePage", "holiday");
        return "admin/holiday/create";
    }

    @PostMapping("/create")
    public String createHoliday(@ModelAttribute HolidayDto dto) {
        Holiday holiday = new Holiday();
        holiday.setName(dto.getName());
        holiday.setDate(dto.getDate());
        holidayRepository.save(holiday);
        return "redirect:/admin/holiday";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Holiday> optionalHoliday = holidayRepository.findById(id);
        if (optionalHoliday.isPresent()) {
            Holiday holiday = optionalHoliday.get();
            HolidayDto dto = new HolidayDto();
            dto.setName(holiday.getName());
            dto.setDate(holiday.getDate());
            model.addAttribute("holiday", dto);
            model.addAttribute("holidayId", holiday.getId());
            model.addAttribute("activePage", "holiday");
            return "admin/holiday/edit";
        } else {
            return "redirect:/admin/holiday";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateHoliday(@PathVariable Long id, @ModelAttribute HolidayDto dto) {
        Optional<Holiday> optionalHoliday = holidayRepository.findById(id);
        if (optionalHoliday.isPresent()) {
            Holiday holiday = optionalHoliday.get();
            holiday.setName(dto.getName());
            holiday.setDate(dto.getDate());
            holidayRepository.save(holiday);
        }
        return "redirect:/admin/holiday";
    }

    @GetMapping("/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        holidayRepository.deleteById(id);
        return "redirect:/admin/holiday";
    }

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<Holiday> holidays = excelService.excelToHolidayList(file.getInputStream());
        holidayRepository.saveAll(holidays);
        return "redirect:/admin/holiday";
    }
}
