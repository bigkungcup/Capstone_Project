-- MySQL Script generated by MySQL Workbench
-- Tue Apr  9 23:33:31 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema bannarug
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `bannarug` ;

-- -----------------------------------------------------
-- Schema bannarug
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bannarug` DEFAULT CHARACTER SET utf8 ;
USE `bannarug` ;

-- -----------------------------------------------------
-- Table `bannarug`.`Book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Book` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Book` (
  `bookId` INT NOT NULL AUTO_INCREMENT,
  `bb_booktypeId` INT NOT NULL,
  `bookName` VARCHAR(255) NOT NULL,
  `author` VARCHAR(255) NOT NULL,
  `bookTotalView` INT NOT NULL,
  `bookRating` FLOAT NOT NULL,
  `bookTag` VARCHAR(255) NULL,
  `bookTotalReview` INT NULL,
  `bookDetail` LONGTEXT NULL,
  `bookCreateDateTime` DATETIME NULL default current_timestamp,
  `bookUpdateDateTime` DATETIME NULL default current_timestamp,
  PRIMARY KEY (`bookId`, `bb_booktypeId`),
  INDEX `fk_Book_Booktype1_idx` (`bb_booktypeId` ASC) VISIBLE,
  CONSTRAINT `fk_Book_Booktype1`
    FOREIGN KEY (`bb_booktypeId`)
    REFERENCES `bannarug`.`Booktype` (`booktypeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Bookmark`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Bookmark` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Bookmark` (
  `bookmarkId` INT NOT NULL AUTO_INCREMENT,
  `bb_bookId` INT NOT NULL,
  `bu_userId` INT NOT NULL,
  `bookmarkStatus` INT NULL,
  `bookmarkCreateDateTime` DATETIME NULL default current_timestamp,
  `bookmarkUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`bookmarkId`, `bb_bookId`, `bu_userId`),
  INDEX `fk_Book_has_User_User1_idx` (`bu_userId` ASC) VISIBLE,
  INDEX `fk_Book_has_User_Book1_idx` (`bb_bookId` ASC) VISIBLE,
  CONSTRAINT `fk_Book_has_User_Book1`
    FOREIGN KEY (`bb_bookId`)
    REFERENCES `bannarug`.`Book` (`bookId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Book_has_User_User1`
    FOREIGN KEY (`bu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Booktype`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Booktype` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Booktype` (
  `booktypeId` INT NOT NULL AUTO_INCREMENT,
  `booktypeName` VARCHAR(255) NOT NULL,
  `booktypeCreateDateTime` DATETIME NULL default current_timestamp,
  `booktypeUpdateDateTme` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`booktypeId`),
  UNIQUE INDEX `booktypeName_UNIQUE` (`booktypeName` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Follow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Follow` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Follow` (
  `followId` INT NOT NULL AUTO_INCREMENT,
  `fu_userId` INT NOT NULL,
  `userFollowId` INT NOT NULL,
  `followStatus` INT NULL,
  `followCreateDateTime` DATETIME NULL default current_timestamp,
  `followUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  INDEX `fk_Follower_User1_idx` (`fu_userId` ASC) VISIBLE,
  INDEX `fk_Follower_User2_idx` (`userFollowId` ASC) VISIBLE,
  PRIMARY KEY (`followId`, `fu_userId`, `userFollowId`),
  CONSTRAINT `fk_Follower_User1`
    FOREIGN KEY (`fu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Follower_User2`
    FOREIGN KEY (`userFollowId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`History`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`History` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`History` (
  `historyId` INT NOT NULL AUTO_INCREMENT,
  `hu_userId` INT NOT NULL,
  `hb_bookId` INT NOT NULL,
  `historyCreateDateTime` DATETIME NULL default current_timestamp,
  `historyUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`historyId`, `hu_userId`, `hb_bookId`),
  INDEX `fk_User_has_Book_Book1_idx` (`hb_bookId` ASC) VISIBLE,
  INDEX `fk_User_has_Book_User1_idx` (`hu_userId` ASC) VISIBLE,
  CONSTRAINT `fk_User_has_Book_User1`
    FOREIGN KEY (`hu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Book_Book1`
    FOREIGN KEY (`hb_bookId`)
    REFERENCES `bannarug`.`Book` (`bookId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`LikeStatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`LikeStatus` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`LikeStatus` (
  `likeStatusId` INT NOT NULL AUTO_INCREMENT,
  `lsr_reviewId` INT NOT NULL,
  `lsu_userId` INT NOT NULL,
  `likeStatus` INT NULL DEFAULT 0,
  `likeStatusCreateDateTime` DATETIME NULL default current_timestamp,
  `likeStatusUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`likeStatusId`, `lsr_reviewId`, `lsu_userId`),
  INDEX `fk_Review_has_User_User1_idx` (`lsu_userId` ASC) VISIBLE,
  INDEX `fk_Review_has_User_Review1_idx` (`lsr_reviewId` ASC) VISIBLE,
  CONSTRAINT `fk_Review_has_User_Review1`
    FOREIGN KEY (`lsr_reviewId`)
    REFERENCES `bannarug`.`Review` (`reviewId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Review_has_User_User1`
    FOREIGN KEY (`lsu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Notification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Notification` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Notification` (
  `notificationId` INT NOT NULL AUTO_INCREMENT,
  `nu_userId` INT NOT NULL,
  `notificationTitle` VARCHAR(255) NOT NULL,
  `notificationDetail` LONGTEXT NOT NULL,
  `notificationStatus` INT NOT NULL,
  `notificationLevel` INT NOT NULL,
  `notificationLink` VARCHAR(255) NULL,
  `notificationType` VARCHAR(45) NULL,
  `notificationCreateDateTime` DATETIME NULL default current_timestamp,
  `notificationUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`notificationId`),
  INDEX `fk_Notification_User1_idx` (`nu_userId` ASC) VISIBLE,
  CONSTRAINT `fk_Notification_User1`
    FOREIGN KEY (`nu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Recommend`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Recommend` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Recommend` (
  `recommendId` INT NOT NULL AUTO_INCREMENT,
  `rb_booktypeId` INT NOT NULL,
  `ru_userId` INT NOT NULL,
  `viewCount` INT NULL,
  `recommendCreateDateTime` DATETIME NULL default current_timestamp,
  `recommendUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`recommendId`, `rb_booktypeId`, `ru_userId`),
  INDEX `fk_Booktype_has_User_User1_idx` (`ru_userId` ASC) VISIBLE,
  INDEX `fk_Booktype_has_User_Booktype1_idx` (`rb_booktypeId` ASC) VISIBLE,
  CONSTRAINT `fk_Booktype_has_User_Booktype1`
    FOREIGN KEY (`rb_booktypeId`)
    REFERENCES `bannarug`.`Booktype` (`booktypeId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Booktype_has_User_User1`
    FOREIGN KEY (`ru_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Report`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Report` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Report` (
  `reportId` INT NOT NULL AUTO_INCREMENT,
  `reportBy` INT NOT NULL,
  `fixBy` INT NULL,
  `reportTitle` VARCHAR(255) NOT NULL,
  `reportDetail` VARCHAR(255) NULL,
  `problemId` INT NOT NULL,
  `reportType` VARCHAR(45) NOT NULL,
  `reportStatus` INT NOT NULL,
  `reportCreateDateTime` DATETIME NULL default current_timestamp,
  `reportUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  `rr_reviewId` INT NULL,
  `rb_bookId` INT NULL,
  `ru_userId` INT NULL,
  PRIMARY KEY (`reportId`, `reportBy`),
  INDEX `fk_Report_User1_idx` (`reportBy` ASC) VISIBLE,
  INDEX `fk_Report_User2_idx` (`fixBy` ASC) VISIBLE,
  INDEX `fk_Report_Review1_idx` (`rr_reviewId` ASC) VISIBLE,
  INDEX `fk_Report_Book1_idx` (`rb_bookId` ASC) VISIBLE,
  INDEX `fk_Report_User3_idx` (`ru_userId` ASC) VISIBLE,
  CONSTRAINT `fk_Report_User1`
    FOREIGN KEY (`reportBy`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_User2`
    FOREIGN KEY (`fixBy`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_Review1`
    FOREIGN KEY (`rr_reviewId`)
    REFERENCES `bannarug`.`Review` (`reviewId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_Book1`
    FOREIGN KEY (`rb_bookId`)
    REFERENCES `bannarug`.`Book` (`bookId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_User3`
    FOREIGN KEY (`ru_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Review` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Review` (
  `reviewId` INT NOT NULL AUTO_INCREMENT,
  `rvu_userId` INT NOT NULL,
  `rvb_bookId` INT NOT NULL,
  `reviewTitle` VARCHAR(255) NOT NULL,
  `reviewDetail` LONGTEXT NOT NULL,
  `reviewRating` FLOAT NOT NULL,
  `spoileFlag` INT NOT NULL,
  `reviewTotalLike` INT NOT NULL,
  `reviewTotalDisLike` INT NOT NULL,
  `reviewCreateDateTime` DATETIME NULL default current_timestamp,
  `reviewUpdateDateTime` DATETIME NULL default current_timestamp,
  PRIMARY KEY (`reviewId`, `rvu_userId`),
  INDEX `fk_Review_User_idx` (`rvu_userId` ASC) VISIBLE,
  INDEX `fk_Review_Book1_idx` (`rvb_bookId` ASC) VISIBLE,
  CONSTRAINT `fk_Review_User`
    FOREIGN KEY (`rvu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Review_Book1`
    FOREIGN KEY (`rvb_bookId`)
    REFERENCES `bannarug`.`Book` (`bookId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`User` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`User` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `displayName` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` LONGTEXT NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `followers` INT NOT NULL,
  `followings` INT NOT NULL,
  `totalReview` INT NOT NULL,
  `totalFavoriteReview` INT NOT NULL,
  `totalLike` INT NOT NULL,
  `bio` LONGTEXT NULL,
  `userCreateDateTime` DATETIME NULL default current_timestamp,
  `userUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `displayName_UNIQUE` (`displayName` ASC) VISIBLE)
ENGINE = InnoDB;

USE `bannarug`;
DROP procedure IF EXISTS `createBookAndBookId`;

USE `bannarug`;
DROP procedure IF EXISTS `bannarug`.`createBookAndBookId`;
;

DELIMITER $$
USE `bannarug`$$
CREATE DEFINER=`root`@`%` PROCEDURE `createBookAndBookId`(
    IN booktypeId INT,
    IN bookName varchar(255),
    IN author varchar(255),
    IN bookTag varchar(255),
    IN bookDetail longtext,
    OUT lastInsertId INT
)
BEGIN
    INSERT INTO Book (bb_booktypeId, bookName, author, bookTag, bookDetail, bookTotalView, bookRating, bookTotalReview) 
    VALUES (bookTypeId, bookName, author, bookTag, bookDetail, 0, 0, 0);
	SET lastInsertId = LAST_INSERT_ID();
END$$

DELIMITER ;
;

USE `bannarug`;
DROP procedure IF EXISTS `updateBookRating`;

USE `bannarug`;
DROP procedure IF EXISTS `bannarug`.`updateBookRating`;
;

DELIMITER $$
USE `bannarug`$$
CREATE DEFINER=`root`@`%` PROCEDURE `updateBookRating`(
	IN in_book_id INT
)
BEGIN
	DECLARE SumData INT;
    DECLARE CountData INT;
    DECLARE Rating FLOAT;
    
    SELECT SUM(reviewRating) INTO SumData FROM Review WHERE rvb_bookId = in_book_id;
    SELECT SumData;
    
    SELECT COUNT(reviewRating) INTO CountData FROM Review WHERE rvb_bookId = in_book_id;
    SELECT CountData;
    
    SET Rating = SumData/CountData;
    SELECT Rating;
    
    IF Rating IS NULL THEN
		  SET Rating = 0;
	  ELSE 
		  SET Rating = Rating;
	  END IF;
    
    SELECT Rating;
    
    UPDATE Book b SET b.bookRating = Rating WHERE b.bookId = in_book_id;
END$$

DELIMITER ;
;

USE `bannarug`
INSERT INTO Booktype (booktypeName) VALUES ('Adventure');
INSERT INTO Booktype (booktypeName) VALUES ('Classic');
INSERT INTO Booktype (booktypeName) VALUES ('Crime');
INSERT INTO Booktype (booktypeName) VALUES ('Fairy tale');
INSERT INTO Booktype (booktypeName) VALUES ('Fantasy');
INSERT INTO Booktype (booktypeName) VALUES ('History');
INSERT INTO Booktype (booktypeName) VALUES ('Horror');
INSERT INTO Booktype (booktypeName) VALUES ('Textbooks');
INSERT INTO Booktype (booktypeName) VALUES ('Literature');
INSERT INTO Booktype (booktypeName) VALUES ('Mystery');
INSERT INTO Booktype (booktypeName) VALUES ('Poetry');
INSERT INTO Booktype (booktypeName) VALUES ('Plays');
INSERT INTO Booktype (booktypeName) VALUES ('Romance');
INSERT INTO Booktype (booktypeName) VALUES ('Science');
INSERT INTO Booktype (booktypeName) VALUES ('Journalism');
INSERT INTO Booktype (booktypeName) VALUES ('Thriller');
INSERT INTO Booktype (booktypeName) VALUES ('War');
INSERT INTO Booktype (booktypeName) VALUES ('Philosophy');
INSERT INTO Booktype (booktypeName) VALUES ('Health and Wellness');
INSERT INTO Booktype (booktypeName) VALUES ('Education');
INSERT INTO Booktype (booktypeName) VALUES ('Cookbook');
INSERT INTO Booktype (booktypeName) VALUES ('Arts');
INSERT INTO Booktype (booktypeName) VALUES ('Religion');
INSERT INTO Booktype (booktypeName) VALUES ('Business and Economics');
INSERT INTO Booktype (booktypeName) VALUES ('Hobby');

SET SQL_SAFE_UPDATES = 0;
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
