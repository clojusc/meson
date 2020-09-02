# meson

[![Build Status][travis-badge]][travis][![Dependencies Status][deps-badge]][deps][![Clojars Project][clojars-badge]][clojars][![Clojure version][clojure-v]](project.clj)

*Clojure Client Library for the Mesos HTTP API*

[![][logo]][logo-large]

**meson** *noun*. A functional particle composed of a Mesos deployment and a Mesos HTTP API service bound together by the strongly interacting force of Clojure.


#### Contents

* [About](#about-)
* [Dependencies](#dependencies-)
* [Versions](#versions-)
* [Quick Start](#quick-start-)
* [Documentation](#documentation-)
* [Usage](#usage-)
* [License](#license-)


## About [&#x219F;](#contents)

This project is a work in progress.

Meson aims to provide an HTTP-only Clojure client API for Mesos with no
dependencies upon the Mesos Java library nor protobufs, thus making
installation and dependencies much easier to manage.


## Dependencies [&#x219F;](#contents)

* Java
* Mesos 1.3+
  * You need a running Mesos deployment
* `lein`


## Versions [&#x219F;](#contents)

| Meson Version              | Meson Status | Mesos | Clojure | Java                   |
|----------------------------|--------------|-------|---------|------------------------|
| 0.4.0 (beta)               | future       | ???   | ???     | ???                    |
| 0.3.0 (alpha)              | future       | ???   | ???     | ???                    |
| 0.2.0-SNAPSHOT (prototype) | in progress  | 1.3   | 1.8.0   | 8 (build 1.8.0_91-b14) |
| 0.1.0-SNAPSHOT (prototype) | released     | 1.0   | 1.8.0   | 8 (build 1.8.0_91-b14) |


## Quick Start [&#x219F;](#contents)

Meson provides some `lein` commands you can use while developing in the library:

* `lein meson mesos start`
* `lein meson mesos stop`
* `lein meson mesos restart`

These may also be used from the Meson REPL (using the `dev` profile):

```clj
(require '[meson.ops.mesos :as mesos])
(mesos/start-local-docker)
(mesos/stop-local-docker)
(mesos/restart-local-docker)
```

These will start a Docker container running with locahost ports 5050 and 5051
mapped to those of the running Mesos container.


## Documentation [&#x219F;](#contents)

Meson API Reference docs are available here:
 * [http://clojusc.github.io/meson/](http://clojusc.github.io/meson/)


## Usage [&#x219F;](#contents)

### Provide Framework, Use Default Handlers and Default/Localhost Mesos

Subscribe a framework with default handlers to a localhost Mesos master.

Purpose: Development/testing.

Start up the REPL (and be sure to have a running Mesos deployment), then:

```clj
(require '[clojure.core.async :as async]
         '[clojure.pprint :refer [pprint]]
         '[meson.api.scheduler.core :as scheduler])
(def framework-info {:framework-info
                     {:user "user1"
                      :name "a-framework"}})
(def channel (scheduler/subscribe framework-info))
```

Since `subscribe` was only called with the framework setup info and didn't also
pass a handler function or mesos master URL, it will use the default framework handler (basically
just a logging message for each scheduler event) and subscribe to localhost. Any real-world Meson
framework will have its own handler, pass it to the `subscribe` function, and also pass a Mesos master
scheduler api URL.

### Provide Framework and Handlers, Use Default/Localhost Mesos

Subscribe a framework, providing your own handler functions, to a localhost Mesos master.

Purpose: Development/testing.

* Define your own handler functions as multi-methods. (See `meson.api.scheduler.handlers` for an example.)
* Same requires and framework definition as previous section.
* Subscribe command is now:

```clj
(def channel (scheduler/subscribe framework-info my-handlers))
```

### Provide Framework, Handlers, and Mesos Master

Subscribe a framework, providing your own handler functions, to a remote Mesos master.

Purpose: Production use.

* Define your own handler functions as multi-methods. (See `meson.api.scheduler.handlers` for an example.)
* Same requires and framework definition as previous section.
* Retrieve the URL to your remote Mesos master scheduler api.
* Subscribe command is now:

```clj
(def channel (scheduler/subscribe framework-info my-handlers "https://my-mesos-master.org:5050/api/v1/scheduler"))
```

### Provide Framework, Mesos Master, Use Default Handlers

Subscribe a framework, with default handler functions, to a remote Mesos master.

This is a scenario in which you want to verify you can successfully subscribe to a remote mesos master,
but have not written handlers yet.

**CAUTION**: The default handlers do NOT decline offers. Once subscribed, all offers will go to the framework and wait,
preventing other frameworks from getting offers.

Purpose: Development/testing.

* Require an additional meson library. (see code below)
* Same framework definition as previous section.
* Retrieve the URL to your remote Mesos master scheduler api.
* Subscribe command is now:

```clojure
(require '[clojure.core.async :as async]
         '[clojure.pprint :refer [pprint]]
         '[meson.api.scheduler.core :as scheduler]
         '[meson.api.scheduler.handlers :as scheduler-handlers])
(def channel (scheduler/subscribe framework-info scheduler-handlers/default "https://my-mesos-master.org:5050/api/v1/scheduler"))
```

### Subscription Results

Regardless, a channel is returned in both cases, and that can be interacted
with directly in the REPL, for example:

```clj
(def result (async/<!! channel))
(pprint result)
```
Which should give something like:
```clj
{:subscribed
 {:framework-id {:value "6919832b-083e-4db5-89ae-a0d1d222510a-0016"},
  :heartbeat-interval-seconds 15.0,
  :master-info
  {:address {:hostname "0414d10a4f36", :ip "172.17.0.2", :port 5050},
   :hostname "0414d10a4f36",
   :id "6919832b-083e-4db5-89ae-a0d1d222510a",
   :ip 33558956,
   :pid "master@172.17.0.2:5050",
   :port 5050,
   :version "1.3.1"}},
 :type :subscribed}
```


## License [&#x219F;](#contents)

Copyright © 2016-2017, Clojure-Aided Enrichment Center

Apache License, Version 2.0.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/meson
[travis-badge]: https://travis-ci.org/clojusc/meson.png?branch=master
[deps]: http://jarkeeper.com/clojusc/meson
[deps-badge]: http://jarkeeper.com/clojusc/meson/status.svg
[logo]: resources/images/Meson-nonet-spin-0-250x.png
[logo-large]: resources/images/Meson-nonet-spin-0-1000x.png
[tag-badge]: https://img.shields.io/github/tag/clojusc/meson.svg
[tag]: https://github.com/clojusc/meson/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[clojars]: https://clojars.org/meson
[clojars-badge]: https://img.shields.io/clojars/v/meson.svg
