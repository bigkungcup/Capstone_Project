package sit.cp23ej2.services;

import java.nio.file.Path;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Book.BookReportDTO;
import sit.cp23ej2.dtos.Report.CreateReportDTO;
import sit.cp23ej2.dtos.Report.PageReportDTO;
import sit.cp23ej2.dtos.Review.ReviewReportDTO;
import sit.cp23ej2.dtos.User.UserReportDTO;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.Report;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.ReportRepository;
import sit.cp23ej2.repositories.ReviewRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class ReportService extends CommonController {

    @Autowired
    private ReportRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    public DataResponse getAllNotFixReport(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Report> allReport = repository.getAllNotFixReport(pageable);

        if (allReport.isEmpty()) {
            throw new HandleExceptionNotFound("Report not found", "Report");
        }

        PageReportDTO pageReportDTO = modelMapper.map(allReport, PageReportDTO.class);

        pageReportDTO.getContent().forEach(reportDTO -> {
            if (reportDTO.getReportType().equals("book")) {
                Book bookById = bookRepository.getBookById(reportDTO.getProblemId());
                BookReportDTO bookReportDTO = modelMapper.map(bookById, BookReportDTO.class);
                BookDTO bookDTO = modelMapper.map(bookById, BookDTO.class);
                try {
                    Path pathFile = fileStorageService.load(bookDTO);
                    if (pathFile != null) {
                        bookReportDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reportDTO.setData(bookReportDTO);
            } else if (reportDTO.getReportType().equals("review")) {
                Review reviewById = reviewRepository.getReviewById(reportDTO.getProblemId());
                ReviewReportDTO reviewReportDTO = modelMapper.map(reviewById, ReviewReportDTO.class);
                try {
                    UserReportDTO userReportDTO = modelMapper.map(reviewById.getUser(), UserReportDTO.class);
                    reviewReportDTO.setUserDetail(userReportDTO);
                    Path pathFile = fileStorageService.loadUserFile(reviewById.getUser().getUserId());
                    if (pathFile != null) {
                        reviewReportDTO.getUserDetail().setFile(baseUrl + "/api/files/filesUser/" + reviewById.getUser().getUserId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reportDTO.setData(reviewReportDTO);
            } else if (reportDTO.getReportType().equals("user")) {
                User userById = userRepository.getUserById(reportDTO.getProblemId());
                UserReportDTO userReportDTO = modelMapper.map(userById, UserReportDTO.class);
                try {
                    Path pathFile = fileStorageService.loadUserFile(userById.getUserId());
                    if (pathFile != null) {
                        userReportDTO.setFile(baseUrl + "/api/files/filesUser/" + userById.getUserId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reportDTO.setData(userReportDTO);
            }
        });

        return responseWithData(pageReportDTO, 200, "OK", "All Report");
    }

    public DataResponse getAllFixReport(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Report> allReport = repository.getAllFixReport(pageable);

        if (allReport.isEmpty()) {
            throw new HandleExceptionNotFound("Report not found", "Report");
        }

        PageReportDTO pageReportDTO = modelMapper.map(allReport, PageReportDTO.class);

        pageReportDTO.getContent().forEach(reportDTO -> {
            if (reportDTO.getReportType().equals("book")) {
                reportDTO.setData(bookRepository.getBookById(reportDTO.getProblemId()));
            } else if (reportDTO.getReportType().equals("review")) {
                reportDTO.setData(reviewRepository.getReviewById(reportDTO.getProblemId()));
            }
        });

        return responseWithData(pageReportDTO, 200, "OK", "All Report");
    }

    public DataResponse insertReport(CreateReportDTO createReportDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if (createReportDTO.getReportType().equals("book")) {
            if (!bookRepository.existsByBookId(createReportDTO.getProblemId())) {
                throw new HandleExceptionNotFound("Book not found", "Book");
            }
        } else if (createReportDTO.getReportType().equals("review")) {
            if (!reviewRepository.existsByReviewId(createReportDTO.getProblemId())) {
                throw new HandleExceptionNotFound("Review not found", "Review");
            }
        } else if (createReportDTO.getReportType().equals("user")) {
            if (!userRepository.existsByUserId(createReportDTO.getProblemId())) {
                throw new HandleExceptionNotFound("User not found", "User");
            }
        }

        repository.insertReport(createReportDTO.getReportTitle(), createReportDTO.getReportDetail(),
                createReportDTO.getProblemId(), createReportDTO.getReportType(), user.getUserId());

        return response(201, "Created", "Report has been created");
    }

    public DataResponse updateReport(Integer reportId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if (!repository.existsByReportId(reportId)) {
            throw new HandleExceptionNotFound("Report not found", "Report");
        }

        if(repository.getReportById(reportId).getReportStatus() == 1){
            throw new HandleExceptionBadRequest("Report has been fixed");
        }

        repository.updateReport(user.getUserId(), reportId);

        return response(200, "OK", "Report has been updated");
    }
}
