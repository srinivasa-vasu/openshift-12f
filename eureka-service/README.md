Spring Cloud Registry Service on OpenShift <sup>![Build Status](https://travis-ci.org/srinivasa-vasu/openshift-12f.svg?branch=master)</sup>
-----------------------------------------
![OCP](https://blog.openshift.com/wp-content/uploads/Logotype_RH_OpenShiftContainerPlatform_wLogo_CMYK_Black-1024x263.jpg "OCP")

### Spring cloud registry service can be deployed in many ways, have listed down 3 most common ways:
***

## Option 1: 

1. Create a new-project (12f) in OCP, and install [config-service](https://github.com/srinivasa-vasu/openshift12f/tree/master/config-service)

> oc project 12f

2. Run registry server template yaml file which would create *ImageStream*, *BuildConfig*, *DeploymentConfig*, *Service* and *Route* in sequence

> oc process -f EurekaServiceTemplate.yml | oc create -f -

## Option 2:

Instead of running the template, OC objects can be created in sequence using the individual yaml files

1. Create a new-project (12f) in OCP, and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service)

> oc project 12f

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

4. Create 12f project and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service)

> oc new-build --name=eureka-service -l app=eureka-service fis-java-openshift:2.0~.

> oc start-build eureka-service --from-dir=. --follow

> oc new-app eureka-service -l app=eureka-service -e config_service=http://config-service

> oc patch dc/eureka-service -p '{"spec":{"template":{"spec":{"containers":[{"name":"eureka-service","ports":[{"name":"app","containerPort":"8761","protocol":"TCP"}]}]}}}}'

> oc patch svc/eureka-service -p '{"spec":{"ports":[{"name":"app","protocol":"tcp","port":"80","targetPort":8761}]}}'

> oc expose svc/eureka-service --port=8761

