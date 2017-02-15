package com.algaworks.brewer.controller.page;

import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PageWrapper<T> {

    private Page<T> page;
    private URIBuilder builderPagina;
    private URIBuilder builderSort;

    // Bug com caracter +; necessário substituição por %20
    // this.uriBuilder =
    // ServletUriComponentsBuilder.fromRequest(httpServletRequest);
    /*
     * public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
     * this.page = page; String httpUrl = httpServletRequest.getRequestURL()
     * .append(httpServletRequest.getQueryString() != null ? "?" +
     * httpServletRequest.getQueryString() : "") .toString().replaceAll("\\+",
     * "%20"); this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl); }
     */

    public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {

        this.page = page;

        try {
            String httpUrl = getFullRequestUrl(httpServletRequest);
            this.builderPagina = new URIBuilder(httpUrl);
            this.builderSort = new URIBuilder(httpUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Url inválida", e);
        }
    }

    public List<T> getConteudo() {
        return page.getContent();
    }

    public boolean isVazia() {
        return page.getContent().isEmpty();
    }

    public int getAtual() {
        return page.getNumber();
    }

    public boolean isPrimeira() {
        return page.isFirst();
    }

    public boolean isUltima() {
        return page.isLast();
    }

    public int getTotal() {
        return page.getTotalPages();
    }

    public String urlParaPagina(int pagina) {
        return builderPagina.setParameter("page", String.valueOf(pagina)).toString();
    }

    public String urlOrdenada(String propriedade) {
        String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));
        return builderSort.setParameter("sort", valorSort).toString();
    }

    public String inverterDirecao(String propriedade) {
        String direcao = "asc";

        Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
        if (order != null) {
            direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
        }

        return direcao;
    }

    public boolean descendente(String propriedade) {
        return inverterDirecao(propriedade).equals("asc");
    }

    public boolean ordenada(String propriedade) {
        Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;

        if (order == null) {
            return false;
        }

        return page.getSort().getOrderFor(propriedade) != null ? true : false;
    }

    private String getFullRequestUrl(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRequestURL()
                .append(httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
                .toString();
    }

}