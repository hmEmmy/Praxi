package me.emmy.practice.api.aether.scoreboard;

import me.emmy.practice.api.aether.scoreboard.cooldown.BoardCooldown;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Set;

public interface BoardAdapter
{
    String getTitle(final Player p0);
    
    List<String> getScoreboard(final Player p0, final Board p1, final Set<BoardCooldown> p2);
    
    void onScoreboardCreate(final Player p0, final Scoreboard p1);
}
