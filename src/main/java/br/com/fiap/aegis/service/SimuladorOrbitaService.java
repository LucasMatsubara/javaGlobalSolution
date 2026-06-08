package br.com.fiap.aegis.service;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.enums.StatusMissao;
import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.enums.TipoDetrito;
import br.com.fiap.aegis.model.CoordenadaOrbital;
import br.com.fiap.aegis.model.DetritoEspacial;
import br.com.fiap.aegis.model.DroneLimpeza;
import br.com.fiap.aegis.model.MissaoId;
import br.com.fiap.aegis.model.MissaoIntercepcao;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import br.com.fiap.aegis.repository.MissaoIntercepcaoRepository;
import br.com.fiap.aegis.repository.SateliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SimuladorOrbitaService {

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    @Autowired
    private DroneLimpezaRepository droneRepository;

    @Autowired
    private MissaoIntercepcaoRepository missaoRepository;

    @Autowired
    private SateliteRepository sateliteRepository;

    @Autowired
    private LogOperacaoService logService;

    private final Random random = new Random();

    // Nomes possíveis para os detritos
    private static final String[] NOMES_DETRITOS = {
            "Fragmento de Foguete", "Satélite Inativo", "Painel Solar",
            "Debris Metálico", "Microdebris"
    };

    // Origens possíveis
    private static final String[] ORIGENS = {
            "Missão Apollo", "ISS", "SpaceX Falcon", "Soyuz MS",
            "Ariane 5", "Delta IV", "Atlas V", "Desconhecida"
    };

    @Scheduled(fixedDelay = 30000) // a cada 30 segundos
    public void gerarDetritosEMissoes() {
        // Só gera se houver satélites E drones cadastrados
        long totalSatelites = sateliteRepository.count();
        long totalDrones = droneRepository.count();

        if (totalSatelites == 0 || totalDrones == 0) {
            return;
        }

        // Gera entre 1 e 3 detritos por ciclo
        int quantidadeDetritos = random.nextInt(3) + 1;

        for (int i = 0; i < quantidadeDetritos; i++) {
            DetritoEspacial detrito = gerarDetritoAleatorio();
            DetritoEspacial detritoSalvo = detritoRepository.save(detrito);

            logService.registarAcao(
                    detritoSalvo.getNome(),
                    "Ameaça detectada em rota com satélite monitorado.",
                    detritoSalvo.getRiscoColisao().name()
            );

            // Cria missão pendente para cada detrito gerado
            criarMissaoPendente(detritoSalvo);
        }
    }

    private DetritoEspacial gerarDetritoAleatorio() {
        DetritoEspacial detrito = new DetritoEspacial();

        // Tipo aleatório
        TipoDetrito[] tipos = TipoDetrito.values();
        TipoDetrito tipo = tipos[random.nextInt(tipos.length)];
        detrito.setTipoDetrito(tipo);

        // Nome baseado no tipo
        detrito.setNome(nomeParaTipo(tipo));

        // Massa baseada no tipo (kg)
        detrito.setMassaKg(massaParaTipo(tipo));

        // Velocidade orbital realista (km/s)
        detrito.setVelocidade(7.0 + random.nextDouble() * 2.0); // entre 7.0 e 9.0 km/s

        // Risco baseado na massa (probabilidade ponderada)
        detrito.setRiscoColisao(calcularRiscoPorMassa(detrito.getMassaKg()));

        // Origem aleatória
        detrito.setOrigem(ORIGENS[random.nextInt(ORIGENS.length)]);

        // Coordenadas orbitais aleatórias realistas
        CoordenadaOrbital coordenada = new CoordenadaOrbital();
        coordenada.setEixoX(Math.round((random.nextDouble() * 180 - 90) * 10.0) / 10.0);  // -90 a 90
        coordenada.setEixoY(Math.round((random.nextDouble() * 360 - 180) * 10.0) / 10.0); // -180 a 180
        coordenada.setAltitude(300.0 + random.nextDouble() * 900.0); // 300 a 1200 km
        detrito.setCoordenada(coordenada);

        return detrito;
    }

    private String nomeParaTipo(TipoDetrito tipo) {
        return switch (tipo) {
            case FRAGMENTO_FOGUETE -> "Fragmento de Foguete";
            case SATELITE_INATIVO  -> "Satélite Inativo";
            case PAINEL_SOLAR      -> "Painel Solar";
            case DEBRIS_METALICO   -> "Debris Metálico";
            case MICRODEBRIS       -> "Microdebris";
        };
    }

    private double massaParaTipo(TipoDetrito tipo) {
        return switch (tipo) {
            case MICRODEBRIS       -> 0.001 + random.nextDouble() * 0.099; // 0.001 a 0.1 kg
            case FRAGMENTO_FOGUETE -> 1.0 + random.nextDouble() * 49.0;    // 1 a 50 kg
            case DEBRIS_METALICO   -> 10.0 + random.nextDouble() * 90.0;   // 10 a 100 kg
            case PAINEL_SOLAR      -> 50.0 + random.nextDouble() * 200.0;  // 50 a 250 kg
            case SATELITE_INATIVO  -> 200.0 + random.nextDouble() * 800.0; // 200 a 1000 kg
        };
    }

    private RiscoColisao calcularRiscoPorMassa(double massaKg) {
        // Probabilidade ponderada baseada na massa
        int sorteio = random.nextInt(100);

        if (massaKg < 0.1) {
            // Microdebris: 60% BAIXO, 30% MODERADO, 10% ALTO, 0% CRITICO
            if (sorteio < 60) return RiscoColisao.BAIXO;
            if (sorteio < 90) return RiscoColisao.MODERADO;
            return RiscoColisao.ALTO;
        } else if (massaKg < 50) {
            // Fragmentos pequenos: 30% BAIXO, 40% MODERADO, 25% ALTO, 5% CRITICO
            if (sorteio < 30) return RiscoColisao.BAIXO;
            if (sorteio < 70) return RiscoColisao.MODERADO;
            if (sorteio < 95) return RiscoColisao.ALTO;
            return RiscoColisao.CRITICO;
        } else if (massaKg < 250) {
            // Médios: 10% BAIXO, 30% MODERADO, 40% ALTO, 20% CRITICO
            if (sorteio < 10) return RiscoColisao.BAIXO;
            if (sorteio < 40) return RiscoColisao.MODERADO;
            if (sorteio < 80) return RiscoColisao.ALTO;
            return RiscoColisao.CRITICO;
        } else {
            // Satélites inativos e grandes: 5% BAIXO, 15% MODERADO, 30% ALTO, 50% CRITICO
            if (sorteio < 5) return RiscoColisao.BAIXO;
            if (sorteio < 20) return RiscoColisao.MODERADO;
            if (sorteio < 50) return RiscoColisao.ALTO;
            return RiscoColisao.CRITICO;
        }
    }

    private void criarMissaoPendente(DetritoEspacial detrito) {
        // Busca qualquer drone disponível para associar na missão pendente
        List<DroneLimpeza> dronesNaBase = droneRepository.findByStatusOperacional(StatusOperacional.NA_BASE);

        if (dronesNaBase.isEmpty()) {
            // Sem drones disponíveis — missão fica sem drone associado por ora
            // O mobile já trata esse caso com a mensagem "Não há drones disponíveis"
            return;
        }

        // Pega o primeiro drone disponível para criar a missão pendente
        // O usuário poderá escolher outro pelo app se quiser
        DroneLimpeza drone = dronesNaBase.get(0);

        // Verifica se já existe missão entre esse drone e esse detrito
        MissaoId missaoId = new MissaoId(drone.getId(), detrito.getId());
        if (missaoRepository.existsById(missaoId)) {
            return;
        }

        MissaoIntercepcao missao = new MissaoIntercepcao();
        missao.setId(missaoId);
        missao.setDrone(drone);
        missao.setDetrito(detrito);
        missao.setStatusMissao(StatusMissao.AUTORIZADA);
        missao.setDataMissao(LocalDateTime.now());
        missaoRepository.save(missao);
    }
}