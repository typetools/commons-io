This is a version of Commons IO that is annotated with type annotations for the Checker Framework.


To build this project
---------------------

```
mvn -B -Dmaven.test.skip=true package
```

This creates file
`target/commons-io-VERSION.jar`.


To update to a newer version of the upstream library
----------------------------------------------------

At https://github.com/apache/commons-io/releases ,
find the commit corresponding to a public release.

Commons IO version 2.7 is commit 6efbccc88318d15c0f5fdcfa0b87e3dc980dca22

Pull in that commit:
```
git pull https://github.com/apache/commons-io <commitid>
```

Update the version number throughout this file.

Use the latest Checker Framework version by changing `pom.xml`.

Search the codebase for all uses of "@since 2.7" and annotate those methods/classes.


To upload to Maven Central
--------------------------

This must be done on a CSE machine, which has access to the necessary passwords.

# Set the version number:
#  * in file cfMavenCentral.xml
#  * in file pom.xml (if different from upstream)
#  * environment variable PACKAGE below

PACKAGE=commons-io-2.6 && \
mvn -B -Dmaven.test.skip=true package && \
mvn source:jar && \
mvn javadoc:javadoc && (cd target/site/apidocs && jar -cf ${PACKAGE}-javadoc.jar org)

## This does not seem to work for me:
# -Dhomedir=/projects/swlab1/checker-framework/hosting-info

[ ! -z "$PACKAGE" ] && \
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=cfMavenCentral.xml -Dgpg.publicKeyring=/projects/swlab1/checker-framework/hosting-info/pubring.gpg -Dgpg.secretKeyring=/projects/swlab1/checker-framework/hosting-info/secring.gpg -Dgpg.keyname=ADF4D638 -Dgpg.passphrase="`cat /projects/swlab1/checker-framework/hosting-info/release-private.password`" -Dfile=target/${PACKAGE}.jar \
&& \
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=cfMavenCentral.xml -Dgpg.publicKeyring=/projects/swlab1/checker-framework/hosting-info/pubring.gpg -Dgpg.secretKeyring=/projects/swlab1/checker-framework/hosting-info/secring.gpg -Dgpg.keyname=ADF4D638 -Dgpg.passphrase="`cat /projects/swlab1/checker-framework/hosting-info/release-private.password`" -Dfile=target/${PACKAGE}-sources.jar -Dclassifier=sources \
&& \
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=cfMavenCentral.xml -Dgpg.publicKeyring=/projects/swlab1/checker-framework/hosting-info/pubring.gpg -Dgpg.secretKeyring=/projects/swlab1/checker-framework/hosting-info/secring.gpg -Dgpg.keyname=ADF4D638 -Dgpg.passphrase="`cat /projects/swlab1/checker-framework/hosting-info/release-private.password`" -Dfile=target/site/apidocs/${PACKAGE}-javadoc.jar -Dclassifier=javadoc

# Complete the release at https://oss.sonatype.org/#stagingRepositories
