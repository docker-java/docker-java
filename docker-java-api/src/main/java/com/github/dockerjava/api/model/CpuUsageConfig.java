package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class CpuUsageConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("total_usage")
    private Long totalUsage;

    @FieldName("percpu_usage")
    private List<Long> percpuUsage;

    @FieldName("usage_in_kernelmode")
    private Long usageInKernelmode;

    @FieldName("usage_in_usermode")
    private Long usageInUsermode;

    /**
     * @see #totalUsage
     */
    @CheckForNull
    public Long getTotalUsage() {
        return totalUsage;
    }

    /**
     * @see #percpuUsage
     */
    @CheckForNull
    public List<Long> getPercpuUsage() {
        return percpuUsage;
    }

    /**
     * @see #usageInKernelmode
     */
    @CheckForNull
    public Long getUsageInKernelmode() {
        return usageInKernelmode;
    }

    /**
     * @see #usageInUsermode
     */
    @CheckForNull
    public Long getUsageInUsermode() {
        return usageInUsermode;
    }
}
