package br.com.exemplo.crud;

import br.com.exemplo.crud.handler.DetalheException;
import br.com.exemplo.crud.mongo.models.EditoraEntity;
import br.com.exemplo.crud.mongo.models.LivroEntity;
import br.com.exemplo.crud.mongo.repositories.LivroRepository;
import br.com.exemplo.crud.utils.RestResponsePage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LivroResourceTest {

    private final String URL = "/livros";
    private final String PARAMETER_ID = "/{id}";
    private final String NAO_ENCONTROU_LIVRO = "Não encontrou o livro";
    private final String EDITORA_EH_OBRIGATORIO = "Editora é obrigatório";
    private final String NOME_EH_OBRIGATORIO = "Nome é obrigatório";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private LivroRepository livroRepository;

    private LivroEntity livroGeral;

    @Before
    public void startEach() {
        livroGeral = new LivroEntity("Teste Livro", new EditoraEntity("Teste Editora"));
        livroGeral = livroRepository.save(livroGeral);
    }

    @After
    public void endEach() {
        livroRepository.deleteAll();
    }

    @Test
    public void listarAll() {

        ParameterizedTypeReference<RestResponsePage<LivroEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<LivroEntity>>(){};
        ResponseEntity<RestResponsePage<LivroEntity>> resposta = testRestTemplate.exchange(URL, HttpMethod.GET, null, tipoRetorno);

        LivroEntity livroBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsLivro(this.livroGeral, livroBody, true);
    }

    @Test
    public void listarPaginada() {

        LivroEntity livroTeste2 = new LivroEntity("Teste Livro2", new EditoraEntity("Teste Editora2"));
        livroTeste2 = livroRepository.save(livroTeste2);

        LivroEntity livroTeste3 = new LivroEntity("Teste Livro3", new EditoraEntity("Teste Editora3"));
        livroRepository.save(livroTeste3);

        String url = UriComponentsBuilder
                .fromUriString(URL)
                .queryParam("page", "1")
                .queryParam("size", "1")
                .build()
                .encode()
                .toUri().toString();

        ParameterizedTypeReference<RestResponsePage<LivroEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<LivroEntity>>(){};
        ResponseEntity<RestResponsePage<LivroEntity>> resposta = testRestTemplate.exchange(url, HttpMethod.GET, null, tipoRetorno);

        LivroEntity livroBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsLivro(livroTeste2, livroBody, true);
    }

    @Test
    public void listarFiltros() {

        String url = UriComponentsBuilder
                .fromUriString(URL)
                .queryParam("nome", livroGeral.getNome())
                .build()
                .encode()
                .toUri().toString();

        ParameterizedTypeReference<RestResponsePage<LivroEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<LivroEntity>>(){};
        ResponseEntity<RestResponsePage<LivroEntity>> resposta = testRestTemplate.exchange(url, HttpMethod.GET, null, tipoRetorno);

        LivroEntity livroBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsLivro(livroGeral, livroBody, true);
    }

    @Test
    public void buscarWithSuccess() {
        ResponseEntity<LivroEntity> resposta = testRestTemplate.getForEntity(URL + PARAMETER_ID, LivroEntity.class, livroGeral.getId());

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsLivro(this.livroGeral, resposta.getBody(), true);
    }

    @Test
    public void buscarNoSuccess() {
        ResponseEntity<DetalheException> resposta = testRestTemplate.getForEntity(URL + PARAMETER_ID, DetalheException.class, "TESTESEMID");

        Assert.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NAO_ENCONTROU_LIVRO));
    }

    @Test
    public void salvarOK() {

        LivroEntity livroTeste2 = new LivroEntity("Teste Livro2", new EditoraEntity("Teste Editora2"));

        HttpEntity<LivroEntity> httpEntity = new HttpEntity<>(livroTeste2);
        ResponseEntity<LivroEntity>  resposta = testRestTemplate.postForEntity(URL, httpEntity, LivroEntity.class);

        LivroEntity livroBody = resposta.getBody();

        Assert.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsLivro(livroTeste2, livroBody, false);
        Assert.assertNotNull(livroBody.getId());
    }

    @Test
    public void salvarErro() {

        HttpEntity<LivroEntity> httpEntity = new HttpEntity<>(new LivroEntity());
        ResponseEntity<DetalheException>  resposta = testRestTemplate.postForEntity(URL, httpEntity, DetalheException.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NOME_EH_OBRIGATORIO));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(EDITORA_EH_OBRIGATORIO));
    }

    @Test
    public void atualizarOK() {

        LivroEntity livroTeste3 = new LivroEntity("Teste Livro3", new EditoraEntity("Teste Editora3"));

        HttpEntity<LivroEntity> httpEntity = new HttpEntity<>(livroTeste3);
        ResponseEntity resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.PUT, httpEntity, ResponseEntity.class, livroGeral.getId());

        Assert.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        Assert.assertNull(resposta.getBody());
    }

    @Test
    public void atualizarErro() {

        HttpEntity<LivroEntity> httpEntity = new HttpEntity<>(new LivroEntity());
        ResponseEntity<DetalheException> resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.PUT, httpEntity, DetalheException.class, livroGeral.getId());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NOME_EH_OBRIGATORIO));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(EDITORA_EH_OBRIGATORIO));
    }

    @Test
    public void deletarOK() {

        ResponseEntity<Void> resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.DELETE, null, Void.class, livroGeral.getId());

        Assert.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        Assert.assertNull(resposta.getBody());
    }


    @Test
    public void deletarNotExistsId() {

        ResponseEntity<DetalheException> resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.DELETE, null, DetalheException.class, Long.MAX_VALUE);

        Assert.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NAO_ENCONTROU_LIVRO));
    }

    private void assertEqualsLivro(LivroEntity livro1, LivroEntity livro2, boolean validaId) {

        if(validaId){
            Assert.assertEquals(livro1.getId(), livro2.getId());
        }

        Assert.assertEquals(livro1.getNome(), livro2.getNome());
        Assert.assertEquals(livro1.getEditora().getNome(), livro2.getEditora().getNome());
    }

}
