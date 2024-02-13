package sit.cp23ej2.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.BooktypeRepository;
import sit.cp23ej2.repositories.HistoryRepository;
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
    private ModelMapper modelMapper;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getBook(int page, int size, Long bookRatng, String sortBy, String sortType)
            throws HandleExceptionNotFound {

        DataResponse response = new DataResponse();

        Pageable pageable;

        if (sortBy == null || sortBy.equals("")) {
            sortBy = "bookId";
        }

        System.out.println("sortBy: " + sortBy);
        System.out.println("sortType: " + sortType);

        if ("DESC".equalsIgnoreCase(sortType)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else if ("ASC".equalsIgnoreCase(sortType)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        }

        PageBookDTO books = modelMapper.map(repository.getAllBooks(pageable, bookRatng), PageBookDTO.class);

        if (books.getContent().size() > 0) {
            List<BookDTO> bookDTOs = books.getContent();
            bookDTOs.forEach(bookDTO -> {
              
                Duration duration = Duration.between(LocalDateTime.now(), bookDTO.getBookUpdateDateTime());
                bookDTO.setCountDateTime(Math.abs(duration.toSeconds()));
                ArrayList<String> bookTag = new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(",")));
                bookDTO.setBookTagList(bookTag);
                try {
                   
                    Path pathFile = fileStorageService.load(bookDTO);
                    if (pathFile != null) {
                        bookDTO.setFile(pathFile.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All books");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(books);
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }

        return response;
    }

    public DataResponse getBookById(Integer bookId) throws HandleExceptionNotFound {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication: " + authentication.getName());
        String currentPrincipalName = authentication.getName();
        System.out.println("currentPrincipalName: " + currentPrincipalName);

        if(!currentPrincipalName.equals("anonymousUser")){
            User user = userRepository.getUserByEmail(currentPrincipalName);
            Integer existsByUserIdAndBookId = historyRepository.existsByUserIdAndBookId(user.getUserId(), bookId);
            if (user != null && existsByUserIdAndBookId == 0) {
                historyRepository.insertHistory(user.getUserId(), bookId);
            }
        }

        DataResponse response = new DataResponse();

        repository.increaseBookTotalView(bookId);

        Book book = repository.getBookById(bookId);
        if (book != null) {
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
            bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(","))));
            try {

                Path pathFile = fileStorageService.load(bookDTO);
                bookDTO.setFile(pathFile.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Book Detail");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(bookDTO);
        } else {
            throw new HandleExceptionNotFound("Book Not Found", "Book");
        }
        return response;
    }

    public DataResponse createBook(CreateBookDTO book, MultipartFile file) {
        DataResponse response = new DataResponse();
        Boolean existsByAuthorAndBookName = repository.existsByAuthorAndBookName(book.getAuthor(), book.getBookName());
        System.out.println("existsByAuthorAndBookName: " + existsByAuthorAndBookName);
        if (!existsByAuthorAndBookName) {
            repository.insertBook(book.getBookTypeId(), book.getBookName(), book.getAuthor(), book.getBookTag(), book.getBookDetail());
            Integer lastInsertId = repository.getLastInsertId();
            System.out.println("insertBook: " + lastInsertId);
            if (file != null) {
                fileStorageService.store(file, lastInsertId);
            }
        } else {
            throw new HandleExceptionBadRequest("Book Already Exists");
        }
        response.setResponse_code(201);
        response.setResponse_status("Created");
        response.setResponse_message("Book Created");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse updateBook(UpdateBookDTO book, Integer bookId, MultipartFile file) {

        DataResponse response = new DataResponse();

        Book dataBook = repository.getBookById(bookId);

        if (dataBook.getAuthor().equals(book.getAuthor()) && dataBook.getBookName().equals(book.getBookName())) {

            repository.updateBook(book.getBookTypeId(), book.getBookName(), book.getAuthor(), book.getBookTag(), book.getBookDetail(),
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

            bookDTO.setBooktype(booktypeRepository.getBooktypeById(book.getBookTypeId()));
            bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(","))));

            try {
                if (book.getStatus() != null) {
                    Path pathFile = fileStorageService.load(bookDTO);
                    bookDTO.setFile(pathFile.toString());
                } else {
                    fileStorageService.deleteFile(newDataBook);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Book Updated");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(bookDTO);
            return response;
        } else {
            Boolean existsByAuthorAndBookName = repository.existsByAuthorAndBookName(book.getAuthor(),
                    book.getBookName());

            if (!existsByAuthorAndBookName) {

                repository.updateBook(book.getBookTypeId(), book.getBookName(), book.getAuthor(), book.getBookTag(), book.getBookDetail(),
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

                bookDTO.setBooktype(booktypeRepository.getBooktypeById(book.getBookTypeId()));
                bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(","))));

                try {
                    if (book.getStatus() != null) {
                        Path pathFile = fileStorageService.load(bookDTO);
                        bookDTO.setFile(pathFile.toString());
                    } else {
                        fileStorageService.deleteFile(newDataBook);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.setResponse_code(200);
                response.setResponse_status("OK");
                response.setResponse_message("Book Updated");
                response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
                response.setData(bookDTO);
                return response;
            } else {
                throw new HandleExceptionBadRequest("Book Already Exists");
            }
        }

    }

    public DataResponse deleteBook(int bookId) {
        DataResponse response = new DataResponse();
        // try {
            Book dataBook = repository.getBookById(bookId);
            Integer deleteStatus = repository.deleteBook(bookId);
            System.out.println("deleteBook: " + deleteStatus);
            if (deleteStatus == 1) {
                fileStorageService.deleteFile(dataBook);
            }
        // } catch (Exception e) {
        //     throw new HandleExceptionBadRequest("Book Can not delete because book have reviews.");
        // }
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Book Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

}
