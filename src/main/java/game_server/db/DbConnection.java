package game_server.db;

import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.enums.Element;
import game.enums.MonsterType;
import game.user.User;
import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Data
public class DbConnection {
    private String username;
    private String password;
    private String dbName;
    private String dbUrl;
    private String port;
    private Connection c;

    public DbConnection() {
        String filePath = "C:\\Users\\mgarc\\IdeaProjects\\UdemyJava\\TCG\\src\\main\\java\\game_server\\dbconfig\\data.properties";
        installDriver();

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.username = prop.getProperty("username");
        this.password = prop.getProperty("password");
        this.dbName = prop.getProperty("dbname");
        this.dbUrl = prop.getProperty("ip");
        this.port = prop.getProperty("port");
    }

    public void installDriver(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found");
            e.printStackTrace();
        }
    }

    public Connection connect(){
        try {
            String fullPath = "jdbc:postgresql://" + this.dbUrl + ":"+ this.port +"/" + this.dbName;
            Connection c = DriverManager.getConnection( fullPath, this.username, this.password);
            c.setAutoCommit(false);
            return c;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
            return null;
        }
    }

    public boolean insertUser(User player) throws SQLException {
        int rows = 0;

        this.c = connect();
        String query = ("INSERT INTO PUBLIC.USER " +
                        "(username, password, bio, token, image, coins, elo, admin) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        PreparedStatement stmt = this.c.prepareStatement(query);
        stmt.setString(1, player.getUsername());
        stmt.setString(2, player.getPassword());
        stmt.setString(3, player.getBio());
        stmt.setString(4, player.getToken());
        stmt.setString(5, player.getImage());
        stmt.setInt(6, player.getCoins());
        stmt.setInt(7, player.getElo());
        stmt.setBoolean(8, player.isAdmin());

        rows = stmt.executeUpdate();

        if (rows > 0) this.c.commit();
        closeConnection(stmt);
        this.c = null;

        return rows > 0;
    }

    public User getLoggedUser(String token) {
        User user = null;
        try {
            if(isLogged(token)) {
                String[] strs = token.split("-");
                user = getUser(strs[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean insertCard(Card card, Boolean toDeck, User user, String cid, int packageId) {
        this.c = connect();
        int rows = 0;

        try {
            String query = (
            "INSERT INTO public.card" +
            " (\"cardId\", name, \"monsterType\", element, damage, deck, owner, \"packageId\")" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, cid);
            stmt.setString(2, card.getName());

            String type = (card.getType() == null) ? null : card.getType().getName();
            stmt.setString(3, type);
            stmt.setString(4,  card.getCardElement().getElementName());
            stmt.setFloat(5, card.getDamage());
            stmt.setBoolean(6, toDeck);

            String owner = (user.isAdmin()) ? null : user.getUsername();
            stmt.setString(7, owner);
            stmt.setInt(8, packageId);
            rows = stmt.executeUpdate();

            if (rows > 0) this.c.commit();
            closeConnection(stmt);

        } catch (SQLException throwables) {
            System.out.println("This card were already loaded to DB");
        }
        return rows > 0;
    }

    public boolean editUserData(User user, String oldUsername) {
        c = connect();
        int rows = 0;
        try {
            String query = (
                "UPDATE public.user " +
                "SET username = ?, password = ?, token = ?, " +
                "    bio      = ?, image    = ?  WHERE username = ?;");

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getToken());
            stmt.setString(4,  user.getBio());
            stmt.setString(5, user.getImage());
            stmt.setString(6, oldUsername);

            rows = stmt.executeUpdate();

            if (rows > 0) c.commit();
            closeConnection(stmt);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rows > 0;
    }

    public boolean editUserStats(User user, int coinsDelta, int eloDelta) {
        c = connect();
        int rows = 0;
        try {
            String query = (
                    "UPDATE public.user SET coins = ?, elo = ? WHERE username LIKE ?;");

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, user.getCoins() + coinsDelta );
            stmt.setInt(2, user.getElo() + eloDelta );
            stmt.setString(3, user.getUsername());

            rows = stmt.executeUpdate();

            if (rows > 0) c.commit();
            closeConnection(stmt);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rows > 0;
    }

    public int getMaxPackageId() {
        try {
            this.c = connect();
            String query = ("SELECT max(\"packageId\") as maxId FROM PUBLIC.CARD;");
            PreparedStatement stmt = this.c.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            try {
                if(rs.next()) {
                    return rs.getInt("maxId");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            closeConnection(rs,stmt);
        } catch (RuntimeException | SQLException e) {
            System.out.println("Get max packageId exception");
            return 0;
        }
        return 0;
    }

    public boolean addSession(String token){
        int rows = 0;
        try {
            if(!isLogged(token)) {
                this.c = connect();
                String query = ("INSERT INTO PUBLIC.SESSION (token) VALUES (?);");
                PreparedStatement stmt = this.c.prepareStatement(query);
                stmt.setString(1, token);
                rows = stmt.executeUpdate();
                if (rows > 0) this.c.commit();

                closeConnection(stmt);
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean isLogged(String token) throws SQLException { //getSession
        this.c = connect();
        String query = ("SELECT * FROM PUBLIC.SESSION WHERE token = ?;");
        PreparedStatement stmt = this.c.prepareStatement(query);
        stmt.setString(1, token);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            closeConnection(rs,stmt);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSession(String token) {
        int rows = 0;
        try {
            if(isLogged(token)) {
                this.c = connect();
                String query = ("DELETE FROM public.session WHERE token LIKE ?;");
                PreparedStatement stmt = this.c.prepareStatement(query);
                stmt.setString(1, token);
                rows = stmt.executeUpdate();

                if (rows > 0) this.c.commit();
                closeConnection(stmt);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rows > 0;
    }

    public User getUser(String username, String pass) {
        User user = null;
        try {
            this.c = connect();
            String query = ( "SELECT * FROM PUBLIC.USER WHERE username = ? AND password = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            try {
                if(rs.next()) {
                    user = buildUser(rs, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeConnection(rs, stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public User getUser(String username) {
        User user = null;
        try {
            this.c = connect();
            String query = ( "SELECT * FROM PUBLIC.USER WHERE username = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            try {
                if(rs.next()) {
                    user = buildUser(rs, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeConnection(rs, stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            this.c = connect();
            String query = ( "SELECT * FROM PUBLIC.USER;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            try {
                if (rs.next()) {
                    do {
                        userList.add(buildUser(rs, false));
                    } while (rs.next());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeConnection(rs, stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    public Card getCardById(String cid) {
        Card card = null;

        try {
            this.c = connect();
            String query = ( "SELECT * FROM PUBLIC.CARD WHERE \"cardId\" = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, cid);
            ResultSet rs = stmt.executeQuery();
            if(rs == null || !rs.next()) {
                System.out.println("Card not found");
                return null;
            }

            card = buildCard(rs);
            closeConnection(rs, stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return card;
    }

    public List<Card> getCardByOwner(String username) {
        List<Card> cardList = new ArrayList<>();
        Card temp;

        try {
            this.c = connect();
            String query = ( "SELECT * FROM PUBLIC.CARD WHERE \"owner\" = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                do {
                    temp = buildCard(rs);
                    cardList.add(temp);
                } while (rs.next());
            }

            closeConnection(rs, stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cardList;
    }

    public List<Card> getPackageCards(int packageId) {
        List<Card> cardList = new ArrayList<>();
        Card temp;

        try {
            this.c = connect();
            String query = ( "SELECT * FROM PUBLIC.CARD WHERE \"packageId\" = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                do {
                    temp = buildCard(rs);
                    cardList.add(temp);
                } while (rs.next());
            }

            closeConnection(rs, stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cardList;
    }

    public void addOwnerToPackage(String username, int packageId) {
        try {
            this.c = connect();
            String query = (
                "UPDATE public.card SET owner = ?, \"packageId\" = null  WHERE \"packageId\" = ?;" );
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setInt(2, packageId);
            int rows = stmt.executeUpdate();
            if (rows > 0) c.commit();

            closeConnection(stmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Card buildCard(ResultSet rs) throws SQLException {
        Card card;

        if ( rs.getString("monsterType") == null) {
            card = new SpellCard(
                    rs.getString("cardId"),
                    Element.find(rs.getString("element")),
                    rs.getString("name"),
                    rs.getFloat("damage"));
        } else {
            card = new MonsterCard(
                    rs.getString("cardId"),
                    Objects.requireNonNull(MonsterType.find(rs.getString("monsterType"))),
                    Objects.requireNonNull(Element.find(rs.getString("element"))),
                    rs.getString("name"),
                    rs.getFloat("damage"));
        }
        return card;
    }

    public User buildUser(ResultSet rs, boolean withPass) throws SQLException {
        String pass;
        if(withPass) {
            pass = rs.getString("password");
        } else {
            pass = "";
        }
        User user = User.builder()
                .username( rs.getString("username") )
                .password( pass )
                .token( rs.getString("token") )
                .bio( rs.getString("bio") )
                .coins( rs.getInt("coins") )
                .elo( rs.getInt("elo") )
                .stack(new CardStack())
                .deck(new CardDeck())
                .isAdmin( rs.getBoolean("admin") )
                .build();
        List<Card> list = getCardByOwner(user.getUsername());
        user.getStack().addListToStack(list);
        return user;
    }

    public CardStack loadDeck(String username, String CardId) throws SQLException {
        String query = ("SELECT * FROM PUBLIC.CARD WHERE owner = ? AND \"cardId\" = ?;");
        PreparedStatement stmt = c.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, CardId);
        ResultSet rs = stmt.executeQuery();
//        Card card;
//        try {
//            CardStack stack = new CardStack();
//            while ( rs.next() ) {
//                if(rs.getString("name").contains("Spell")) {
//                    card = new SpellCard(cardElement, aName, (int) damage);
//                } else {
//                    card = new MonsterCard(cardElement, aName, (int) damage);
//                }
//            }
//        } catch (RuntimeException e) {
//            System.out.println("Unable to create stack");
//        }

        return null;
    }

    public boolean deleteUser(User user) {
        try {
            this.c = connect();
            String query = ( "DELETE FROM PUBLIC.USER WHERE username = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            int rows = stmt.executeUpdate();

            if (rows > 0) this.c.commit();
            closeConnection(stmt);
            this.c = null;

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean deleteCard(String cid) {
        try {
            this.c = connect();
            String query = ( "DELETE FROM PUBLIC.CARD WHERE \"cardId\" = ?;" );
            PreparedStatement stmt = this.c.prepareStatement(query);
            stmt.setString(1, cid);
            int rows = stmt.executeUpdate();

            if (rows > 0) this.c.commit();
            closeConnection(stmt);

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void closeConnection(ResultSet rs, PreparedStatement stmt) throws SQLException {
        rs.close();
        stmt.close();
        this.c.close();
    }

    public void closeConnection(PreparedStatement stmt) throws SQLException {
        stmt.close();
        this.c.close();
    }

}


