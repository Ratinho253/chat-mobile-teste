package br.senai.sp.jandira.s_book

data class UserChat(
    val id_chat: String,
    val users: List<UsuarioChat2>,
    val isGroup: Boolean,
    val data_criacao: String,
    val hora_criacao: String
)
