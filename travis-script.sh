#!/usr/bin/env bash

set -ex

IS_COVERITY_SCAN_BRANCH=`ruby -e "puts '${TRAVIS_BRANCH}' =~ /\\A$COVERITY_SCAN_BRANCH_PATTERN\\z/ ? 1 : 0"`


if [ "${FAST_BUILD}" == "true" ]; then
    if [ "$COVERITY" == "true" ] && [ "$IS_COVERITY_SCAN_BRANCH" = "1" ]; then
        export COVERITY_SCAN_BUILD_COMMAND="mvn package"
        curl -s "https://scan.coverity.com/scripts/travisci_build_coverity_scan.sh" | bash
    else
        mvn package
    fi
else
    if [ "$COVERITY" == "true" ] && [ "$IS_COVERITY_SCAN_BRANCH" = "1" ]; then
        COVERITY_SCAN_BUILD_COMMAND="mvn verify"
        curl -s "https://scan.coverity.com/scripts/travisci_build_coverity_scan.sh" | bash
    else
        mvn verify
    fi
fi
