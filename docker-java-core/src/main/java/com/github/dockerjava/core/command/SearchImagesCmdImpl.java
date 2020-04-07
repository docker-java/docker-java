package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;

import javax.annotation.Nonnull;

/**
 * Search images
 *
 * @param term
 *            - search term
 *
 */
public class SearchImagesCmdImpl extends AbstrDockerCmd<SearchImagesCmd, List<SearchItem>> implements SearchImagesCmd {

    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 100;

    private String term;
    private Integer limit;

    public SearchImagesCmdImpl(SearchImagesCmd.Exec exec, String term) {
        super(exec);
        withTerm(term);
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public SearchImagesCmd withTerm(String term) {
        checkNotNull(term, "term was not specified");
        this.term = term;
        return this;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public SearchImagesCmd withLimit(@Nonnull Integer limit) {
        String errorMessage = String.format("Limit %s is outside the range of [%s, %s]", limit, MIN_LIMIT, MAX_LIMIT);
        checkArgument(limit <= MAX_LIMIT, errorMessage);
        checkArgument(limit >= MIN_LIMIT, errorMessage);
        this.limit = limit;
        return this;
    }
}
