-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: sri
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` text,
  `word` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=265 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (54,'destination','beach'),(55,'destination','island'),(56,'destination','mountain'),(57,'destination','cityvillag'),(58,'destination','forest'),(59,'destination','desert'),(60,'destination','jungl'),(61,'destination','lakeriv'),(62,'destination','harbor'),(63,'destination','coastlin'),(64,'destination','canyon'),(65,'destination','oasi'),(66,'destination','archipelago'),(67,'destination','delta'),(68,'destination','wetland'),(69,'destination','prairi'),(70,'destination','savannah'),(71,'destination','lagoon'),(72,'destination','reef'),(73,'destination','grotto'),(74,'destination','peninsula'),(75,'destination','highland'),(76,'destination','lowland'),(77,'destination','cliffsid'),(78,'destination','crater'),(79,'destination','volcano'),(80,'destination','glacier'),(81,'destination','waterfal'),(82,'destination','vallei'),(83,'destination','nation'),(84,'destination','park'),(85,'destination','wildlif'),(86,'destination','reserv'),(87,'destination','mangrov'),(88,'destination','coral'),(89,'destination','hidden'),(90,'destination','cove'),(91,'destination','stepp'),(92,'destination','rainforest'),(93,'destination','iceberg'),(94,'destination','fjord'),(95,'destination','sand'),(96,'destination','dune'),(97,'destination','meadow'),(98,'destination','plateau'),(99,'destination','ravin'),(100,'destination','cliff'),(101,'destination','bamboo'),(102,'destination','atol'),(103,'destination','snowcap'),(104,'destination','peak'),(105,'destination','roll'),(106,'destination','hill'),(107,'essentials','passport'),(108,'essentials','visa'),(109,'essentials','id'),(110,'essentials','ticket'),(111,'essentials','itinerari'),(112,'essentials','map'),(113,'essentials','guidebook'),(114,'essentials','travel'),(115,'essentials','insur'),(116,'essentials','backpack'),(117,'essentials','suitcas'),(118,'essentials','board'),(119,'essentials','pass'),(120,'essentials','wallet'),(121,'essentials','compass'),(122,'essentials','camera'),(123,'essentials','adapt'),(124,'essentials','power'),(125,'essentials','bank'),(126,'essentials','first'),(127,'essentials','aid'),(128,'essentials','kit'),(129,'essentials','pillow'),(130,'essentials','sleep'),(131,'essentials','bag'),(132,'essentials','hike'),(133,'essentials','boot'),(134,'essentials','sunscreen'),(135,'essentials','sunglass'),(136,'essentials','toiletri'),(137,'essentials','flashlight'),(138,'essentials','portabl'),(139,'essentials','charger'),(140,'essentials','reusabl'),(141,'essentials','water'),(142,'essentials','bottl'),(143,'essentials','snack'),(144,'essentials','pack'),(145,'essentials','list'),(146,'essentials','blanket'),(147,'essentials','headphon'),(148,'essentials','neck'),(149,'essentials','luggag'),(150,'essentials','tag'),(151,'essentials','hand'),(152,'essentials','sanit'),(153,'essentials','emerg'),(154,'essentials','contact'),(155,'essentials','multi'),(156,'essentials','tool'),(157,'essentials','knife'),(158,'essentials','foldabl'),(159,'essentials','waterproof'),(160,'essentials','cover'),(161,'essentials','lock'),(162,'essentials','currenc'),(163,'essentials','convert'),(164,'essentials','translat'),(165,'essentials','app'),(166,'accomodation','hotel'),(167,'accomodation','resort'),(168,'accomodation','motel'),(169,'accomodation','hostel'),(170,'accomodation','lodg'),(171,'accomodation','villa'),(172,'accomodation','cabin'),(173,'accomodation','chalet'),(174,'accomodation','treehous'),(175,'accomodation','campsit'),(176,'accomodation','yurt'),(177,'accomodation','tent'),(178,'accomodation','bungalow'),(179,'accomodation','penthous'),(180,'accomodation','capsul'),(181,'accomodation','guesthous'),(182,'accomodation','inn'),(183,'accomodation','manor'),(184,'accomodation','cottag'),(185,'accomodation','castl'),(186,'accomodation','stai'),(187,'accomodation','caravan'),(188,'accomodation','igloo'),(189,'accomodation','overwat'),(190,'accomodation','eco'),(191,'accomodation','homestai'),(192,'accomodation','bed'),(193,'accomodation','breakfast'),(194,'accomodation','farmhous'),(195,'accomodation','board'),(196,'accomodation','hous'),(197,'accomodation','tini'),(198,'accomodation','heritag'),(199,'activity','sightse'),(200,'activity','hike'),(201,'activity','trek'),(202,'activity','camp'),(203,'activity','dive'),(204,'activity','surf'),(205,'activity','snorkel'),(206,'activity','kayak'),(207,'activity','boat'),(208,'activity','rock'),(209,'activity','climb'),(210,'activity','paraglid'),(211,'activity','bunge'),(212,'activity','jump'),(213,'activity','cave'),(214,'activity','horseback'),(215,'activity','ride'),(216,'activity','wildlif'),(217,'activity','safari'),(218,'activity','bird'),(219,'activity','watch'),(220,'activity','food'),(221,'activity','tour'),(222,'activity','cultur'),(223,'activity','immers'),(224,'activity','river'),(225,'activity','raft'),(226,'activity','stargaz'),(227,'activity','glacier'),(228,'activity','snowboard'),(229,'activity','wakeboard'),(230,'activity','sandboard'),(231,'activity','ic'),(232,'activity','windsurf'),(233,'activity','paddleboard'),(234,'activity','scuba'),(235,'activity','desert'),(236,'activity','photographi'),(237,'activity','whale'),(238,'activity','train'),(239,'activity','journei'),(240,'activity','citi'),(241,'activity','walk'),(242,'activity','ziplin'),(243,'activity','mountain'),(244,'activity','bike'),(245,'activity','skydiv'),(246,'activity','snowsho'),(247,'activity','archaeolog'),(248,'activity','local'),(249,'activity','market'),(250,'activity','wineri'),(251,'activity','visit'),(252,'activity','hot'),(253,'activity','air'),(254,'activity','balloon'),(255,'activity','museum'),(256,'activity','culinari'),(257,'activity','workshop'),(258,'activity','street'),(259,'activity','art'),(260,'activity','ski'),(261,'destination','winter'),(262,'destination','sunni'),(263,'destination','sun'),(264,'destination','snow');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-14 23:27:55
