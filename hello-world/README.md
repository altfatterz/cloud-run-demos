

Switch to project and set some config:

```bash
$ gcloud config set project cloud-run-demos-306217
$ gcloud config set run/region europe-west6
$ gcloud config set run/platform managed 
```

Enable Services:

```bash
$ gcloud services enable run.googleapis.com
$ gcloud services enable cloudbuild.googleapis.com
$ gcloud services enable eventarc.googleapis.com
$ gcloud services enable cloudbuild.googleapis.com
$ gcloud services enable logging.googleapis.com
$ gcloud services enable artifactregistry.googleapis.com
````

```bash
$ gcloud run services list
   SERVICE                     REGION        URL                                                         LAST DEPLOYED BY                       LAST DEPLOYED AT
✔  my-first-cloud-run-service  europe-west6  https://my-first-cloud-run-service-edbd2pdvbq-oa.a.run.app  zoltan.altfatter@cloudnativecoach.com  2021-02-28T17:54:53.133355Z
```

```bash
$ gcloud run revisions list
   REVISION                              ACTIVE  SERVICE                     DEPLOYED                 DEPLOYED BY
✔  my-first-cloud-run-service-00001-ruc  yes     my-first-cloud-run-service  2021-02-28 17:53:17 UTC  zoltan.altfatter@cloudnativecoach.com
```

Build the image and test locally

```bash
$ mvn clean package jib:dockerBuild
$ docker run -p 8080:8080 gcr.io/cloud-run-demos-306217/hello-world -d
$ http :8080/
Hello World!
```

Deploy the image to GCR

```bash
$ mvn jib:build
```

```bash
$ gcloud run deploy hello-world --image gcr.io/cloud-run-demos-306217/hello-world --allow-unauthenticated
```

```bash
Deploying container to Cloud Run service [hello-world] in project [cloud-run-demos-306217] region [europe-west6]
✓ Deploying... Done.
  ✓ Creating Revision...
  ✓ Routing traffic...
  ✓ Setting IAM Policy...
Done.
Service [hello-world] revision [hello-world-00001-zul] has been deployed and is serving 100 percent of traffic.
Service URL: https://hello-world-edbd2pdvbq-oa.a.run.app
```

```bash
$ curl https://hello-world-edbd2pdvbq-oa.a.run.app
Hello World!!
```


Resources:

Trigger Cloud Run with events from more than 60 Google Cloud sources:
https://cloud.google.com/blog/products/serverless/build-event-driven-applications-in-cloud-run

https://cloudevents.io/

