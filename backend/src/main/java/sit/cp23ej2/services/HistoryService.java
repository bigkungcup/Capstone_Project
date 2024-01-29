package sit.cp23ej2.services;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Book.PageBookDTO;
import sit.cp23ej2.dtos.History.PageHistoryDTO;
import sit.cp23ej2.entities.History;
import sit.cp23ej2.entities.User;
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

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getBookHistory(Integer page, Integer size) {

        DataResponse response = new DataResponse();

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
                history.setFile(pathFile.toString());
            }

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
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("All Books");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        response.setData(bookHistory);

        return response;
    }

}
