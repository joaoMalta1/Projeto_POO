package model;


import java.util.*;
import java.util.stream.*;

class Baralho {
	private Queue<Carta> baralho;
	
	void voltaParaBaralho(Carta carta) {
		baralho.offer(carta);
	}
	
//	retorna a nova posicao do jogador apos pegar a carta
	int comprarCarta(Jogador j, Banco b) {
		Carta carta = baralho.poll();
		int novaPos = carta.retirada(j, b);
		
		CentralPartida.getInstance().notificaSorteOuReves(carta.getNome());

		return novaPos;
    }
	
    private void criarBaralho(String nomeCartas, String qntDeveReceber, String especialidade) {
        String[] nomes = nomeCartas.split("\n");
        String[] valores = qntDeveReceber.split("\n");
        String[] especialidades = especialidade.split("\n");
        
        List<Carta> cartasTemp = IntStream.range(0, nomes.length)
            .mapToObj(i -> {
                String nome = nomes[i].strip();
                double valor = Double.parseDouble(valores[i].strip());
                String esp = especialidades[i].strip();
                
                return switch(esp) {
                    case "LIVRE DA PRISAO" -> new CartaLivreDaPrisao(nome, this);
                    case "VA PARA PRISAO" -> new CartaVaParaPrisao(nome, this);
                    case "RECEBER 50 DOS JOGADORES" -> new CartaReceberDeJogadores(nome, this);
                    case "PONTO DE PARTIDA" -> new CartaPontoDePartida(nome, valor, this);
                    default -> new Carta(nome, valor, this);
                };
            })
            .collect(Collectors.toList());
        
        /* MUDANÇA TEMPORÁRIA PARA FINS DE TESTE */
//      -------------------------------------------  

        // MODO TESTE - COLOCAR CARTAS ESPECÍFICAS NO INÍCIO
        List<Carta> cartasTeste = new ArrayList<>();
        
        // Encontrar as cartas especiais
        Carta livrePrisao = cartasTemp.stream()
            .filter(c -> c instanceof CartaLivreDaPrisao)
            .findFirst().orElse(null);
            
        Carta vaParaPrisao = cartasTemp.stream()
            .filter(c -> c instanceof CartaVaParaPrisao)
            .findFirst().orElse(null);
            
        Carta receberJogadores = cartasTemp.stream()
            .filter(c -> c instanceof CartaReceberDeJogadores)
            .findFirst().orElse(null);
            
        Carta pontoPartida = cartasTemp.stream()
            .filter(c -> c instanceof CartaPontoDePartida)
            .findFirst().orElse(null);
        
        // Remover as cartas especiais da lista temporária
        cartasTemp.remove(livrePrisao);
        cartasTemp.remove(vaParaPrisao);
        cartasTemp.remove(receberJogadores);
        cartasTemp.remove(pontoPartida);
        
        // Adicionar as cartas especiais no início na ordem desejada
        if (livrePrisao != null) cartasTeste.add(livrePrisao);
        if (vaParaPrisao != null) cartasTeste.add(vaParaPrisao);
        if (receberJogadores != null) cartasTeste.add(receberJogadores);
        if (pontoPartida != null) cartasTeste.add(pontoPartida);
        
        // Embaralhar as cartas restantes e adicionar após as especiais
        Collections.shuffle(cartasTemp);
        cartasTeste.addAll(cartasTemp);
        
        baralho = new LinkedList<>(cartasTeste);
        
        // Para verificação durante testes (opcional)
        System.out.println("[DEBUG] Primeiras 4 cartas do baralho:");
        Iterator<Carta> iterator = baralho.iterator();
        for (int i = 0; i < 4 && iterator.hasNext(); i++) {
            Carta carta = iterator.next();
            System.out.println((i+1) + ": " + carta.getNome() + " - " + carta.getClass().getSimpleName());
        }
//      -------------------------------------------  
        /* MUDANÇA TEMPORÁRIA PARA FINS DE TESTE */
        
//        Collections.shuffle(cartasTemp);
//        baralho = new LinkedList<>(cartasTemp);
    }
    
    private void imprimirBaralho() {
    	for(Carta c : baralho) {
    		System.out.println(c.getNome());
    	}
    }
	
//    estas strings de definição foram retiradas da tabela criada pelo grupo "SorteOuRevés.ods"
	Baralho(){
		final String nomeCartas = """
				chance1
				chance2
				chance3
				chance4
				chance5
				chance6
				chance7
				chance8
				chance9
				chance10
				chance11
				chance12
				chance13
				chance14
				chance15
				chance16
				chance17
				chance18
				chance19
				chance20
				chance21
				chance22
				chance23
				chance24
				chance25
				chance26
				chance27
				chance28
				chance29
				chance30
				""";
		final String qntDeveReceber = """ 
				25
				150
				80
				200
				50
				50
				100
				100
				0
				200
				0
				45
				100
				100
				20
				-15
				-25
				-45
				-30
				-100
				-100
				-40
				0
				-30
				-50
				-25
				-30
				-45
				-50
				-50
				""";
		final String especialidade = """
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				LIVRE DA PRISAO
				PONTO DE PARTIDA
				RECEBER 50 DOS JOGADORES
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				VA PARA PRISAO
				nada
				nada
				nada
				nada
				nada
				nada
				nada
				""";
		criarBaralho(nomeCartas, qntDeveReceber, especialidade);
		imprimirBaralho();
	}
}