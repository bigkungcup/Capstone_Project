package sit.cp23ej2.services;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Book.CreateBookDTO;
import sit.cp23ej2.dtos.Book.PageBookDTO;
import sit.cp23ej2.dtos.Book.UpdateBookDTO;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.exception.HandleExceptionBadRequest;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;

@Service
public class BookService extends CommonController {

    @Autowired
    private BookRepository repository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getBook(int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        // Page<Book> book = repository.getAllBooks(pageable);
        PageBookDTO books = modelMapper.map(repository.getAllBooks(pageable), PageBookDTO.class);
        if (books.getContent().size() > 0) {
            List<BookDTO> bookDTOs = books.getContent();
            bookDTOs.forEach(bookDTO -> {
                // bookDTO.setBookTotalView(bookDTO.getBookTotalView() + 1);
                // repository.updateBook(bookDTO.getBookName(), bookDTO.getAuthor(),
                // bookDTO.getBookGenre(), bookDTO.getBookDetail(), bookDTO.getBookTotalView(),
                // bookDTO.getBookRating(), bookDTO.getBookId());
                // LocalDateTime targetTime =
                // LocalDateTime.parse(bookDTO.getBookUpdateDateTime());
                // if(Math.abs(duration.toHours()) > 24){
                // bookDTO.setCountDateTime(Math.abs(duration.toDays()) + " days");
                // }else if (Math.abs(duration.toMinutes()) > 60) {
                // bookDTO.setCountDateTime(Math.abs(duration.toHours()) + " hours");
                // }else if (Math.abs(duration.toSeconds()) > 60) {
                // bookDTO.setCountDateTime(Math.abs(duration.toMinutes()) + " minutes");
                // }else if (Math.abs(duration.toSeconds()) < 60) {
                // bookDTO.setCountDateTime(Math.abs(duration.toSeconds()) + " seconds ago");
                // }
                // bookDTO.setUpdateDateTime(Math.abs(duration.toDays()) + " days ago " +
                // Math.abs(duration.toHours()) + " hours ago " + Math.abs(duration.toMinutes())
                // + " minutes ago " + Math.abs(duration.toSeconds()) + " seconds ago");
                Duration duration = Duration.between(LocalDateTime.now(), bookDTO.getBookUpdateDateTime());
                bookDTO.setCountDateTime(Math.abs(duration.toSeconds()));
                // Path path = Paths.get(fileStorageProperties.getUploadDir() + "/" +
                // bookDTO.getBookName());
                // System.out.println("Path" + path.toString());
                // Resource resource = fileStorageService.loadAsResource(bookDTO);
                try {
                    // Path pathFile = Files.list(path).collect(Collectors.toList()).get(0);
                    // System.out.println("Path File" + pathFile.toString());
                    // System.out.println("File name" + resource.getFile().getName());
                    // System.out.println("Resource"+ resource.getURI().toString());
                    Path pathFile = fileStorageService.load(bookDTO);
                    if(pathFile != null){
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

    public DataResponse getBookById(int bookId) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Book book = repository.getBookById(bookId);
        if (book != null) {
            BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
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
            repository.insertBook(book.getBookName(), book.getAuthor(), book.getBookGenre(), book.getBookDetail());
            if (file != null) {
                fileStorageService.store(file, book.getBookName(), book.getAuthor());
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
       
        repository.updateBook(book.getBookName(), book.getAuthor(), book.getBookGenre(), book.getBookDetail(),
                book.getBookRating(), bookId);

        Book newDataBook = repository.getBookById(bookId);
        BookDTO bookDTO = modelMapper.map(newDataBook, BookDTO.class);

        if (file != null) {
            Book dataBook = repository.getBookById(bookId);
            fileStorageService.deleteFile(dataBook);
            fileStorageService.store(file, book.getBookName(), book.getAuthor());
        }

        try {
            if(book.getFilePath() != null){
                Path pathFile = fileStorageService.load(bookDTO);
                bookDTO.setFile(pathFile.toString());
            }else{
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
    }

    public DataResponse deleteBook(int bookId) {
        DataResponse response = new DataResponse();
        try {
            Integer deleteStatus = repository.deleteBook(bookId);
            System.out.println("deleteBook: " + deleteStatus);
            if(deleteStatus == 1){
                Book dataBook = repository.getBookById(bookId);
                fileStorageService.deleteFile(dataBook);
            }
        }catch (Exception e) {
            throw new HandleExceptionBadRequest("Book Can not delete because book have reviews.");
        }
        response.setResponse_code(200);
        response.setResponse_status("OK");
        response.setResponse_message("Book Deleted");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

}
