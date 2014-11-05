package fr.univrouen.telephonie.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import fr.univrouen.telephonie.Application;
import fr.univrouen.telephonie.domain.Telephone;
import fr.univrouen.telephonie.repository.TelephoneRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TelephoneResource REST controller.
 *
 * @see TelephoneResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TelephoneResourceTest {

    private static final Long DEFAULT_NUMERO = 0L;
    private static final Long UPDATED_NUMERO = 1L;
    
    private static final String DEFAULT_PROPRIETAIRE = "SAMPLE_TEXT";
    private static final String UPDATED_PROPRIETAIRE = "UPDATED_TEXT";
    
    private static final Integer DEFAULT_PIN = 0;
    private static final Integer UPDATED_PIN = 1;
    

   @Inject
   private TelephoneRepository telephoneRepository;

   private MockMvc restTelephoneMockMvc;

   private Telephone telephone;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelephoneResource telephoneResource = new TelephoneResource();
        ReflectionTestUtils.setField(telephoneResource, "telephoneRepository", telephoneRepository);
        this.restTelephoneMockMvc = MockMvcBuilders.standaloneSetup(telephoneResource).build();
    }

    @Before
    public void initTest() {
        telephone = new Telephone();
        telephone.setNumero(DEFAULT_NUMERO);
        telephone.setProprietaire(DEFAULT_PROPRIETAIRE);
        telephone.setPin(DEFAULT_PIN);
    }

    @Test
    @Transactional
    public void createTelephone() throws Exception {
        // Validate the database is empty
        assertThat(telephoneRepository.findAll()).hasSize(0);

        // Create the Telephone
        restTelephoneMockMvc.perform(post("/app/rest/telephones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telephone)))
                .andExpect(status().isOk());

        // Validate the Telephone in the database
        List<Telephone> telephones = telephoneRepository.findAll();
        assertThat(telephones).hasSize(1);
        Telephone testTelephone = telephones.iterator().next();
        assertThat(testTelephone.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testTelephone.getProprietaire()).isEqualTo(DEFAULT_PROPRIETAIRE);
        assertThat(testTelephone.getPin()).isEqualTo(DEFAULT_PIN);;
    }

    @Test
    @Transactional
    public void getAllTelephones() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

        // Get all the telephones
        restTelephoneMockMvc.perform(get("/app/rest/telephones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(telephone.getId().intValue()))
                .andExpect(jsonPath("$.[0].numero").value(DEFAULT_NUMERO.intValue()))
                .andExpect(jsonPath("$.[0].proprietaire").value(DEFAULT_PROPRIETAIRE.toString()))
                .andExpect(jsonPath("$.[0].pin").value(DEFAULT_PIN));
    }

    @Test
    @Transactional
    public void getTelephone() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

        // Get the telephone
        restTelephoneMockMvc.perform(get("/app/rest/telephones/{id}", telephone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telephone.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.intValue()))
            .andExpect(jsonPath("$.proprietaire").value(DEFAULT_PROPRIETAIRE.toString()))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN));
    }

    @Test
    @Transactional
    public void getNonExistingTelephone() throws Exception {
        // Get the telephone
        restTelephoneMockMvc.perform(get("/app/rest/telephones/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelephone() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

        // Update the telephone
        telephone.setNumero(UPDATED_NUMERO);
        telephone.setProprietaire(UPDATED_PROPRIETAIRE);
        telephone.setPin(UPDATED_PIN);
        restTelephoneMockMvc.perform(post("/app/rest/telephones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telephone)))
                .andExpect(status().isOk());

        // Validate the Telephone in the database
        List<Telephone> telephones = telephoneRepository.findAll();
        assertThat(telephones).hasSize(1);
        Telephone testTelephone = telephones.iterator().next();
        assertThat(testTelephone.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTelephone.getProprietaire()).isEqualTo(UPDATED_PROPRIETAIRE);
        assertThat(testTelephone.getPin()).isEqualTo(UPDATED_PIN);;
    }

    @Test
    @Transactional
    public void deleteTelephone() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

        // Get the telephone
        restTelephoneMockMvc.perform(delete("/app/rest/telephones/{id}", telephone.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Telephone> telephones = telephoneRepository.findAll();
        assertThat(telephones).hasSize(0);
    }
}
