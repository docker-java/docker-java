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
     * @see #activeFile
     */
    @CheckForNull
    public Long getActiveFile() {
        return activeFile;
    }

    /**
     * @see #cache
     */
    @CheckForNull
    public Long getCache() {
        return cache;
    }

    /**
     * @see #dirty
     */
    @CheckForNull
    public Long getDirty() {
        return dirty;
    }

    /**
     * @see #hierarchicalMemoryLimit
     */
    @CheckForNull
    public Long getHierarchicalMemoryLimit() {
        return hierarchicalMemoryLimit;
    }

    /**
     * @see #hierarchicalMemswLimit
     */
    @CheckForNull
    public Long getHierarchicalMemswLimit() {
        return hierarchicalMemswLimit;
    }

    /**
     * @see #inactiveAnon
     */
    @CheckForNull
    public Long getInactiveAnon() {
        return inactiveAnon;
    }

    /**
     * @see #inactiveFile
     */
    @CheckForNull
    public Long getInactiveFile() {
        return inactiveFile;
    }

    /**
     * @see #mappedFile
     */
    @CheckForNull
    public Long getMappedFile() {
        return mappedFile;
    }

    /**
     * @see #pgfault
     */
    @CheckForNull
    public Long getPgfault() {
        return pgfault;
    }

    /**
     * @see #pgmajfault
     */
    @CheckForNull
    public Long getPgmajfault() {
        return pgmajfault;
    }

    /**
     * @see #pgpgin
     */
    @CheckForNull
    public Long getPgpgin() {
        return pgpgin;
    }

    /**
     * @see #pgpgout
     */
    @CheckForNull
    public Long getPgpgout() {
        return pgpgout;
    }

    /**
     * @see #rss
     */
    @CheckForNull
    public Long getRss() {
        return rss;
    }

    /**
     * @see #rssHuge
     */
    @CheckForNull
    public Long getRssHuge() {
        return rssHuge;
    }

    /**
     * @see #swap
     */
    @CheckForNull
    public Long getSwap() {
        return swap;
    }

    /**
     * @see #totalActiveAnon
     */
    @CheckForNull
    public Long getTotalActiveAnon() {
        return totalActiveAnon;
    }

    /**
     * @see #totalActiveFile
     */
    @CheckForNull
    public Long getTotalActiveFile() {
        return totalActiveFile;
    }

    /**
     * @see #totalCache
     */
    @CheckForNull
    public Long getTotalCache() {
        return totalCache;
    }

    /**
     * @see #totalDirty
     */
    @CheckForNull
    public Long getTotalDirty() {
        return totalDirty;
    }

    /**
     * @see #totalInactiveAnon
     */
    @CheckForNull
    public Long getTotalInactiveAnon() {
        return totalInactiveAnon;
    }

    /**
     * @see #totalInactiveFile
     */
    @CheckForNull
    public Long getTotalInactiveFile() {
        return totalInactiveFile;
    }

    /**
     * @see #totalMappedFile
     */
    @CheckForNull
    public Long getTotalMappedFile() {
        return totalMappedFile;
    }

    /**
     * @see #totalPgfault
     */
    @CheckForNull
    public Long getTotalPgfault() {
        return totalPgfault;
    }

    /**
     * @see #totalPgmajfault
     */
    @CheckForNull
    public Long getTotalPgmajfault() {
        return totalPgmajfault;
    }

    /**
     * @see #totalPgpgin
     */
    @CheckForNull
    public Long getTotalPgpgin() {
        return totalPgpgin;
    }

    /**
     * @see #totalPgpgout
     */
    @CheckForNull
    public Long getTotalPgpgout() {
        return totalPgpgout;
    }

    /**
     * @see #totalRss
     */
    @CheckForNull
    public Long getTotalRss() {
        return totalRss;
    }

    /**
     * @see #totalRssHuge
     */
    @CheckForNull
    public Long getTotalRssHuge() {
        return totalRssHuge;
    }

    /**
     * @see #totalSwap
     */
    @CheckForNull
    public Long getTotalSwap() {
        return totalSwap;
    }

    /**
     * @see #totalUnevictable
     */
    @CheckForNull
    public Long getTotalUnevictable() {
        return totalUnevictable;
    }

    /**
     * @see #totalWriteback
     */
    @CheckForNull
    public Long getTotalWriteback() {
        return totalWriteback;
    }

    /**
     * @see #unevictable
     */
    @CheckForNull
    public Long getUnevictable() {
        return unevictable;
    }

    /**
     * @see #writeback
     */
    @CheckForNull
    public Long getWriteback() {
        return writeback;
    }
}
