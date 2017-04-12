Spring Cloud Config Server on OpenShift 
-----------------------------------------
![alt text](https://blog.openshift.com/wp-content/uploads/Logotype_RH_OpenShiftContainerPlatform_wLogo_CMYK_Black-1024x263.jpg "OCP")

### Spring cloud server can be deployed in many ways, have listed down 3 most common ways:
***

## Option 1: 

1. Create a new-project in OCP

> oc new-project 12f

2. Run config server template yaml file which would create *ImageStream*, *BuildConfig*, *DeploymentConfig*, *Service* and *Route* in sequence

> oc process -f ConfigServerTemplate.yml | oc create -f -

## Option 2:

Instead of running the template, OC objects can be created in sequence using the individual yaml files

> oc new-project 12f

> oc create -f ImageStream.yml

> oc create -f BuildConfig.yml

> oc create -f DeploymentConfig.yml

> oc create -f Service.yml

> oc create -f Route.yml

## Option 3:

This is more about using OC CLI to create the individual components

1. Check out the repo to the local file system

2. Navigate to config-service directory

3. Login to OCP and run the following commands,

> oc new-project 12f

> oc new-build --name=config-service -l app=config-service fis-java-openshift:2.0~.

> oc start-build config-service --from-dir=. --follow

> oc new-app config-service -l app=config-service

> oc expose svc/config-service
