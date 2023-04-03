# README for FIPS-enabled ZooKeeper

This branch contains a 3.7.1 ZooKeeper that is enabled for FIPS operation using Bouncy Castle FIPS libraries. It is a prototype at this time. 

# References

* [Legion of the Bouncy Castle Inc. Java (D)TLS API and JSSE Provider User Guide](https://downloads.bouncycastle.org/fips-java/BC-FJA-(D)TLSUserGuide-1.0.13.pdf)
* [Bouncy Castle FIPS release download page](https://www.bouncycastle.org/fips-java/)

# Prerequisites

* OpenJDK 11.0.18 or greater
* Apache Maven 3.6.0 or greater (only if you want to do builds)

# Build (including Docker Image)

```
mvn clean install
cd zookeeper-assembly
./docker-build.sh
```

# Install Release

```
cd zookeeper-assembly/target
tar -xf apache-zookeeper-3.7.1-bin.tar.gz
```

# Configuration

```
cd <releasedir>
cd config
cp fips.zoo_sample.cfg zoo.cfg
```

(Edit to taste.) 

# Run

```
cd <releasedir>
export SERVER_JVMFLAGS="-Dfips_enabled=true -Dorg.bouncycastle.fips.approved_only=true -Djava.security.properties=$PWD/conf/fips.java.security"
bin/zkServer.sh start
```

What these properties mean:
* fips_enabled - Provokes BC FIPS library loading and self-check. 
* org.bouncycastle.fips.approved_only - If true run in FIPS mode.
* java.security.properties - Points to a file containing overrides for Java Security settings. 

# Confirm operation

Ensure you see messages like the following in the log. 

```
2023-04-03 05:32:08,896 [myid:1] - INFO  [main:ZooKeeperServerMain@119] - FIPS is enabled; invoking configuration
2023-04-03 05:32:08,897 [myid:1] - INFO  [main:FipsManagerImpl@44] - FIPS: FipsStatus isReady: true
2023-04-03 05:32:08,897 [myid:1] - INFO  [main:FipsManagerImpl@45] - FIPS: FipsStatus status message: READY
2023-04-03 05:32:08,897 [myid:1] - INFO  [main:FipsManagerImpl@46] - FIPS: isInApprovedOnlyMode: true
2023-04-03 05:32:08,897 [myid:1] - INFO  [main:FipsManagerImpl@49] - FIPS: Java security providers
2023-04-03 05:32:08,899 [myid:1] - INFO  [main:FipsManagerImpl@52] - FIPS: [0] BCFIPS version 1.000203
2023-04-03 05:32:08,899 [myid:1] - INFO  [main:FipsManagerImpl@52] - FIPS: [1] BCJSSE version 1.0014
...
2023-04-03 05:32:08,901 [myid:1] - INFO  [main:FipsManagerImpl@56] - FIPS: KeyManagerFactory default algorithm: PKIX
2023-04-03 05:32:08,901 [myid:1] - INFO  [main:FipsManagerImpl@57] - FIPS: TrustManagerFactory default algorithm: PKIX
```

# Which libraries were added?

Bouncy Castle FIPS libraries: 

* https://downloads.bouncycastle.org/fips-java/bc-fips-1.0.2.3.jar
* https://downloads.bouncycastle.org/fips-java/bctls-fips-1.0.14.jar
* https://downloads.bouncycastle.org/fips-java/bcpkix-fips-1.0.7.jar

# Other things worthy of note?

Yes. ZooKeeper use Bouncy Castle 1.60 for testing. If you refer to the Bouncy Castle FIPs libraries using static 
references to classes, it will break the ZooKeeper build. Among other things the BC FIPS provider self-test
will fail because it will see classes from BC 1.60. We get around the problem by adding simple provider that loads 
using Class.forName() if property fips_enabled is set to any value other than null. 
