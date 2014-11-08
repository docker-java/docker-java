Set-up a Docker Registry with Plain Text Authentication
--
This creates a registry that runs locally with SSL and authentication set-up.

Pre-requisites, fig (on OS-X with Homebrew):

    brew install fig

To build:

    fig up	
    
Test it works:

    curl https://localhost:5443/v1/users/ -k -f --basic --user registry:registry

Based on <https://medium.com/@deeeet/building-private-docker-registry-with-basic-authentication-with-self-signed-certificate-using-it-e6329085e612>.

