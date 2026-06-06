package com.othello.model;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Data Access Object pour gérer les requêtes SQL
 * Sauvegarde les résultats de partie et les scores des joueurs
 */
public class DatabaseDAO {

    private static final String DB_URL = "jdbc:sqlite:othello_game.db";
    private static Connection connection;

    /**
     * Initialise la base de données
     */
    public static void initializeDatabase() {
        try {
            // Charge le driver JDBC SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);

            createTables();
            System.out.println("✓ Base de données initialisée");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver SQLite non trouvé. " +
                    "Ajouter sqlite-jdbc-x.x.x.jar au classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données");
            e.printStackTrace();
        }
    }

    /**
     * Crée les tables si elles n'existent pas
     */
    private static void createTables() throws SQLException {
        String playersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                "totalGames INTEGER DEFAULT 0," +
                "wins INTEGER DEFAULT 0," +
                "losses INTEGER DEFAULT 0," +
                "draws INTEGER DEFAULT 0," +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String gamesTable = "CREATE TABLE IF NOT EXISTS games (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player1Id INTEGER NOT NULL," +
                "player2Id INTEGER NOT NULL," +
                "winnerId INTEGER," +
                "player1Score INTEGER," +
                "player2Score INTEGER," +
                "totalMoves INTEGER," +
                "gameDuration INTEGER," +
                "playedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "moveHistory TEXT," +
                "FOREIGN KEY (player1Id) REFERENCES players(id)," +
                "FOREIGN KEY (player2Id) REFERENCES players(id)," +
                "FOREIGN KEY (winnerId) REFERENCES players(id)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(playersTable);
            stmt.execute(gamesTable);
        }
    }

    /**
     * Sauvegarde les infos d'un joueur
     */
    public static int savePlayer(String playerName) {
        String query = "INSERT OR IGNORE INTO players (name) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();

            // Récupère l'ID du joueur
            return getPlayerId(playerName);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du joueur");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Récupère l'ID d'un joueur par son nom
     */
    public static int getPlayerId(String playerName) {
        String query = "SELECT id FROM players WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Sauvegarde une partie complète
     */
    public static boolean saveGame(GameSession session) {
        String query = "INSERT INTO games " +
                "(player1Id, player2Id, winnerId, player1Score, player2Score, " +
                "totalMoves, gameDuration, moveHistory) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            int player1Id = savePlayer(session.getPlayer1().getName());
            int player2Id = savePlayer(session.getPlayer2().getName());

            pstmt.setInt(1, player1Id);
            pstmt.setInt(2, player2Id);

            // Winner
            if (session.getWinner() != null) {
                int winnerId = getPlayerId(session.getWinner().getName());
                pstmt.setInt(3, winnerId);
            } else {
                pstmt.setNull(3, Types.INTEGER);  // Égalité
            }

            pstmt.setInt(4, session.getPlayer1().getScore());
            pstmt.setInt(5, session.getPlayer2().getScore());
            pstmt.setInt(6, session.getTotalMoves());
            pstmt.setLong(7, java.time.temporal.ChronoUnit.SECONDS
                    .between(session.getStartTime(), session.getEndTime()));
            pstmt.setString(8, session.getMoveHistory());

            pstmt.executeUpdate();

            // Mets à jour les stats du joueur
            updatePlayerStats(session);

            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde de la partie");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour les statistiques du joueur
     */
    private static void updatePlayerStats(GameSession session) {
        String query = "UPDATE players SET totalGames = totalGames + 1, " +
                "wins = wins + CASE WHEN id = ? THEN 1 ELSE 0 END, " +
                "losses = losses + CASE WHEN id = ? THEN 1 ELSE 0 END, " +
                "draws = draws + CASE WHEN ? THEN 1 ELSE 0 END " +
                "WHERE id IN (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            int player1Id = getPlayerId(session.getPlayer1().getName());
            int player2Id = getPlayerId(session.getPlayer2().getName());

            // Compare using unique names or structural IDs instead of object references (==)
            boolean player1Won = session.getWinner() != null &&
                    session.getWinner().getName().equals(session.getPlayer1().getName());
            boolean player2Won = session.getWinner() != null &&
                    session.getWinner().getName().equals(session.getPlayer2().getName());

            pstmt.setInt(1, player1Won ? player1Id : -1);
            pstmt.setInt(2, player2Won ? player2Id : -1);
            pstmt.setBoolean(3, session.getWinner() == null);
            pstmt.setInt(4, player1Id);
            pstmt.setInt(5, player2Id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour des stats");
            e.printStackTrace();
        }
    }

    /**
     * Récupère les meilleurs joueurs (leaderboard)
     */
    public static List<Map<String, Object>> getTopPlayers(int limit) {
        List<Map<String, Object>> topPlayers = new ArrayList<>();
        String query = "SELECT * FROM players ORDER BY wins DESC, totalGames DESC LIMIT ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> player = new HashMap<>();
                player.put("name", rs.getString("name"));
                player.put("totalGames", rs.getInt("totalGames"));
                player.put("wins", rs.getInt("wins"));
                player.put("losses", rs.getInt("losses"));
                player.put("draws", rs.getInt("draws"));
                topPlayers.add(player);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du classement");
            e.printStackTrace();
        }

        return topPlayers;
    }

    /**
     * Récupère les stats d'un joueur
     */
    public static Map<String, Object> getPlayerStats(String playerName) {
        Map<String, Object> stats = new HashMap<>();
        String query = "SELECT * FROM players WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.put("name", rs.getString("name"));
                stats.put("totalGames", rs.getInt("totalGames"));
                stats.put("wins", rs.getInt("wins"));
                stats.put("losses", rs.getInt("losses"));
                stats.put("draws", rs.getInt("draws"));

                int total = rs.getInt("totalGames");
                if (total > 0) {
                    double winRate = (rs.getDouble("wins") / total) * 100;
                    stats.put("winRate", String.format("%.1f%%", winRate));
                } else {
                    stats.put("winRate", "0%");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des stats");
            e.printStackTrace();
        }

        return stats;
    }

    /**
     * Récupère l'historique des parties d'un joueur
     */
    public static List<Map<String, Object>> getPlayerGameHistory(String playerName, int limit) {
        List<Map<String, Object>> history = new ArrayList<>();
        String query = "SELECT g.*, p1.name as player1Name, p2.name as player2Name, w.name as winnerName " +
                "FROM games g " +
                "JOIN players p1 ON g.player1Id = p1.id " +
                "JOIN players p2 ON g.player2Id = p2.id " +
                "LEFT JOIN players w ON g.winnerId = w.id " +
                "WHERE p1.name = ? OR p2.name = ? " +
                "ORDER BY g.playedAt DESC LIMIT ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, playerName);
            pstmt.setInt(3, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> game = new HashMap<>();
                game.put("player1", rs.getString("player1Name"));
                game.put("player2", rs.getString("player2Name"));
                game.put("player1Score", rs.getInt("player1Score"));
                game.put("player2Score", rs.getInt("player2Score"));
                game.put("winner", rs.getString("winnerName"));
                game.put("totalMoves", rs.getInt("totalMoves"));
                game.put("playedAt", rs.getString("playedAt"));
                history.add(game);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique");
            e.printStackTrace();
        }

        return history;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
