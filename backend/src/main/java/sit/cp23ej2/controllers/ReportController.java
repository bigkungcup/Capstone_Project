package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Report.CreateReportDTO;
import sit.cp23ej2.services.ReportService;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "*"
}, methods = {
        RequestMethod.OPTIONS,
        RequestMethod.GET,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.POST
}, allowedHeaders = "*")
@RestController
@Validated
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/fix")
    public DataResponse getAllFixReport(@RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return reportService.getAllFixReport(page, size);
    }

    @GetMapping("/notfix")
    public DataResponse getAllNotFixReport(@RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size) {
        return reportService.getAllNotFixReport(page, size);
    }

    @PostMapping("")
    public DataResponse insertReport(@RequestBody CreateReportDTO createReportDTO) {
        return reportService.insertReport(createReportDTO);
    }

    @PutMapping("/{reportId}")
    public DataResponse updateReport(Integer reportId) {
        return reportService.updateReport(reportId);
    }
}
