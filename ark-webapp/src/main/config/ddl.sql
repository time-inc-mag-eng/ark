/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary table structure for view `VBus_Sys`
--

DROP TABLE IF EXISTS `VBus_Sys`;
/*!50001 DROP VIEW IF EXISTS `VBus_Sys`*/;
/*!50001 CREATE TABLE `VBus_Sys` (
  `PUBLICATION` varchar(50),
  `APPLICATION_DEVICE` varchar(50),
  `ISSUE` varchar(255),
  `COVER_DATE` datetime,
  `SALE_DATE` datetime,
  `PRODUCTID` varchar(100)
) ENGINE=MyISAM */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Sort_Order` int(11) DEFAULT NULL,
  `Dossier_Id` varchar(255) DEFAULT NULL,
  `Article_Id` varchar(255) DEFAULT NULL,
  `Folio_Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKD458CCF65B8FDE60` (`Folio_Id`),
  CONSTRAINT `FKD458CCF65B8FDE60` FOREIGN KEY (`Folio_Id`) REFERENCES `folio` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_application`
--

DROP TABLE IF EXISTS `base_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_application` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Publication_Vendor_Name` varchar(255) NOT NULL,
  `Product_Id` varchar(255) NOT NULL,
  `Publication_GroupId` tinyint(3) unsigned NOT NULL,
  `Active` bit(1) NOT NULL DEFAULT b'1',
  `Default_Price` decimal(19,2) DEFAULT '0.00',
  `Send_Msg` bit(1) NOT NULL DEFAULT b'1',
  `Content_Email_Id` int(11) DEFAULT NULL,
  `Preview_Email_Id` int(11) DEFAULT NULL,
  `Content_First` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_content_email` (`Content_Email_Id`),
  UNIQUE KEY `ux_preview_email` (`Preview_Email_Id`),
  KEY `fk_Application_publication_group` (`Publication_GroupId`),
  KEY `Content_Email_FK` (`Content_Email_Id`),
  KEY `Preview_Email_FK` (`Preview_Email_Id`),
  CONSTRAINT `Content_Email_FK` FOREIGN KEY (`Content_Email_Id`) REFERENCES `email` (`Id`),
  CONSTRAINT `fk_Application_publication_group` FOREIGN KEY (`Publication_GroupId`) REFERENCES `publication_group` (`Id`),
  CONSTRAINT `Preview_Email_FK` FOREIGN KEY (`Preview_Email_Id`) REFERENCES `email` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cds_application`
--

DROP TABLE IF EXISTS `cds_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cds_application` (
  `Base_Application_Id` smallint(5) NOT NULL,
  `Min_Version` varchar(255) NOT NULL,
  `Publish_Key` varchar(255) NOT NULL,
  `Issue_Key` varchar(255) NOT NULL,
  `Product_Key` varchar(255) NOT NULL,
  `Subscription` bit(1) DEFAULT NULL,
  `Preview_Setting_Id` int(11) NOT NULL,
  `Content_Setting_Id` int(11) NOT NULL,
  `Origin_Content_Id` int(11) NOT NULL,
  `Echo_Id` int(11) DEFAULT NULL,
  `Ns_Feed_Id` int(11) DEFAULT NULL,
  `Origin_Preview_Id` int(11) NOT NULL,
  PRIMARY KEY (`Base_Application_Id`),
  UNIQUE KEY `ux_preview_setting` (`Preview_Setting_Id`),
  UNIQUE KEY `ux_content_setting` (`Content_Setting_Id`),
  UNIQUE KEY `ux_ns_feed_id` (`Ns_Feed_Id`),
  KEY `FK56E928C33759D315` (`Base_Application_Id`),
  KEY `FK56E928C31C251A01` (`Content_Setting_Id`),
  KEY `FK56E928C3A73DD774` (`Origin_Content_Id`),
  KEY `FK56E928C36C0B7AC1` (`Ns_Feed_Id`),
  KEY `FK56E928C3629EF894` (`Echo_Id`),
  KEY `FK56E928C3F3317C6E` (`Preview_Setting_Id`),
  KEY `FKB32EC0E9C776C7655` (`Origin_Preview_Id`),
  CONSTRAINT `FK56E928C31C251A01` FOREIGN KEY (`Content_Setting_Id`) REFERENCES `content_setting` (`Id`),
  CONSTRAINT `FK56E928C33759D315` FOREIGN KEY (`Base_Application_Id`) REFERENCES `base_application` (`id`),
  CONSTRAINT `FK56E928C3629EF894` FOREIGN KEY (`Echo_Id`) REFERENCES `echo_setting` (`Id`),
  CONSTRAINT `FK56E928C36C0B7AC1` FOREIGN KEY (`Ns_Feed_Id`) REFERENCES `ns_feed_setting` (`Id`),
  CONSTRAINT `FK56E928C3A73DD774` FOREIGN KEY (`Origin_Content_Id`) REFERENCES `origin_content` (`Id`),
  CONSTRAINT `FK56E928C3F3317C6E` FOREIGN KEY (`Preview_Setting_Id`) REFERENCES `preview_setting` (`Id`),
  CONSTRAINT `FKB32EC0E9C776C7655` FOREIGN KEY (`Origin_Preview_Id`) REFERENCES `origin_preview` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_parser`
