package guru.springframework.sdjpaintro;

import guru.springframework.sdjpaintro.domain.AuthorUuid;
import guru.springframework.sdjpaintro.domain.BookNatural;
import guru.springframework.sdjpaintro.domain.BookUuid;
import guru.springframework.sdjpaintro.domain.composite.AuthorComposite;
import guru.springframework.sdjpaintro.domain.composite.AuthorEmbedded;
import guru.springframework.sdjpaintro.domain.composite.NameId;
import guru.springframework.sdjpaintro.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles({"local"})
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.sdjpaintro.bootstrap"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MySQLIntegrationTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorUuidRepository authorUuidRepository;
    @Autowired
    BookUuidRepository bookUuidRepository;
    @Autowired
    BookNaturalRepository bookNaturalRepository;
    @Autowired
    AuthorCompositeRepository authorCompositeRepository;
    @Autowired
    AuthorEmbeddedRepository authorEmbeddedRepository;

    @Test
    void authorEmbeddedTest() {
        NameId nameId = new NameId("TestNEmb", "TeatLNEmb");
        AuthorEmbedded authorEmbedded = new AuthorEmbedded(nameId);
        authorEmbedded.setCountry("US");

        AuthorEmbedded saved = authorEmbeddedRepository.save(authorEmbedded);

        assertThat(saved).isNotNull();
        assertThat(authorEmbeddedRepository.getById(nameId)).isNotNull();
    }

    @Test
    void authorCompositeTest() {
        NameId nameId = new NameId("TestN", "TeatLN");
        AuthorComposite authorComposite = new AuthorComposite();
        authorComposite.setFirstName(nameId.getFirstName());
        authorComposite.setLastName(nameId.getLastName());
        authorComposite.setCountry("US");

        AuthorComposite saved = authorCompositeRepository.save(authorComposite);

        assertThat(saved).isNotNull();
        assertThat(authorCompositeRepository.getById(nameId)).isNotNull();
    }

    @Test
    void testBookNatural() {
        BookNatural bookNatural = new BookNatural();
        bookNatural.setTitle("Test Book");
        BookNatural bookSaved = bookNaturalRepository.save(bookNatural);

        assertThat(bookNaturalRepository.getById(bookNatural.getTitle())).isNotNull();
    }

    @Test
    void testBookUuid() {
        BookUuid savedBook = bookUuidRepository.save(new BookUuid());
        assertThat(bookUuidRepository.getById(savedBook.getId())).isNotNull();
    }

    @Test
    void testAuthorUuid() {
        AuthorUuid savedAuthor = authorUuidRepository.save(new AuthorUuid());
        assertThat(authorUuidRepository.findAll().contains(savedAuthor)).isNotNull();
    }

    @Test
    void testMySQL() {
        long countBefore = bookRepository.count();
        assertThat(countBefore).isEqualTo(2);
    }
}
