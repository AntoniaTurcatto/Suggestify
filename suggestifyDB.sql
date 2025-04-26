drop schema if exists suggestify;
create schema suggestify;

use suggestify;

CREATE TABLE usuario (
  idUsuario int NOT NULL AUTO_INCREMENT,
  nome varchar(40) not null,
  email varchar(60) not null,
  senha varchar(45) not null,
  tipo tinyint(1) not null COMMENT '0 -> comum; 1 -> artista',
  nomeArtistico varchar(60),
  fotoPerfil longblob not null,
  bio varchar(50),
  PRIMARY KEY (idUsuario)
);

CREATE TABLE tag (
  idTag int not null auto_increment,
  nome varchar(20) not null,
  primary key(idTag)
);

CREATE TABLE album(
	idAlbum int not null auto_increment,
    fkUsuario int not null,
    nomeAlbum varchar(50) not null,
    imagem longblob not null,
    primary key(idAlbum),
    constraint constraintAlbumArtista foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE
);

CREATE TABLE musica (
	idMusica int not null auto_increment,
    nomeMusica varchar(50) not null,
    audio longblob,
    fkAlbum int not null,
    primary key(idMusica),
    constraint constraintMusicaAlbum foreign key (fkAlbum) REFERENCES album(idAlbum) ON DELETE CASCADE
);

CREATE TABLE tagsDoAlbum(
	idTagsDoAlbum int not null auto_increment,
    fkTag int not null,
    fkAlbum int not null,
    primary key (idTagsDoAlbum),
    constraint constraintTagsDoAlbumTag foreign key (fkTag) references tag(idTag) ON DELETE CASCADE,
    constraint constraintTagsDoAlbumAlbum foreign key (fkAlbum) REFERENCES album(idAlbum) ON DELETE CASCADE
);

CREATE TABLE tagsDoUsuario(
	idTagDoUsuario int not null auto_increment,
    fkTag int not null,
    fkUsuario int not null,
    primary key(idTagDoUsuario),
    constraint constraintTagDoUsuarioTag foreign key (fkTag) REFERENCES tag(idTag) ON DELETE CASCADE,
    constraint constraintTagDoUsuarioUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE
);

CREATE TABLE tagsDeMusica(
	idTagDeMusica int not null auto_increment,
    fkTag int not null,
    fkMusica int not null,
    primary key(idTagDeMusica),
    constraint constraintTagDeMusicaTag foreign key (fkTag) REFERENCES tag(idTag) ON DELETE CASCADE,
    constraint constraintTagDeMusicaMusica foreign key (fkMusica) REFERENCES musica(idMusica) ON DELETE CASCADE
);

CREATE TABLE colaboracoes(
	idColaboracoes int not null auto_increment,
	fkMusica int not null,
	fkUsuario int not null,
    primary key(idColaboracoes),
    constraint constraintColaboracoesMusica foreign key (fkMusica) REFERENCES musica(idMusica) ON DELETE CASCADE,
    constraint constraintColaboracoesUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE
);

CREATE TABLE comunidade(
	idComunidade int not null auto_increment,
    fotoComunidade longblob not null,
    fkUsuarioPrincipal int not null,
    corFundoDoPost varchar(8) not null,
    corFonteDoPost varchar(8) not null,
    primary key(idComunidade),
    constraint constraintComunidadeUsuario foreign key (fkUsuarioPrincipal) REFERENCES usuario(idUsuario) ON DELETE CASCADE
);

CREATE TABLE usuariosDaComunidade(
	idUsuariosDaComunidade int not null auto_increment,
    fkUsuario int not null,
    fkComunidade int not null,
    primary key(idUsuariosDaComunidade),
    constraint constraintUsuariosDaComunidadeUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    constraint constraintUsuariosDaComunidadeComunidade foreign key (fkComunidade) REFERENCES comunidade(idComunidade) ON DELETE CASCADE
);

CREATE TABLE publicacao(
	idPublicacao int not null auto_increment,
    texto varchar(200),
    likes int not null,
    imagem longblob,
    fkUsuario int not null,
    fkComunidade int not null,
    primary key(idPublicacao),
    constraint constraintPublicacaoUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    constraint constraintPublicacaoComunidade foreign key (fkComunidade) REFERENCES comunidade(idComunidade) ON DELETE CASCADE
);

