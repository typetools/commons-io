#!/bin/bash

# Fail the whole script if any command fails
set -e


## Diagnostic output
# Output lines of this script as they are read.
set -o verbose
# Output expanded lines of this script as they are executed.
set -o xtrace
# Don't use "-d" to debug ant, because that results in a log so long
# that Travis truncates the log and terminates the job.

export SHELLOPTS


## To type-check all non-test files: mvn compile
## To type-check all files including tests: mvn install
## To skip compiling the tests:  mvn install -Dmaven.test.skip=true

git clone https://github.com/typetools/checker-framework.git
(cd checker-framework && ./.travis-build-without-test.sh)
export CHECKERFRAMEWORK=`pwd`/checker-framework
mvn compile
