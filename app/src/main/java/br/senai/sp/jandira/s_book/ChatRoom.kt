package br.senai.sp.jandira.s_book

data class ChatRoom(
    val id_chat: String,
    val users: List<UserChat>,
    val isGroup: Boolean,
    val data_criacao: String,
    val hora_criacao: String
)
