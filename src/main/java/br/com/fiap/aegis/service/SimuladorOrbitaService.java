package br.com.fiap.aegis.service;

import br.com.fiap.aegis.enums.*;
import br.com.fiap.aegis.model.*;
import br.com.fiap.aegis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SimuladorOrbitaService {

    @Autowired private DetritoEspacialRepository detritoRepository;
    @Autowired private DroneLimpezaRepository droneRepository;
    @Autowired private MissaoIntercepcaoRepository missaoRepository;
    @Autowired private SateliteRepository sateliteRepository;
    @Autowired private LogOperacaoService logService;

    private final Random random = new Random();

    private static final String[] ORIGENS = {
            "Missão Apollo", "ISS", "SpaceX Falcon", "Soyuz MS",
            "Ariane 5", "Delta IV", "Atlas V", "Desconhecida"
    };

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void gerarDetritosEMissoes() {
        // ✅ FIX: try/catch garante que uma falha não para o scheduler para sempre
        try {
            List<Satelite> satelites = sateliteRepository.findAll();
            long totalDrones = droneRepository.count();

            if (satelites.isEmpty() || totalDrones == 0) return;

            int quantidadeDetritos = random.nextInt(3) + 1;

            for (int i = 0; i < quantidadeDetritos; i++) {
                Satelite sateliteAlvo = satelites.get(random.nextInt(satelites.size()));
                Empresa empresaAlvo = sateliteAlvo.getEmpresa();

                // ✅ FIX: passa o sateliteAlvo real (com ID), não um new Satelite() vazio
                DetritoEspacial detrito = gerarDetritoAleatorio(empresaAlvo, sateliteAlvo);
                DetritoEspacial detritoSalvo = detritoRepository.save(detrito);

                logService.registarAcao(
                        detritoSalvo.getNome(),
                        "Ameaça detectada em rota com satélite monitorado.",
                        detritoSalvo.getRiscoColisao().name(),
                        empresaAlvo
                );

                criarMissaoPendente(detritoSalvo);
            }
        } catch (Exception e) {
            // Loga o erro mas NÃO relança — garante que o scheduler continue rodando
            System.err.println("[SimuladorOrbita] ERRO ao gerar detritos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ FIX: recebe o sateliteAlvo real ao invés de criar new Satelite() vazio
    private DetritoEspacial gerarDetritoAleatorio(Empresa empresa, Satelite sateliteAlvo) {
        DetritoEspacial detrito = new DetritoEspacial();

        TipoDetrito[] tipos = TipoDetrito.values();
        TipoDetrito tipo = tipos[random.nextInt(tipos.length)];
        detrito.setTipoDetrito(tipo);
        detrito.setNome(nomeParaTipo(tipo));
        detrito.setMassaKg(massaParaTipo(tipo));
        detrito.setVelocidade(7.0 + random.nextDouble() * 2.0);
        detrito.setRiscoColisao(calcularRiscoPorMassa(detrito.getMassaKg()));
        detrito.setOrigem(ORIGENS[random.nextInt(ORIGENS.length)]);
        detrito.setSatelite(sateliteAlvo);  // ✅ satélite real com ID válido
        detrito.setEmpresa(empresa);

        CoordenadaOrbital coordenada = new CoordenadaOrbital();
        coordenada.setEixoX(Math.round((random.nextDouble() * 180 - 90) * 10.0) / 10.0);
        coordenada.setEixoY(Math.round((random.nextDouble() * 360 - 180) * 10.0) / 10.0);
        coordenada.setAltitude(300.0 + random.nextDouble() * 900.0);
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
            case MICRODEBRIS       -> 0.001 + random.nextDouble() * 0.099;
            case FRAGMENTO_FOGUETE -> 1.0   + random.nextDouble() * 49.0;
            case DEBRIS_METALICO   -> 10.0  + random.nextDouble() * 90.0;
            case PAINEL_SOLAR      -> 50.0  + random.nextDouble() * 200.0;
            case SATELITE_INATIVO  -> 200.0 + random.nextDouble() * 800.0;
        };
    }

    private RiscoColisao calcularRiscoPorMassa(double massaKg) {
        int sorteio = random.nextInt(100);
        if (massaKg < 0.1) {
            if (sorteio < 60) return RiscoColisao.BAIXO;
            if (sorteio < 90) return RiscoColisao.MODERADO;
            return RiscoColisao.ALTO;
        } else if (massaKg < 50) {
            if (sorteio < 30) return RiscoColisao.BAIXO;
            if (sorteio < 70) return RiscoColisao.MODERADO;
            if (sorteio < 95) return RiscoColisao.ALTO;
            return RiscoColisao.CRITICO;
        } else if (massaKg < 250) {
            if (sorteio < 10) return RiscoColisao.BAIXO;
            if (sorteio < 40) return RiscoColisao.MODERADO;
            if (sorteio < 80) return RiscoColisao.ALTO;
            return RiscoColisao.CRITICO;
        } else {
            if (sorteio < 5)  return RiscoColisao.BAIXO;
            if (sorteio < 20) return RiscoColisao.MODERADO;
            if (sorteio < 50) return RiscoColisao.ALTO;
            return RiscoColisao.CRITICO;
        }
    }

    private void criarMissaoPendente(DetritoEspacial detrito) {
        try {
            List<DroneLimpeza> dronesNaBase = droneRepository.findByStatusOperacional(StatusOperacional.NA_BASE);
            if (dronesNaBase.isEmpty()) return;

            DroneLimpeza drone = dronesNaBase.get(0);
            MissaoId missaoId = new MissaoId(drone.getId(), detrito.getId());
            if (missaoRepository.existsById(missaoId)) return;

            MissaoIntercepcao missao = new MissaoIntercepcao();
            missao.setId(missaoId);
            missao.setDrone(drone);
            missao.setDetrito(detrito);
            missao.setStatusMissao(StatusMissao.AUTORIZADA);
            missao.setDataMissao(LocalDateTime.now());
            missaoRepository.save(missao);
        } catch (Exception e) {
            System.err.println("[SimuladorOrbita] ERRO ao criar missão pendente: " + e.getMessage());
        }
    }
}