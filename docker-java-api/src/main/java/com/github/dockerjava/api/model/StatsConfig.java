package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import javax.annotation.CheckForNull;
import java.io.Serializable;

public class StatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("active_anon")
    private Long activeAnon;

    @FieldName("active_file")
    private Long activeFile;

    @FieldName("cache")
    private Long cache;

    @FieldName("dirty")
    private Long dirty;

    @FieldName("hierarchical_memory_limit")
    private Long hierarchicalMemoryLimit;

    @FieldName("hierarchical_memsw_limit")
    private Long hierarchicalMemswLimit;

    @FieldName("inactive_anon")
    private Long  inactiveAnon;

    @FieldName("inactive_file")
    private Long inactiveFile;

    @FieldName("mapped_file")
    private Long mappedFile;

    @FieldName("pgfault")
    private Long pgfault;

    @FieldName("pgmajfault")
    private Long pgmajfault;

    @FieldName("pgpgin")
    private Long pgpgin;

    @FieldName("pgpgout")
    private Long pgpgout;

    @FieldName("rss")
    private Long rss;

    @FieldName("rss_huge")
    private Long rssHuge;

    @FieldName("swap")
    private Long swap;

    @FieldName("total_active_anon")
    private Long totalActiveAnon;

    @FieldName("total_active_file")
    private Long totalActiveFile;

    @FieldName("total_cache")
    private Long totalCache;

    @FieldName("total_dirty")
    private Long totalDirty;

    @FieldName("total_inactive_anon")
    private Long totalInactiveAnon;

    @FieldName("total_inactive_file")
    private Long totalInactiveFile;

    @FieldName("total_mapped_file")
    private Long totalMappedFile;

    @FieldName("total_pgfault")
    private Long totalPgfault;

    @FieldName("total_pgmajfault")
    private Long totalPgmajfault;

    @FieldName("total_pgpgin")
    private Long totalPgpgin;

    @FieldName("total_pgpgout")
    private Long totalPgpgout;

    @FieldName("total_rss")
    private Long totalRss;

    @FieldName("total_rss_huge")
    private Long totalRssHuge;

    @FieldName("total_swap")
    private Long totalSwap;

    @FieldName("total_unevictable")
    private Long totalUnevictable;

    @FieldName("total_writeback")
    private Long totalWriteback;

    @FieldName("unevictable")
    private Long unevictable;

    @FieldName("writeback")
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
