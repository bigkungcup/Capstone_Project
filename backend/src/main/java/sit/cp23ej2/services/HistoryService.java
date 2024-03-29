package sit.cp23ej2.services;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.History.PageHistoryDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.HistoryRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class HistoryService extends CommonController {

    @Autowired
    private HistoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getBookHistory(Integer page, Integer size) {

        // DataResponse response = new DataResponse();

        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        // System.out.println("TEST: " +
        // repository.getBookHistory(pageable,user.getUserId()));


        PageHistoryDTO bookHistory = modelMapper.map(repository.getBookHistory(pageable, user.getUserId()), PageHistoryDTO.class);

        bookHistory.getContent().forEach((history) -> {
            BookDTO book = modelMapper.map(history.getBook(), BookDTO.class);
            Path pathFile = fileStorageService.load(book);
            if (pathFile != null) {
                // history.setFile(pathFile.toString());
                // book.setFile(pathFile.toString());
                // bookDTO.setFile("http://localhost:8080/api/files/filesBook/" + bookDTO.getBookId());
                book.setFile(baseUrl + "/api/files/filesBook/" + book.getBookId());
              
            }

            // Duration duration = Duration.between(LocalDateTime.now(), book.getBookCreateDateTime());
            // book.setCountDateTime(Math.abs(duration.toSeconds()));
            history.getBook().setBookTag(history.getBook().getBookTag().replaceAll(",", ", "));
            book.setBookTagList(new ArrayList<String>(Arrays.asList(history.getBook().getBookTag().split(","))));
            history.setBookData(book);

            Duration duration = Duration.between(LocalDateTime.now(), history.getHistoryUpdateDateTime());
            history.setCountDateTime(Math.abs(duration.toSeconds()));
        });

        // bookHistory.forEach((history) -> {
        //     // System.out.println("TEST: " + history.getBook().getAuthor());
        //     BookDTO book = modelMapper.map(history.getBook(), BookDTO.class);
        //     // Path pathFile = fileStorageService.load(book);
        //     // if (pathFile != null) {
        //     //     history.setFile(pathFile.toString());
        //     // }

        // });

        // PageBookDTO books = modelMapper.map(repository.getBookHistory(pageable,
        // user.getUserId()), PageBookDTO.class);

        return responseWithData(bookHistory, 200, "OK", "All History");
    }

    public DataResponse deleteHistory(Integer historyId) {
        // DataResponse response = new DataResponse();
        if(!repository.existsByHistoryId(historyId)){
            throw new HandleExceptionNotFound("History Not Found", "History");
        }
        repository.deleteHistory(historyId);

        return response(200, "OK", "History Deleted");
    }

    public DataResponse deleteHistoryByUserId() {
        // DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        repository.deleteHistoryByUserId(user.getUserId());

        return response(200, "OK", "History Deleted");
    }

    public DataResponse deleteHistoryByUserIdAndAdmin(Integer userId) {
        // DataResponse response = new DataResponse();

        repository.deleteHistoryByUserId(userId);

        return response(200, "OK", "History Deleted");
    }

}
