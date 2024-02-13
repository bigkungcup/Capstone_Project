-- MySQL Script generated by MySQL Workbench
-- Tue Feb 13 01:28:52 2024
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
  `bookUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
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
  `bookmarkCreateDateTime` DATETIME NULL default current_timestamp,
  `bookmarkUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`bookmarkId`, `bb_bookId`, `bu_userId`),
  INDEX `fk_Book_has_User_User1_idx` (`bu_userId` ASC) VISIBLE,
  INDEX `fk_Book_has_User_Book1_idx` (`bb_bookId` ASC) VISIBLE,
  CONSTRAINT `fk_Book_has_User_Book1`
    FOREIGN KEY (`bb_bookId`)
    REFERENCES `bannarug`.`Book` (`bookId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Book_has_User_User1`
    FOREIGN KEY (`bu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Booktype`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Booktype` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Booktype` (
  `booktypeId` INT NOT NULL AUTO_INCREMENT,
  `bookTypeName` VARCHAR(255) NOT NULL,
  `booktypeCreateDateTime` DATETIME NULL default current_timestamp,
  `booktypeUpdateDateTme` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`booktypeId`),
  UNIQUE INDEX `bookTypeName_UNIQUE` (`bookTypeName` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`FavoriteReview`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`FavoriteReview` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`FavoriteReview` (
  `favoriteReviewId` INT NOT NULL AUTO_INCREMENT,
  `frr_reviewId` INT NOT NULL,
  `fru_userId` INT NOT NULL,
  `favoriteReviewCreateDateTime` DATETIME NULL default current_timestamp,
  `favoriteReviewUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`favoriteReviewId`, `frr_reviewId`, `fru_userId`),
  INDEX `fk_Review_has_User_User1_idx` (`fru_userId` ASC) VISIBLE,
  INDEX `fk_Review_has_User_Review1_idx` (`frr_reviewId` ASC) VISIBLE,
  CONSTRAINT `fk_Review_has_User_Review1`
    FOREIGN KEY (`frr_reviewId`)
    REFERENCES `bannarug`.`Review` (`reviewId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Review_has_User_User1`
    FOREIGN KEY (`fru_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Follower`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Follower` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Follower` (
  `followerId` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `followId` INT NOT NULL,
  `followerCreateDateTime` DATETIME NULL default current_timestamp,
  `followerUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  INDEX `fk_Follower_User1_idx` (`userId` ASC) VISIBLE,
  INDEX `fk_Follower_User2_idx` (`followId` ASC) VISIBLE,
  PRIMARY KEY (`followerId`),
  CONSTRAINT `fk_Follower_User1`
    FOREIGN KEY (`userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Follower_User2`
    FOREIGN KEY (`followId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE NO ACTION
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
-- Table `bannarug`.`Notification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Notification` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Notification` (
  `notificationId` INT NOT NULL AUTO_INCREMENT,
  `nu_userId` INT NOT NULL,
  `notificationTitle` VARCHAR(255) NOT NULL,
  `notificationDetail` LONGTEXT NOT NULL,
  `notificationCreateDateTime` DATETIME NULL default current_timestamp,
  `notificationUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`notificationId`),
  INDEX `fk_Notification_User1_idx` (`nu_userId` ASC) VISIBLE,
  CONSTRAINT `fk_Notification_User1`
    FOREIGN KEY (`nu_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bannarug`.`Recomment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bannarug`.`Recomment` ;

CREATE TABLE IF NOT EXISTS `bannarug`.`Recomment` (
  `recommentId` INT NOT NULL AUTO_INCREMENT,
  `rb_booktypeId` INT NOT NULL,
  `ru_userId` INT NOT NULL,
  `viewCount` INT NULL,
  `recommentCreateDateTime` DATETIME NULL default current_timestamp,
  `recommentUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
  PRIMARY KEY (`recommentId`, `rb_booktypeId`, `ru_userId`),
  INDEX `fk_Booktype_has_User_User1_idx` (`ru_userId` ASC) VISIBLE,
  INDEX `fk_Booktype_has_User_Booktype1_idx` (`rb_booktypeId` ASC) VISIBLE,
  CONSTRAINT `fk_Booktype_has_User_Booktype1`
    FOREIGN KEY (`rb_booktypeId`)
    REFERENCES `bannarug`.`Booktype` (`booktypeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Booktype_has_User_User1`
    FOREIGN KEY (`ru_userId`)
    REFERENCES `bannarug`.`User` (`userId`)
    ON DELETE NO ACTION
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
  `reviewUpdateDateTime` DATETIME NULL default current_timestamp on update current_timestamp,
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
  `follows` INT NOT NULL,
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
    IN bookTypeId INT,
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

USE `bannarug`
INSERT INTO Booktype (bookTypeName) VALUES ('Adventure');
INSERT INTO Booktype (bookTypeName) VALUES ('Classic');
INSERT INTO Booktype (bookTypeName) VALUES ('Crime');
INSERT INTO Booktype (bookTypeName) VALUES ('Fairy tale');
INSERT INTO Booktype (bookTypeName) VALUES ('Fantasy');
INSERT INTO Booktype (bookTypeName) VALUES ('History');
INSERT INTO Booktype (bookTypeName) VALUES ('Horror');
INSERT INTO Booktype (bookTypeName) VALUES ('Textbooks');
INSERT INTO Booktype (bookTypeName) VALUES ('Literature');
INSERT INTO Booktype (bookTypeName) VALUES ('Mystery');
INSERT INTO Booktype (bookTypeName) VALUES ('Poetry');
INSERT INTO Booktype (bookTypeName) VALUES ('Plays');
INSERT INTO Booktype (bookTypeName) VALUES ('Romance');
INSERT INTO Booktype (bookTypeName) VALUES ('Science');
INSERT INTO Booktype (bookTypeName) VALUES ('Journalism');
INSERT INTO Booktype (bookTypeName) VALUES ('Thriller');
INSERT INTO Booktype (bookTypeName) VALUES ('War');
INSERT INTO Booktype (bookTypeName) VALUES ('Philosophy');
INSERT INTO Booktype (bookTypeName) VALUES ('Health and Wellness');
INSERT INTO Booktype (bookTypeName) VALUES ('Education');
INSERT INTO Booktype (bookTypeName) VALUES ('Cookbook');
INSERT INTO Booktype (bookTypeName) VALUES ('Arts');
INSERT INTO Booktype (bookTypeName) VALUES ('Religion');
INSERT INTO Booktype (bookTypeName) VALUES ('Business and Economics');
INSERT INTO Booktype (bookTypeName) VALUES ('Hobby');


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
