package com.tvpage.demo.model;

import java.util.List;

/**
 * Created by MTPC-110 on 3/30/2017.
 */

public class TvPageProductVideoModel {

    private String merchandised;

    private String tags;

    private String loginId;

    private Asset asset;

    private String visibility;

    private String date_modified;

    private String VIDEO_LENGTH;

    private String status;

    private String settings;

    private String data;

    private String referenceId;

    private String date_created;

    private String entityType;

    private String sourceId;

    private String id;

    private String category;

    private String title;

    private String duration;

    private String search;

    private String transcripts;

    private String description;

    private String titleTextEncoded;

    private String published;

    private String entityIdParent;

    public String getEntityIdParent() {
        return entityIdParent;
    }

    public void setEntityIdParent(String entityIdParent) {
        this.entityIdParent = entityIdParent;
    }

    public String getMerchandised() {
        return merchandised;
    }

    public void setMerchandised(String merchandised) {
        this.merchandised = merchandised;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getVIDEO_LENGTH() {
        return VIDEO_LENGTH;
    }

    public void setVIDEO_LENGTH(String VIDEO_LENGTH) {
        this.VIDEO_LENGTH = VIDEO_LENGTH;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(String transcripts) {
        this.transcripts = transcripts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleTextEncoded() {
        return titleTextEncoded;
    }

    public void setTitleTextEncoded(String titleTextEncoded) {
        this.titleTextEncoded = titleTextEncoded;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }



    public static class Asset {


        private String dashUrl;

        private String mediaDuration;

        private String thumbnailUrl;

        private String type;

        private String prettyDuration;

        private List<VideoMeta> videoMeta;

        private String hlsUrl;

        private List<User> user;

        private List<Sources> sources;

        private String tags;

        private String hdvFlag;

        private String author;

        private String description;

        private String videoId;

        public String getDashUrl() {
            return dashUrl;
        }

        public void setDashUrl(String dashUrl) {
            this.dashUrl = dashUrl;
        }

        public String getMediaDuration() {
            return mediaDuration;
        }

        public void setMediaDuration(String mediaDuration) {
            this.mediaDuration = mediaDuration;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrettyDuration() {
            return prettyDuration;
        }

        public void setPrettyDuration(String prettyDuration) {
            this.prettyDuration = prettyDuration;
        }

        public List<VideoMeta> getVideoMeta() {
            return videoMeta;
        }

        public void setVideoMeta(List<VideoMeta> videoMeta) {
            this.videoMeta = videoMeta;
        }

        public String getHlsUrl() {
            return hlsUrl;
        }

        public void setHlsUrl(String hlsUrl) {
            this.hlsUrl = hlsUrl;
        }

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

        public List<Sources> getSources() {
            return sources;
        }

        public void setSources(List<Sources> sources) {
            this.sources = sources;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getHdvFlag() {
            return hdvFlag;
        }

        public void setHdvFlag(String hdvFlag) {
            this.hdvFlag = hdvFlag;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

    public static class VideoMeta {
        private String duration;

        private String height;

        private String width;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }

    public static class User {
        private String lastName;

        private String emailAddress;

        private String firstName;

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }

    public static class Sources {
        private String height;

        private String file;

        private String width;

        private String quality;

        private String mime;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }
    }
}
