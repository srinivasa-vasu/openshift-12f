package com.redhat.sb;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@EnableDiscoveryClient
public class ReservationServiceApplication implements CommandLineRunner {

    private static final Logger LOG = Logger.getLogger(ReservationServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

    private final ReservationRepository reservationRepository;

    public ReservationServiceApplication(@Autowired ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of("Emirates,SIA,Qantas,AI,KLM,JetBlue".split(","))
                .forEach(name -> reservationRepository.save(new Reservation(name)));
        reservationRepository.findAll().forEach(element -> LOG.info(element.getReservationName()));

    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @RestResource(path = "/name")
    List<Reservation> findByReservationName(@Param("name") String rn);

    @RestResource(path = "/id")
    List<Reservation> findById(@Param("id") Long id);
}


@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private String reservationName;

    public Reservation() { // for JPA
    }

    public Reservation(String name) {
        this.reservationName = name;
    }

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

