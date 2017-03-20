## Samatra-dispatch project template

Project bootstrap using [samatra-extras](https://github.com/springernature/samatra-extras) and [samatra](https://github.com/springernature/samatra) 

## Samatra-dispatch.g8 instructions.
To bootstrap a new samatra project run the following:

```
wget https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.13/sbt-launch.jar

cat > sbt <<EOF 
#!/bin/bash
SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"
java \$SBT_OPTS -jar `dirname $0`/sbt-launch.jar "\$@"
EOF

chmod a+x sbt

./sbt new git@github.com:springernature/samatra-dispatch.g8
```

## What you get out of the box

- App-anatomy compliant build that will create a pipeline that creates a qa instance in CF
- Logging to kibana
- JVM, request and system monitoring sent to graphite/statsd
- Gzipped filter
- Utility to load env variables from file into System.env - so it's easy to run Main and tests from within intellij 
- Samatra example contorller using dispatch http to proxy and mustache to render
