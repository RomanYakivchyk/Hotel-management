package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.ImportExportView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImportExportController {

    @GetMapping("/importExport")
    public String importExport(Model model) {
        model.addAttribute("importExportView", new ImportExportView());
        return "importExport.html";
    }

    @GetMapping("/uploadFile")
    public String uploadFile() {
        return "uploadFile.html";
    }

    @PostMapping("/importExport")
    public String selectedAction(@ModelAttribute ImportExportView importExportView, RedirectAttributes redirectAttributes) {
        if (importExportView.getType().equals("client")) {
            redirectAttributes.addAttribute("message", "Імпорт даних клієнтів");
        } else if (importExportView.getType().equals("vac")) {
            redirectAttributes.addAttribute("message", "Імпорт даних бронювання");
        }

        if (importExportView.getAction().equals("export")) {
            return "redirect:/downloadFile";
        } else if (importExportView.getAction().equals("import")) {
            return "redirect:/uploadFile";
        }
        return null;
    }

}