--

DROP TABLE IF EXISTS `content_parser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_parser` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_setting`
--

DROP TABLE IF EXISTS `content_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_setting` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Package_Setting_Id` int(11) DEFAULT NULL,
  `Content_Parser_Id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ux_package_setting` (`Package_Setting_Id`),
  KEY `FK8F8A608A56235191` (`Package_Setting_Id`),
  KEY `FK8F8A608A9DC6D27D` (`Content_Parser_Id`),
  CONSTRAINT `FK8F8A608A56235191` FOREIGN KEY (`Package_Setting_Id`) REFERENCES `package_setting` (`Id`),
  CONSTRAINT `FK8F8A608A9DC6D27D` FOREIGN KEY (`Content_Parser_Id`) REFERENCES `content_parser` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dps_application`
--

DROP TABLE IF EXISTS `dps_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dps_application` (
  `Base_Application_Id` smallint(5) NOT NULL,
  `Rendition` varchar(255) NOT NULL DEFAULT 'ANY',
  `Preview_Setting_Id` int(11) NOT NULL,
  `Echo_Id` int(11) DEFAULT NULL,
  `Ns_Feed_Id` int(11) DEFAULT NULL,
  `Dps_Server_Id` int(11) NOT NULL,
  `Auto_Publish` bit(1) DEFAULT b'1',
  `Origin_Preview_Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Base_Application_Id`),
  UNIQUE KEY `ux_dps_server` (`Dps_Server_Id`),
  UNIQUE KEY `ux_preview_setting` (`Preview_Setting_Id`),
  UNIQUE KEY `ux_ns_feed_id` (`Ns_Feed_Id`),
  KEY `FK472A06783759D315` (`Base_Application_Id`),
  KEY `FK472A0678C77E69BC` (`Dps_Server_Id`),
  KEY `FK472A06786C0B7AC1` (`Ns_Feed_Id`),
  KEY `FK53B227FCA2245C82` (`Echo_Id`),
  KEY `FK472A0678838A214B` (`Preview_Setting_Id`),
  KEY `FKB32EC0E9C776C7658` (`Origin_Preview_Id`),
  CONSTRAINT `FK472A06783759D315` FOREIGN KEY (`Base_Application_Id`) REFERENCES `base_application` (`id`),
  CONSTRAINT `FK472A06786C0B7AC1` FOREIGN KEY (`Ns_Feed_Id`) REFERENCES `ns_feed_setting` (`Id`),
  CONSTRAINT `FK472A0678838A214B` FOREIGN KEY (`Preview_Setting_Id`) REFERENCES `preview_setting` (`Id`),
  CONSTRAINT `FK472A0678C77E69BC` FOREIGN KEY (`Dps_Server_Id`) REFERENCES `dps_server` (`Id`),
  CONSTRAINT `FK53B227FCA2245C82` FOREIGN KEY (`Echo_Id`) REFERENCES `echo_setting` (`Id`),
  CONSTRAINT `FKB32EC0E9C776C7658` FOREIGN KEY (`Origin_Preview_Id`) REFERENCES `origin_preview` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dps_server`
--

DROP TABLE IF EXISTS `dps_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dps_server` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Retry_Count` int(11) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `User_Name` varchar(255) DEFAULT NULL,
  `User_Password` varchar(255) DEFAULT NULL,
  `Consumer_Secret` varchar(255) DEFAULT NULL,
  `Consumer_Key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `echo_setting`
--

