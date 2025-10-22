package mames1.community.japan.osu.object;

import io.github.cdimascio.dotenv.Dotenv;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            Logger.log("MySQL接続を確立しました。", Level.INFO);

            return DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database + "?autoReconnect=true&useSSL=false",
                    user,
                    password
            );
        } catch (Exception e) {
            Logger.log("MySQL接続に失敗しました: " + e.getMessage(), Level.ERROR);
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
            Logger.log("リンクデータの保存に失敗しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
