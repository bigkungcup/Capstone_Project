package sit.cp23ej2.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import sit.cp23ej2.entities.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

        @Query(value = "SELECT reportId, reportBy, fixBy, reportTitle, reportDetail, problemId, reportType, " +
                        " reportStatus, reportCreateDateTime, reportUpdateDateTime, rr_reviewId, rb_bookId, ru_userId " +
                        " FROM Report r " +
                        " WHERE reportStatus = 1 ", nativeQuery = true)
        Page<Report> getAllFixReport(Pageable pageable);

        @Query(value = "SELECT reportId, reportBy, fixBy, reportTitle, reportDetail, problemId, reportType, " +
                        " reportStatus, reportCreateDateTime, reportUpdateDateTime, rr_reviewId, rb_bookId, ru_userId " +
                        " FROM Report r " +
                        " WHERE reportStatus = 0", nativeQuery = true)
        Page<Report> getAllNotFixReport(Pageable pageable);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO Report (reportTitle, reportDetail, problemId, reportType, reportStatus, reportBy, rb_bookId) "
                        +
                        "VALUES (:reportTitle, :reportDetail, :problemId, :reportType, 0, :reportBy, :problemId)", nativeQuery = true)
        void insertReportBook(@Param("reportTitle") String reportTitle, @Param("reportDetail") String reportDetail,
                        @Param("problemId") Integer problemId,
                        @Param("reportType") String reportType, @Param("reportBy") Integer reportBy);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO Report (reportTitle, reportDetail, problemId, reportType, reportStatus, reportBy, rr_reviewId) "
                        +
                        "VALUES (:reportTitle, :reportDetail, :problemId, :reportType, 0, :reportBy, :problemId)", nativeQuery = true)
        void insertReportReview(@Param("reportTitle") String reportTitle, @Param("reportDetail") String reportDetail,
                        @Param("problemId") Integer problemId,
                        @Param("reportType") String reportType, @Param("reportBy") Integer reportBy);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO Report (reportTitle, reportDetail, problemId, reportType, reportStatus, reportBy, ru_userId) "
                        +
                        "VALUES (:reportTitle, :reportDetail, :problemId, :reportType, 0, :reportBy, :problemId)", nativeQuery = true)
        void insertReportUser(@Param("reportTitle") String reportTitle, @Param("reportDetail") String reportDetail,
                        @Param("problemId") Integer problemId,
                        @Param("reportType") String reportType, @Param("reportBy") Integer reportBy);

        @Modifying
        @Transactional
        @Query(value = "UPDATE Report SET reportStatus = 1, fixBy = :fixBy " +
                        "WHERE reportId = :reportId", nativeQuery = true)
        void updateReport(@Param("fixBy") Integer fixBy, @Param("reportId") Integer reportId);

        boolean existsByReportId(Integer reportId);

        @Query(value = "SELECT reportId, reportBy, fixBy, reportTitle, reportDetail, problemId, reportType, " +
                        " reportStatus, reportCreateDateTime, reportUpdateDateTime " +
                        " FROM Report r " +
                        " WHERE reportId = :reportId", nativeQuery = true)
        Report getReportById(@Param("reportId") Integer reportId);

}
