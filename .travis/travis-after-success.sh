#!/usr/bin/env bash

if [[ $CODECOV == "true" ]]; then
    codecov
fi

if [[ $TRAVIS_BRANCH == "master" ]] && [[ $TRAVIS_PULL_REQUEST == "false" ]] && [[ $DEPLOY == "true" ]];
then
    cat <<EOF >> ~/settings.xml
<settings>
 <servers>
   <server>
    <id>ossrh</id>
    <username>\${env.OSSRH_USER}</username>
    <password>\${env.OSSRH_PASS}</password>
    </server>
   </servers>
</settings>
EOF
    mvn deploy -DskipITs --settings ~/settings.xml
 fi
