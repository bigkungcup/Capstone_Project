package sit.cp23ej2.services;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import sit.cp23ej2.controllers.CommonController;
import sit.cp23ej2.dtos.DataResponse;
import sit.cp23ej2.dtos.Book.BookDTO;
import sit.cp23ej2.dtos.Folloing.FollowingReviewDTO;
import sit.cp23ej2.dtos.Review.CreateReviewDTO;
import sit.cp23ej2.dtos.Review.PageReviewAllDTO;
import sit.cp23ej2.dtos.Review.PageReviewMeDTO;
import sit.cp23ej2.dtos.Review.ReviewMeDTO;
import sit.cp23ej2.dtos.Review.ReviewRatingStatisticsDTO;
import sit.cp23ej2.dtos.Review.UpdateReviewDTO;
import sit.cp23ej2.dtos.User.UserDTO;
import sit.cp23ej2.dtos.User.UserReviewDTO;
import sit.cp23ej2.entities.LikeStatus;
import sit.cp23ej2.entities.Review;
import sit.cp23ej2.entities.User;
import sit.cp23ej2.exception.HandleExceptionForbidden;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.repositories.BookRepository;
import sit.cp23ej2.repositories.BookmarkRepository;
import sit.cp23ej2.repositories.FollowReposiroty;
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

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FollowReposiroty followReposiroty;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

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

        PageReviewAllDTO reviews = modelMapper.map(repository.getReviewByBookId(pageable, bookId, reviewRating),
                PageReviewAllDTO.class);
        if (reviews.getContent().size() > 0) {

            reviews.getContent().forEach(review -> {
                UserReviewDTO userDTO = modelMapper.map(review.getUser(), UserReviewDTO.class);
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
                    followReposiroty.getFollowingList(user.getUserId()).forEach(following -> {
                        FollowingReviewDTO folloingReview = modelMapper.map(following, FollowingReviewDTO.class);
                        if (review.getUser().getUserId() == following.getUserfollow().getUserId()) {
                            userDTO.setFollowingReview(folloingReview);
                        }
                    });

                }

                Duration duration = Duration.between(LocalDateTime.now(), review.getReviewCreateDateTime());
                review.setCountDateTime(Math.abs(duration.toSeconds()));

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
                    // if (likeStatus.size() == 0) {
                    // LikeStatus likestatus = new LikeStatus();
                    // likestatus.setLikeStatus(0);
                    // review.setLikeStatus(likestatus);
                    // } else {
                    if (likeStatus.size() > 0) {
                        likeStatus.forEach(like -> {
                            if (review.getReviewId().equals(like.getLsr_reviewId())) {
                                review.setLikeStatus(like);
                            }
                        });
                    }
                    // likeStatus.forEach(like -> {
                    // if (review.getReviewId().equals(like.getLsr_reviewId())) {
                    // review.setLikeStatus(like);
                    // }
                    // // else {
                    // // LikeStatus likestatus = new LikeStatus();
                    // // likestatus.setLikeStatus(0);
                    // // review.setLikeStatus(likestatus);
                    // // }
                    // });
                    // }
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

    public DataResponse getReviewByUserId(Integer userId, int page, int size) throws HandleExceptionNotFound {
        DataResponse response = new DataResponse();

        Pageable pageable = PageRequest.of(page, size);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        PageReviewMeDTO reviews = modelMapper.map(repository.getReviewByUserId(pageable, userId),
                PageReviewMeDTO.class);

        if (reviews.getContent().size() > 0) {

            reviews.getContent().forEach(review -> {
                UserDTO userDTO = modelMapper.map(review.getUser(), UserDTO.class);
                try {
                    Path pathFile = fileStorageService.loadUserFile(userDTO.getUserId());
                    if (pathFile != null) {
                        userDTO.setFile(baseUrl + "/api/files/filesUser/" + userDTO.getUserId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                review.setUserDetail(userDTO);

                BookDTO bookDTO = modelMapper.map(review.getBook(), BookDTO.class);
                Path pathFile = fileStorageService.load(bookDTO);
                try {
                    if (pathFile != null) {
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" + review.getBook().getBookId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                review.setBookDetail(bookDTO);

                Duration duration = Duration.between(LocalDateTime.now(), review.getReviewCreateDateTime());
                review.setCountDateTime(Math.abs(duration.toSeconds()));
            });

            if (user != null) {
                List<LikeStatus> likeStatus = likeStatusRepository.getLikeStatus(userId);
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
            List<ReviewMeDTO> reviewDTOs = new ArrayList<>();
            reviews.forEach(review -> {
                ReviewMeDTO reviewDTO = modelMapper.map(review, ReviewMeDTO.class);
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

                BookDTO bookDTO = modelMapper.map(review.getBook(), BookDTO.class);
                try {
                    // && fileStorageService.load(review.getBook().getBookId()) != null
                    Path pathFile = fileStorageService.load(bookDTO);
                    if (pathFile != null) {
                        bookDTO.setFile(baseUrl + "/api/files/filesBook/" +
                                review.getBook().getBookId());
                    }

                    if (bookDTO.getBookTag() != null) {
                        bookDTO.setBookTag(bookDTO.getBookTag().replaceAll(",", ", "));
                        bookDTO.setBookTagList(new ArrayList<String>(Arrays.asList(bookDTO.getBookTag().split(", "))));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                reviewDTO.setBookDetail(bookDTO);

                if (user != null) {
                    List<LikeStatus> likeStatus = likeStatusRepository.getLikeStatus(user.getUserId());
                    likeStatus.forEach(like -> {
                        if (review.getReviewId() == like.getLsr_reviewId()) {
                            reviewDTO.setLikeStatus(like);
                        }
                    });
                }

                Duration duration = Duration.between(LocalDateTime.now(), review.getReviewCreateDateTime());
                reviewDTO.setCountDateTime(Math.abs(duration.toSeconds()));

                reviewDTOs.add(reviewDTO);
            });
            return responseWithData(reviewDTOs, 200, "OK", "New Review");
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
    }

    public DataResponse getReviewRatingCount() {
        ReviewRatingStatisticsDTO reviewRatingCount = repository.getReviewRatingCount();

        if (reviewRatingCount != null) {
            return responseWithData(reviewRatingCount, 200, "OK", "Review Rating Count");
        } else {
            throw new HandleExceptionNotFound("Review Not Found", "Review");
        }
    }

    public DataResponse createReviewByBookId(CreateReviewDTO review) {
        DataResponse response = new DataResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.getUserByEmail(currentPrincipalName);

        if (userRepository.getUserById(review.getUserId()) == null) {
            throw new HandleExceptionNotFound("User Not Found", "User");
        }

        if (!user.getUserId().equals(review.getUserId())) {
            throw new HandleExceptionForbidden("Can not create review for user: ");
        }

        repository.insertReview(review.getRating(), review.getDetail(), review.getTitle(), review.getUserId(),
                review.getBookId(), review.getSpoileFlag());

        BookDTO bookDTO = modelMapper.map(bookRepository.getBookById(review.getBookId()), BookDTO.class);

        bookmarkRepository.getBookmarkListByBookId(review.getBookId()).forEach(bookmark -> {
            // System.out.println("Insert Notification check bookmark");
            // if (bookmark.getBook().getBookId() == review.getBookId()) {
                // throw new HandleExceptionForbidden("Can not create review for user: ");
                System.out.println("Insert Notification Success");
                notificationRepository.insertNotification(bookmark.getUser().getUserId(),
                        bookmark.getBook().getBookName(), "has a new review. Check it now!", 0, 0,
                        "/book/" + bookmark.getBook().getBookId() + "/", "Bookmark");
            // }
        });

        followReposiroty.getFollowerList(user.getUserId()).forEach(following -> {
            // if (following.getUserfollow().getUserId() == review.getUserId()) {
                System.out.println("Insert Notification Success");
                notificationRepository.insertNotification(following.getUserfollow().getUserId(),
                        following.getUserfollow().getDisplayName() + "(Following)",
                        "has created a review in " + bookDTO.getBookName(), 0, 0, "/book/" + bookDTO.getBookId() + "/",
                        "Review");
                        // System.out.println("Insert Notification");
            // }
        });

        bookRepository.increaseBookTotalReview(review.getBookId());
        userRepository.increaseTotalReview(review.getUserId());
        bookRepository.updateBookRating(review.getBookId());
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
            bookRepository.updateBookRating(reviewData.getBook().getBookId());
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
            bookRepository.updateBookRating(reviewData.getBook().getBookId());
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
            bookRepository.updateBookRating(review.getBook().getBookId());
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

            // try {
            //     System.out.println("Send Email");
            //     Map<String, Object> variables = new HashMap<>();
            //     variables.put("bookName", review.getBook().getBookName());
            //     variables.put("reviewName", review.getReviewTitle());
            //     variables.put("reviewDetail", review.getReviewDetail());
            //     emailService.sendEmail(review.getUser().getEmail(), "Your review in Bannarug has been deleted.",
            //             "Delete Review", variables);
            // } catch (AddressException e) {
            //     System.out.println("Address Exception" + e.getMessage());
            //     e.printStackTrace();
            // } catch (MessagingException e) {
            //     System.out.println("Messaging Exception" + e.getMessage());
            //     e.printStackTrace();
            // }

            bookRepository.updateBookRating(review.getBook().getBookId());
            response.setResponse_code(200);
            response.setResponse_status("OK");
            response.setResponse_message("Review Deleted");
            response.setResponse_datetime(sdf3.format(new Timestamp(System.currentTimeMillis())));
        }

        return response;
    }

}
