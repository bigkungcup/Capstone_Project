package sit.cp23ej2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import sit.cp23ej2.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

        @Query(value = "SELECT n.notificationId, n.nu_userId, n.notificationTitle, n.notificationDetail, n.notificationStatus, n.notificationLevel, "
                        +
                        " n.notificationLink, n.notificationType, n.notificationCreateDateTime, n.notificationUpdateDateTime"
                        +
                        " FROM Notification n WHERE n.nu_userId = :userId AND n.notificationLevel = :notificationLevel", nativeQuery = true)
        List<Notification> getNotificationByUserIdAndLevel(@Param("userId") Integer userId,
                        @Param("notificationLevel") Integer notificationLevel);

        @Query(value = "SELECT n.notificationId, n.nu_userId, n.notificationTitle, n.notificationDetail, n.notificationStatus, n.notificationLevel, "
                        +
                        " n.notificationLink, n.notificationType, n.notificationCreateDateTime, n.notificationUpdateDateTime"
                        +
                        " FROM Notification n WHERE n.nu_userId = :userId", nativeQuery = true)
        List<Notification> getNotificationByUserId(@Param("userId") Integer userId);

        @Query(value = "SELECT n.notificationId, n.nu_userId, n.notificationTitle, n.notificationDetail, n.notificationStatus, n.notificationLevel, "
                        +
                        " n.notificationLink, n.notificationType, n.notificationCreateDateTime, n.notificationUpdateDateTime"
                        +
                        " FROM Notification n", nativeQuery = true)
        List<Notification> getNotificationAll();

        @Query(value = " SELECT COUNT(n.notificationId) " +
                        " FROM Notification n WHERE n.nu_userId = :userId AND n.notificationStatus = 0", nativeQuery = true)
        Integer getCountNotification(@Param("userId") Integer userId);

        @Query(value = " SELECT COUNT(n.notificationId) " +
                        " FROM Notification n WHERE n.nu_userId = :userId AND n.notificationStatus = 0 AND n.notificationLevel = 0", nativeQuery = true)
        Integer getCountNotificationNormal(@Param("userId") Integer userId);

        @Query(value = " SELECT COUNT(n.notificationId) " +
                        " FROM Notification n WHERE n.nu_userId = :userId AND n.notificationStatus = 0 AND n.notificationLevel = 1", nativeQuery = true)
        Integer getCountNotificationSystem(@Param("userId") Integer userId);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO Notification (nu_userId, notificationTitle, notificationDetail, notificationStatus, notificationLevel, notificationLink, notificationType)"
                        +
                        "VALUES (:nu_userId, :notificationTitle, :notificationDetail, :notificationStatus, :notificationLevel, :notificationLink, :notificationType);", nativeQuery = true)
        void insertNotification(@Param("nu_userId") Integer userId,
                        @Param("notificationTitle") String notificationTitle,
                        @Param("notificationDetail") String notificationDetail,
                        @Param("notificationStatus") Integer notificationStatus,
                        @Param("notificationLevel") Integer notificationLevel,
                        @Param("notificationLink") String notificationLink,
                        @Param("notificationType") String notificationType);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus =
        // :notificationStatus, notificationLevel = :notificationLevel, notificationLink
        // = :notificationLink"
        // +
        // " WHERE notificationId = :notificationId", nativeQuery = true)
        // void updateNotification(@Param("notificationStatus") Integer
        // notificationStatus,
        // @Param("notificationLevel") Integer notificationLevel,
        // @Param("notificationLink") String notificationLink,
        // @Param("notificationId") Integer notificationId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Notification SET notificationStatus = :notificationStatus "
                        +
                        " WHERE notificationId = :notificationId", nativeQuery = true)
        void updateNotification(@Param("notificationStatus") Integer notificationStatus,
                        @Param("notificationId") Integer notificationId);

        boolean existsByNotificationId(Integer notificationId);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus = 1 WHERE nu_userId = :userId", nativeQuery = true)
        // void updateAllNotification(@Param("userId") Integer userId);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus = 1 WHERE nu_userId = :userId AND notificationLevel = 0", nativeQuery = true)
        // void updateAllNotificationNormal(@Param("userId") Integer userId);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus = 1 WHERE nu_userId = :userId AND notificationLevel = 1", nativeQuery = true)
        // void updateAllNotificationSystem(@Param("userId") Integer userId);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus = 1 WHERE notificationId = :notificationId", nativeQuery = true)
        // void updateNotificationById(@Param("notificationId") Integer notificationId);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus = 1 WHERE notificationId = :notificationId AND notificationLevel = 0", nativeQuery = true)
        // void updateNotificationNormalById(@Param("notificationId") Integer notificationId);

        // @Modifying
        // @Transactional
        // @Query(value = "UPDATE Notification SET notificationStatus = 1 WHERE notificationId = :notificationId AND notificationLevel = 1", nativeQuery = true)
        // void updateNotificationSystemById(@Param("notificationId") Integer notificationId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM Notification WHERE notificationId = :notificationId", nativeQuery = true)
        void deleteNotificationById(@Param("notificationId") Integer notificationId);
}
