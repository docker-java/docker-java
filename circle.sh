#!/bin/bash -ex

case "$1" in
    pre_machine)
        # copy certificates to default directory ~/.docker
        mkdir .docker
        cp $CIRCLE_PROJECT_REPONAME/etc/certs/* .docker

        # configure docker deamon to use SSL and provide the path to the certificates
        docker_opts='DOCKER_OPTS="$DOCKER_OPTS -H tcp://127.0.0.1:2376 --tlsverify --tlscacert='$HOME'/.docker/ca.pem --tlscert='$HOME'/.docker/server-cert.pem --tlskey='$HOME'/.docker/server-key.pem"'
        sudo sh -c "echo '$docker_opts' >> /etc/default/docker"

        # debug output
        cat /etc/default/docker
        ls -la $HOME/.docker
        ;;

    post_machine)
        # fix permissions on docker.log so it can be collected as an artifact
        sudo chown ubuntu:ubuntu /var/log/upstart/docker.log

        # validate that docker is working
        docker version
        ;;

    dependencies)
        mvn dependency:resolve
        ;;

    test)
        mvn clean verify
        ;;


    collect_test_reports)
        mvn surefire-report:report-only
        cp -R target/surefire-reports $CIRCLE_TEST_REPORTS
        if [ -e target/failsafe-reports ] ; then
            mvn surefire-report:failsafe-report-only
            cp -R target/failsafe-reports $CIRCLE_TEST_REPORTS
        fi
        mvn site:site
        cp -R target/site $CIRCLE_TEST_REPORTS
        ;;
esac
