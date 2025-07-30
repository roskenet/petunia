# petunia-postgres

A Helm chart for deploying a PostgreSQL instance as part of **Project Petunia**.

This chart assumes that a central Kubernetes Secret named `petunia` already exists in the same namespace and provides the required credentials (e.g. the root password). You can manage that via the `secrets` chart.

---

## 📦 Chart Structure

This chart includes:

- A PostgreSQL Deployment with custom configuration
- Persistent Volume and Persistent Volume Claim
- ConfigMap with `postgresql.conf` and `pg_hba.conf`
- A ClusterIP Service
- An init container to handle one-time initialisation

---

## 🔐 Prerequisite: Secrets

Before deploying this chart, you must install the `secrets` chart to create the required secret:

```bash
helm upgrade --install secrets ./helm/secrets \
  --set secrets.postgresRootPassword=myStrongPassword
```

This creates a Secret called petunia with the key postgres-root-password.

## 🚀 Install / Upgrade

```bash
helm upgrade --install postgres ./helm/postgres
```

## 🔄 Uninstall

```bash
helm uninstall postgres
```

kubectl delete pvc postgres-data
kubectl delete pv postgres-data

## 🧪 Testing & Validation

Bevor du das Chart installierst, kannst du es lokal überprüfen und dir anschauen, was Kubernetes daraus macht.

### 🔍 Lint the chart

```bash
helm lint ./helm/postgres
```

### 🧱 Template rendering

```bash
helm template postgres ./helm/postgres
```

## 🧼 Clean-up

```bash
helm uninstall postgres
helm uninstall secrets
kubectl delete pvc postgres-data
kubectl delete pv postgres-data
```

## 💬 Contact

This chart is part of Project Petunia, a playground for distributed systems, 
microservices, and containerised applications.

Felix Roske <felix@roskenet.de>
