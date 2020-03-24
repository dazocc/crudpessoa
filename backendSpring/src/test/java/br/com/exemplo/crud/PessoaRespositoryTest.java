package br.com.exemplo.crud;

import br.com.exemplo.crud.jpa.models.PessoaEntity;
import br.com.exemplo.crud.jpa.repositories.PessoaRepository;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoaRespositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PessoaEntity pessoaEntity;

    @Before
    public void init(){
        pessoaEntity = new PessoaEntity();

        pessoaEntity.setNome("Davison Teste");
        pessoaEntity.setCpf("58365158086");
        pessoaEntity.setDataNascimento(LocalDate.now());
        pessoaEntity.setEmail("testando_unico@gmail.com");

        pessoaEntity = pessoaRepository.save(pessoaEntity);
    }

    @After
    public void end(){
        pessoaRepository.deleteAll();
    }

    @Test
    public void existsByCpfNotExists(){
        Boolean existe = pessoaRepository.existsByCpf("58365158082");
        Assert.assertFalse(existe);
    }

    @Test
    public void existsByCpfExists(){
        Boolean existe = pessoaRepository.existsByCpf("58365158086");
        Assert.assertTrue(existe);
    }

    @Test
    public void existsByCpfAndIdNotExists(){
        Boolean existe = pessoaRepository.existsByCpfAndIdNot(pessoaEntity.getCpf(), pessoaEntity.getId());
        Assert.assertFalse(existe);
    }

    @Test
    public void existsByCpfAndIdNotExits(){
        Boolean existe = pessoaRepository.existsByCpfAndIdNot(pessoaEntity.getCpf(), 2L);
        Assert.assertTrue(existe);
    }


}