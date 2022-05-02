package com.bootcamp.microservicemeetup.repository;


import com.bootcamp.microservicemeetup.model.entity.Meetup;
import com.bootcamp.microservicemeetup.model.entity.Meetup;
import com.bootcamp.microservicemeetup.model.entity.Registration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MeetupRepository repository;


    @Test
    @DisplayName("Should get a meetup by id")
    public void findMeetupByIdTest() {

        //cenario
        Meetup meetup = createNewMeetup("Java Back-end");
        entityManager.persist(meetup);

        //execução
        Optional<Meetup> foundMeetup = repository
                .findById(meetup.getId());

        //assert
        assertThat(foundMeetup.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Should save a meetup")
    public void saveMeetupTest() {

        Meetup meetup = createNewMeetup("Java Back-end");

        Meetup savedMeetup = repository.save(meetup);

        assertThat(savedMeetup.getId()).isNotNull();

    }


    public static Meetup createNewMeetup(String meetup) {
        return Meetup.builder()
                .event(meetup)
                .meetupDate("28/04/22")
                .build();
    }

}
