package sit.cp23ej2.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Book.CreateBookDTO;
import sit.cp23ej2.dtos.Book.PageBookDTO;
import sit.cp23ej2.dtos.Book.UpdateBookDTO;
import sit.cp23ej2.dtos.Bookmark.BookmarkBookDTO;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.Recommend;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.BookmarkRepository;
import sit.cp23ej2.repositories.BooktypeRepository;
import sit.cp23ej2.repositories.HistoryRepository;
import sit.cp23ej2.repositories.NotificationRepository;
import sit.cp23ej2.repositories.RecommendRepository;
import sit.cp23ej2.repositories.UserRepository;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;

@Service
public class BookService extends CommonController {

    @Autowired
    private BookRepository repository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BooktypeRepository booktypeRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private RecommendRepository recommendRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getBook(int page, int size, Long bookRating, Long booktypeId, String search, String sortBy, String sortType)
            throws HandleExceptionNotFound {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // DataResponse response = new DataResponse();

        Pageable pageable;

        if (sortBy == null || sortBy.equals("")) {
            sortBy = "bookId";
        }

        if ("DESC".equalsIgnoreCase(sortType)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else if ("ASC".equalsIgnoreCase(sortType)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        }

        PageBookDTO books = modelMapper.map(repository.getAllBooks(pageable, bookRating, booktypeId, search), PageBookDTO.class);

        if (books.getContent().size() > 0) {
            List<BookDTO> bookDTOs = books.getContent();
            bookDTOs.forEach(bookDTO -> {

                Duration duration = Duration.between(LocalDateTime.now(), bookDTO.getBookCreateDateTime());
                bookDTO.setCountDateTime(Math.abs(duration.toSeconds()));
                if(bookDTO.getBookTag() != null){
                    System.out.println("bookDTO.getBookTag(): " + bookDTO.getBookTag());
                    bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                    ArrayList<String> bookTag = new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", ")));
                    bookDTO.setBookTagList(bookTag);
                }
                try {

                    Path pathFile = fileStorageService.load(bookDTO);
                    if (pathFile != null) {
                        // bookDTO.setFile("http://localhost:8080/api/files/filesBook/" + bookDTO.getBookId());
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!currentPrincipalName.equals("anonymousUser")) {
                    bookmarkRepository.getBookmarkListByUserId(userRepository.getUserByEmail(currentPrincipalName).getUserId()).forEach(bookmark -> {
                        BookmarkBookDTO bookmarks = modelMapper.map(bookmark, BookmarkBookDTO.class);
                        if (bookmark.getBook().getBookId() == bookDTO.getBookId()) {
                            bookDTO.setBookmark(bookmarks);
                        }
                    });
                }

              
            });
            return responseWithData(books, 200, "OK", "All books");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }

    }

    public DataResponse getBookById(Integer bookId) throws HandleExceptionNotFound {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println("authentication: " + authentication.getName());
        String currentPrincipalName = authentication.getName();
        // System.out.println("currentPrincipalName: " + currentPrincipalName);

        Book book = repository.getBookById(bookId);

        // if (book == null) {
        //     throw new HandleExceptionNotFound("Book Not Found", "Book");
        // }

        // DataResponse response = new DataResponse();

        repository.increaseBookTotalView(bookId);

        if (book != null) {
            
            if (!currentPrincipalName.equals("anonymousUser")) {
                User user = userRepository.getUserByEmail(currentPrincipalName);
                Integer existsByUserIdAndBookId = historyRepository.existsByUserIdAndBookId(user.getUserId(), bookId);
                if (user != null && existsByUserIdAndBookId == 0) {
                    historyRepository.insertHistory(user.getUserId(), bookId);
                }else if(user != null && existsByUserIdAndBookId != 0){
                    historyRepository.updateHistory(user.getUserId(), bookId);
                }
            }

            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            if(bookDTO.getBookTag() != null){
                bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
            }
            bookDTO.setBookTotalBookmark(bookmarkRepository.getBookmarkListByBookId(bookId).size());
            try {

                Path pathFile = fileStorageService.load(bookDTO);
                if(pathFile != null){
                    // bookDTO.setFile(pathFile.toString());
                    // bookDTO.setFile("http://localhost:8080/api/files/filesBook/" + bookDTO.getBookId());
                    bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!currentPrincipalName.equals("anonymousUser")) {
                User userByEmail = userRepository.getUserByEmail(currentPrincipalName);
                bookmarkRepository.getBookmarkListByUserId(userByEmail.getUserId()).forEach(bookmark -> {
                    BookmarkBookDTO bookmarks = modelMapper.map(bookmark, BookmarkBookDTO.class);
                    if (bookmark.getBook().getBookId() == bookDTO.getBookId()) {
                        bookDTO.setBookmark(bookmarks);
                    }
                });
                Recommend recommendBooktypeIdByUserId = recommendRepository.existsByBooktypeIdAndUserId(book.getBooktype().getBooktypeId(), userByEmail.getUserId());
                System.out.println("recommendBooktypeIdByUserId: " + recommendBooktypeIdByUserId);
                if(recommendBooktypeIdByUserId != null){
                    recommendRepository.updateRecommend(book.getBooktype().getBooktypeId(), userByEmail.getUserId());
                }else{
                    recommendRepository.insertRecommend(book.getBooktype().getBooktypeId(), userByEmail.getUserId());
                }
            }
            
            return responseWithData(bookDTO, 200, "OK", "Book Detail");
        }else{
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse getBookRanking(Integer booktypeId, String sortParam) {

        if(!sortParam.equals("totalReview") && !sortParam.equals("totalView")){
            throw new HandleExceptionBadRequest("Sort Param is invalid");
        }
        
        List<Book> books = repository.getBookRanking(booktypeId, sortParam);

        if (books.size() > 0) {
            List<BookDTO> bookDTOs = new ArrayList<>();
            books.forEach(book -> {
                BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                if(bookDTO.getBookTag() != null){
                    bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                    bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                }
                try {
                    Path pathFile = fileStorageService.load(bookDTO);
                    if(pathFile != null){
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookDTOs.add(bookDTO);
            });
            return responseWithData(bookDTOs, 200, "OK", "Book Ranking");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse getBookMostView() {
        List<Book> books = repository.getBookByMostView();

        if (books.size() > 0) {
            List<BookDTO> bookDTOs = new ArrayList<>();
            books.forEach(book -> {
                BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                if(bookDTO.getBookTag() != null){
                    bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                    bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                }
                try {
                    Path pathFile = fileStorageService.load(bookDTO);
                    if(pathFile != null){
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookDTOs.add(bookDTO);
            });
            return responseWithData(bookDTOs, 200, "OK", "Book Most View");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse getBookByCreateDateTime(){
        List<Book> books = repository.getBookByCreateDateTime();

        if (books.size() > 0) {
            List<BookDTO> bookDTOs = new ArrayList<>();
            books.forEach(book -> {
                BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                if(bookDTO.getBookTag() != null){
                    bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                    bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                }
                try {
                    Path pathFile = fileStorageService.load(bookDTO);
                    if(pathFile != null){
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookDTOs.add(bookDTO);
            });
            return responseWithData(bookDTOs, 200, "OK", "New Book");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse getBookByRandom() {
        List<Book> books = repository.getBookByRandom();

        if (books.size() > 0) {
            List<BookDTO> bookDTOs = new ArrayList<>();
            books.forEach(book -> {
                BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                if(bookDTO.getBookTag() != null){
                    bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                    bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                }
                try {
                    Path pathFile = fileStorageService.load(bookDTO);
                    if(pathFile != null){
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookDTOs.add(bookDTO);
            });
            return responseWithData(bookDTOs, 200, "OK", "Book By Random");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse getSimilarBook(Integer booktypeId, Integer bookId){
        List<Book> books = repository.getBookByBooktype(booktypeId);
        books.removeIf(book -> book.getBookId() == bookId);
        if (books.size() > 0) {
            List<BookDTO> bookDTOs = new ArrayList<>();
            books.forEach(book -> {
                BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                Path pathFile = fileStorageService.load(bookDTO);
                if(pathFile != null){
                    if(bookDTO.getBookTag() != null){
                        bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                        bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                    }
                    try {
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bookDTOs.add(bookDTO);
                }
            });

            if(bookDTOs.size() == 0){
                throw new HandleExceptionNotFound("Book Not Found", "Book");
            }

            return responseWithData(bookDTOs, 200, "OK", "Similar Book");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse getRecommendBook(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.getUserByEmail(currentPrincipalName);
        Integer recommendBooktypeIdByUserId = recommendRepository.getRecommendBooktypeIdByUserId(user.getUserId());
        List<Book> books = repository.getBookByRecommendBook(recommendBooktypeIdByUserId);

        if (books.size() > 0) {
            List<BookDTO> bookDTOs = new ArrayList<>();
            books.forEach(book -> {
                BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                Path pathFile = fileStorageService.load(bookDTO);
                if(pathFile != null){
                    if(bookDTO.getBookTag() != null){
                        bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                        bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                    }
                   
                    try {
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bookDTOs.add(bookDTO);
                }
            });

            if(bookDTOs.size() == 0){
                throw new HandleExceptionNotFound("Book Not Found", "Book");
            }
            return responseWithData(bookDTOs, 200, "OK", "Recommand Book");
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
    }

    public DataResponse createBook(CreateBookDTO book, MultipartFile file) {
        // DataResponse response = new DataResponse();
        Boolean existsByAuthorAndBookName = repository.existsByAuthorAndBookName(book.getAuthor(), book.getBookName());
        System.out.println("existsByAuthorAndBookName: " + existsByAuthorAndBookName);
        if (!existsByAuthorAndBookName) {
            repository.insertBook(book.getBooktypeId(), book.getBookName(), book.getAuthor(), book.getBookTag(),
                    book.getBookDetail());
            Integer lastInsertId = repository.getLastInsertId();
            System.out.println("insertBook: " + lastInsertId);
            if (file != null) {
                fileStorageService.store(file, lastInsertId);
            }
        } else {
            throw new HandleExceptionBadRequest("Book Already Exists");
        }
        return response(201, "Created", "Book Created");
    }

    public DataResponse updateBook(UpdateBookDTO book, Integer bookId, MultipartFile file) {

        // DataResponse response = new DataResponse();

        Book dataBook = repository.getBookById(bookId);

        if (dataBook == null) {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }

        if (dataBook.getAuthor().equals(book.getAuthor()) && dataBook.getBookName().equals(book.getBookName())) {

            repository.updateBook(book.getBooktypeId(), book.getBookName(), book.getAuthor(), book.getBookTag(),
                    book.getBookDetail(), new Timestamp(System.currentTimeMillis()).toString(),
                    bookId);

            if (file != null) {
                fileStorageService.deleteFile(dataBook);
                fileStorageService.store(file, bookId);
            }

            Book newDataBook = repository.getBookById(bookId);
            newDataBook.setAuthor(book.getAuthor());
            newDataBook.setBookName(book.getBookName());
            newDataBook.setBookTag(book.getBookTag());
            newDataBook.setBookDetail(book.getBookDetail());
            BookDTO bookDTO = modelMapper.map(newDataBook, BookDTO.class);

            bookDTO.setBooktype(booktypeRepository.getBooktypeById(book.getBooktypeId()));
            if(bookDTO.getBookTag() != null){
                bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
            }
            bookDTO.setBookUpdateDateTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            
            try {
                if (book.getStatus() != null) {
                    Path pathFile = fileStorageService.load(bookDTO);
                    if(pathFile != null){
                        // bookDTO.setFile("http://localhost:8080/api/files/filesBook/" + bookDTO.getBookId());
                        // bookDTO.setFile(pathFile.toString());
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                    }
                } else {
                    fileStorageService.deleteFile(newDataBook);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseWithData(bookDTO, 200, "OK", "Book Updated");
        } else {
            Boolean existsByAuthorAndBookName = repository.existsByAuthorAndBookName(book.getAuthor(),
                    book.getBookName());

            if (!existsByAuthorAndBookName) {

                repository.updateBook(book.getBooktypeId(), book.getBookName(), book.getAuthor(), book.getBookTag(),
                        book.getBookDetail(), new Timestamp(System.currentTimeMillis()).toString(),
                        bookId);

                if (file != null) {
                    fileStorageService.deleteFile(dataBook);
                    fileStorageService.store(file, bookId);
                }

                Book newDataBook = repository.getBookById(bookId);
                newDataBook.setAuthor(book.getAuthor());
                newDataBook.setBookName(book.getBookName());
                newDataBook.setBookTag(book.getBookTag());
                newDataBook.setBookDetail(book.getBookDetail());
                
                BookDTO bookDTO = modelMapper.map(newDataBook, BookDTO.class);
                
                bookDTO.setBooktype(booktypeRepository.getBooktypeById(book.getBooktypeId()));
                if(bookDTO.getBookTag() != null){
                    bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                    bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                }
                bookDTO.setBookUpdateDateTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());

                try {
                    if (book.getStatus() != null) {
                        Path pathFile = fileStorageService.load(bookDTO);
                        if(pathFile != null){
                            // bookDTO.setFile(pathFile.toString());
                            // bookDTO.setFile("http://localhost:8080/api/files/filesBook/" + bookDTO.getBookId());
                            bookDTO.setFile(baseUrl + "/api/files/filesBook/" + bookDTO.getBookId());
                        }
                    } else {
                        fileStorageService.deleteFile(newDataBook);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return responseWithData(bookDTO, 200, "OK", "Book Updated");
            } else {
                throw new HandleExceptionBadRequest("Book Already Exists");
            }
        }

    }

    public DataResponse deleteBook(int bookId) {
        // DataResponse response = new DataResponse();
        // try {
        Book dataBook = repository.getBookById(bookId);

        if (dataBook == null) {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }

        Integer deleteStatus = repository.deleteBook(bookId);
        System.out.println("deleteBook: " + deleteStatus);
        if (deleteStatus == 1) {
            fileStorageService.deleteFile(dataBook);
        }
        // } catch (Exception e) {
        // throw new HandleExceptionBadRequest("Book Can not delete because book have
        // reviews.");
        // }

        notificationRepository.getNotificationAll().forEach(notification -> {
            if(notification.getNotificationLink() != null && notification.getNotificationLink().contains("/book/" + bookId)){
                notificationRepository.deleteNotificationById(notification.getNotificationId());
            }
        });

        return response(200, "OK", "Book Deleted");
    }

}
