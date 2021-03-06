Spring Boot microservice on OpenShift <sup>![Build Status](https://travis-ci.org/srinivasa-vasu/openshift-12f.svg?branch=master)</sup>
-----------------------------------------
![OCP](https://blog.openshift.com/wp-content/uploads/Logotype_RH_OpenShiftContainerPlatform_wLogo_CMYK_Black-1024x263.jpg "OCP")

### Spring boot micro service (leveraging msa libraries like eureka, config, hystrix, zuul, feign and zipkin). 
It uses zuul proxy and feign to interact with other micro services. Requests are proxied through zuul and feign abstracts the REST calls 
to other services. Zuul uses ribbon for load balancing and services are discovered through eureka. It can be deployed in many ways, have 
listed down 3 most common ways:
***

## Option 1: 

1. Create a new-project (12f) in OCP, and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service), [eureka-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/eureka-service) and [zipkin-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/zipkin-service)

> oc project 12f

2. Run registry server template yaml file which would create *ImageStream*, *BuildConfig*, *DeploymentConfig*, *Service* and *Route* in sequence

> oc process -f ReservationServiceAPITemplate.yml | oc create -f -

## Option 2:

Instead of running the template, OC objects can be created in sequence using the individual yaml files

1. Create a new-project (12f) in OCP, and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service), [eureka-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/eureka-service) and [zipkin-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/zipkin-service)

> oc project 12f

> oc create -f ImageStream.yml

> oc create -f BuildConfig.yml

> oc create -f DeploymentConfig.yml

> oc create -f Service.yml

> oc create -f Route.yml

## Option 3:

This is more about using OC CLI to create the individual components

1. Check out the repo to the local file system

2. Navigate to reservation-service directory

3. Login to OCP and run the following commands,

4. Create 12f project and install and install [config-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/config-service), [eureka-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/eureka-service) and [zipkin-service](https://github.com/srinivasa-vasu/openshift-12f/tree/master/zipkin-service)

> oc new-build --name=reservation-client -l app=reservation-client fis-java-openshift:2.0~.

> oc start-build reservation-client --from-dir=. --follow

> oc new-app reservation-client -l app=reservation-client -e config_service=http://config-service -e 
eureka_service=http://eureka-service/eureka

> oc patch dc/reservation-client -p '{"spec":{"template":{"spec":{"containers":[{"name":"reservation-client","ports":[{"name":"app",
"containerPort":"9999","protocol":"TCP"}]}]}}}}'

> oc patch svc/reservation-client -p '{"spec":{"ports":[{"name":"app","protocol":"tcp","port":"80","targetPort":9999}]}}'

> oc expose svc/reservation-client --port=9999

