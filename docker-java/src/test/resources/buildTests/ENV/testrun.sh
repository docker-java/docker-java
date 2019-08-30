#!/bin/sh

echo "Hello Word" > hello.txt
echo "hello.txt Created"

TEST_FILE=/tmp/subst-file-abc123.txt
TEST_FILE_2=/tmp/subst-file-2-abc123.txt

if test -f $TEST_FILE
then

	if test -f $TEST_FILE_2
	then
		echo "testENVSubstitution successfully completed"
  		exit 0
	else
		echo "$TEST_FILE_2 does not exist"
  		exit 1
	fi

else

	echo "$TEST_FILE does not exist"
  	exit 1
  
fi

