Spring Cloud Registry Server on OpenShift 
-----------------------------------------
![alt text](https://blog.openshift.com/wp-content/uploads/Logotype_RH_OpenShiftContainerPlatform_wLogo_CMYK_Black-1024x263.jpg "OCP")

### Spring cloud registry server can be deployed in many ways, have listed down 3 most common ways:
***

## Option 1: 

1. Create a new-project in OCP

> oc new-project 12f

2. Run config server template yaml file which would create *ImageStream*, *BuildConfig*, *DeploymentConfig*, *Service* and *Route* in sequence

> oc process -f EurekaServiceTemplate.yml | oc create -f -

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

2. Navigate to eureka-service directory

3. Login to OCP and run the following commands,

Create 12f project and install config-service

> oc new-build --name=eureka-service -l app=config-service fis-java-openshift:2.0~.

> oc start-build eureka-service --from-dir=. --follow

> oc new-app eureka-service -l app=config-service -e config_service=http://config-service

> oc expose svc/eureka-service
