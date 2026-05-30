package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.EmpresaRequestDTO;
import br.com.fiap.aegis.dto.EmpresaResponseDTO;
import br.com.fiap.aegis.model.Empresa;
import br.com.fiap.aegis.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public EmpresaResponseDTO cadastrarEmpresa(EmpresaRequestDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.nome());
        empresa.setCnpj(dto.cnpj());

        Empresa empresaSalva = empresaRepository.save(empresa);

        return mapearParaResponseDTO(empresaSalva);
    }

    public List<EmpresaResponseDTO> listarTodas() {
        return empresaRepository.findAll().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
    }

    private EmpresaResponseDTO mapearParaResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj()
        );
    }
}
