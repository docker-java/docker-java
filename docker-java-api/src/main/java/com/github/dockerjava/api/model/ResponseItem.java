package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Represents a pull response stream item
 */
@EqualsAndHashCode
@ToString
public class ResponseItem implements Serializable {
    private static final long serialVersionUID = -5187169652557467828L;

    @FieldName("stream")
    private String stream;

    @FieldName("status")
    private String status;

    @FieldName("progressDetail")
    private ProgressDetail progressDetail;

    @Deprecated
    @FieldName("progress")
    private String progress;

    @FieldName("id")
    private String id;

    @FieldName("from")
    private String from;

    @FieldName("time")
    private Long time;

    @FieldName("errorDetail")
    private ErrorDetail errorDetail;

    @Deprecated
    @FieldName("error")
    private String error;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("aux")
    private AuxDetail aux;

    @CheckForNull
    public String getStream() {
        return stream;
    }

    @CheckForNull
    public String getStatus() {
        return status;
    }

    @CheckForNull
    public ProgressDetail getProgressDetail() {
        return progressDetail;
    }

    @CheckForNull
    @Deprecated
    public String getProgress() {
        return progress;
    }

    @CheckForNull
    public String getId() {
        return id;
    }

    @CheckForNull
    public String getFrom() {
        return from;
    }

    @CheckForNull
    public Long getTime() {
        return time;
    }

    @CheckForNull
    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    @Deprecated
    public String getError() {
        return error;
    }

    /**
     * @see #aux
     */
    @CheckForNull
    public AuxDetail getAux() {
        return aux;
    }

    /**
     * Returns whether the error field indicates an error
     *
     * @returns true: the error field indicates an error, false: the error field doesn't indicate an error
     */
    public boolean isErrorIndicated() {
        // check both the deprecated and current error fields, just in case
        return getError() != null || getErrorDetail() != null;
    }

    @EqualsAndHashCode
    @ToString
    public static class ProgressDetail implements Serializable {
        private static final long serialVersionUID = -1954994695645715264L;

        @FieldName("current")
        Long current;

        @FieldName("total")
        Long total;

        @FieldName("start")
        Long start;

        @CheckForNull
        public Long getCurrent() {
            return current;
        }

        @CheckForNull
        public Long getTotal() {
            return total;
        }

        @CheckForNull
        public Long getStart() {
            return start;
        }
    }

    @EqualsAndHashCode
    @ToString
    public static class ErrorDetail implements Serializable {
        private static final long serialVersionUID = -9136704865403084083L;

        @FieldName("code")
        Integer code;

        @FieldName("message")
        String message;

        @CheckForNull
        public Integer getCode() {
            return code;
        }

        @CheckForNull
        public String getMessage() {
            return message;
        }
    }

    @EqualsAndHashCode
    @ToString
    public static class AuxDetail implements Serializable {
        private static final long serialVersionUID = 1L;

        @FieldName("Size")
        private Integer size;

        @FieldName("Tag")
        private String tag;

        @FieldName("Digest")
        private String digest;

        @CheckForNull
        public Integer getSize() {
            return size;
        }

        @CheckForNull
        public String getTag() {
            return tag;
        }

        @CheckForNull
        public String getDigest() {
            return digest;
        }
    }
}
