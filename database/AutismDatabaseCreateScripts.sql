SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `obe` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `obe` ;

-- -----------------------------------------------------
-- Table `obe`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`User` (
  `idUser` INT NOT NULL COMMENT 'PK for user record to attach to Person record.',
  `loginName` VARCHAR(16) NULL COMMENT 'Customer\'s own created ID to login',
  `password` VARCHAR(16) NULL,
  `type` VARCHAR(1) NULL COMMENT 'Type could be client/patient(C) or therapist(T)',
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Patient` (
  `idPatient` INT NOT NULL,
  `idUser` INT NOT NULL,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  PRIMARY KEY (`idPatient`),
  INDEX `idUserPatient` (`idUser` ASC),
  CONSTRAINT `idUserPatient`
    FOREIGN KEY (`idUser`)
    REFERENCES `obe`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Program` (
  `idProgram` INT NOT NULL,
  `idPerson` INT NULL,
  `name` VARCHAR(45) NULL,
  `dateBegin` DATETIME NULL,
  `dateCompleted` DATETIME NULL,
  PRIMARY KEY (`idProgram`),
  INDEX `idPerson_idx` (`idPerson` ASC),
  CONSTRAINT `idPersonProgram`
    FOREIGN KEY (`idPerson`)
    REFERENCES `obe`.`Patient` (`idPatient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Therapist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Therapist` (
  `idTherapist` INT NOT NULL,
  `idUser` INT NOT NULL,
  `firstName` VARCHAR(45) NULL,
  `LastName` VARCHAR(45) NULL,
  PRIMARY KEY (`idTherapist`),
  INDEX `idUser_idx` (`idUser` ASC),
  CONSTRAINT `idUserTherapist`
    FOREIGN KEY (`idUser`)
    REFERENCES `obe`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Series`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Series` (
  `idSeries` INT NOT NULL,
  `idProgram` INT NULL,
  `date` DATETIME NULL,
  `idTherapist` INT NULL,
  `idPatient` INT NULL,
  `type` VARCHAR(4) NULL COMMENT '1. Intro (INT)\n2. Mass Trial (MT)\n3. Mass Trial - RR (MTRR) \n4. Random Rotate (RR)' /* comment truncated */ /*5. Check for Mastery (CFM)*/,
  PRIMARY KEY (`idSeries`),
  INDEX `idProgram_idx` (`idProgram` ASC),
  INDEX `idTherapistRunBy_idx` (`idTherapist` ASC),
  INDEX `idPatientSeries_idx` (`idPatient` ASC),
  CONSTRAINT `idTherapistSeries`
    FOREIGN KEY (`idTherapist`)
    REFERENCES `obe`.`Therapist` (`idTherapist`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idProgramSeries`
    FOREIGN KEY (`idProgram`)
    REFERENCES `obe`.`Program` (`idProgram`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idPatientSeries`
    FOREIGN KEY (`idPatient`)
    REFERENCES `obe`.`Patient` (`idPatient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Trial`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Trial` (
  `idTrial` INT NOT NULL,
  `idSeries` INT NULL,
  `name` VARCHAR(45) NULL,
  `passed` TINYINT(1) NOT NULL,
  `promptType` VARCHAR(3) NULL COMMENT 'If not null that means a prompt was use.  To determine the prompt type read the values as follows:\n1. Hand over hand (HOH)\n2. Model (MDL)\n3. Verbal (VBL)' /* comment truncated */ /*4. Visual (VSL)*/,
  PRIMARY KEY (`idTrial`),
  INDEX `idProgram_idx` (`idSeries` ASC),
  CONSTRAINT `idSeriesTrial`
    FOREIGN KEY (`idSeries`)
    REFERENCES `obe`.`Series` (`idSeries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Stimulus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Stimulus` (
  `idStimulus` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `idTrial` INT NULL,
  `type` VARCHAR(10) NULL COMMENT 'Type can be Verbal or Visual.',
  PRIMARY KEY (`idStimulus`),
  INDEX `idTrial_idx` (`idTrial` ASC),
  CONSTRAINT `idTrialStimulus`
    FOREIGN KEY (`idTrial`)
    REFERENCES `obe`.`Trial` (`idTrial`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Response`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Response` (
  `idResponse` INT NOT NULL,
  `name` VARCHAR(45) NULL COMMENT 'How the person responded to the stimulus.  Determines if the trial passed or failed.',
  `idTrial` INT NULL,
  `idStimulus` INT NULL,
  `successful` TINYINT(1) NULL COMMENT 'result says if the response was successful or not',
  PRIMARY KEY (`idResponse`),
  INDEX `idTrial_idx` (`idTrial` ASC),
  INDEX `idStimulus_idx` (`idStimulus` ASC),
  CONSTRAINT `idTrialResponse`
    FOREIGN KEY (`idTrial`)
    REFERENCES `obe`.`Trial` (`idTrial`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idStimulusResponse`
    FOREIGN KEY (`idStimulus`)
    REFERENCES `obe`.`Stimulus` (`idStimulus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Reinforcement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Reinforcement` (
  `idReinforcement` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `idTrial` INT NULL,
  `idResponse` INT NULL,
  PRIMARY KEY (`idReinforcement`),
  INDEX `idTrial_idx` (`idTrial` ASC),
  INDEX `idResponse_idx` (`idResponse` ASC),
  CONSTRAINT `idTrialReinforcement`
    FOREIGN KEY (`idTrial`)
    REFERENCES `obe`.`Trial` (`idTrial`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idResponseReinforcement`
    FOREIGN KEY (`idResponse`)
    REFERENCES `obe`.`Response` (`idResponse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `obe`.`Therapist_Patients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `obe`.`Therapist_Patients` (
  `idTherapistPatients` INT NOT NULL,
  `idTherapist` INT NOT NULL COMMENT 'Ties a Therapist that can have many patients.',
  `idPatient` INT NOT NULL COMMENT 'Ties the therapist to a patient.',
  PRIMARY KEY (`idTherapistPatients`),
  INDEX `idTherapist_idx` (`idTherapist` ASC),
  INDEX `idPatient_idx` (`idPatient` ASC),
  UNIQUE INDEX `therapistPatient_idx` (`idTherapist` ASC, `idPatient` ASC),
  CONSTRAINT `idTherapistTP`
    FOREIGN KEY (`idTherapist`)
    REFERENCES `obe`.`Therapist` (`idTherapist`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idPatientTP`
    FOREIGN KEY (`idPatient`)
    REFERENCES `obe`.`Patient` (`idPatient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
