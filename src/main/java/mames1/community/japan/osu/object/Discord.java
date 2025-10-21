package mames1.community.japan.osu.object;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Discord {

    String clientId;
    String clientSecret;
    String redirectUri;

    public Discord() {
        Dotenv dotenv = Dotenv.configure().load();
        this.clientId = dotenv.get("DISCORD_CLIENT_ID");
        this.clientSecret = dotenv.get("DISCORD_CLIENT_SECRET");
        this.redirectUri = dotenv.get("DISCORD_REDIRECT_URI");
    }
}
