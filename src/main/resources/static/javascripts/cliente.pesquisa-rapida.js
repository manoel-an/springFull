Brewer = Brewer || {};

Brewer.PesquisaRapidaCliente = (function() {
	
	function PesquisaRapidaCliente() {
		this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientes');
		this.form = this.pesquisaRapidaClientesModal.find('form');
		this.nomeInput = $('#nomeClienteModal');
		this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-clientes-btn');
		this.containerTabelaPesquisa = $('#containerTabelaPesquisaRapidaClientes');
		//this.htmlTabelaPesquisa = $('#tabela-pesquisa-rapida-cliente').html();
		//this.template = Handlebars.compile(this.htmlTabelaPesquisa);
		this.mensagemErro = $('.js-mensagem-erro');
	}
	
	PesquisaRapidaCliente.prototype.iniciar = function(event) {
		this.pesquisaRapidaBtn.on('click', onPesquisaRapidaClicado.bind(this));
		this.pesquisaRapidaClientesModal.on('shown.bs.modal', onModalShow.bind(this));
	}
	
	function onModalShow() {
		this.nomeInput.focus();
	}
	
	function onPesquisaConcluida(resultado){
		this.form.find('.form-group').removeClass('has-error');
		this.mensagemErro.addClass('hidden');
		//var html = this.template(resultado);
		//this.containerTabelaPesquisa.html(html);
		this.containerTabelaPesquisa.html(resultado);
		var tabelaClientePesquisaRapida = new Brewer.TabelaClientePesquisaRapida(this.pesquisaRapidaClientesModal);
		tabelaClientePesquisaRapida.iniciar();
	}
	
	function onPesquisaRapidaClicado(event){
		event.preventDefault();
		$.ajax({
			url: this.pesquisaRapidaClientesModal.find('form').attr('action'),
			method: "GET",
			contentType: 'application/json',
			data: {
				nome: this.nomeInput.val()
			},
			error: onErroPesquisa.bind(this),
			success: onPesquisaConcluida.bind(this)
		});		
	}
	
	function onErroPesquisa(){
		this.form.find('.form-group').addClass('has-error');
		this.mensagemErro.removeClass('hidden');
	}
	

	return PesquisaRapidaCliente;
	
}());

Brewer.TabelaClientePesquisaRapida = (function() {
	
	function TabelaClientePesquisaRapida(modal){
		this.modalCliente = modal;
		this.cliente = $('.js-cliente-pesquisa-rapida');
	}
	
	TabelaClientePesquisaRapida.prototype.iniciar = function() {
		this.cliente.on('click', onClienteSelecionado.bind(this));
	}
	
	function onModalShow(){
		this.nomeInput.focus();
	}
	
	function onClienteSelecionado(evento){
		this.modalCliente.modal('hide');
		var clienteSelecionado = $(evento.currentTarget);
		$('#nomeCliente').val(clienteSelecionado.data('nome'));
		$('#codigoCliente').val(clienteSelecionado.data('codigo'));
	}
	
	return TabelaClientePesquisaRapida;
	
}());

$(function() {
	var pesquisaRapidaCliente = new Brewer.PesquisaRapidaCliente();
	pesquisaRapidaCliente.iniciar();
});