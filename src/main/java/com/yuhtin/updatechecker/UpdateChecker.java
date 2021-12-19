package com.yuhtin.updatechecker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuhtin.updatechecker.model.ConnectionResolver;
import com.yuhtin.updatechecker.model.GithubRelease;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class UpdateChecker {

    private static final Gson SERIALIZER = new Gson();

    private static final String GITHUB_API_LINK = "https://api.github.com/repos/{user}/{pluginName}/tags";
    private static final String PLUGIN_DOWNLOAD_LINK = "https://github.com/{user}/{pluginName}/releases/tag/{version}";

    private final Plugin plugin;

    public UpdateChecker(Plugin plugin, String githubUsername) {
        this.plugin = plugin;

        this.githubUsername = githubUsername;
        this.currentVersion = plugin.getDescription().getVersion();

        updateRelease(new GithubRelease(currentVersion, null, null, null, null));
    }

    private final String githubUsername;
    private final String currentVersion;

    private GithubRelease release;
    private String moreRecentVersion;
    private String downloadLink;

    public void check() {
        ConnectionResolver connectionResolver = new ConnectionResolver(GITHUB_API_LINK
                .replace("{user}", githubUsername)
                .replace("{pluginName}", plugin.getDescription().getName())
        );

        connectionResolver.connect();

        String response = connectionResolver.getResponse();
        if (response == null) {
            updateRelease(new GithubRelease(githubUsername, currentVersion, null, null, new GithubRelease.Commit(null, null)));
            return;
        }

        JsonElement element = new JsonParser().parse(response);
        JsonArray array = element.getAsJsonArray();
        if (array.size() == 0) return;

        GithubRelease release = SERIALIZER.fromJson(array.get(0), GithubRelease.class);
        updateRelease(release);
    }

    private void updateRelease(GithubRelease release) {
        this.release = release;
        moreRecentVersion = release.getVersion();
        downloadLink = updateDownloadLink(release.getVersion());
    }

    private String updateDownloadLink(String moreRecentVersion) {
        return PLUGIN_DOWNLOAD_LINK
                .replace("{user}", githubUsername)
                .replace("{pluginName}", plugin.getName())
                .replace("{version}", moreRecentVersion);
    }

    @NotNull
    public String getMoreRecentVersion() {
        return moreRecentVersion;
    }

    @NotNull
    public String getCurrentVersion() {
        return currentVersion;
    }

    public boolean canUpdate() {
        return !moreRecentVersion.equals(currentVersion);
    }

    @NotNull
    public String getDownloadLink() {
        return downloadLink;
    }

    @NotNull
    public GithubRelease getRelease() {
        return release;
    }
}
