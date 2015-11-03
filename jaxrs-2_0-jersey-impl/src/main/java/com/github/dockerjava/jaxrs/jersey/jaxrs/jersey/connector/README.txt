This package exists as a workaround to https://java.net/jira/browse/JERSEY-2852.
It introduces ApacheConnectorClientResponse which extends ClientResponse and closes
the underlying CloseableHttpResponse when close() is called.