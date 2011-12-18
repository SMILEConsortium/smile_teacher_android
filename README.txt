==================
SMILE Junction App
==================

To setup pre-req dependencies from scratch:

mvn install:install-file -Dfile=./smileplug-adm-app/lib/compatibility-v4-r3.jar -Dversion=r3 -DartifactId=compatibility-v4 -DgroupId=android.support -DgeneratePom=true -Dpackaging=jarg
(windows paths should be used on windows)

To build:
mvn clean install
