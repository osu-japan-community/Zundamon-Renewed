package mames1.community.japan.osu.object;

import io.github.cdimascio.dotenv.Dotenv;
import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySQL {

    final String host;
    final String user;
    final String password;
    final String database;

    public MySQL () {
        Dotenv dotenv = Dotenv.configure().load();
        host = dotenv.get("MYSQL_HOST");
        user = dotenv.get("MYSQL_USER");
        password = dotenv.get("MYSQL_PASSWORD");
        database = dotenv.get("MYSQL_DATABASE");
    }

    private Connection getConnection() {
        try {

            AppLogger.log("MySQL接続を確立しました。", LogLevel.INFO);

            return DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database + "?autoReconnect=true&useSSL=false",
                    user,
                    password
            );
        } catch (Exception e) {
            AppLogger.log("MySQL接続に失敗しました: " + e.getMessage(), LogLevel.ERROR);
            return null;
        }
    }

    public void saveLinkData(Member member, Link link) {
        try {

            PreparedStatement ps;
            Connection connection = getConnection();

            ps = Objects.requireNonNull(connection).prepareStatement(
                    "replace into users (id, bancho) values (?, ?)"
            );
            ps.setLong(1, member.getIdLong());
            ps.setLong(2, link.getBanchoId());
            ps.executeUpdate();

        } catch (Exception e) {
            AppLogger.log("リンクデータの保存に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }
    }

    public void addBanwords(String content) {

        try {
            PreparedStatement ps;
            Connection connection = getConnection();

            ps = Objects.requireNonNull(connection).prepareStatement(
                    "INSERT INTO banword (content) VALUES (?)"
            );
            ps.setString(1, content);
            ps.executeUpdate();

            Main.banWords.add(content);

        } catch (Exception e) {
            AppLogger.log("BanWordの追加に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }
    }

    public void removeBanwords(String content) {

        try {
            PreparedStatement ps;
            Connection connection = getConnection();

            ps = Objects.requireNonNull(connection).prepareStatement(
                    "DELETE FROM banword WHERE content = ?"
            );
            ps.setString(1, content);
            ps.executeUpdate();

            Main.banWords.remove(content);

        } catch (Exception e) {
            AppLogger.log("BanWordの削除に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }
    }

    public List<String> getBanwords() {

        try {

            PreparedStatement ps;
            ResultSet result;
            Connection connection = getConnection();
            List<String> banWords = new ArrayList<>();

            ps = Objects.requireNonNull(connection).prepareStatement(
                    "SELECT content FROM banword"
            );

            result = ps.executeQuery();

            while(result.next()) {
                banWords.add(result.getString("content").toLowerCase());
            }

            return banWords;

        } catch (Exception e) {
            AppLogger.log("BanWordの取得に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }

        return new ArrayList<>();
    }
}
