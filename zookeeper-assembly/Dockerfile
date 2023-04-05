# Build a FIPS-enabled ZooKeeper. 
FROM eclipse-temurin:11-jre

ARG SHORT_DISTRO_NAME=zookeeper-3.7.1-fips
ARG DISTRO_NAME=apache-zookeeper-3.7.1-bin

ENV ZOO_CONF_DIR=/conf \
    ZOO_DATA_DIR=/data \
    ZOO_DATA_LOG_DIR=/datalog \
    ZOO_LOG_DIR=/logs \
    ZOO_TICK_TIME=2000 \
    ZOO_INIT_LIMIT=5 \
    ZOO_SYNC_LIMIT=2 \
    ZOO_AUTOPURGE_PURGEINTERVAL=0 \
    ZOO_AUTOPURGE_SNAPRETAINCOUNT=3 \
    ZOO_MAX_CLIENT_CNXNS=60 \
    ZOO_STANDALONE_ENABLED=true \
    ZOO_ADMINSERVER_ENABLED=true

# Add a user with an explicit UID/GID and create necessary directories
RUN set -eux; \
    groupadd -r zookeeper --gid=1000; \
    useradd -r -g zookeeper --uid=1000 zookeeper; \
    mkdir -p "$ZOO_DATA_LOG_DIR" "$ZOO_DATA_DIR" "$ZOO_CONF_DIR" "$ZOO_LOG_DIR"; \
    chown zookeeper:zookeeper "$ZOO_DATA_LOG_DIR" "$ZOO_DATA_DIR" "$ZOO_CONF_DIR" "$ZOO_LOG_DIR"

# Install required packges
RUN set -eux; \
    apt-get update; \
    DEBIAN_FRONTEND=noninteractive \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        dirmngr \
        gosu \
        gnupg \
        netcat \
        wget; \
    rm -rf /var/lib/apt/lists/*; \
# Verify that gosu binary works
    gosu nobody true

# Get ZooKeeper build, untar, and clean up. 
COPY target/"$DISTRO_NAME.tar.gz" .
RUN set -eux; \
    echo "My current directory: $PWD"; \
    tar -zxf "$DISTRO_NAME.tar.gz"; \
    chown -R zookeeper:zookeeper "/$DISTRO_NAME"; \
    mv "$DISTRO_NAME/conf/"* "$ZOO_CONF_DIR"; \
    rm "$DISTRO_NAME.tar.gz"; 

WORKDIR $DISTRO_NAME
VOLUME ["$ZOO_DATA_DIR", "$ZOO_DATA_LOG_DIR", "$ZOO_LOG_DIR"]

EXPOSE 2281 2888 3888 8080

ENV PATH=$PATH:/$DISTRO_NAME/bin \
    ZOOKEEPER_HOME=/$DISTRO_NAME \
    ZOOCFGDIR=$ZOO_CONF_DIR \
    SERVER_JVMFLAGS="-Dfips.enabled=true -Dorg.bouncycastle.fips.approved_only=true -Djava.security.properties=$ZOOCFGDIR/conf/fips.java.security"

COPY docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["zkServer.sh", "start-foreground"]
