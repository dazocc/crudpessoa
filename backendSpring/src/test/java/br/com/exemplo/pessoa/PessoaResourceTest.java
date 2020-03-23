package br.com.exemplo.pessoa;

import br.com.exemplo.pessoa.handler.DetalheException;
import br.com.exemplo.pessoa.jpa.models.PessoaEntity;
import br.com.exemplo.pessoa.jpa.repositories.PessoaRepository;
import br.com.exemplo.pessoa.utils.DateUtils;
import br.com.exemplo.pessoa.utils.RestResponsePage;
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

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PessoaResourceTest {

    private final String URL = "/pessoas";
    private final String PARAMETER_ID = "/{id}";
    private final String NAO_ENCONTROU_PESSOA = "Não encontrou a pessoa";
    private final String EMAIL_EH_OBRIGATORIO = "Email é obrigatório";
    private final String CPF_EH_OBRIGATORIO = "CPF é obrigatório";
    private final String NOME_EH_OBRIGATORIO = "Nome é obrigatório";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PessoaRepository pessoaRepository;

    private PessoaEntity pessoaGeral;

    @Before
    public void startEach() {
        pessoaGeral = new PessoaEntity("Davison Geral", "58365158086", "testando_unicoGeral@gmail.com", LocalDate.now());
        pessoaGeral = pessoaRepository.save(pessoaGeral);
    }

    @After
    public void endEach() {
        pessoaRepository.deleteAll();
    }

    @Test
    public void listarAll() {

        ParameterizedTypeReference<RestResponsePage<PessoaEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<PessoaEntity>>(){};
        ResponseEntity<RestResponsePage<PessoaEntity>> resposta = testRestTemplate.exchange(URL, HttpMethod.GET, null, tipoRetorno);

        PessoaEntity pessoaBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(this.pessoaGeral, pessoaBody, true);
    }

    @Test
    public void listarPaginada() {

        PessoaEntity pessoaTeste2 = new PessoaEntity("Davison Teste2", "16102333012", "testando_unico2@gmail.com", LocalDate.now());
        pessoaTeste2 = pessoaRepository.save(pessoaTeste2);

        PessoaEntity pessoaTeste3 = new PessoaEntity("Davison Teste3", "05350441000", "testando_unico3@gmail.com", LocalDate.now());
        pessoaRepository.save(pessoaTeste3);

        String url = UriComponentsBuilder
                .fromUriString(URL)
                .queryParam("page", "1")
                .queryParam("size", "1")
                .build()
                .encode()
                .toUri().toString();

        ParameterizedTypeReference<RestResponsePage<PessoaEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<PessoaEntity>>(){};
        ResponseEntity<RestResponsePage<PessoaEntity>> resposta = testRestTemplate.exchange(url, HttpMethod.GET, null, tipoRetorno);

        PessoaEntity pessoaBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(pessoaTeste2, pessoaBody, true);
    }

    @Test
    public void listarFiltros() {

        String url = UriComponentsBuilder
                .fromUriString(URL)
                .queryParam("cpf", pessoaGeral.getCpf())
                .queryParam("nome", "Davison")
                .queryParam("email", pessoaGeral.getEmail())
                .queryParam("dataNascimento", DateUtils.getDataDDMMYYYY(pessoaGeral.getDataNascimento()))
                .build()
                .encode()
                .toUri().toString();

        ParameterizedTypeReference<RestResponsePage<PessoaEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<PessoaEntity>>(){};
        ResponseEntity<RestResponsePage<PessoaEntity>> resposta = testRestTemplate.exchange(url, HttpMethod.GET, null, tipoRetorno);

        PessoaEntity pessoaBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(pessoaGeral, pessoaBody, true);
    }

    @Test
    public void buscarWithSuccess() {
        ResponseEntity<PessoaEntity> resposta = testRestTemplate.getForEntity(URL + PARAMETER_ID, PessoaEntity.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(this.pessoaGeral, resposta.getBody(), true);
    }

    @Test
    public void buscarNoSuccess() {
        ResponseEntity<DetalheException> resposta = testRestTemplate.getForEntity(URL + PARAMETER_ID, DetalheException.class, Long.MAX_VALUE);

        Assert.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NAO_ENCONTROU_PESSOA));
    }

    @Test
    public void salvarOK() {

        PessoaEntity pessoaHttp = new PessoaEntity("Davison Teste2", "16102333012", "testando_unico2@gmail.com", LocalDate.now());

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(pessoaHttp);
        ResponseEntity<PessoaEntity>  resposta = testRestTemplate.postForEntity(URL, httpEntity, PessoaEntity.class);

        PessoaEntity pessoaBody = resposta.getBody();

        Assert.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(pessoaHttp, pessoaBody, false);
        Assert.assertNotNull(pessoaBody.getId());
    }

    @Test
    public void salvarErro() {

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(new PessoaEntity());
        ResponseEntity<DetalheException>  resposta = testRestTemplate.postForEntity(URL, httpEntity, DetalheException.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(EMAIL_EH_OBRIGATORIO));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(CPF_EH_OBRIGATORIO));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NOME_EH_OBRIGATORIO));
    }

    @Test
    public void atualizarOK() {

        PessoaEntity pessoaHttp = new PessoaEntity("Davison Teste3", "52931824046", "testando_unico3@gmail.com", LocalDate.now());

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(pessoaHttp);
        ResponseEntity resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.PUT, httpEntity, ResponseEntity.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        Assert.assertNull(resposta.getBody());
    }

    @Test
    public void atualizarErro() {

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(new PessoaEntity());
        ResponseEntity<DetalheException> resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.PUT, httpEntity, DetalheException.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(EMAIL_EH_OBRIGATORIO));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(CPF_EH_OBRIGATORIO));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NOME_EH_OBRIGATORIO));
    }

    @Test
    public void deletarOK() {

        ResponseEntity<Void> resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.DELETE, null, Void.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        Assert.assertNull(resposta.getBody());
    }


    @Test
    public void deletarNotExistsId() {

        ResponseEntity<DetalheException> resposta = testRestTemplate.exchange(URL + PARAMETER_ID, HttpMethod.DELETE, null, DetalheException.class, Long.MAX_VALUE);

        Assert.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains(NAO_ENCONTROU_PESSOA));
    }

    private void assertEqualsPessoa(PessoaEntity pessoa1, PessoaEntity pessoa2, boolean validaId) {

        if(validaId){
            Assert.assertEquals(pessoa1.getId(), pessoa2.getId());
        }

        Assert.assertEquals(pessoa1.getNome(), pessoa2.getNome());
        Assert.assertEquals(pessoa1.getEmail(), pessoa2.getEmail());
        Assert.assertEquals(pessoa1.getCpf(), pessoa2.getCpf());
    }

}
