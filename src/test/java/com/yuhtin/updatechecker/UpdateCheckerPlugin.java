package com.yuhtin.updatechecker;

import com.yuhtin.updatechecker.model.GithubRelease;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateCheckerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        UpdateChecker updateChecker = new UpdateChecker(this, "Yuhtin");
        updateChecker.check();

        if (updateChecker.canUpdate()) {
            GithubRelease lastRelease = updateChecker.getLastRelease();

            getLogger().info("This plugin needs to be updated");
            getLogger().info("New version: " + lastRelease.getVersion());
            getLogger().info("Download: " + lastRelease.getDownloadURL());
        } else {
            getLogger().info("This plugin is up to date");
        }
    }
}
