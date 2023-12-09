# By default, build on JDK 17 on CentOS 7.
ARG jdk=17
# Red Hat UBI 9 (ubi9-minimal) should be used on JDK 20 and later.
ARG dist=centos7
FROM eclipse-temurin:${jdk}-${dist}

LABEL org.opencontainers.image.source=https://github.com/jboss-dockerfiles/wildfly org.opencontainers.image.title=wildfly org.opencontainers.imag.url=https://github.com/jboss-dockerfiles/wildfly org.opencontainers.image.vendor=WildFly

WORKDIR /opt/jboss

RUN groupadd -r jboss -g 1000 && useradd -u 1000 -r -g jboss -m -d /opt/jboss -s /sbin/nologin -c "JBoss user" jboss && \
    chmod 755 /opt/jboss

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 30.0.0.Final
ENV WILDFLY_SHA1 15f56267c97f1b4e422f56b771075a2ae586dd34
ENV JBOSS_HOME /opt/jboss/wildfly

USER root

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && curl -L -O https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

COPY build/libs/jax_rs_service-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
#COPY src/main/resources/keystore/server.keystore $JBOSS_HOME/standalone/configuration
#COPY src/main/resources/keystore/client.truststore $JBOSS_HOME/standalone/configuration
#COPY src/main/resources/keystore/spring.crt .
#COPY src/main/resources/standalone.xml $JBOSS_HOME/standalone/configuration

RUN keytool -import -noprompt -trustcacerts -alias spring -file spring.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit && \
    keytool -genkeypair -alias localhost -keyalg RSA -keysize 2048 -validity 365 -keystore server.keystore -dname "cn=Server Administrator,o=Acme,c=GB" -keypass secret -storepass secret && \
    cp server.keystore $JBOSS_HOME/standalone/configuration && \
    keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -validity 365 -keystore client.keystore -dname "CN=client" -keypass secret -storepass secret && \
    keytool -exportcert -keystore server.keystore -alias localhost -keypass secret -storepass secret -file server.crt $ keytool -exportcert -keystore client.keystore -alias client -keypass secret -storepass secret -file client.crt && \
    keytool -importcert -keystore server.truststore -storepass secret -alias client -trustcacerts -file client.crt -noprompt && \
    keytool -importcert -keystore client.truststore -storepass secret -alias localhost -trustcacerts -file server.crt -noprompt && \
    cp client.truststore $JBOSS_HOME/standalone/configuration

USER jboss

# Set the default command to run on boot
# This will boot WildFly in standalone mode and bind to all interfaces
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]