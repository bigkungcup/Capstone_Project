package sit.cp23ej2.services;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Bookmark.PageBookmarkDTO;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookmarkRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class BookmarkService extends CommonController {
    
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getBookmarkByUserId(Integer page, Integer size) throws HandleExceptionNotFound{
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        PageBookmarkDTO pageBookmarkDTO = modelMapper.map(bookmarkRepository.getBookmarkByUserId(user.getUserId(), pageable), PageBookmarkDTO.class);
       
        if(pageBookmarkDTO.getContent().size() > 0){
            pageBookmarkDTO.getContent().forEach(bookmark -> {
                BookDTO book = modelMapper.map(bookmark.getBook(), BookDTO.class);
                Path pathFile = fileStorageService.load(book);
                if (pathFile != null) {
                    bookmark.getBook().setFile(pathFile.toString());

                }
                bookmark.getBook().setBookTag(bookmark.getBook().getBookTag().replaceAll(",", ", "));
                book.setBookTagList(new ArrayList<String>(Arrays.asList(bookmark.getBook().getBookTag().split(","))) );
            });
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Bookmarks");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(pageBookmarkDTO);
        }else{
            throw new HandleExceptionNotFound("Bookmark Not Found", "Bookmark");
        }

        return response;
    }

    public DataResponse insertBookmark(Integer bb_bookId) {
        DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        bookmarkRepository.insertBookmark(bb_bookId, user.getUserId());
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Bookmark Inserted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse deleteBookmark(Integer bb_bookId) {
        DataResponse response = new DataResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        bookmarkRepository.deleteBookmark(bb_bookId, user.getUserId());
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Bookmark Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse deleteBookmarkById(Integer bookmarkId) {
        DataResponse response = new DataResponse();
        bookmarkRepository.deleteBookmarkById(bookmarkId);
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Bookmark Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }
}
