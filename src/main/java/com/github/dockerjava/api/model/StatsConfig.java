package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

class StatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("active_anon")
    private Long activeAnon;

    @JsonProperty("active_file")
    private Long activeFile;

    @JsonProperty("cache")
    private Long cache;

    @JsonProperty("dirty")
    private Long dirty;

    @JsonProperty("hierarchical_memory_limit")
    private Long hierarchicalMemoryLimit;

    @JsonProperty("hierarchical_memsw_limit")
    private Long hierarchicalMemswLimit;

    @JsonProperty("inactive_anon")
    private Long  inactiveAnon;

    @JsonProperty("inactive_file")
    private Long inactiveFile;

    @JsonProperty("mapped_file")
    private Long mappedFile;

    @JsonProperty("pgfault")
    private Long pgfault;

    @JsonProperty("pgmajfault")
    private Long pgmajfault;

    @JsonProperty("pgpgin")
    private Long pgpgin;

    @JsonProperty("pgpgout")
    private Long pgpgout;

    @JsonProperty("rss")
    private Long rss;

    @JsonProperty("rss_huge")
    private Long rssHuge;

    @JsonProperty("swap")
    private Long swap;

    @JsonProperty("total_active_anon")
    private Long totalActiveAnon;

    @JsonProperty("total_active_file")
    private Long totalActiveFile;

    @JsonProperty("total_cache")
    private Long totalCache;

    @JsonProperty("total_dirty")
    private Long totalDirty;

    @JsonProperty("total_inactive_anon")
    private Long totalInactiveAnon;

    @JsonProperty("total_inactive_file")
    private Long totalInactiveFile;

    @JsonProperty("total_mapped_file")
    private Long totalMappedFile;

    @JsonProperty("total_pgfault")
    private Long totalPgfault;

    @JsonProperty("total_pgmajfault")
    private Long totalPgmajfault;

    @JsonProperty("total_pgpgin")
    private Long totalPgpgin;

    @JsonProperty("total_pgpgout")
    private Long totalPgpgout;

    @JsonProperty("total_rss")
    private Long totalRss;

    @JsonProperty("total_rss_huge")
    private Long totalRssHuge;

    @JsonProperty("total_swap")
    private Long totalSwap;

    @JsonProperty("total_unevictable")
    private Long totalUnevictable;

    @JsonProperty("total_writeback")
    private Long totalWriteback;

    @JsonProperty("unevictable")
    private Long unevictable;

    @JsonProperty("writeback")
    private Long writeback;

    /**
     * @see #activeAnon
     */
    @CheckForNull
    public Long getActiveAnon() {
        return activeAnon;
    }

    /**
     * @see #activeAnon
     */
    public StatsConfig withActiveAnon(Long activeAnon) {
        this.activeAnon = activeAnon;
        return this;
    }

    /**
     * @see #activeFile
     */
    @CheckForNull
    public Long getActiveFile() {
        return activeFile;
    }

    /**
     * @see #activeFile
     */
    public StatsConfig withActiveFile(Long activeFile) {
        this.activeFile = activeFile;
        return this;
    }

    /**
     * @see #cache
     */
    @CheckForNull
    public Long getCache() {
        return cache;
    }

    /**
     * @see #cache
     */
    public StatsConfig withCache(Long cache) {
        this.cache = cache;
        return this;
    }

    /**
     * @see #dirty
     */
    @CheckForNull
    public Long getDirty() {
        return dirty;
    }

    /**
     * @see #dirty
     */
    public StatsConfig withDirty(Long dirty) {
        this.dirty = dirty;
        return this;
    }

    /**
     * @see #hierarchicalMemoryLimit
     */
    @CheckForNull
    public Long getHierarchicalMemoryLimit() {
        return hierarchicalMemoryLimit;
    }

    /**
     * @see #hierarchicalMemoryLimit
     */
    public StatsConfig withHierarchicalMemoryLimit(Long hierarchicalMemoryLimit) {
        this.hierarchicalMemoryLimit = hierarchicalMemoryLimit;
        return this;
    }

    /**
     * @see #hierarchicalMemswLimit
     */
    @CheckForNull
    public Long getHierarchicalMemswLimit() {
        return hierarchicalMemswLimit;
    }

    /**
     * @see #hierarchicalMemswLimit
     */
    public StatsConfig withHierarchicalMemswLimit(Long hierarchicalMemswLimit) {
        this.hierarchicalMemswLimit = hierarchicalMemswLimit;
        return this;
    }

    /**
     * @see #inactiveAnon
     */
    @CheckForNull
    public Long getInactiveAnon() {
        return inactiveAnon;
    }

    /**
     * @see #inactiveAnon
     */
    public StatsConfig withInactiveAnon(Long inactiveAnon) {
        this.inactiveAnon = inactiveAnon;
        return this;
    }

    /**
     * @see #inactiveFile
     */
    @CheckForNull
    public Long getInactiveFile() {
        return inactiveFile;
    }

    /**
     * @see #inactiveFile
     */
    public StatsConfig withInactiveFile(Long inactiveFile) {
        this.inactiveFile = inactiveFile;
        return this;
    }

    /**
     * @see #mappedFile
     */
    @CheckForNull
    public Long getMappedFile() {
        return mappedFile;
    }

    /**
     * @see #mappedFile
     */
    public StatsConfig withMappedFile(Long mappedFile) {
        this.mappedFile = mappedFile;
        return this;
    }

    /**
     * @see #pgfault
     */
    @CheckForNull
    public Long getPgfault() {
        return pgfault;
    }

    /**
     * @see #pgfault
     */
    public StatsConfig withPgfault(Long pgfault) {
        this.pgfault = pgfault;
        return this;
    }

    /**
     * @see #pgmajfault
     */
    @CheckForNull
    public Long getPgmajfault() {
        return pgmajfault;
    }

    /**
     * @see #pgmajfault
     */
    public StatsConfig withPgmajfault(Long pgmajfault) {
        this.pgmajfault = pgmajfault;
        return this;
    }

    /**
     * @see #pgpgin
     */
    @CheckForNull
    public Long getPgpgin() {
        return pgpgin;
    }

    /**
     * @see #pgpgin
     */
    public StatsConfig withPgpgin(Long pgpgin) {
        this.pgpgin = pgpgin;
        return this;
    }

    /**
     * @see #pgpgout
     */
    @CheckForNull
    public Long getPgpgout() {
        return pgpgout;
    }

    /**
     * @see #pgpgout
     */
    public StatsConfig withPgpgout(Long pgpgout) {
        this.pgpgout = pgpgout;
        return this;
    }

    /**
     * @see #rss
     */
    @CheckForNull
    public Long getRss() {
        return rss;
    }

    /**
     * @see #rss
     */
    public StatsConfig withRss(Long rss) {
        this.rss = rss;
        return this;
    }

    /**
     * @see #rssHuge
     */
    @CheckForNull
    public Long getRssHuge() {
        return rssHuge;
    }

    /**
     * @see #rssHuge
     */
    public StatsConfig withRssHuge(Long rssHuge) {
        this.rssHuge = rssHuge;
        return this;
    }

    /**
     * @see #swap
     */
    @CheckForNull
    public Long getSwap() {
        return swap;
    }

    /**
     * @see #swap
     */
    public StatsConfig withSwap(Long swap) {
        this.swap = swap;
        return this;
    }

    /**
     * @see #totalActiveAnon
     */
    @CheckForNull
    public Long getTotalActiveAnon() {
        return totalActiveAnon;
    }

    /**
     * @see #totalActiveAnon
     */
    public StatsConfig withTotalActiveAnon(Long totalActiveAnon) {
        this.totalActiveAnon = totalActiveAnon;
        return this;
    }

    /**
     * @see #totalActiveFile
     */
    @CheckForNull
    public Long getTotalActiveFile() {
        return totalActiveFile;
    }

    /**
     * @see #totalActiveFile
     */
    public StatsConfig withTotalActiveFile(Long totalActiveFile) {
        this.totalActiveFile = totalActiveFile;
        return this;
    }

    /**
     * @see #totalCache
     */
    @CheckForNull
    public Long getTotalCache() {
        return totalCache;
    }

    /**
     * @see #totalCache
     */
    public StatsConfig withTotalCache(Long totalCache) {
        this.totalCache = totalCache;
        return this;
    }

    /**
     * @see #totalDirty
     */
    @CheckForNull
    public Long getTotalDirty() {
        return totalDirty;
    }

    /**
     * @see #totalDirty
     */
    public StatsConfig withTotalDirty(Long totalDirty) {
        this.totalDirty = totalDirty;
        return this;
    }

    /**
     * @see #totalInactiveAnon
     */
    @CheckForNull
    public Long getTotalInactiveAnon() {
        return totalInactiveAnon;
    }

    /**
     * @see #totalInactiveAnon
     */
    public StatsConfig withTotalInactiveAnon(Long totalInactiveAnon) {
        this.totalInactiveAnon = totalInactiveAnon;
        return this;
    }

    /**
     * @see #totalInactiveFile
     */
    @CheckForNull
    public Long getTotalInactiveFile() {
        return totalInactiveFile;
    }

    /**
     * @see #totalInactiveFile
     */
    public StatsConfig withTotalInactiveFile(Long totalInactiveFile) {
        this.totalInactiveFile = totalInactiveFile;
        return this;
    }

    /**
     * @see #totalMappedFile
     */
    @CheckForNull
    public Long getTotalMappedFile() {
        return totalMappedFile;
    }

    /**
     * @see #totalMappedFile
     */
    public StatsConfig withTotalMappedFile(Long totalMappedFile) {
        this.totalMappedFile = totalMappedFile;
        return this;
    }

    /**
     * @see #totalPgfault
     */
    @CheckForNull
    public Long getTotalPgfault() {
        return totalPgfault;
    }

    /**
     * @see #totalPgfault
     */
    public StatsConfig withTotalPgfault(Long totalPgfault) {
        this.totalPgfault = totalPgfault;
        return this;
    }

    /**
     * @see #totalPgmajfault
     */
    @CheckForNull
    public Long getTotalPgmajfault() {
        return totalPgmajfault;
    }

    /**
     * @see #totalPgmajfault
     */
    public StatsConfig withTotalPgmajfault(Long totalPgmajfault) {
        this.totalPgmajfault = totalPgmajfault;
        return this;
    }

    /**
     * @see #totalPgpgin
     */
    @CheckForNull
    public Long getTotalPgpgin() {
        return totalPgpgin;
    }

    /**
     * @see #totalPgpgin
     */
    public StatsConfig withTotalPgpgin(Long totalPgpgin) {
        this.totalPgpgin = totalPgpgin;
        return this;
    }

    /**
     * @see #totalPgpgout
     */
    @CheckForNull
    public Long getTotalPgpgout() {
        return totalPgpgout;
    }

    /**
     * @see #totalPgpgout
     */
    public StatsConfig withTotalPgpgout(Long totalPgpgout) {
        this.totalPgpgout = totalPgpgout;
        return this;
    }

    /**
     * @see #totalRss
     */
    @CheckForNull
    public Long getTotalRss() {
        return totalRss;
    }

    /**
     * @see #totalRss
     */
    public StatsConfig withTotalRss(Long totalRss) {
        this.totalRss = totalRss;
        return this;
    }

    /**
     * @see #totalRssHuge
     */
    @CheckForNull
    public Long getTotalRssHuge() {
        return totalRssHuge;
    }

    /**
     * @see #totalRssHuge
     */
    public StatsConfig withTotalRssHuge(Long totalRssHuge) {
        this.totalRssHuge = totalRssHuge;
        return this;
    }

    /**
     * @see #totalSwap
     */
    @CheckForNull
    public Long getTotalSwap() {
        return totalSwap;
    }

    /**
     * @see #totalSwap
     */
    public StatsConfig withTotalSwap(Long totalSwap) {
        this.totalSwap = totalSwap;
        return this;
    }

    /**
     * @see #totalUnevictable
     */
    @CheckForNull
    public Long getTotalUnevictable() {
        return totalUnevictable;
    }

    /**
     * @see #totalUnevictable
     */
    public StatsConfig withTotalUnevictable(Long totalUnevictable) {
        this.totalUnevictable = totalUnevictable;
        return this;
    }

    /**
     * @see #totalWriteback
     */
    @CheckForNull
    public Long getTotalWriteback() {
        return totalWriteback;
    }

    /**
     * @see #totalWriteback
     */
    public StatsConfig withTotalWriteback(Long totalWriteback) {
        this.totalWriteback = totalWriteback;
        return this;
    }

    /**
     * @see #unevictable
     */
    @CheckForNull
    public Long getUnevictable() {
        return unevictable;
    }

    /**
     * @see #unevictable
     */
    public StatsConfig withUnevictable(Long unevictable) {
        this.unevictable = unevictable;
        return this;
    }

    /**
     * @see #writeback
     */
    @CheckForNull
    public Long getWriteback() {
        return writeback;
    }

    /**
     * @see #writeback
     */
    public StatsConfig withWriteback(Long writeback) {
        this.writeback = writeback;
        return this;
    }
}

