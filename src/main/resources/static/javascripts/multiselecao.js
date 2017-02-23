Brewer = Brewer || {};

Brewer.MultiSelecao = (function() {
	
	function MultiSelecao() {
		this.statusBtn = $('.js-status-btn');
		this.selecaoCheckbox = $('.js-selecao');
		this.selecaoTodosCheckbox = $('.js-selecao-todos');
		this.tooltipBtn = $('[data-toggle="tooltip"]');
	}
	
	MultiSelecao.prototype.iniciar = function() {
		this.statusBtn.on('click', onStatusBtnClicado.bind(this));
		this.selecaoTodosCheckbox.on('click', onSelecaoTodosClicado.bind(this));
		this.selecaoCheckbox.on('click', onSelecaoClicado.bind(this));
		this.tooltipBtn.tooltip();
	}
	
	function onStatusBtnClicado(event) {
		var botaoClicado = $(event.currentTarget);
		var status = botaoClicado.data('status');
		var url = botaoClicado.data('url');
		
		var checkBoxSelecionados = this.selecaoCheckbox.filter(':checked');
		var codigos = $.map(checkBoxSelecionados, function(c) {
			return $(c).data('codigo');
		});
		
		if (codigos.length > 0) {
			$.ajax({
				url: url,
				method: 'PUT',
				data: {
					codigos: codigos,
					status: status
				}, 
				success: function() {
					window.location.reload();
				}
			});
			
		}
	}
	
	function onSelecaoTodosClicado() {
		var status = this.selecaoTodosCheckbox.prop('checked');
		this.selecaoCheckbox.prop('checked', status);
		statusBotaoAcao.call(this, status);
	}
	
	function onSelecaoClicado() {
		var selecaoCheckboxChecados = this.selecaoCheckbox.filter(':checked');
		this.selecaoTodosCheckbox.prop('checked', selecaoCheckboxChecados.length >= this.selecaoCheckbox.length);
		statusBotaoAcao.call(this, selecaoCheckboxChecados.length);
	}
	
	function statusBotaoAcao(ativar) {
		ativar ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
	}
	
	return MultiSelecao;
	
}());


Brewer.ModalStatusPlus = (function() {
	
	function ModalStatusPlus() {
		this.modal = $('#modalGrupo');
		this.form = this.modal.find('form');
	}
	
	ModalStatusPlus.prototype.iniciar = function(event) {
		//this.form.on('submit', function(event) { event.preventDefault() });
		this.modal.on('shown.bs.modal', onModalShow.call(this, event));
		//this.modal.on('hide.bs.modal', onModalClose.bind(this));
	}
	
	function onModalShow(event) {
		var botaoClicado = $(event.currentTarget);
		var codigo = botaoClicado.data('codigo');
		
		$.ajax({
			url: "/brewer/usuarios/carregarGrupos",
//			contentType: 'application/json',
			method: 'POST',
//			data: JSON.stringify({ codigo: codigo }), 
			data: {
				codigo: codigo
			},
			beforeSend: iniciarRequisicao.bind(this),
			//error: alert('deu merda'),
			success: retornoGrupos.bind(this)
		});
	}
	
	function iniciarRequisicao(){
		$("#lista").html("");
	}
	
	
	function retornoGrupos(resposta){
		resposta.forEach(function(obj) {
	        var itemHTML = ["<li class='list-group-item list-group-item-success'>",
                                    obj.nome,
                           "</li>"].join('\n');
	        $("#lista").append(itemHTML);
		});
	}

	return ModalStatusPlus;
	
}());

$(function() {
	var multiSelecao = new Brewer.MultiSelecao();
	multiSelecao.iniciar();
});

function verStatusPlus(event){
	var modalStatusPlus = new Brewer.ModalStatusPlus();
	modalStatusPlus.iniciar(event);
}