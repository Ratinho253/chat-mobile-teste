package br.senai.sp.jandira.s_book

data class MensagensResponse(
    var status: Int,
    var message: String,
    var id_chat: String,
    var usuarios: List<UsuarioChat2>,
    var data_criacao: String,
    var hora_criacao: String,
    var mensagens: List<Mensagens>
)
