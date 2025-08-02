# Setting the Stage

## inotify

Especially Apache Kafka uses many file handles:

```
[2025-08-02 09:24:47,839] TRACE [Controller id=0] Checking need to trigger auto leader balancing (kafka.controller.KafkaController)
failed to create fsnotify watcher: too many open files%
```

Fix this:

```
vi /etc/sysctl.conf
```

and add:

```
fs.inotify.max_user_watches=524288
fs.inotify.max_user_instances=512
fs.inotify.max_queued_events=16384
```

```
sudo sysctl -p
```

