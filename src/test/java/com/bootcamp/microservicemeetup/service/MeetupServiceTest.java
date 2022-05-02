package com.bootcamp.microservicemeetup.service;


import com.bootcamp.microservicemeetup.exception.BusinessException;
import com.bootcamp.microservicemeetup.model.entity.Meetup;
import com.bootcamp.microservicemeetup.model.entity.Registration;
import com.bootcamp.microservicemeetup.repository.MeetupRepository;
import com.bootcamp.microservicemeetup.service.impl.MeetupServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/* copiei as anotações das outras classes de teste */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MeetupServiceTest {

    MeetupService meetupService;

    /* RegistrationService mockou o RegistrationRepository
       além, disso, o Service só conversa com o Repository
     */
    @MockBean
    MeetupRepository repository;

    /* a classe MeetupService é uma inteface, entendo que a sua classe de
        implementação deva ser instanciada sempre que necessário.
     */
    @BeforeEach
    public void setup(){
        this.meetupService = new MeetupServiceImpl(repository);
    }


    @Test
    @DisplayName("should save a meetup")
    public void saveMeetupTest(){

        //cenario, criando um meetup, sendo que o método é criado posteriormente
        //cenário, Registration user p/ ser usado no assert, comparando se a pessoa cadastrada no meetup é ela mesma
        Meetup meetup = createValidMeetup();
        Registration user = createValidRegistration();

        //execução, quando o repository salvar o meetup criado
        //execução, o método existsByMeetup foi criado para saber se o Meetup existe. Se ele for falso, salvar
        Mockito.when(repository.existsByRegistration(user)).thenReturn(false);
        Mockito.when(repository.save(meetup)).thenReturn(createValidMeetup());

        Meetup savedMeetup = meetupService.save(meetup);

        // assert
        assertThat(savedMeetup.getId()).isEqualTo(101);
        assertThat(savedMeetup.getEvent()).isEqualTo("Java Back-end");
        assertThat(savedMeetup.getRegistration()).isEqualTo(user);
        assertThat(savedMeetup.getMeetupDate()).isEqualTo("17/05/22");
        assertThat(savedMeetup.getIsRegistered()).isEqualTo(true);
    }


    @Test
    @DisplayName("Should throw business error when try " +
            "to save a new meetup with a duplicated one")
    public void shouldNotSaveAsMeetupDuplicatedTest(){

        Meetup meetup = createValidMeetup();
        Registration user = createValidRegistration();

        Mockito.when(repository.existsByRegistration(user)).thenReturn(true);
        Mockito.when(repository.save(meetup)).thenReturn(createValidMeetup());

        Throwable exception = Assertions.catchThrowable( () -> meetupService.save(meetup));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Meetup already created");

        Mockito.verify(repository, Mockito.never()).save(meetup);

    }



    @Test
    @DisplayName("Should get an Meetup by Id")
    public void getMeetupByIdTest() {

        // cenario
        Integer id = 11;
        Meetup meetup = createValidMeetup();
        meetup.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(meetup));


        // execucao
        Optional<Meetup> foundMeetup = meetupService.getById(id);

        assertThat(foundMeetup.isPresent()).isTrue();
        assertThat(foundMeetup.get().getId()).isEqualTo(id);
        assertThat(foundMeetup.get().getEvent()).isEqualTo(meetup.getEvent());
        assertThat(foundMeetup.get().getRegistration()).isEqualTo(meetup.getRegistration());
        assertThat(foundMeetup.get().getMeetupDate()).isEqualTo(meetup.getMeetupDate());
        assertThat(foundMeetup.get().getIsRegistered()).isEqualTo(meetup.getIsRegistered());

    }


    @Test
    @DisplayName("Should update a meetup")
    public void updateMeetupTest() {

        // cenario
        Integer id = 11;
        Meetup updatingMeetup = Meetup.builder().id(11).build();

        // execucao
        Meetup updatedMeetup = createValidMeetup();
        updatedMeetup.setId(id);

        Mockito.when(repository.save(updatingMeetup)).thenReturn(updatedMeetup);
        Meetup meetup = meetupService.update(updatingMeetup);

        // assert
        assertThat(meetup.getId()).isEqualTo(updatedMeetup.getId());
        assertThat(meetup.getEvent()).isEqualTo(updatedMeetup.getEvent());
        assertThat(meetup.getRegistration()).isEqualTo(updatedMeetup.getRegistration());
        assertThat(meetup.getMeetupDate()).isEqualTo(updatedMeetup.getMeetupDate());
        assertThat(meetup.getIsRegistered()).isEqualTo(updatedMeetup.getIsRegistered());
    }

        //método criado conforme linha 50, pois é preciso instanciar um meetup para poder salva-lo
    private Meetup createValidMeetup() {
        return Meetup.builder()
                .id(101)
                .event("Java Back-end")
                .registration(createValidRegistration())
                .meetupDate("17/05/22")
                .isRegistered(true)
                .build();
    }

    //copiamos este método do RegistrationService, pois ele cria um registro e precisávamos disso no Meetup
    //vide linha 51
    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101)
                .name("Maisa Matos")
                .dateOfRegistration("01/04/2022")
                .registration("001")
                .build();
    }





}
