package dev.siea.data;

public record CountingSettings(String countingChannelId, int countAlongAfterSeconds, boolean allowSameUserCount) {

}
