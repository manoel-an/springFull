Brewer.Venda = (function() {
	
	function Venda(tabelaItens) {
		this.tabelaItens = tabelaItens;
	}
	
	Venda.prototype.iniciar = function(event) {
		this.tabelaItens.on('tabela-itens-atualizada', onTabelaItensAtualizada.bind(this));
	}
	
	function onTabelaItensAtualizada(evento, valorTotalItens){
		console.log(valorTotalItens);
	}
	
	return Venda;
	
}());



$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.iniciar();
	
	var tabelaItens = new Brewer.TabelaItens(autocomplete);
	tabelaItens.iniciar();
	
	var venda = new Brewer.Venda(tabelaItens);
	venda.iniciar();
});