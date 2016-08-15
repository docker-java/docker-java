#!/usr/bin/env bash



IS_COVERITY_SCAN_BRANCH=`ruby -e "puts '${TRAVIS_BRANCH}' =~ /\\A$COVERITY_SCAN_BRANCH_PATTERN\\z/ ? 1 : 0"`

export COVERITY_ALLOWED=true
# Verify upload is permitted
AUTH_RES=`curl -s --form project="$COVERITY_SCAN_PROJECT_NAME" --form token="$COVERITY_SCAN_TOKEN" $SCAN_URL/api/upload_permitted`
if [ "$AUTH_RES" = "Access denied" ]; then
  echo -e "\033[33;1mCoverity Scan API access denied. Check COVERITY_SCAN_PROJECT_NAME and COVERITY_SCAN_TOKEN.\033[0m"
  COVERITY_ALLOWED=false
else
  AUTH=`echo $AUTH_RES | ruby -e "require 'rubygems'; require 'json'; puts JSON[STDIN.read]['upload_permitted']"`
  if [ "$AUTH" = "true" ]; then
    echo -e "\033[33;1mCoverity Scan analysis authorized per quota.\033[0m"
  else
    WHEN=`echo $AUTH_RES | ruby -e "require 'rubygems'; require 'json'; puts JSON[STDIN.read]['next_upload_permitted_at']"`
    echo -e "\033[33;1mCoverity Scan analysis NOT authorized until $WHEN.\033[0m"

    COVERITY_ALLOWED=false
  fi
fi

set -ex

if [ "${FAST_BUILD}" == "true" ]; then
    if [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
       [ "$COVERITY" == "true" ] &&
       [ "$IS_COVERITY_SCAN_BRANCH" = "1" ] &&
       [ "$COVERITY_ALLOWED" == "true" ]; then
        export COVERITY_SCAN_BUILD_COMMAND="mvn package"
        #curl -s "https://scan.coverity.com/scripts/travisci_build_coverity_scan.sh" | bash
        ./.travis/travisci_build_coverity_scan.sh
    else
        mvn package
    fi
else
    if [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
       [ "$COVERITY" == "true" ] &&
       [ "$IS_COVERITY_SCAN_BRANCH" = "1" ] &&
       [ "$COVERITY_ALLOWED" == "true" ]; then
        export COVERITY_SCAN_BUILD_COMMAND="mvn verify"
        #curl -s "https://scan.coverity.com/scripts/travisci_build_coverity_scan.sh" | bash
        ./.travis/travisci_build_coverity_scan.sh
    else
        mvn verify
    fi
fi
