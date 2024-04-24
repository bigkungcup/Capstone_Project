package sit.cp23ej2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.services.HistoryService;

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
@RequestMapping("/api/history")
public class HistoryController {

        @Autowired
        private HistoryService historyService;

        @GetMapping("")
        public DataResponse getBookHistory(@RequestParam(defaultValue = "0", required = false) Integer page,
                        @RequestParam(defaultValue = "10", required = false) Integer size) {
                return historyService.getBookHistory(page, size);
        }

        @DeleteMapping("/{historyId}")
        public DataResponse deleteHistory(@PathVariable Integer historyId) {
                return historyService.deleteHistory(historyId);
        }

        @DeleteMapping("/all")
        public DataResponse deleteHistoryByUserId() {
                return historyService.deleteHistoryByUserId();
        }

        @DeleteMapping("/admin/{userId}")
        public DataResponse deleteHistoryByUserId(@PathVariable Integer userId) {
                return historyService.deleteHistoryByUserIdAndAdmin(userId);
        }
}
