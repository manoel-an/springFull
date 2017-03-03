var Brewer = Brewer || {};

$(document).ready(function() {
	$( "#listagemRefrigerante" ).hide();
	$('#progress').removeClass("progress");
	$('#progress .progress-bar').css('width', 0 + '%');
	console.log(Brewer);
});

$(function () {
    $('#fileupload').fileupload({
        dataType: 'json',
 
        done: function (e, data) {
        	$('#files').html("");
            $.each(data.result, function (index, file) {
            	$('#files').append("<tr>" +
            							"<td>"+file.fileName+"</td>" +
            							" <td> <a href='/brewer/files/download/"+index+"' target='_blank' class='btn btn-info'>Ver</a> </td>" +
            							" <td> <a  class='btn btn-danger'>Remover</a> </td>" +
            					   "</tr>");
            });
            $('#progress .progress-bar').css('width', 0 + '%');
            $('#progress').removeClass("progress");
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress').addClass("progress");
            $('#progress .progress-bar').css(
                'width',
                progress + '%'
            );
        }
    }).prop('disabled', !$.support.fileInput)
    	.parent().addClass($.support.fileInput ? undefined : 'disabled');
});

$(function(){
    $(".datepicker").datepicker({
    	dateFormat: "dd/mm/yy",
    	lang: 'pt-BR'
    });
});

$( "#parametroPesquisa" ).keyup(function() {
	var parametroPesquisa = $("#parametroPesquisa").val();
	var url = "/brewer/refrigerantes/pesquisar/" + parametroPesquisa;
	$.ajax({
		url: url,
		method: 'GET',
		data: {filtro : parametroPesquisa}
	}).done(function(data){
		atualizarListagem(data);
	}).fail(function(data){
		alert('erro');
	});
});

function removerRefrigerante(id, btn){
	var url = "/brewer/refrigerantes/remover/" + id;
	$.get(url, function(data){
		btn.closest("tr").remove();
	});
}

function editarRefrigerante(id, btn){
	var url = "/brewer/refrigerantes/editar/" + id;
	$.get(url, function(data){
		preencherFormulario(data);
		cadastrarRefrigerantes();
	});
}

function salvarRefrigerante(){
	var url = "/brewer/refrigerantes/novoajax";
	$.ajax({
		url: url,
		method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(recuperarRefrigerante()),
	}).done(function(data){
		listarRefrigerantes();
		atualizarListagem(data);
		var sucessos = $("#sucessoAJAX");
		sucessos.html("");
		sucessos.removeClass("hidden");
		sucessos.append("<i class='fa fa-check'></i><span>Salvo com sucesso!</span><br>");
	}).fail(function(data){
		var erros = $("#errosAJAX");
		erros.html("");
		erros.removeClass("hidden");
		jQuery.each(data.responseJSON, function() {
			erros.append("<i class='fa  fa-exclamation-circle'></i><span> " + this.defaultMessage + "</span><br>");
		});
	});
}

function salvarMarca(){
	var url = "/brewer/refrigerantes/novamarca"
	var marca = new Object();
	var erros = $("#mensagensErroMarca");
	marca.nome = $("#nomeMarca").val();
	$.ajax({
		url: url,
		method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(marca),
	}).done(function(data){
		var selectMarca = $("#marcaRefrigerante");
		erros.html("");
		erros.addClass("hidden");
		selectMarca.append('<option value=' + data.id + '>' + data.nome + '</option>');
		selectMarca.val(data.id);
		$("#nomeMarca").val("");
		$("#modalCadastroMarca").modal('hide');
	}).fail(function(data){
		erros.html("");
		erros.removeClass("hidden");
		jQuery.each(data.responseJSON, function() {
			erros.append("<i class='fa  fa-exclamation-circle'></i><span> " + this.defaultMessage + "</span><br>");
		});
	});
}

function atualizarListagem(data){
	var tabela = $("#refrigerantes");
	tabela.html("");
	jQuery.each(data, function() {
		tabela.append(
		"<tr>" +
			" <td>" + this.id +"<td/>" +
			" <td>" + this.nome +"<td/>" +
			" <td>" + this.descricao +"<td/>" +
			" <td>" + this.sabor +"<td/>" +
			" <td>" + this.preco +"<td/>" +
			" <td>" + this.preco +"<td/>" +
			" <td> <a onclick='editarRefrigerante("+this.id+", this)' class='btn btn-info'>Editar</a> </td>" +
			" <td> <a onclick='removerRefrigerante("+this.id+", this)' class='btn btn-danger'>Remover</a> </td>" +
		"</tr>");
	});
}

function preencherFormulario(data){
	$('#idRefrigerante').val(data['id']);
	$('#nomeRefrigerante').val(data['nome']);
	$('#descricaoRefrigerante').val(data['descricao']);
	if(data['marca'] != null){
		$('#marcaRefrigerante').val(data['marca']['id']);
	}else{
		$('#marcaRefrigerante').val("");
	}
	//$('#dataCriacao').val((new Date(data['dataCriacao']).toString('yyyy-mm-dd')));
	$('#dataCriacao').val(data['dataCriacao']);
	$('#id_'+data["sabor"]).prop('checked', true);
	$('#precoRefrigerante').val(data['preco'])
}

function recuperarRefrigerante(){
	var refrigerante = new Object();
	refrigerante.id = $('#idRefrigerante').val();
	refrigerante.nome = $('#nomeRefrigerante').val();
	refrigerante.descricao = $('#descricaoRefrigerante').val();
	if($( "#marcaRefrigerante option:selected" ).val()){
		refrigerante.marca = new Object();
		refrigerante.marca.id = $( "#marcaRefrigerante option:selected" ).val();
		refrigerante.marca.nome = $( "#marcaRefrigerante option:selected" ).text();
	}
	refrigerante.dataCriacao = new Date($('#dataCriacao').val()).getTime();
	refrigerante.sabor = $('input[type=radio]:checked', 'form').val();
	refrigerante.preco = $('#precoRefrigerante').val();
	return refrigerante;
}

function listarRefrigerantes(){
	$("#cadastroRefrigerante").slideUp(1, function(){
		$("#listagemRefrigerante").slideDown(200);
		$("#errosAJAX").addClass("hidden");
	});
}

function cadastrarRefrigerantes(){
	$("#listagemRefrigerante").slideUp(1, function(){
		$("#cadastroRefrigerante").slideDown(200);
		$("#sucessoAJAX").addClass("hidden");
	});
}

