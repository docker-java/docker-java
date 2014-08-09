package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.client.model.SearchItem;

/**
 * Search images
 *
 * @param term - search term
 *
 */
public interface SearchImagesCmd extends DockerCmd<List<SearchItem>> {

	public String getTerm();

	public SearchImagesCmd withTerm(String term);

}