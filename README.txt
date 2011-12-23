==================
SMILE Junction App
==================

Software requirements: Maven 3.0.3 (required by one of the plugins), an avd called 2.1, and ....

To setup pre-req dependencies from scratch:

mvn install:install-file -Dfile=./smileplug-adm-app/lib/compatibility-v4-r3.jar -Dversion=r3 -DartifactId=compatibility-v4 -DgroupId=android.support -DgeneratePom=true -Dpackaging=jarg
(windows paths should be used on windows)

To build:
mvn clean install
