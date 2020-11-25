## Samatra project template

Project bootstrap using [samatra-extras](https://github.com/springernature/samatra-extras) and [samatra](https://github.com/springernature/samatra), including controller and functional tests using [samatra-testing](https://github.com/springernature/samatra-testing). More documentation [here](https://github.com/springernature/samatra-extras/wiki)

To bootstrap a new samatra project run the following:
```
./sbt new git@github.com:westernsam/samatra.g8
```

## What you get out of the box

- Project structure and sbt build with run, test and zip targets
- Example Samatra controllers using asynchttpclient to proxy and mustache to render (non-blocking and async is supported)
- Unit and functional tests in memory
- Utility to load env variables from file into System.env - so it's easy to run Main and tests from within intellij 
- Logging
- JVM, request and system monitoring sent to graphite/statsd
- Gzipped filter


