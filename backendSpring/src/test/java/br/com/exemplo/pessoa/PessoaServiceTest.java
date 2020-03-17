package br.com.exemplo.pessoa;

import br.com.exemplo.pessoa.exceptions.PessoaValidacaoException;
import br.com.exemplo.pessoa.models.PessoaEntity;
import br.com.exemplo.pessoa.services.PessoaService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoaServiceTest {

    private final Long TAMANHO_ARQUIVO = 1024L*1024L;

    @Autowired
    private PessoaService pessoaService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void validarAntesAtualizarArquivoNull() {
        pessoaService.validaAntesAtualizarArquivo(null, null);
    }

    @Test
    public void validarAntesAtualizarArquivoTamanhoMenosUm() {
        pessoaService.validaAntesAtualizarArquivo(TAMANHO_ARQUIVO - 1, null);
    }

    @Test
    public void validarAntesAtualizarArquivoTamanhoEquals() {
        pessoaService.validaAntesAtualizarArquivo(TAMANHO_ARQUIVO, null);
    }

    @Test
    public void validarAntesAtualizarArquivoTamanhoMaisUm() {
        expectedException.expect(PessoaValidacaoException.class);
        expectedException.expectMessage("cadastroPessoaArquivo.M1");
        pessoaService.validaAntesAtualizarArquivo(TAMANHO_ARQUIVO + 1, null);
    }

    @Test
    public void validarAntesAtualizarArquivoArquivoValido() {
        pessoaService.validaAntesAtualizarArquivo(null, "nome.jpeg");
    }


    @Test
    public void validarAntesAtualizarArquivoArquivoInvalido() {
        expectedException.expect(PessoaValidacaoException.class);
        expectedException.expectMessage("cadastroPessoaArquivo.M2");
        pessoaService.validaAntesAtualizarArquivo(null, "nome.pdf");
    }

    @Test
    public void validarAntesAtualizarArquivoArquivoSemExtensao() {
        pessoaService.validaAntesAtualizarArquivo(null, "pdf");
    }

    @Test
    public void validaAntesSalvarAtualizarNull() {
        pessoaService.validaAntesSalvarAtualizar(null);
    }

    @Test
    public void validaAntesSalvarAtualizarPessoaNula() {
        pessoaService.validaAntesSalvarAtualizar(new PessoaEntity());
    }


    @Test
    public void validaAntesSalvarAtualizarPessoaCpf() {
        PessoaEntity pessoaEntity = new PessoaEntity();
        pessoaEntity.setCpf("58365158086");
        pessoaService.validaAntesSalvarAtualizar(pessoaEntity);
    }




}
