package sit.cp23ej2.services;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

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
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.CheckLikeReview;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionForbidden;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.CheckLikeReviewRepository;
import sit.cp23ej2.repositories.ReviewRepository;
import sit.cp23ej2.repositories.UserRepository;

@Service
public class ReviewService extends CommonController {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CheckLikeReviewRepository checkLikeReviewRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getReviewByBookId(int bookId, int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        PageReviewDTO reviews = modelMapper.map(repository.getReviewByBookId(bookId, pageable), PageReviewDTO.class);
        if (reviews.getContent().size() > 0) {

            reviews.getContent().forEach(review -> {
                UserDTO userDTO = modelMapper.map(review.getUser(), UserDTO.class);
                try {
                    Path pathFile = fileStorageService.loadUserFile(review.getUser().getUserId());
                    System.out.println(pathFile.toString());
                    // user.setFile(pathFile.toString());
                    // bookDTO.setFile("http://localhost:8080/api/files/filesUser/" + user.getUserId());
                    userDTO.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                  
                } catch (Exception e) {
                    e.printStackTrace();
                }
                review.setUserDetail(userDTO);
            });

            List<CheckLikeReview> likeStatus = checkLikeReviewRepository.getLikeStatus(user.getUserId());;

            reviews.getContent().forEach(review -> {
                likeStatus.forEach(like -> {
                    if (review.getReviewId() == like.getClr_reviewId()) {
                        review.setLikeStatus(like.getLikeStatus());
                    }
                });
            });

            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("All Reviews");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(reviews);
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }

        return response;
    }

    public DataResponse getReviewById(int reviewId) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Review review = repository.getReviewById(reviewId);
        if (review != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(review);
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
        return response;
    }

    public DataResponse getReviewByUserId(int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        Pageable pageable = PageRequest.of(page, size);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        PageReviewDTO review = modelMapper.map(repository.getReviewByUserId(user.getUserId(), pageable),
                PageReviewDTO.class);

        if (review != null) {
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(review);
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
        return response;
    }

    public DataResponse createReviewByBookId(CreateReviewDTO review) {
        DataResponse response = new DataResponse();
        repository.insertReview(review.getRating(), review.getDetail(), review.getTitle(), review.getUserId(),
                review.getBookId(), review.getSpoileFlag());
        bookRepository.increaseBookTotalReview(review.getBookId());
        userRepository.increaseTotalReview(review.getUserId());
        bookRepository.updateBookReting(review.getBookId());
        response.setResponse_code(201);
        response.setResponse_status("Created");
        response.setResponse_message("Review Created");
        response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        return response;
    }

    public DataResponse updateReviewByBookId(UpdateReviewDTO review, Integer reviewId) {
        DataResponse response = new DataResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        Review reviewData = repository.getReviewById(reviewId);

        if (reviewData == null) {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }

        // System.out.println(reviewData.getUser().getUserId() + " " + user.getUserId()
        // + " " + user.getRole());

        if (reviewData.getUser().getUserId() != user.getUserId() && !user.getRole().equals("ADMIN")) {
            throw new HandleExceptionForbidden("Can not update review for user: ");
        } else if (reviewData.getUser().getUserId() == user.getUserId() && user.getRole().equals("USER")) {
            repository.updateReview(review.getRating(), review.getDetail(), review.getTitle(), review.getSpoileFlag(),
                    reviewId);
            Review dataReview = repository.getReviewById(reviewId);

            dataReview.setReviewTitle(review.getTitle());
            dataReview.setReviewDetail(review.getDetail());
            dataReview.setReviewRating(review.getRating());
            dataReview.setSpoileFlag(review.getSpoileFlag());

            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review Updated");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(dataReview);
        } else if (user.getRole().equals("ADMIN")) {
            repository.updateReview(review.getRating(), review.getDetail(), review.getTitle(), review.getSpoileFlag(),
                    reviewId);
            Review dataReview = repository.getReviewById(reviewId);

            dataReview.setReviewTitle(review.getTitle());
            dataReview.setReviewDetail(review.getDetail());
            dataReview.setReviewRating(review.getRating());
            dataReview.setSpoileFlag(review.getSpoileFlag());

            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review Updated");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(dataReview);
        }

        return response;
    }

    // public DataResponse updateReviewTotalLikeAndTotalDisLike(UpdateReviewDTO
    // review, Integer reviewId) {
    // DataResponse response = new DataResponse();
    // repository.updateTotalLikeAndTotalDisLike(review.getTotalLike(),
    // review.getTotalDisLike(), reviewId);
    // Review dataReview = repository.getReviewById(reviewId);
    // response.setResponse_code(200);
    // response.setResponse_status("OK");
    // response.setResponse_message("Review TotalLike and TotalDislike Updated");
    // response.setResponse_datetime(sdf3.format(new
    // Timestamp(System.currentTimeMillis())));
    // response.setData(dataReview);
    // return response;
    // }

    public DataResponse deleteReviewByBookId(int reviewId) {
        DataResponse response = new DataResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);
        Review review = repository.getReviewById(reviewId);

        if (review == null) {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }

        // System.out.println(review.getUser().getUserId() + " " + user.getUserId() + "
        // " + user.getRole());

        if (review.getUser().getUserId() != user.getUserId() && !user.getRole().equals("ADMIN")) {
            throw new HandleExceptionForbidden("Can not delete review for user: ");
        } else if (review.getUser().getUserId() == user.getUserId() && user.getRole().equals("USER")) {
            bookRepository.decreaseBookTotalReview(review.getBook().getBookId());
            userRepository.decreaseTotalReview(review.getUser().getUserId());
            Integer deleteStatus = repository.deleteReview(reviewId);
            if (deleteStatus == 0) {
                throw new HandleExceptionNotFound("Review Not Found", "Review");
            }
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review Deleted");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        } else if (user.getRole().equals("ADMIN")) {
            bookRepository.decreaseBookTotalReview(review.getBook().getBookId());
            userRepository.decreaseTotalReview(review.getUser().getUserId());
            Integer deleteStatus = repository.deleteReview(reviewId);
            if (deleteStatus == 0) {
                throw new HandleExceptionNotFound("Review Not Found", "Review");
            }
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review Deleted");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        }

        return response;
    }

}
