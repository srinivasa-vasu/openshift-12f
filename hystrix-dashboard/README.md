Spring Boot Hystrix dashboard on OpenShift <sup>![Build Status](https://travis-ci.org/srinivasa-vasu/openshift-12f.svg?branch=master)</sup>
-----------------------------------------
![OCP](https://blog.openshift.com/wp-content/uploads/Logotype_RH_OpenShiftContainerPlatform_wLogo_CMYK_Black-1024x263.jpg "OCP")

### Spring boot Hystrix dashbaord can be deployed in many ways, 
have listed down 3 most common ways:
***

## Option 1: 

1. Create a new-project (12f) in OCP, and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service) and [eureka-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/eureka-service)

> oc project 12f

2. Run registry server template yaml file which would create *ImageStream*, *BuildConfig*, *DeploymentConfig*, *Service* and *Route* in sequence

> oc process -f ZipkinServiceTemplate.yml | oc create -f -

## Option 2:

Instead of running the template, OC objects can be created in sequence using the individual yaml files

1. Create a new-project (12f) in OCP, and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service) and [eureka-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/eureka-service)

> oc project 12f

> oc create -f ImageStream.yml

> oc create -f BuildConfig.yml

> oc create -f DeploymentConfig.yml

> oc create -f Service.yml

> oc create -f Route.yml

## Option 3:

This is more about using OC CLI to create the individual components

1. Check out the repo to the local file system

2. Navigate to zipkin-service directory

3. Login to OCP and run the following commands,

4. Create 12f project and install and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service) and [eureka-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/eureka-service)

> oc new-build --name=zipkin-service -l app=zipkin-service fis-java-openshift:2.0~.

> oc start-build zipkin-service --from-dir=. --follow

> oc new-app zipkin-service -l app=zipkin-service -e config_service=http://config-service -e 
eureka_service=http://eureka-service/eureka

> oc patch dc/zipkin-service -p '{"spec":{"template":{"spec":{"containers":[{"name":"zipkin-service","ports":[{"name":"app",
"containerPort":"9411","protocol":"TCP"}]}]}}}}'

> oc patch svc/zipkin-service -p '{"spec":{"ports":[{"name":"app","protocol":"tcp","port":"80","targetPort":9411}]}}'

> oc expose svc/zipkin-service --port=9411

