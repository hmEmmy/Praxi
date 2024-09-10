package me.emmy.practice.util.chat;

import lombok.experimental.UtilityClass;
import me.emmy.practice.Practice;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Artex
 * @date 28/08/2024 - 15:55
 */
@UtilityClass
public class Logger {

    public void debug(String message) {
        if (Practice.getInstance().getMainConfig().getBoolean("plugin.debugging")) {
            //Bukkit.getConsoleSender().sendMessage(CC.translate("&8&o[&7&oDEBUG&8&o] &f&o" + message));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&7DEBUG&8] &f" + message));
        }
    }
}