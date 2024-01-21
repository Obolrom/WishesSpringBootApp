Steps to run the application in local k8s cluster

1. apply postgres-config.yaml
2. apply postgres-pvc-pv.yaml
3. apply postgres-deployment.yaml
4. apply postgres-service.yaml
5. apply expenses-api-depl.yaml
6. apply expenses-ingress.yaml (before enable ingress addon in minikube)