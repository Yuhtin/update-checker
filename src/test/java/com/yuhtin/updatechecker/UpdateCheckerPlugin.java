package com.yuhtin.updatechecker;

import org.bukkit.plugin.java.JavaPlugin;

public class UpdateCheckerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        UpdateChecker updateChecker = new UpdateChecker(this, "Yuhtin");
        updateChecker.check();

        if (updateChecker.canUpdate()) {
            getLogger().info("This plugin needs to be updated");
            getLogger().info("New version: " + updateChecker.getMoreRecentVersion());
            getLogger().info("Download: " + updateChecker.getDownloadLink());
        } else {
            getLogger().info("This plugin is up to date");
        }
    }
}