DROP TABLE IF EXISTS `echo_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `echo_setting` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Token` varchar(255) DEFAULT NULL,
  `Product_Id` varchar(255) DEFAULT NULL,
  `Delay_Minute` int(11) NOT NULL DEFAULT '0',
  `Surrogate_Key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email`
--

DROP TABLE IF EXISTS `email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `receipients` longtext NOT NULL,
  `Subject_Template` longtext NOT NULL,
  `Body_Template` longtext NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_log`
--

DROP TABLE IF EXISTS `event_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Event` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Occurrence` datetime NOT NULL,
  `Issue_Name` varchar(255) NOT NULL,
  `Short_Date` datetime NOT NULL,
  `Publication_GroupId` tinyint(3) unsigned NOT NULL,
  `App_Name` varchar(255) DEFAULT NULL,
  `Reference_Id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK5C6729A96121B74` (`Publication_GroupId`),
  CONSTRAINT `FK5C6729A96121B74` FOREIGN KEY (`Publication_GroupId`) REFERENCES `publication_group` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_naming`
--

DROP TABLE IF EXISTS `file_naming`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_naming` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Template` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `folio`
--

DROP TABLE IF EXISTS `folio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folio` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Dps_Folio_Id` varchar(255) DEFAULT NULL,
  `Orientation` varchar(255) NOT NULL,
  `Width` int(11) NOT NULL,
  `Height` int(11) NOT NULL,
  `Html_Resource` bit(1) NOT NULL DEFAULT b'0',
  `Issue_Meta_Id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Issue_Meta_Id` (`Issue_Meta_Id`),
  KEY `Folio_Issue_Meta_FK` (`Issue_Meta_Id`),
  CONSTRAINT `Folio_Issue_Meta_FK` FOREIGN KEY (`Issue_Meta_Id`) REFERENCES `issue_meta` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Publication_GroupId` tinyint(3) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `short_date` datetime DEFAULT NULL,
  `Test_Issue` bit(1) DEFAULT b'0',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Publication_GroupId` (`Publication_GroupId`,`name`,`short_date`),
  CONSTRAINT `fk_PublicationGroup_issues` FOREIGN KEY (`Publication_GroupId`) REFERENCES `publication_group` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `issue_meta`
--

DROP TABLE IF EXISTS `issue_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue_meta` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `onsale_date` datetime DEFAULT NULL,
  `ApplicationId` smallint(5) DEFAULT NULL,
  `Volume` varchar(100) NOT NULL DEFAULT '',
  `ReferenceId` varchar(100) NOT NULL DEFAULT '' COMMENT 'This is the Id reference from other system, eg. Apple InApp item Id',
  `Active` bit(1) DEFAULT b'1',
  `Size` float unsigned zerofill DEFAULT '000000000000',
  `Min_Version` varchar(10) NOT NULL DEFAULT '0.1' COMMENT 'Minimum version of the Application required to run this issue',
  `Published_Date` datetime DEFAULT NULL,
  `PaymentId` tinyint(3) unsigned NOT NULL,
  `IssueId` int(10) unsigned NOT NULL,
  `PreviewUploaded` bit(1) DEFAULT b'0',
  `ContentUploaded` bit(1) DEFAULT b'0',
  `Last_Updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `Price` decimal(19,2) DEFAULT '0.00',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ReferenceId` (`ReferenceId`,`ApplicationId`),
  KEY `fk_Issue_Application` (`ApplicationId`),
  KEY `PaymentId` (`PaymentId`),
  KEY `IssueId` (`IssueId`),
  KEY `Issue_Meta_Application_FK` (`ApplicationId`),
  CONSTRAINT `Issue_Meta_Application_FK` FOREIGN KEY (`ApplicationId`) REFERENCES `base_application` (`id`),
  CONSTRAINT `issue_meta_ibfk_1` FOREIGN KEY (`PaymentId`) REFERENCES `payment_type` (`Id`),
  CONSTRAINT `issue_meta_ibfk_2` FOREIGN KEY (`IssueId`) REFERENCES `issue` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_location`
--

DROP TABLE IF EXISTS `media_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_location` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Filename` varchar(255) NOT NULL,
  `Relative_Path` varchar(255) NOT NULL,
  `Issue_Id` int(10) unsigned DEFAULT NULL,
  `Modified` timestamp NULL DEFAULT NULL,
  `Cdn_Url` varchar(255) NOT NULL,
  `Preview_Url` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Filename` (`Filename`,`Issue_Id`),
  KEY `FKC798CFE9738F0060` (`Issue_Id`),
  CONSTRAINT `media_location_ibfk_1` FOREIGN KEY (`Issue_Id`) REFERENCES `issue` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_type`
--

DROP TABLE IF EXISTS `media_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_type` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ns_feed_setting`
--

