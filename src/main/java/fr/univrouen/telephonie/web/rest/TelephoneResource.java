package fr.univrouen.telephonie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.univrouen.telephonie.domain.Telephone;
import fr.univrouen.telephonie.repository.TelephoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Telephone.
 */
@RestController
@RequestMapping("/app")
public class TelephoneResource {

    private final Logger log = LoggerFactory.getLogger(TelephoneResource.class);

    @Inject
    private TelephoneRepository telephoneRepository;

    /**
     * POST  /rest/telephones -> Create a new telephone.
     */
    @RequestMapping(value = "/rest/telephones",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Telephone telephone) {
        log.debug("REST request to save Telephone : {}", telephone);
        telephoneRepository.save(telephone);
    }

    /**
     * GET  /rest/telephones -> get all the telephones.
     */
    @RequestMapping(value = "/rest/telephones",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Telephone> getAll() {
        log.debug("REST request to get all Telephones");
        return telephoneRepository.findAll();
    }

    /**
     * GET  /rest/telephones/:id -> get the "id" telephone.
     */
    @RequestMapping(value = "/rest/telephones/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> get(@PathVariable Long id) {
        log.debug("REST request to get Telephone : {}", id);
        return Optional.ofNullable(telephoneRepository.findOne(id))
            .map(telephone -> new ResponseEntity<>(
                telephone,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/telephones/:id -> delete the "id" telephone.
     */
    @RequestMapping(value = "/rest/telephones/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Telephone : {}", id);
        telephoneRepository.delete(id);
    }
}
