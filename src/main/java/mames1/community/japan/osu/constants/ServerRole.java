package mames1.community.japan.osu.constants;

import lombok.Getter;
import mames1.community.japan.osu.Main;
import net.dv8tion.jda.api.entities.Role;

@Getter
public enum ServerRole {

    MAPPER(Main.bot.getJda().getRoleById(1090228160714526762L)),
    SKINNER(Main.bot.getJda().getRoleById(1090228065461882901L)),
    SAKKYOKU(Main.bot.getJda().getRoleById(1090514847667597373L)),
    ESI(Main.bot.getJda().getRoleById(1090514847667597373L)),
    STD(Main.bot.getJda().getRoleById(1089162880278069258L)),
    TAIKO(Main.bot.getJda().getRoleById(1089163590193389698L)),
    CATCH(Main.bot.getJda().getRoleById(1089162928462233661L)),
    MANIA(Main.bot.getJda().getRoleById(1089162961572077688L)),
    MULTI(Main.bot.getJda().getRoleById(1089914703326748772L)),
    TUUWA(Main.bot.getJda().getRoleById(1089160067187757093L)),
    FOURK(Main.bot.getJda().getRoleById(1316380765071736834L)),
    SEVENK(Main.bot.getJda().getRoleById(1316380834076561440L));

    final Role role;

    ServerRole(Role role) {
        this.role = role;
    }
}
