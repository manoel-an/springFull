package com.algaworks.brewer.venda;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.session.TabelasItensSession;

public class TabelaItensVendaTest {

    private TabelasItensSession tabelaItensVenda;

    @Before
    public void setUp() {
        this.tabelaItensVenda = new TabelasItensSession();
    }

    @Test
    public void deveCalcularValorTotalSemItens() throws Exception {
        // assertEquals(BigDecimal.ZERO, tabelaItensVenda.);
    }

    @Test
    public void deveCalcularValorTotalComUmItem() throws Exception {
        Cerveja cerveja = new Cerveja();
        BigDecimal valor = new BigDecimal("8.90");
        cerveja.setValor(valor);

        // tabelaItensVenda.adicionarItem(cerveja, 1);

        // assertEquals(valor, tabelaItensVenda.getValorTotal());
    }
}