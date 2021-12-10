package com.yuhtin.updatechecker.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GithubRelease {

    public GithubRelease(String name, String zipball_url, String tarball_url, String node_id, Commit commit) {
        this.name = name;
        this.zipball_url = zipball_url;
        this.tarball_url = tarball_url;
        this.node_id = node_id;
        this.commit = commit;
    }

    private final String name;
    private final String zipball_url;
    private final String tarball_url;
    private final String node_id;

    private final Commit commit;

    @NotNull
    public String getVersion() {
        return name;
    }

    @Nullable
    public String getZipball_url() {
        return zipball_url;
    }

    @Nullable
    public String getTarball_url() {
        return tarball_url;
    }

    @Nullable
    public String getNode_id() {
        return node_id;
    }

    @Nullable
    public Commit getCommit() {
        return commit;
    }

    static class Commit {
        public Commit(String sha, String url) {
            this.sha = sha;
            this.url = url;
        }

        private final String sha;
        private final String url;

        public String getSha() {
            return sha;
        }

        public String getUrl() {
            return url;
        }

    }
}
