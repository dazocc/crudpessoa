package br.com.exemplo.pessoa;

import br.com.exemplo.pessoa.handler.DetalheException;
import br.com.exemplo.pessoa.helper.RestResponsePage;
import br.com.exemplo.pessoa.models.PessoaEntity;
import br.com.exemplo.pessoa.repositories.PessoaFilter;
import br.com.exemplo.pessoa.repositories.PessoaRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PessoaControllerTest {

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
        ResponseEntity<RestResponsePage<PessoaEntity>> resposta = testRestTemplate.exchange("/pessoas", HttpMethod.GET, null, tipoRetorno);

        PessoaEntity pessoaBody = resposta.getBody().getContent().get(0);

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(this.pessoaGeral, pessoaBody, true);
    }

//    @Test
//    public void listarPaginada() {
//
//        String url = UriComponentsBuilder
//                .fromUriString("/pessoa")
////                .queryParam("cpf", "58365158086")
////                .queryParam("pageNumber", "1")
////                .queryParam("pageSize", "2")
////                .queryParam("sortBy", "id")
////                .queryParam("sortDirection", "desc")
//                .build()
//                .encode()
//                .toUri().toString();
////        uriComponents = uriComponents.expand(Collections.singletonMap("name", "test"));
//
////        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("/pessoas").queryParam("cpf", "58365158086");
////                .queryParam("pageNumber", 1)
////                .queryParam("pageSize", 10)
////                .queryParam("sortBy", "id")
////                .queryParam("sortDirection", "desc")
////                .queryParam("search", "hello search");
////
////        Map<String, String> params = new HashMap<>();
////        params.put("page", "0");
////        params.put("size", "1");
////        params.put("cpf", "58365158086");
//
////        Pageable pageable = PageRequest.of(1, 1);
////        PessoaFilter pessoaFilter = new PessoaFilter();
////        pessoaFilter.setCpf("58365158086");
//        ParameterizedTypeReference<RestResponsePage<PessoaEntity>> tipoRetorno =  new ParameterizedTypeReference<RestResponsePage<PessoaEntity>>(){};
//        ResponseEntity<RestResponsePage<PessoaEntity>> resposta = testRestTemplate.exchange(url, HttpMethod.GET, null, tipoRetorno);
//
//        PessoaEntity pessoaBody = resposta.getBody().getContent().get(0);
//
//        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
//        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
//        assertEqualsPessoa(this.pessoaGeral, pessoaBody, true);
//    }

    @Test
    public void buscarWithSuccess() {
        ResponseEntity<PessoaEntity> resposta = testRestTemplate.getForEntity("/pessoas/{id}", PessoaEntity.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(this.pessoaGeral, resposta.getBody(), true);
    }

    @Test
    public void buscarNoSuccess() {
        ResponseEntity<DetalheException> resposta = testRestTemplate.getForEntity("/pessoas/{id}", DetalheException.class, Long.MAX_VALUE);

        Assert.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("Não encontrou a pessoa"));
    }

    @Test
    public void salvarOK() {

        PessoaEntity pessoaHttp = new PessoaEntity();
        pessoaHttp.setNome("Davison Teste2");
        pessoaHttp.setCpf("16102333012");
        pessoaHttp.setDataNascimento(LocalDate.now());
        pessoaHttp.setEmail("testando_unico2@gmail.com");

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(pessoaHttp);
        ResponseEntity<PessoaEntity>  resposta = testRestTemplate.postForEntity("/pessoas", httpEntity, PessoaEntity.class);

        PessoaEntity pessoaBody = resposta.getBody();

        Assert.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEqualsPessoa(pessoaHttp, pessoaBody, false);
        Assert.assertNotNull(pessoaBody.getId());
    }

    @Test
    public void salvarErro() {

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(new PessoaEntity());
        ResponseEntity<DetalheException>  resposta = testRestTemplate.postForEntity("/pessoas", httpEntity, DetalheException.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("Email é obrigatório"));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("CPF é obrigatório"));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("Nome é obrigatório"));
    }

    @Test
    public void atualizarOK() {

        PessoaEntity pessoaHttp = new PessoaEntity();
        pessoaHttp.setNome("Davison Teste3");
        pessoaHttp.setCpf("52931824046");
        pessoaHttp.setDataNascimento(LocalDate.now());
        pessoaHttp.setEmail("testando_unico3@gmail.com");

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(pessoaHttp);
        ResponseEntity resposta = testRestTemplate.exchange("/pessoas/{id}", HttpMethod.PUT, httpEntity, ResponseEntity.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        Assert.assertNull(resposta.getBody());
    }

    @Test
    public void atualizarErro() {

        HttpEntity<PessoaEntity> httpEntity = new HttpEntity<>(new PessoaEntity());
        ResponseEntity<DetalheException> resposta = testRestTemplate.exchange("/pessoas/{id}", HttpMethod.PUT, httpEntity, DetalheException.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("Email é obrigatório"));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("CPF é obrigatório"));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("Nome é obrigatório"));
    }

    @Test
    public void deletarOK() {

        ResponseEntity<Void> resposta = testRestTemplate.exchange("/pessoas/{id}", HttpMethod.DELETE, null, Void.class, pessoaGeral.getId());

        Assert.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        Assert.assertNull(resposta.getBody());
    }


    @Test
    public void deletarNotExistsId() {

        ResponseEntity<DetalheException> resposta = testRestTemplate.exchange("/pessoas/{id}", HttpMethod.DELETE, null, DetalheException.class, Long.MAX_VALUE);

        Assert.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assert.assertTrue(resposta.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        Assert.assertTrue(resposta.getBody().getTitulos().contains("Não encontrou a pessoa"));
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
