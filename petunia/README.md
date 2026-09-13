# k3s Cluster Setup Guide

This guide provides step-by-step instructions to set up a complete k3s cluster from scratch with Nginx Ingress Controller and automatic TLS certificate management via Let's Encrypt.

## Prerequisites

- A running k3s cluster (v1.24+)
- `kubectl` configured to access your cluster
- `helm` 3.x installed
- Administrative access to the k3s cluster

## Overview of Components

1. **Nginx Ingress Controller** - Routes external traffic to services running in the cluster
2. **cert-manager** - Automates TLS certificate provisioning and renewal using Let's Encrypt
3. **ClusterIssuer** - Defines the Let's Encrypt certificate authority configuration

---

## Step 1: Configure kubectl (if needed)

If your k3s kubeconfig is not automatically configured:

```bash
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
```

Verify cluster access:
```bash
kubectl cluster-info
```

---

## Step 2: Install Nginx Ingress Controller

Create the namespace:
```bash
kubectl create namespace ingress-nginx
```

Add the Helm repository:
```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
```

Install the Nginx Ingress Controller:
```bash
helm install nginx-ingress ingress-nginx/ingress-nginx \
  --namespace ingress-nginx \
  --set controller.publishService.enabled=true
```

Verify installation:
```bash
kubectl get all -n ingress-nginx
kubectl get svc -n ingress-nginx nginx-ingress-ingress-nginx-controller
```

You should see a LoadBalancer or NodePort service with an external IP.

---

## Step 3: Install cert-manager

