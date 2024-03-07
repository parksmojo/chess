package dataAccess;

public class DatabaseSetUp {
    public DatabaseSetUp() {
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                var createUserTable = """
                        CREATE TABLE `user_data` (
                           `iduser_data` int NOT NULL AUTO_INCREMENT,
                           `username` varchar(45) DEFAULT NULL,
                           `password` varchar(45) DEFAULT NULL,
                           `email` varchar(45) DEFAULT NULL,
                           PRIMARY KEY (`iduser_data`)
                         ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                         """;
                try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                    createTableStatement.executeUpdate();
                }
                var createAuthTable = """
                        CREATE TABLE `auth_data` (
                          `idauth_data` int NOT NULL AUTO_INCREMENT,
                          `authToken` varchar(45) DEFAULT NULL,
                          `username` varchar(45) DEFAULT NULL,
                          PRIMARY KEY (`idauth_data`)
                        ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                        """;
                try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                    createTableStatement.executeUpdate();
                }
                var createGameTable = """
                        CREATE TABLE `game_data` (
                           `game_ID` int NOT NULL AUTO_INCREMENT,
                           `white_username` varchar(45) DEFAULT NULL,
                           `black_username` varchar(45) DEFAULT NULL,
                           `game_name` varchar(45) NOT NULL,
                           `game` json DEFAULT NULL,
                           PRIMARY KEY (`game_ID`),
                           UNIQUE KEY `game_name_UNIQUE` (`game_name`)
                         ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                         """;
                try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                    createTableStatement.executeUpdate();
                }
            }
        } catch (Exception e){
            System.out.println("Could not set up database");
        }
    }
}