DROP TABLE IF EXISTS `ns_feed_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ns_feed_setting` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Relative_Path` varchar(255) DEFAULT NULL,
  `File_Name` varchar(255) DEFAULT NULL,
  `Origin_Preview_Id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKB32EC0E9C776C7651` (`Origin_Preview_Id`),
  CONSTRAINT `FKB32EC0E9C776C7651` FOREIGN KEY (`Origin_Preview_Id`) REFERENCES `origin_preview` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_content`
--

DROP TABLE IF EXISTS `origin_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_content` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Base_URL_Protected` varchar(250) NOT NULL,
  `Protected_Dir` varchar(250) NOT NULL,
  `Origin_Path` varchar(255) DEFAULT NULL,
  `Origin_Server_Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK37DCA52334ACEDA21` (`Origin_Server_Id`),
  CONSTRAINT `FK37DCA52334ACEDA21` FOREIGN KEY (`Origin_Server_Id`) REFERENCES `origin_server` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_media`
--

DROP TABLE IF EXISTS `origin_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_media` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Origin_Preview_Id` int(11) DEFAULT NULL,
  `Publication_Group_Id` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Publication_Group_Id` (`Publication_Group_Id`),
  KEY `FK7C1B0D8BB5B6163B` (`Publication_Group_Id`),
  KEY `FK7C1B0D8BC66B2439` (`Origin_Preview_Id`),
  CONSTRAINT `FK7C1B0D8BB5B6163B` FOREIGN KEY (`Publication_Group_Id`) REFERENCES `publication_group` (`Id`),
  CONSTRAINT `FK7C1B0D8BC66B2439` FOREIGN KEY (`Origin_Preview_Id`) REFERENCES `origin_preview` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_media_type`
--

DROP TABLE IF EXISTS `origin_media_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_media_type` (
  `Origin_Media_Id` int(11) NOT NULL,
  `Media_Type_Id` int(11) NOT NULL,
  PRIMARY KEY (`Origin_Media_Id`,`Media_Type_Id`),
  KEY `FKFE67F96E27388A69` (`Media_Type_Id`),
  KEY `FKFE67F96E3F0FD041` (`Origin_Media_Id`),
  CONSTRAINT `FKFE67F96E27388A69` FOREIGN KEY (`Media_Type_Id`) REFERENCES `media_type` (`Id`),
  CONSTRAINT `FKFE67F96E3F0FD041` FOREIGN KEY (`Origin_Media_Id`) REFERENCES `origin_media` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_preview`
--

DROP TABLE IF EXISTS `origin_preview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_preview` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Origin_Base_Url` varchar(250) NOT NULL,
  `Cdn_Base_Url` varchar(250) NOT NULL,
  `Origin_Path` varchar(255) DEFAULT NULL,
  `Origin_Server_Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKFE62CEE234ACEDA1` (`Origin_Server_Id`),
  CONSTRAINT `FKFE62CEE234ACEDA1` FOREIGN KEY (`Origin_Server_Id`) REFERENCES `origin_server` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_server`
--

DROP TABLE IF EXISTS `origin_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_server` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Origin_Server` varchar(255) DEFAULT NULL,
  `Origin_Port` int(11) DEFAULT NULL,
  `Origin_Username` varchar(255) DEFAULT NULL,
  `Origin_Password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_setting`
--

DROP TABLE IF EXISTS `package_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `package_setting` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `Parent_Packer_Id` int(11) DEFAULT NULL,
  `File_Naming_Id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ux_file_naming` (`File_Naming_Id`),
  UNIQUE KEY `ux_parent_packer` (`Parent_Packer_Id`),
  KEY `FK53B227FCB2245C82` (`Parent_Packer_Id`),
  CONSTRAINT `FK53B227FCB2245C82` FOREIGN KEY (`Parent_Packer_Id`) REFERENCES `package_setting` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `path_naming`
--

DROP TABLE IF EXISTS `path_naming`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `path_naming` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Template` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_type`
--

