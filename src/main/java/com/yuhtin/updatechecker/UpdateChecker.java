package com.yuhtin.updatechecker;

import com.google.gson.*;
import com.yuhtin.updatechecker.model.ConnectionResolver;
import com.yuhtin.updatechecker.model.GithubRelease;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpdateChecker {

    private static final String GITHUB_API_LINK = "https://api.github.com/repos/{user}/{pluginName}/releases";
    private static final Gson SERIALIZER = new GsonBuilder().serializeNulls().create();

    private final Plugin plugin;

    public UpdateChecker(Plugin plugin, String githubUsername) {
        this.plugin = plugin;

        this.githubUsername = githubUsername;
        this.currentVersion = plugin.getDescription().getVersion();
    }

    private final String githubUsername;
    private final String currentVersion;

    private GithubRelease lastRelease;

    public void check() {
        ConnectionResolver connectionResolver = new ConnectionResolver(GITHUB_API_LINK
                .replace("{user}", githubUsername)
                .replace("{pluginName}", plugin.getDescription().getName())
        );

        connectionResolver.connect();

        String response = connectionResolver.getResponse();
        if (response == null) return;

        JsonElement element = new JsonParser().parse(response);
        JsonArray array = element.getAsJsonArray();
        if (array.size() == 0) return;

        GithubRelease release = SERIALIZER.fromJson(array.get(0), GithubRelease.class);
        if (release != null) updateRelease(release);
    }

    private void updateRelease(@Nullable GithubRelease release) {
        this.lastRelease = release;
    }

    @NotNull
    public String getCurrentVersion() {
        return currentVersion;
    }

    @Nullable
    public GithubRelease getLastRelease() {
        return lastRelease;
    }

    public boolean canUpdate() {
        return lastRelease != null && !lastRelease.getVersion().equalsIgnoreCase(currentVersion);
    }
}
