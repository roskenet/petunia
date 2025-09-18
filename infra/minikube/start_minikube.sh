#!/bin/bash

PROFILE=${1:-minikube}
MEMORY=${2:-8192}
CPUS=${3:-2}
MOUNT=$HOME/.local/share/mounts

mkdir -p $MOUNT

minikube start -p "$PROFILE" --driver=docker --mount --mount-string="$MOUNT/$PROFILE:/mnt" --addons=ingress --memory=$MEMORY --cpus=$CPUS

minikube ssh -p "$PROFILE" -- "sudo tee -a /etc/sysctl.conf > /dev/null <<'EOF'
# Setting for elasticache
vm.max_map_count=262144

# Settings for kafka
fs.inotify.max_user_watches=524288
fs.inotify.max_user_instances=512
fs.inotify.max_queued_events=16384

net.ipv4.ip_forward=1
EOF

sudo sysctl -p"