DROP TABLE IF EXISTS `payment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_type` (
  `Id` tinyint(3) unsigned NOT NULL,
  `Name` varchar(25) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `preview_image`
--

DROP TABLE IF EXISTS `preview_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preview_image` (
  `Preview_Setting_Id` int(11) NOT NULL,
  `Regex` varchar(255) DEFAULT NULL,
  `Error_Msg` varchar(255) DEFAULT NULL,
  `Image_Key` varchar(255) NOT NULL,
  PRIMARY KEY (`Preview_Setting_Id`,`Image_Key`),
  KEY `FK2987FA24A352F020` (`Preview_Setting_Id`),
  CONSTRAINT `FK2987FA24A352F020` FOREIGN KEY (`Preview_Setting_Id`) REFERENCES `preview_setting` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `preview_setting`
--

DROP TABLE IF EXISTS `preview_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preview_setting` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `package_setting_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ux_package_setting` (`package_setting_id`),
  KEY `FKEBDB50D99EA4F166` (`package_setting_id`),
  CONSTRAINT `FKEBDB50D99EA4F166` FOREIGN KEY (`package_setting_id`) REFERENCES `package_setting` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `publication_group`
--

DROP TABLE IF EXISTS `publication_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publication_group` (
  `Id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `AD_Group` varchar(255) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Report_Retention` int(11) DEFAULT '30',
  `Issue_Retention` int(11) unsigned NOT NULL,
  `Active` bit(1) NOT NULL DEFAULT b'1',
  `Media_Issue_Retention` int(10) NOT NULL DEFAULT '3',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_event`
--

DROP TABLE IF EXISTS `push_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `push_event` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Reference_Id` varchar(255) NOT NULL,
  `Push_Product_Id` varchar(255) NOT NULL,
  `Push_Schedule_Date` datetime NOT NULL,
  `Push_Surrogate_Id` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scp_application`
--

DROP TABLE IF EXISTS `scp_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scp_application` (
  `Base_Application_Id` smallint(5) NOT NULL,
  `Content_Setting_Id` int(11) NOT NULL,
  `Scp_Server_Id` int(11) NOT NULL,
  `Preview_Setting_Id` int(11) NOT NULL,
  PRIMARY KEY (`Base_Application_Id`),
  UNIQUE KEY `ux_preview_setting` (`Preview_Setting_Id`),
  UNIQUE KEY `ux_content_setting` (`Content_Setting_Id`),
  UNIQUE KEY `ux_scp_server` (`Scp_Server_Id`),
  KEY `FK4738EFB13759D315` (`Base_Application_Id`),
  KEY `FK4738EFB16958D52E` (`Scp_Server_Id`),
  KEY `FK4738EFB11C251A01` (`Content_Setting_Id`),
  KEY `FK4738EFB1F3317C6E` (`Preview_Setting_Id`),
  CONSTRAINT `FK4738EFB11C251A01` FOREIGN KEY (`Content_Setting_Id`) REFERENCES `content_setting` (`Id`),
  CONSTRAINT `FK4738EFB13759D315` FOREIGN KEY (`Base_Application_Id`) REFERENCES `base_application` (`id`),
  CONSTRAINT `FK4738EFB16958D52E` FOREIGN KEY (`Scp_Server_Id`) REFERENCES `scp_server` (`Id`),
  CONSTRAINT `FK4738EFB1F3317C6E` FOREIGN KEY (`Preview_Setting_Id`) REFERENCES `preview_setting` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scp_server`
--

DROP TABLE IF EXISTS `scp_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scp_server` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(255) NOT NULL,
  `Host` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Port` int(11) NOT NULL,
  `Path_Naming_Id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ux_path_naming` (`Path_Naming_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `VBus_Sys`
--

/*!50001 DROP TABLE `VBus_Sys`*/;
/*!50001 DROP VIEW IF EXISTS `VBus_Sys`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`ark_ro`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `VBus_Sys` AS select `p`.`Name` AS `PUBLICATION`,`a`.`Name` AS `APPLICATION_DEVICE`,`i`.`name` AS `ISSUE`,`i`.`short_date` AS `COVER_DATE`,`im`.`onsale_date` AS `SALE_DATE`,`im`.`ReferenceId` AS `PRODUCTID` from (((`issue_meta` `im` join `issue` `i` on((`im`.`IssueId` = `i`.`Id`))) join `base_application` `a` on((`im`.`ApplicationId` = `a`.`id`))) join `publication_group` `p` on((`a`.`Publication_GroupId` = `p`.`Id`))) */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

