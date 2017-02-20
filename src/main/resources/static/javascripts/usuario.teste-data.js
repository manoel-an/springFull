var Brewer = Brewer || {};

Brewer.TesteData = (function() {
	
	function TesteData() {
		this.data = $('.js-date');
	}
	
	TesteData.prototype.iniciar = function() {
		console.log('Data', this.data.val());
		var date = this.data.val().split("/");
		dataJS = new Date(date[2], date[1] - 1, date[0])
		console.log('DataJS', dataJS);
		$.ajax({
			url: "/brewer/usuarios/testeData",
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ dataNascimento: dataJS }),
			error: onError.bind(this),
			success: onSucess.bind(this)
		});		
	}
	
	function onError(obj){
		alert(obj);
	}
	
	function onSucess(obj){
		alert(obj);
	}
	
	return TesteData;
	
}());

function testeData(){
	var testeData = new Brewer.TesteData();
	testeData.iniciar();
}