CREATE TABLE comentario(
	idComentario int not null auto_increment,
    texto varchar(200) not null,
    likes int not null,
	fkUsuario int not null,
    fkPublicacao int not null,
    primary key(idComentario),
    constraint constraintComentarioUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    constraint constraintComentarioPublicacao foreign key (fkPublicacao) REFERENCES publicacao(idPublicacao) ON DELETE CASCADE
);

create table publicacaoQueUsuarioCurtiu(
	idPublicacaoQueUsuarioCurtiu int not null auto_increment,
    fkUsuario int not null,
    fkPublicacao int not null,
    primary key(idPublicacaoQueUsuarioCurtiu),
    constraint constraintPublicacaoUsuarioCurtiuUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    constraint constraintPublicacaoUsuarioCurtiuPublicacao foreign key (fkPublicacao) REFERENCES publicacao(idPublicacao) ON DELETE CASCADE
);

create table comentarioQueUsuarioCurtiu(
	idComentarioQueUsuarioCurtiu int not null auto_increment,
    fkUsuario int not null,
    fkComentario int not null,
    primary key(idComentarioQueUsuarioCurtiu),
    constraint constraintComentarioUsuarioCurtiuUsuario foreign key (fkUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    constraint constraintComentarioUsuarioCurtiuComentario foreign key (fkComentario) REFERENCES comentario(idComentario) ON DELETE CASCADE
);

/* 
tabela para sugestão de generos parecidos
*/


/*
	usuario cria tag
    destino da tag:
	sugestao <- tag -> generoFilho
	tem que fazer um novo registro em generoFilho para a recomendacao funcionar
*/
create table sugestoesDeTags(
	idTagSugerida int not null auto_increment,
    tipoConteudo int not null, /* comunidade = 0; musica = 1; album = 2 */
    fkTag int not null,
    fkConteudo int not null,
    primary key(idTagSugerida),
    constraint constraintSugestoesDeTagsTag foreign key(fkTag) references tag(idTag)
);


create table generoPai(
	idGeneroPai int not null auto_increment,
    fkTag int not null,
    primary key (idGeneroPai),
    constraint constraintTagDoGeneroPai foreign key (fkTag) references tag(idTag) on delete cascade
);
/* genero filho pode ser pai de outro genero filho, então este filho terá um pai que tem um pai. Exemplo: rock <- metal <- thrash metal*/
create table generoFilho(
	idGeneroFilho int not null auto_increment,
    fkTagGenero int not null,
    fkGeneroPai int not null,
    primary key (idGeneroFilho),
    constraint constraintTagDoGeneroFilho foreign key(fkTagGenero) references tag(idTag) on delete cascade,
    constraint constraintGeneroPaiDoGeneroFilho foreign key (fkGeneroPai) references generoPai(idGeneroPai) on delete cascade
);

insert into tag(idTag,nome) 
values(null, 'FOLK'),
(null, 'ROCK'),
(null, 'HIP-HOP'),
(null, 'POP'),
(null, 'REGGAE'),
(null, 'JAZZ'),
(null, 'BLUES'),
(null, 'CLASSICA');


insert into generoPai(idGeneroPai,fkTag) values
(null, 1),
(null, 2),
(null, 3),
(null, 4),
(null, 5),
(null, 6),
(null, 7),
(null, 8);



insert into tag(idTag,nome) values
/*subgeneros folk */
(null, 'ARABESQUE'),
(null, 'ARAPAHO'),
(null, 'CALGIJA'),
(null, 'CAPE BRETON FIDDLING'),
(null, 'CARISO'),
(null, 'CHALGA'),
(null, 'CHUTNEY'),
(null, 'DANGDUT'),
(null, 'DENE'),
(null, 'KAISO'),
(null, 'KOPLO'),
(null, 'KWAKWAKA WAKW'),
(null, 'LAIKO'),
(null, 'MENTO'),
(null, 'METIS'),
(null, 'MUINEIRA'),
(null, 'NEOFOLK'),
(null, 'PARANG'),
(null, 'FOLK POP'),
(null, 'PUB SONG'),
(null, 'QUAN HO'),
(null, 'QUEBEC FIDDLE'),
(null, 'REBETIKO'),
(null, 'REEL'),
(null, 'RIELDANS'),
(null, 'FOLK ROCK'),
(null, 'ROOTS REVIVAL'),
(null, 'SCHOTTISCHE'),
(null, 'SEVDALINKA'),
(null, 'SKIFFLE'),
(null, 'SKILADIKO'),
(null, 'STAROGRADSKA'),
(null, 'TALKING BLUES'),
(null, 'TALLAVA'),
(null, 'TAMANG SELO'),
(null, 'TIERRA CALIENTE'),
(null, 'TSIFTETELI'),
(null, 'TURBO FOLK'),
(null, 'XAM'),
/*   SUBGENEROS ROCK - link: https://www.guitarguitar.co.uk/news/142079/*/
(null, 'CLASSIC ROCK'),
(null, 'POP ROCK'),
(null, 'SOFT ROCK'),
(null, 'PSYCHEDELIC ROCK'),
(null, 'GLAM ROCK'),
(null, 'HARD ROCK'),
(null, 'BLUES ROCK'),
(null, 'SOUTHERN ROCK'),
(null, 'PROG ROCK'),
(null, 'PUNK ROCK'),
(null, 'POST PUNK/NEW WAVE'),
(null, 'GOTH ROCK'),
(null, 'INDIE ROCK'),
(null, 'BRITPOP'),
(null, 'MATH ROCK'),
(null, 'GRUNGE'),
(null, 'SHOEGAZE'),
(null, 'INDRUSTRIAL'),
(null, 'POST ROCK'),
(null, 'EMO'),
/* SUBGENEROS HIP-HOP - link: https://blog.landr.com/rap-styles/*/
(null, 'GANGSTA RAP'),
(null, 'TRAP'),
(null, 'RAP ROCK'),
(null, 'POP RAP'),
(null, 'HYPHY'),
(null, 'PIMP RAP'),
(null, 'G-FUNK'),
(null, 'DRILL'),
(null, 'HORRORCORE'),
(null, 'BOOMBAP'),
(null, 'JAZZ RAP'),
(null, 'MUMBLE RAP'),
(null, 'CRUNK'),
(null, 'EMO RAP'),
(null, 'BOUNCE'),
(null, 'LATIN TRAP'),
(null, 'SOUNDCLOUD RAP'),
(null, 'LO-FI HIP-HOP'),
(null, 'FOOTWORK'),
/* SUBGENEROS POP - link: https://musicgenreguide.fandom.com/wiki/Pop*/
(null, 'ADULT CONTEMPORARY'),
(null, 'ART POP'),
(null, 'BUBBLEGUM POP'),
(null, 'C-POP'),
(null, 'EUROPOP'),
(null, 'INDIAN POP'),
(null, 'INDIE POP'),
(null, 'J-POP'),
(null, 'K-POP'),
(null, 'TEEN POP'),
(null, 'FOLK POP'),
(null, 'PSYCHEDELIC POP'),
(null, 'POP PUNK'),
(null, 'SYNTHPOP'),
/* SUBGENEROS REGGAE - link:https://victrola.com/blogs/articles/the-types-of-reggae-music*/
(null, 'ROOTS REGGAE'),
(null, 'DUB'),
(null, 'DANCEHALL REGGAE'),
(null, 'RAGGA'),
(null, 'REGGAE ROCK'),
(null, 'REGGAETON'),
/*SUBGENEROS JAZZ - link: https://www.anselmoacademy.org/different-types-of-jazz-music/*/
(null, 'SWING'),
(null, 'BEBOP'),
(null, 'HARD BOP'),
(null, 'COOL JAZZ'),
(null, 'MODAL JAZZ'),
(null, 'FREE JAZZ'),
(null, 'POST BOP'),
(null, 'SMOOTH JAZZ'),
(null, 'JAZZ FUSION'),
(null, 'AVANT-GARDE JAZZ'),
/* SUBGENEROS BLUES - link: https://www.anselmoacademy.org/different-types-of-blues-music/*/
(null, 'DELTA BLUES'),
(null, 'CHICAGO BLUES'),
(null, 'CONTEMPORARY BLUES'),
(null, 'BOOGIE-WOOGIE'),
(null, 'AFRICAN BLUES'),
/* SUBGENEROS CLASSICA - link: https://www.greatertorontomusic.ca/post/7-eras-of-classical-music*/
(null, 'MEDIEVAL'),
(null, 'RENASCENCA'),
(null, 'BARROCO'),
(null, 'CLASSICO'),
(null, 'ROMANTISMO'),
(null, 'ROMANTISMO TARDIO'),
(null, 'MODERNISMO');


insert into generoFilho(idGeneroFilho,fkTagGenero,fkGeneroPai) values
/*subgeneros folk */
(null, 9,1),
(null, 10,1),
(null, 11,1),
(null, 12,1),
(null, 13,1),
(null, 14,1),
(null, 15,1),
(null, 16,1),
(null, 17,1),
(null, 18,1),
(null, 19,1),
(null, 20,1),
(null, 21,1),
(null, 22,1),
(null, 23,1),
(null, 24,1),
(null, 25,1),
(null, 26,1),
(null, 27,1),
(null, 28,1),
(null, 29,1),
(null, 30,1),
(null, 31,1),
(null, 32,1),
(null, 33,1),
(null, 34,1),
(null, 35,1),
(null, 36,1),
(null, 37,1),
(null, 38,1),
(null, 39,1),
(null, 40,1),
(null, 41,1),
(null, 42,1),
(null, 43,1),
(null, 44,1),
(null, 45,1),
(null, 46,1),
(null, 47,1),
/*   SUBGENEROS ROCK - link: https://www.guitarguitar.co.uk/news/142079/*/
(null, 48,2),
(null, 49,2),
(null, 50,2),
(null, 51,2),
(null, 52,2),
(null, 53,2),
(null, 54,2),
(null, 55,2),
(null, 56,2),
(null, 57,2),
(null, 58,2),
(null, 59,2),
(null, 60,2),
(null, 61,2),
(null, 62,2),
(null, 63,2),
(null, 64,2),
(null, 65,2),
(null, 66,2),
(null, 67,2),
/* SUBGENEROS HIP-HOP - link: https://blog.landr.com/rap-styles/*/
(null, 68,3),
(null, 69,3),
(null, 70,3),
(null, 71,3),
(null, 72,3),
(null, 73,3),
(null, 74,3),
(null, 75,3),
(null, 76,3),
(null, 77,3),
(null, 78,3),
(null, 79,3),
(null, 80,3),
(null, 81,3),
(null, 82,3),
(null, 83,3),
(null, 84,3),
(null, 85,3),
(null, 86,3),
/* SUBGENEROS POP - link: https://musicgenreguide.fandom.com/wiki/Pop*/
(null, 87,4),
(null, 88,4),
(null, 89,4),
(null, 90,4),
(null, 91,4),
(null, 92,4),
(null, 93,4),
(null, 94,4),
(null, 95,4),
(null, 96,4),
(null, 97,4),
(null, 98,4),
(null, 99,4),
(null, 100,4),
/* SUBGENEROS REGGAE - link:https://victrola.com/blogs/articles/the-types-of-reggae-music*/
(null, 101,5),
(null, 102,5),
(null, 103,5),
(null, 104,5),
(null, 105,5),
(null, 106,5),
/*SUBGENEROS JAZZ - link: https://www.anselmoacademy.org/different-types-of-jazz-music/*/
(null, 107,6),
(null, 108,6),
(null, 109,6),
(null, 110,6),
(null, 111,6),
(null, 112,6),
(null, 113,6),
(null, 114,6),
(null, 115,6),
(null, 116,6),
/* SUBGENEROS BLUES - link: https://www.anselmoacademy.org/different-types-of-blues-music/*/
(null, 117,7),
(null, 118,7),
(null, 119,7),
(null, 120,7),
(null, 121,7),
/* SUBGENEROS CLASSICA - link: https://www.greatertorontomusic.ca/post/7-eras-of-classical-music*/
(null, 122,8),
(null, 123,8),
(null, 124,8),
(null, 125,8),
(null, 126,8),
(null, 127,8),
(null, 128,8);

