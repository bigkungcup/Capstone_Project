package sit.cp23ej2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.BookmarkStatus.CreateBookmarkStatus;
import sit.cp23ej2.dtos.BookmarkStatus.UpdateBookmarkStatus;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookmarkRepository;
import sit.cp23ej2.repositories.BookmarkStatusRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class BookmarkStatusService extends CommonController {

    @Autowired
    private BookmarkStatusRepository repository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    public DataResponse insertBookmarkStatus(CreateBookmarkStatus param) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        repository.getBookmarkStatus(user.getUserId()).forEach(bookmarkStatus1 -> {
            if (bookmarkStatus1.getBsb_bookmarkId() == param.getBookmarkId()) {
                throw new HandleExceptionBadRequest("Bookmark Status Already Exists");
            }
        });

        boolean existsByBookmarkId = bookmarkRepository.existsByBookmarkId(param.getBookmarkId()); // ตรวจสอบว่ามี bookmarkId นี้หรือไม่

        if (!existsByBookmarkId) {
            throw new HandleExceptionNotFound("Bookmark Not Found", "Bookmark");
        }

        repository.insertBookmarkStatus(param.getBookmarkId(), user.getUserId(), param.getBookmarkStatus());
        return response(201, "Created", "Bookmark Status Created");
    }


    public DataResponse updateBookmarkStatus(Integer bookmarkStatusId, UpdateBookmarkStatus param) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        repository.getBookmarkStatus(user.getUserId()).forEach(bookmarkStatus1 -> {
            if (bookmarkStatus1.getBsb_bookmarkId() == param.getBookmarkId()) {
                throw new HandleExceptionNotFound("Bookmark Status Not Found", "Bookmark Status");
            }
        });

        boolean existsByBookmarkId = bookmarkRepository.existsByBookmarkId(param.getBookmarkId()); // ตรวจสอบว่ามี bookmarkId นี้หรือไม่

        if (!existsByBookmarkId) {
            throw new HandleExceptionNotFound("Bookmark Not Found", "Bookmark");
        }

        if(param.getBookmarkStatus() == 1){
            repository.updateBookmarkStatus(param.getBookmarkStatus(), bookmarkStatusId);
            return response(200, "OK", "Bookmark Status Updated");
        }else{
            repository.deleteBookmarkStatus(bookmarkStatusId);
            return response(200, "OK", "Bookmark Status Deleted");
        }
       
    }
    
}
