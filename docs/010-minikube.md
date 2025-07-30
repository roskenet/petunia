# Minikube

Start a minikube cluster:

```shell
minikube start --profile=petunia --driver=docker --mount --mount-string="$HOME/Mounts/petunia:/mnt" --addons=ingress
```

