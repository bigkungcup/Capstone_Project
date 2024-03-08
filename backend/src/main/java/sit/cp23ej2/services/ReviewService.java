package sit.cp23ej2.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import sit.cp23ej2.dtos.Folloing.FollowingReviewDTO;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewMeDTO;
import sit.cp23ej2.dtos.Review.ReviewDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.entities.LikeStatus;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionForbidden;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.BookmarkRepository;
import sit.cp23ej2.repositories.FollowingReposiroty;
import sit.cp23ej2.repositories.LikeStatusRepository;
import sit.cp23ej2.repositories.NotificationRepository;
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

    // @Autowired
    // private FollowingReposiroty followerReposiroty;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FollowingReposiroty followingReposiroty;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private NotificationRepository notificationRepository;

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

                    // System.out.println(pathFile.toString());
                    // user.setFile(pathFile.toString());
                    // bookDTO.setFile("http://localhost:8080/api/files/filesUser/" +
                    // user.getUserId());
                    if (review.getUser() != null
                            && fileStorageService.loadUserFile(review.getUser().getUserId()) != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + review.getUser().getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (user != null) {
                    followingReposiroty.getReviewFollowings(user.getUserId()).forEach(following -> {
                        FollowingReviewDTO folloingReview = modelMapper.map(following, FollowingReviewDTO.class);
                        if (review.getUser().getUserId() == following.getFollowingId()) {
                            userDTO.setFollowerReview(folloingReview);
                        }
                    });

                }
                // followerReposiroty.getReviewFollowings(user.getUserId()).forEach(following ->
                // {
                // FollowerReviewDTO followerReview = modelMapper.map(following,
                // FollowerReviewDTO.class);
                // if (review.getUser().getUserId() == following.getFollowerId()) {
                // userDTO.setFollowerReview(followerReview);
                // }
                // });
                review.setUserDetail(userDTO);
            });

            if (user != null) {
                List<LikeStatus> likeStatus = likeStatusRepository.getLikeStatus(user.getUserId());
                reviews.getContent().forEach(review -> {
                    if (likeStatus.size() == 0) {
                        LikeStatus likestatus = new LikeStatus();
                        likestatus.setLikeStatus(0);
                        review.setLikeStatus(likestatus);
                    } else {
                        likeStatus.forEach(like -> {
                            if (review.getReviewId() == like.getLsr_reviewId()) {
                                review.setLikeStatus(like);
                            } else {
                                LikeStatus likestatus = new LikeStatus();
                                likestatus.setLikeStatus(0);
                                review.setLikeStatus(likestatus);
                            }
                        });
                    }
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
                            review.setLikeStatus(like);
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

    public DataResponse getReviewByCreateDateTime() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        List<Review> reviews = repository.getReviewByCreateDateTime();
        if (reviews.size() > 0) {
            List<ReviewDTO> reviewDTOs = new ArrayList<>();
            reviews.forEach(review -> {
                ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
                UserDTO userDTO = modelMapper.map(review.getUser(), UserDTO.class);
                try {
                    if (review.getUser() != null
                            && fileStorageService.loadUserFile(review.getUser().getUserId()) != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + review.getUser().getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                reviewDTO.setUserDetail(userDTO);

                // BookDTO bookDTO = modelMapper.map(review.getBook(), BookDTO.class);
                // try {
                //     // && fileStorageService.load(review.getBook().getBookId()) != null
                //     if (review.getBook() != null) {
                //         bookDTO.setFile(baseUrl + "/api/files/filesBook/" + review.getBook().getBookId());
                //     }

                // } catch (Exception e) {
                //     e.printStackTrace();
                // }
                // review.set(bookDTO);

                if (user != null) {
                    List<LikeStatus> likeStatus = likeStatusRepository.getLikeStatus(user.getUserId());
                    
                        likeStatus.forEach(like -> {
                            if (review.getReviewId() == like.getLsr_reviewId()) {
                                reviewDTO.setLikeStatus(like);
                            }
                    });
                }

                reviewDTOs.add(reviewDTO);
            });
            return responseWithData(reviewDTOs, 200, "OK", "New Review");
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
    }

    public DataResponse createReviewByBookId(CreateReviewDTO review) {
        DataResponse response = new DataResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        repository.insertReview(review.getRating(), review.getDetail(), review.getTitle(), review.getUserId(),
                review.getBookId(), review.getSpoileFlag());

        BookDTO bookDTO = modelMapper.map(bookRepository.getBookById(review.getBookId()), BookDTO.class);

        bookmarkRepository.getBookmarkListByUserId(user.getUserId()).forEach(bookmark -> {
            if (bookmark.getBook().getBookId() == review.getBookId()) {
                // throw new HandleExceptionForbidden("Can not create review for user: ");
                notificationRepository.insertNotification(user.getUserId(),
                        bookmark.getBook().getBookName(), "has a new review. Check it now!", 0, 0, "/book/" + bookmark.getBook().getBookId() + "/", "Bookmark");
            }
        });

        followingReposiroty.getFollowingList(user.getUserId()).forEach(following -> {
            if (following.getFollowingId() == review.getUserId()) {
                notificationRepository.insertNotification(user.getUserId(),
                       following.getUserfollowing().getDisplayName() + "(Following)", "has created a review in " + bookDTO.getBookName(), 0, 0, "/book/" + bookDTO.getBookId() + "/", "Review");
            }
        });

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
