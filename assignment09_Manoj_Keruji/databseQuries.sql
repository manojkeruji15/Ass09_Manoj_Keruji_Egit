CREATE TABLE `moviedetails` 
( `idMovieDetails` int NOT NULL,
 `MovieName` varchar(45) NOT NULL, 
 `MovieType` varchar(45) NOT NULL,
 `DateR` date NOT NULL,
 `MovieLang` varchar(45) NOT NULL,
 `rating` bigint NOT NULL,
 `totalBussiness` bigint NOT NULL,
 PRIMARY KEY (`idMovieDetails`));
 
 CREATE TABLE `castm` 
 ( `idMovieD` int NOT NULL,
 `cast` varchar(45) NOT NULL,
 KEY `idMovieD_idx` (`idMovieD`),
 CONSTRAINT `idMovieD` FOREIGN KEY (`idMovieD`) REFERENCES `moviedetails` (`idMovieDetails`));
