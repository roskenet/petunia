# Pitfalls

export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

kubectl create namespace ingress-nginx

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm install nginx-ingress ingress-nginx/ingress-nginx \
  --namespace ingress-nginx \
  --set controller.publishService.enabled=true

kubectl get all -n ingress-nginx

kubectl apply -f https://github.com/cert-manager/cert-manager/releases/latest/download/cert-manager.crds.yaml

# helm upgrade cert-manager jetstack/cert-manager \
#   --namespace cert-manager \
#   --reuse-values \
#   --version v1.18.2 \
#   --set installCRDs=false \
#   --set "extraArgs={--acme-http01-ingress-path-type=ImplementationSpecific}"

Das hier funktioniert:

  helm upgrade cert-manager jetstack/cert-manager   --set installCRDs=false --namespace cert-manager   -f values.yaml

Die values.yaml:


```
installCRDs: true 
 
config: 
  featureGates: 
    ACMEHTTP01IngressPathTypeExact: false 
```
