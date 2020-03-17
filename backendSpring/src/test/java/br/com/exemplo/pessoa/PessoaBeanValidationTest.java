package br.com.exemplo.pessoa;

import br.com.exemplo.pessoa.models.PessoaEntity;
import br.com.exemplo.pessoa.repositories.PessoaRepository;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PessoaBeanValidationTest {

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
    }

    @Test
    public void saveWithEmailNull() throws Exception{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("notempty.email");
        pessoaEntity.setEmail(null);
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithEmailEmpty(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("notempty.email");
        pessoaEntity.setEmail("");
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithEmailInvalid(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("valid.email");
        pessoaEntity.setEmail("teste");
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithEmailMax(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("size.max.email");
        pessoaEntity.setEmail("YF9COAPfm2hOhhJzHsSE2BRfKKwFXpYwwXzBYFgB8DPflZUsIoxApFg1QR7n8UausOwGTSIRXZy8yIq7xe8djeU0xReum4fUJRcYF9COAPfm2hOhhJzHsSE2BRfKKwFXpYwwXzBYFgB8DPflZUsIoxApFg1QR7n8UausOwGTSIRXZy8yIq7xe8djeU0xReum4fUJRcYF9COAPfm2hOhhJzHsSE2BRfKKwFXpYwwXzBYFgB8DPflZUsIoxApFg1QR7n8UausOwGTSIRXZy8yIq7xe8djeU0xReum4fUJRcYF9COAPfm2hOhhJzHsSE2BRfKKwFXpYwwXzBYFgB8DPflZUsIoxApFg1QR7n8UausOwGTSIRXZy8yIq7xe8djeU0xReum4fUJRc@gmail.com");
        pessoaRepository.save(pessoaEntity);
    }


    @Test
    public void saveWithNomeNull(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("notempty.nome");
        pessoaEntity.setNome(null);
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithNomeEmpty(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("notempty.nome");
        pessoaEntity.setNome("");
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithNomeMax(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("size.max.nome");
        pessoaEntity.setNome("JoknT3TytQS4Rru9BZzIzwxqrIg93RUC66pPz9EjQYdjFOjHnYPcKDJ3Lp3E7NH4JyJ9yOiFmCprROz5nFYzJirUNlaLziF9XO8JoknT3TytQS4Rru9BZzIzwxqrIg93RUC66pPz9EjQYdjFOjHnYPcKDJ3Lp3E7NH4JyJ9yOiFmCprROz5nFYzJirUNlaLziF9XO8");
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithCpfNull(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("notempty.cpf");
        pessoaEntity.setCpf(null);
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithCpfEmpty(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("notempty.cpf");
        pessoaEntity.setCpf("");
        pessoaRepository.save(pessoaEntity);
    }

    @Test
    public void saveWithCpfInvalid(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("cpf.not.valid");
        pessoaEntity.setCpf("58365158082");
        pessoaRepository.save(pessoaEntity);
    }


}