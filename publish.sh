GPG_TTY=$(tty)
export GPG_TTY
# for java doc
export JAVA_HOME=`/usr/libexec/java_home -F -v 1.8`
mvn -s $MAVEN_SETTINGS clean deploy -Dgpg.passphrase=$GPG_PASSPHRASE -DskipTests=true  -Pbasepom.oss-release