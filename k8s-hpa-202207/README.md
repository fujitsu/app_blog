# MicroProfile Metricsを使ったオートスケール

HorizontalPodAutoscalerとPrometheus Adapterを使い、
MicroProfile Metricsから得られるアプリケーション固有メトリクスに基づいた
オートスケールの使用例。


## アプリケーションの作成とディプロイ

### 準備
Launcherを```https://github.com/fujitsu/launcher/releases```からダウンロードする。
以下の例では、launcher-4.0.jarを使用している。

### アプリケーションのビルド

```
$ cd app
$ mvn package
$ cd ..
$ java -jar launcher-4.0.jar --deploy app/target/hpa-0.1.war --generate hpa-uber.jar
$ docker build -t hpa-demo -f docker/Dockerfile .
$ kubectl apply -f docker/hpademo.yml
```

### アプリケーションの実行

```
$ curl http://***.***.***.***:30080/hello/inc
$ curl http://***.***.***.***:30080/hello/dec
```
(***は、環境に応じてものに読み替える。)


## Prometheusのインストール

```
$ kubectl create ns prom
$ helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
$ helm install prsrv prometheus-community/prometheus -n prom
$ helm install pradp prometheus-community/prometheus-adapter -n prom
$ helm upgrade pradp prometheus-community/prometheus-adapter -n prom -f yaml/external-metrics.yaml
```

インストールが成功すると、External APIにアクセスできるようになる。

```
$ kubectl get --raw /apis/external.metrics.k8s.io/v1beta1
{"kind":"APIResourceList","apiVersion":"v1","groupVersion":"external.metrics.k8s.io/v1beta1","resources":[{"name":"hpa_demo_metric","singularName":"","namespaced":true,"kind":"ExternalMetricValueList","verbs":["get"]}]}

$ kubectl get --raw "/apis/external.metrics.k8s.io/v1beta1/namespaces/default/hpa_demo_metric"
{"kind":"ExternalMetricValueList","apiVersion":"external.metrics.k8s.io/v1beta1","metadata":{},"items":[{"metricName":"hpa_demo_metric","metricLabels":{},"timestamp":"2022-06-23T01:57:02Z","value":"2"}]}
```

## HPAの設定と確認

```
$ kubectl apply -f yaml/hpa.yml
```

スケールの様子は、```kubectl get hpa```で確認できる。  
アプリケーションに何度かアクセスすると、```TARGERTS```の値が変わるとともに、
```REPLICAS```の値が変わり、Pod数も増減することが確認できる。

```
$ kubectl get hpa
NAME        REFERENCE              TARGETS       MINPODS   MAXPODS   REPLICAS   AGE
hpa-demo   Deployment/hpa-app   <unknown>/5   1         3         0          5s

$ kubectl get hpa
NAME        REFERENCE              TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
hpa-demo   Deployment/hpa-app   2/5       1         3         1          37s

$ kubectl get hpa
NAME        REFERENCE              TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
hpa-demo   Deployment/hpa-app   6/5       1         3         3          7m36s

$ kubectl get pod
NAME                         READY   STATUS    RESTARTS   AGE
hpa-app-7b458c7547-jgrxx   1/1     Running   0          110s
hpa-app-7b458c7547-r6tr4   1/1     Running   0          103m
hpa-app-7b458c7547-txlwv   1/1     Running   0          95s
```
