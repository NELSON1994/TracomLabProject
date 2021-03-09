package com.tracom.atlas.service;

public interface LoggerServiceExtension {
    void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes);
}