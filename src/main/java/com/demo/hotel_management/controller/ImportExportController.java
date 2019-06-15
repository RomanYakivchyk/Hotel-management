package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.ImportExportView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ImportExportController {

    @GetMapping("/importExport")
    public String importExport(Model model) {
        model.addAttribute("importExportView", new ImportExportView());
        return "importExportSelect.html";
    }


    @PostMapping("/importExport/select")
    public String selectedAction(@ModelAttribute ImportExportView importExportView) {
        if (importExportView.getAction().equals("export")) {
            return "ss";
        } else if (importExportView.getAction().equals("import")) {
            return "sasds";
        }
        return null;
    }

}
