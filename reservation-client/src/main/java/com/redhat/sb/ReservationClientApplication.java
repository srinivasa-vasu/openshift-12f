package com.redhat.sb;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableZuulProxy
@EnableFeignClients
public class ReservationClientApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);

    }

    @Value("${peer.service.name}")
    private String serviceName;

    @Bean
    CommandLineRunner runner(DiscoveryClient client) {
        return args -> client.getInstances(serviceName).
                forEach(element -> LOG.info(String.format("%s %s:%s", element.getServiceId(), element.getHost(), element.getPort())));
    }
}

@RestController
@RequestMapping("/reservations")
class ReservationApiGatewayRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationApiGatewayRestController.class);

    private ReservationService reservationService;

    public ReservationApiGatewayRestController(@Autowired ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Collection<String> getReservationNamesFallback() {
        return Arrays.asList("Fallback: NA-AIR");
    }

    @RequestMapping("/names")
    @HystrixCommand(fallbackMethod = "getReservationNamesFallback")
    public Collection<String> getReservationNames() {

        LOG.info("Get Reservation Names");

        return reservationService.airlines().getContent().stream().map(Reservation::getReservationName).collect(Collectors.toList());

    }

}

@FeignClient("reservation-service")
interface ReservationService {

    @RequestMapping(method = RequestMethod.GET, value = "/reservations/")
    Resources<Reservation> airlines();

}

class Reservation {

    private Long id;
    private String reservationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }
}

@RestController
class HealthController {

    @RequestMapping(method = RequestMethod.GET, value = "/healthz")
    public String health() {
        return "I'm OK";
    }
}
