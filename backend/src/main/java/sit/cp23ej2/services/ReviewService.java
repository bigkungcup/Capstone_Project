package sit.cp23ej2.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Follow.FollowerReviewDTO;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewMeDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.LikeStatus;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionForbidden;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.FollowerReposiroty;
import sit.cp23ej2.repositories.LikeStatusRepository;
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
    private LikeStatusRepository likeStatusRepository;
    
    @Autowired
    private FollowerReposiroty followerReposiroty;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${base_url}")
    private String baseUrl;

    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataResponse getReviewByBookId(Integer bookId, int page, int size, Long reviewRating, String sortBy,
            String sortType) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();
        // Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        Pageable pageable;

        if (sortBy == null || sortBy.equals("")) {
            sortBy = "reviewId";
        }

        if ("DESC".equalsIgnoreCase(sortType)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else if ("ASC".equalsIgnoreCase(sortType)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        }

        PageReviewDTO reviews = modelMapper.map(repository.getReviewByBookId(pageable, bookId, reviewRating),
                PageReviewDTO.class);
        if (reviews.getContent().size() > 0) {

            reviews.getContent().forEach(review -> {
                UserDTO userDTO = modelMapper.map(review.getUser(), UserDTO.class);
                try {
                    // Path pathFile =
                    // fileStorageService.loadUserFile(review.getUser().getUserId());
                    // System.out.println(pathFile.toString());
                    // user.setFile(pathFile.toString());
                    // bookDTO.setFile("http://localhost:8080/api/files/filesUser/" +
                    // user.getUserId());
                    if (review.getUser() != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + review.getUser().getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(user != null){
                    followerReposiroty.getReviewFollowings(user.getUserId()).forEach(following -> {
                        FollowerReviewDTO followerReview = modelMapper.map(following, FollowerReviewDTO.class);
                        if (review.getUser().getUserId() == following.getFollowerId()) {
                            userDTO.setFollowerReview(followerReview);
                        }
                    });

                }
                // followerReposiroty.getReviewFollowings(user.getUserId()).forEach(following -> {
                //     FollowerReviewDTO followerReview = modelMapper.map(following, FollowerReviewDTO.class);
                //     if (review.getUser().getUserId() == following.getFollowerId()) {
                //         userDTO.setFollowerReview(followerReview);
                //     }
                // });
                review.setUserDetail(userDTO);
            });

            if (user != null) {
                List<LikeStatus> likeStatus = likeStatusRepository.getLikeStatus(user.getUserId());
                reviews.getContent().forEach(review -> {
                    likeStatus.forEach(like -> {
                        if (review.getReviewId() == like.getLsr_reviewId()) {
                            review.setLikeStatus(like);
                        }else{
                            LikeStatus likestatus = new LikeStatus();
                            likestatus.setLikeStatus(0);
                            review.setLikeStatus(likestatus);
                        }
                    });
                });
            }

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

    public DataResponse getReviewById(Integer reviewId) throws HandleExceptionNotFound {
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

        PageReviewMeDTO reviews = modelMapper.map(repository.getReviewByUserId(pageable, user.getUserId()),
                PageReviewMeDTO.class);

        if (reviews.getContent().size() > 0) {

            reviews.getContent().forEach(review -> {
                UserDTO userDTO = modelMapper.map(review.getUser(), UserDTO.class);
                try {
                    if (user != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + user.getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                review.setUserDetail(userDTO);

                BookDTO bookDTO = modelMapper.map(review.getBook(), BookDTO.class);
                try {
                    if (user != null) {
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + review.getBook().getBookId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                review.setBookDetail(bookDTO);
            });

            if (user != null) {
                List<LikeStatus> likeStatus = likeStatusRepository.getLikeStatus(user.getUserId());
                reviews.getContent().forEach(review -> {
                    likeStatus.forEach(like -> {
                        if (review.getReviewId() == like.getLsr_reviewId()) {
                            review.setLikeStatus(like.getLikeStatus());
                        }
                    });
                });
            }

            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
            response.setData(reviews);
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
