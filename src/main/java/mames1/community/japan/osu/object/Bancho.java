package mames1.community.japan.osu.object;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

@Getter
public class Bancho {

    String clientSecret;
    String redirectUri;
    int clientId;

    public Bancho() {
        Dotenv dotenv = Dotenv.configure().load();
        this.clientSecret = dotenv.get("BANCHO_CLIENT_SECRET");
        this.redirectUri = dotenv.get("BANCHO_REDIRECT_URI");
        this.clientId = Integer.parseInt(dotenv.get("BANCHO_CLIENT_ID"));
    }
}
