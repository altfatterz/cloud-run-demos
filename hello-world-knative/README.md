
Use Kubernetes 1 node cluster from Docker For Mac 

Install Knative: 

https://knative.dev/docs/install/any-kubernetes-cluster/

```bash
$ kubectl --namespace istio-system get service istio-ingressgateway
NAME                   TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)                                                                      AGE
istio-ingressgateway   LoadBalancer   10.106.234.179   localhost     15021:31545/TCP,80:31111/TCP,443:30353/TCP,15012:31116/TCP,15443:32485/TCP   25s
```

```bash
$ kubctl get pods --all-namespaces

istio-system      istio-ingressgateway-7f6b78d5b7-bknw2    1/1     Running     1          23m
istio-system      istiod-7fcb569c74-2qzmz                  1/1     Running     1          22m
istio-system      istiod-7fcb569c74-7nhdm                  1/1     Running     1          23m
istio-system      istiod-7fcb569c74-sh9rc                  1/1     Running     1          22m
knative-serving   activator-86956bbd6f-5cb6b               1/1     Running     2          24m
knative-serving   autoscaler-54cbd576f6-dzwhc              1/1     Running     2          24m
knative-serving   controller-79c9cccd6f-c5hgs              1/1     Running     2          24m
knative-serving   default-domain-fd975                     0/1     Completed   0          22m
knative-serving   istio-webhook-56748b47-5bbmj             1/1     Running     2          23m
knative-serving   networking-istio-5db557d5c4-cccpq        1/1     Running     2          23m
knative-serving   webhook-5fd484cf4-ckqtx                  1/1     Running     3          24m
kube-system       coredns-f9fd979d6-hxhgz                  1/1     Running     2          29m
kube-system       coredns-f9fd979d6-j2bt4                  1/1     Running     2          29m
kube-system       etcd-docker-desktop                      1/1     Running     2          27m
kube-system       kube-apiserver-docker-desktop            1/1     Running     3          28m
kube-system       kube-controller-manager-docker-desktop   1/1     Running     3          28m
kube-system       kube-proxy-457st                         1/1     Running     2          29m
kube-system       kube-scheduler-docker-desktop            1/1     Running     2          28m
kube-system       storage-provisioner                      1/1     Running     3          27m
kube-system       vpnkit-controller                        1/1     Running     2          27m
```

Build image:
```bash
$ mvn clean package
```

Login to Dockerhub
```bash
$ docker login
```

Push the image
```bah
$ docker push altfatterz/hello-world-knative:0.0.1-SNAPSHOT
```

Create service:

```bash
$ kn service create hello-world-knative --image=docker.io/altfatterz/hello-world-knative:0.0.1-SNAPSHOT --env TARGET="Knative"
```

Test service:
```bash
$ curl http://hello-world-knative.default.127.0.0.1.xip.io
```

View service and revisions
```bash
$ kn services list
$ kn revisions list
```



View Knative Services:

```bash
$ kubectl get ksvc
NAME                  URL                                                   LATESTCREATED               LATESTREADY                 READY   REASON
hello-world-knative   http://hello-world-knative.default.127.0.0.1.xip.io   hello-world-knative-00002   hello-world-knative-00002   True
```

View Kubernetes Services:

```bash
$ kubectl get svc
```

Update Knative Service:

```bash
$ kn service update hello-world-knative --image=docker.io/altfatterz/hello-world-knative:0.0.1-SNAPSHOT --env TARGET="Knative community"
```




Resources:
* https://knative.dev/docs/install/any-kubernetes-cluster/
* https://knative.dev/blog/1/01/01/how-to-set-up-a-local-knative-environment-with-kind-and-without-dns-headaches/