package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;
import com.google.common.base.Preconditions;

import javax.ws.rs.client.WebTarget;

/**
 * Search images
 *
 * @param term - search term
 *
 */
public class SearchImagesCommand extends AbstrDockerCmd<SearchImagesCommand, List<SearchItem>> implements SearchImagesCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchImagesCommand.class);

	private String term;

	public SearchImagesCommand(String term) {
		withTerm(term);
	}

    @Override
	public String getTerm() {
        return term;
    }

    @Override
	public SearchImagesCmd withTerm(String term) {
		Preconditions.checkNotNull(term, "term was not specified");
		this.term = term;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("search ")
            .append(term)
            .toString();
    }

	protected List<SearchItem> impl() {

		WebTarget webResource = baseResource.path("/images/search").queryParam("term", term);
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SearchItem>>() {
        });
	}
}
