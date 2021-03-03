Build the image and test locally

```bash
$ mvn clean package jib:dockerBuild
$ docker run -p 8080:8080 gcr.io/cloud-run-demos-306217/hello-world-event -d
$ http post :8080
```

Cloud Build

```bash
$ gcloud builds submit --tag gcr.io/$(gcloud config get-value project)/hello-world-event
```

```bash
$ gcloud builds list
```

List the container images

```bash
$ gcloud container images list
```

Deploy 

```bash
$ gcloud run deploy hello-world-event \
    --image gcr.io/$(gcloud config get-value project)/hello-world-event \
    --allow-unauthenticated
```

```bash
$ http post https://hello-world-event-edbd2pdvbq-ez.a.run.app
```

Setup:

1. Grant the iam.serviceAccountTokenCreator role to the Pub/Sub service account.
2. Grant the eventarc.eventReceiver role to the Compute Engine service account.

```bash
$ export PROJECT_NUMBER="$(gcloud projects describe $(gcloud config get-value project) --format='value(projectNumber)')"
$ gcloud projects add-iam-policy-binding $(gcloud config get-value project) \
    --member="serviceAccount:service-${PROJECT_NUMBER}@gcp-sa-pubsub.iam.gserviceaccount.com"\
    --role='roles/iam.serviceAccountTokenCreator'
$ gcloud projects add-iam-policy-binding $(gcloud config get-value project) \
    --member=serviceAccount:${PROJECT_NUMBER}-compute@developer.gserviceaccount.com \
    --role='roles/eventarc.eventReceiver'      
```

Create an Eventarc trigger

```bash
$ gcloud config set eventarc/location europe-west4
```

```bash
$ gcloud eventarc triggers create hello-world-event-trigger \
--destination-run-service=hello-world-event \
--destination-run-region=europe-west4 \
--event-filters="type=google.cloud.audit.log.v1.written" \
--event-filters="serviceName=storage.googleapis.com" \
--event-filters="methodName=storage.objects.create" \
--service-account=${PROJECT_NUMBER}-compute@developer.gserviceaccount.com
```

Note: Although your trigger will be created immediately, it can take up to 10 minutes for triggers to be fully functional.

```bash
$ gcloud eventarc triggers list
NAME                       TYPE                               DESTINATION_RUN_SERVICE  DESTINATION_RUN_PATH  ACTIVE
hello-world-event-trigger  google.cloud.audit.log.v1.written  hello-world-event                              By 21:46:00
```

Create a  bucket:

```bash
$ gsutil mb -l europe-west4 gs://hello-world-event-$(gcloud config get-value project)/
```

```bash
$ echo "Hello World" > random.txt
$ gsutil cp random.txt gs://hello-world-event-$(gcloud config get-value project)/random.txt
```

Check the logs:

```bash
$ gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=hello-world-event" | grep Detect
textPayload: 'Detected change in Cloud Storage bucket: storage.googleapis.com/projects/_/buckets/hello-world-event-cloud-run-demos-306217/objects/random.txt'
```

To delete the trigger:

```bash
$ gcloud eventarc triggers delete hello-world-event-trigger
```