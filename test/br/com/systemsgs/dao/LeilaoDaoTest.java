package br.com.systemsgs.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.systemsgs.dominio.Leilao;
import br.com.systemsgs.dominio.Usuario;

public class LeilaoDaoTest {
	
	private Session session;
	private UsuarioDao usuarioDao;
	private LeilaoDao leilaoDao;
	
	@Before
	public void init() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);
		
		session.beginTransaction();
		
	}
	
	@After
	public void termina() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveContarLeiloesNaoEncerrados() {
		
		Usuario guilherme = new Usuario("Guilherme Santos", "guitestt@gmail.com");
		
		Leilao ativo = new Leilao("Notebook", 1500.0, guilherme, false);
		Leilao encerrado = new Leilao("Celular", 700.0, guilherme, false);
		encerrado.encerra();
		
		usuarioDao.salvar(guilherme);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(1L, total);
		
	}
	
	@Test
    public void deveRetornarZeroSeNaoHaLeiloesNovos() {
    Usuario gui = new Usuario("Guilherme Santos", "gui@teste.com.br");

    Leilao encerrado = new Leilao("XBox", 700.0, gui, false);
    Leilao tambemEncerrado = new Leilao("Geladeira", 1500.0, gui, false);
   
    encerrado.encerra();
    tambemEncerrado.encerra();

    usuarioDao.salvar(gui);
    leilaoDao.salvar(encerrado);
    leilaoDao.salvar(tambemEncerrado);

    long total = leilaoDao.total();

    assertEquals(0L, total);
    
	}
	
	@Test
    public void deveRetornarLeiloesDeProdutosNovos() {
        Usuario gui = new Usuario("Guilherme Santos","gui@teste.com.br");

        Leilao produtoNovo = new Leilao("XBox", 700.0, gui, false);
        Leilao produtoUsado =  new Leilao("Geladeira", 1500.0, gui,true);

        usuarioDao.salvar(gui);
        leilaoDao.salvar(produtoNovo);
        leilaoDao.salvar(produtoUsado);

        List<Leilao> novos = leilaoDao.novos();

        assertEquals(1, novos.size());
        assertEquals("XBox", novos.get(0).getNome());
    }
	
	 @Test
	    public void deveTrazerSomenteLeiloesAntigos() {
	        Usuario gui = new Usuario("Guilherme Santos", "gui@teste.com.br");

	        Leilao recente = new Leilao("XBox", 700.0, gui, false);
	        Leilao antigo = new Leilao("Geladeira", 1500.0, gui,true);

	        Calendar dataRecente = Calendar.getInstance();
	        Calendar dataAntiga = Calendar.getInstance();
	        dataAntiga.add(Calendar.DAY_OF_MONTH, -10);

	        recente.setDataAbertura(dataRecente);
	        antigo.setDataAbertura(dataAntiga);

	        usuarioDao.salvar(gui);
	        leilaoDao.salvar(recente);
	        leilaoDao.salvar(antigo);

	        List<Leilao> antigos = leilaoDao.antigos();

	        assertEquals(1, antigos.size());
	        assertEquals("Geladeira", antigos.get(0).getNome());
	    }


}