cert-manager handles automatic TLS certificate provisioning using ACME (Let's Encrypt).

Add the Helm repository:
```bash
helm repo add jetstack https://charts.jetstack.io
helm repo update
```

Create the namespace:
```bash
kubectl create namespace cert-manager
```

Install cert-manager with custom configuration:
```bash
helm install cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  -f values.yaml
```

The `values.yaml` file contains necessary configuration flags:
```yaml
installCRDs: true

config:
  featureGates:
    ACMEHTTP01IngressPathTypeExact: false
```

Verify installation:
```bash
kubectl get all -n cert-manager
kubectl get crds | grep cert-manager
```

---

## Step 4: Create the ClusterIssuer for Let's Encrypt

The ClusterIssuer defines how cert-manager should interact with Let's Encrypt to provision certificates.

Apply the ClusterIssuer:
```bash
kubectl apply -f clusterissuer.yaml
```

**⚠️ Important:** Update the email address in `clusterissuer.yaml` to your own email before applying.

Verify the ClusterIssuer is ready:
```bash
kubectl get clusterissuer
kubectl describe clusterissuer letsencrypt-prod
```

You should see `READY: True` and a message about ACME account registration.

---

## Step 5: Verify Everything is Working

Check all components are running:
```bash
# Check Nginx Ingress
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx

# Check cert-manager
kubectl get pods -n cert-manager
kubectl get clusterissuer
```

Test certificate issuance by creating a test ingress:
```bash
kubectl apply -f - <<EOF
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: test-tls
  annotations:
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  ingressClassName: nginx
  tls:
  - hosts:
    - yourdomain.com
    secretName: test-tls-secret
  rules:
  - host: yourdomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: nginx-ingress-ingress-nginx-controller
            port:
              number: 80
EOF

# Check certificate creation
kubectl get certificate
kubectl describe certificate test-tls-secret
```

Clean up test resources:
```bash
kubectl delete ingress test-tls
kubectl delete certificate test-tls-secret
```

---

## Configuration Files

### `values.yaml`
Helm values for cert-manager configuration:
- `installCRDs: true` - Automatically installs cert-manager CRDs
- Feature gates for ACME HTTP-01 challenge handling

### `clusterissuer.yaml`
Defines the ClusterIssuer resource for Let's Encrypt:
- Uses ACME v2 protocol
- HTTP-01 challenge solver (validates domain ownership via HTTP)
- Works with Nginx Ingress Controller

---

## Helm Metadata & Release Management

Helm automatically stores release information and history on the Kubernetes cluster in the form of secrets.

### View All Installed Releases

List all releases across all namespaces:
```bash
helm list -A
```

Expected output:
```
NAME           NAMESPACE        REVISION  UPDATED                    STATUS    CHART                 APP VERSION
nginx-ingress  ingress-nginx    1         2025-07-14 10:41:06...    deployed  ingress-nginx-4.13.0  1.13.0
cert-manager   cert-manager     2         2025-07-14 12:34:21...    deployed  cert-manager-v1.18.2  v1.18.2
```

### View Release Details

Get the values used when installing a release:
```bash
helm get values cert-manager -n cert-manager
helm get values nginx-ingress -n ingress-nginx
```

View the complete Kubernetes manifest deployed by a release:
```bash
helm get manifest cert-manager -n cert-manager
```

Check the current status of a release:
```bash
helm status cert-manager -n cert-manager
helm status nginx-ingress -n ingress-nginx
```

### View Release History

See all revisions of a release (useful after upgrades):
```bash
helm history cert-manager -n cert-manager
helm history nginx-ingress -n ingress-nginx
```

### Helm Release Secrets

Helm stores release metadata as Kubernetes secrets. View them directly:
```bash
# List all Helm release secrets
kubectl get secrets -n cert-manager | grep sh.helm.release
kubectl get secrets -n ingress-nginx | grep sh.helm.release

# View a specific release secret
kubectl get secret sh.helm.release.v1.cert-manager.v2 -n cert-manager -o yaml
```

**Note:** The secrets use a binary/compressed format and are managed automatically by Helm. Do not manually edit them.

---

## Troubleshooting

### General Health Checks

Check overall cluster health:
```bash
# Verify all pods are running
kubectl get pods -n ingress-nginx
kubectl get pods -n cert-manager

# Check for pod errors or restarts
kubectl get pods -n ingress-nginx -o wide
kubectl get pods -n cert-manager -o wide

# View events in namespaces
kubectl get events -n ingress-nginx --sort-by='.lastTimestamp'
kubectl get events -n cert-manager --sort-by='.lastTimestamp'
```

### ClusterIssuer Issues

Check if ClusterIssuer is ready and registered:
```bash
kubectl get clusterissuer
kubectl describe clusterissuer letsencrypt-prod

# Expected: READY should be True, and status should show "The ACME account was registered"
```

View cert-manager logs:
```bash
kubectl logs -n cert-manager deploy/cert-manager -f
kubectl logs -n cert-manager deploy/cert-manager-webhook
kubectl logs -n cert-manager deploy/cert-manager-cainjector
```

### Certificate Issues

Check if certificates are being created:
```bash
kubectl get certificate -A
kubectl get certificate <cert-name> -n <namespace> -o yaml
kubectl describe certificate <cert-name> -n <namespace>
```

Check certificate secrets:
```bash
kubectl get secrets -A | grep tls
kubectl get secret <cert-secret-name> -n <namespace> -o yaml
```

View certificate events and errors:
```bash
kubectl describe certificate <cert-name> -n <namespace>
# Look at the "Events" section for error messages
```

### Nginx Ingress Issues

Check Nginx controller logs:
```bash
kubectl logs -n ingress-nginx deploy/nginx-ingress-ingress-nginx-controller -f
```

Verify ingress resources are properly recognized:
```bash
kubectl get ingress -A
kubectl describe ingress <ingress-name> -n <namespace>
```

Check Nginx service and endpoints:
```bash
kubectl get svc -n ingress-nginx
kubectl get endpoints -n ingress-nginx
```

Verify ingressClassName is correct:
```bash
kubectl get ingressclass
# Should show: nginx   <none>
```

### DNS and Connectivity

Test DNS resolution from within the cluster:
```bash
kubectl run -it --rm debug --image=alpine --restart=Never -- nslookup yourdomain.com
```

Test connectivity to external ACME server:
```bash
kubectl run -it --rm debug --image=alpine --restart=Never -- \
  wget -O- https://acme-v02.api.letsencrypt.org/directory
```

Check if domain points to cluster:
```bash
# From your local machine
nslookup yourdomain.com
dig yourdomain.com
```

### Helm Release Status

Verify Helm releases are deployed correctly:
```bash
helm list -A
helm status nginx-ingress -n ingress-nginx
helm status cert-manager -n cert-manager
```

Check Helm release history (useful after upgrades):
```bash
helm history nginx-ingress -n ingress-nginx
helm history cert-manager -n cert-manager
```

View the values used in a release:
```bash
helm get values cert-manager -n cert-manager
helm get values nginx-ingress -n ingress-nginx
```

Get the full manifest for a release:
```bash
helm get manifest cert-manager -n cert-manager | kubectl apply --dry-run=client -f -
```

### Common Issues & Solutions

**Issue: ClusterIssuer stuck in "False" state**
- Check Let's Encrypt is reachable: `kubectl logs -n cert-manager deploy/cert-manager`
- Verify email in clusterissuer.yaml is correct
- Check firewall allows HTTPS to acme-v02.api.letsencrypt.org

**Issue: Certificate pending for hours**
- Verify domain DNS points to your Nginx LoadBalancer/NodePort IP
- Check Nginx can receive HTTP traffic on port 80
- View challenge status: `kubectl describe challenge -A`
- Check Nginx logs for HTTP-01 challenge requests

**Issue: "unable to determine FQDN for value" error**
- Ensure `cert-manager.io/cluster-issuer: "letsencrypt-prod"` annotation is in Ingress
- Verify ingress has `tls` section with valid `hosts` and `secretName`

**Issue: Nginx not routing to backend**
- Verify backend service exists and is running
- Check ingress has correct service name and port
- Verify ingressClassName is set to "nginx"

**Issue: "ImplementationSpecific" path type errors**
- This is why `ACMEHTTP01IngressPathTypeExact: false` is needed in values.yaml
- Ensure cert-manager was installed with the provided values.yaml

---

## Maintenance & Upgrades

### Checking for Updates

Check available versions for each component:
```bash
# Nginx Ingress Controller
helm search repo ingress-nginx

# cert-manager
helm search repo jetstack/cert-manager
```

### Upgrading Nginx Ingress Controller

```bash
helm repo update
helm upgrade nginx-ingress ingress-nginx/ingress-nginx \
  --namespace ingress-nginx \
  --set controller.publishService.enabled=true
```

### Upgrading cert-manager

```bash
helm repo update
helm upgrade cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  -f values.yaml
```

### Backing Up Helm Releases

Export a release configuration for backup:
```bash
# Save cert-manager release
helm get values cert-manager -n cert-manager > cert-manager-backup-values.yaml
helm get manifest cert-manager -n cert-manager > cert-manager-backup-manifest.yaml

# Save nginx-ingress release
helm get values nginx-ingress -n ingress-nginx > nginx-ingress-backup-values.yaml
helm get manifest nginx-ingress -n ingress-nginx > nginx-ingress-backup-manifest.yaml
```

### Certificate Management

View all issued certificates:
```bash
kubectl get certificate -A
kubectl get secret -A | grep tls
```

Manually trigger certificate renewal (if needed):
```bash
kubectl delete secret <cert-secret-name> -n <namespace>
# cert-manager will automatically recreate it
```

---

## Notes

- Certificates are automatically renewed 30 days before expiration
- The ACME account is registered with Let's Encrypt (see status in ClusterIssuer)
- HTTP-01 challenges require the domain to be publicly accessible
- Certificates are stored as Kubernetes secrets (by default in the namespace where the Ingress is)
- Helm stores release history automatically; use `helm history` to track changes
- Each `helm upgrade` creates a new release revision that can be rolled back if needed
