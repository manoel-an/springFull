package com.algaworks.brewer.repository.helper.refrigerante;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Refrigerante;
import com.algaworks.brewer.repository.filter.RefrigeranteFilter;
import com.algaworks.brewer.repository.helper.cliente.PageableQueries;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class RefrigerantesImpl implements PageableQueries<Refrigerante, RefrigeranteFilter> {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
	public Page<Refrigerante> filtrar(RefrigeranteFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Refrigerante.class);
		paginacaoUtil.preparar(criteria, pageable);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(RefrigeranteFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Refrigerante.class);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

}
