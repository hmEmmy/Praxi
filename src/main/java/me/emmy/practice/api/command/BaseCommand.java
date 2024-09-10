package me.emmy.practice.api.command;

import me.emmy.practice.Practice;

/**
 * @author minnymin3
 */
public abstract class BaseCommand {

    public Practice main = Practice.getInstance();

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
