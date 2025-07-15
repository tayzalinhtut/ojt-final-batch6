package com.ojt.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ojt.dto.ResourceDTO;
import com.ojt.entity.Resources;
import com.ojt.service.ResourceService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/resources")
    public String showResources(Model model, @ModelAttribute("message") String message) {
        List<Resources> resources = null;
        resources = resourceService.getAllResource();
        model.addAttribute("message", message);

        model.addAttribute("resources", resources);
        model.addAttribute("activePage", "resources");
        System.out.println("Flash Message: " + message);


        return "admin/resource/resource-management";
    }

    @GetMapping("/resource/create")
    public String showResourceCreate(Model model) {
        ResourceDTO resourceDTO = new ResourceDTO();

        model.addAttribute("resourceDTO", resourceDTO);
        model.addAttribute("activePage", "resources");
        return "admin/resource/resource-create";
    }

    @PostMapping("/resource/create")
    public String addResource(@Valid @ModelAttribute("resourceDTO") ResourceDTO resourceDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "admin/resource/resource-create";
        }

        resourceService.addResource(resourceDTO);
        redirectAttributes.addFlashAttribute("message", "Resource successfully created!");


        return "redirect:/admin/resources";
    }

    @GetMapping("/resource/edit/{id}")
    public String showResourceEdit(Model model, @PathVariable("id") Long id) {
        ResourceDTO resourceDTO = new ResourceDTO();
        Resources resource = null;
        resource = resourceService.getResourceById(id);

        resourceDTO.setName(resource.getName());
        model.addAttribute("activePage", "resources");
        model.addAttribute("resourceDTO", resourceDTO);

        return "admin/resource/resource-edit";
    }

    @PostMapping("/resource/edit/{id}")
    public String editResource(@PathVariable("id") Long id, @Valid @ModelAttribute("resourceDTO") ResourceDTO resourceDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "admin/resource/resource-edit";
        }

        resourceService.updateResource(id, resourceDTO);
        redirectAttributes.addFlashAttribute("message", "Resource successfully updated!");


        return "redirect:/admin/resources";
    }

    @GetMapping("/resource/delete")
    public String delteResource(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) throws Exception {
        resourceService.deleteResource(id);

        redirectAttributes.addFlashAttribute("message", "Resource successfuly deleted!");

        return "redirect:/admin/resources";
    }

}